package org.melo;

import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.data.gemfire.support.GemfireCacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

@SpringBootApplication
@Configuration
@EnableCaching
@EnableGemfireRepositories
@RestController
@RequestMapping("/values")
@SuppressWarnings("unused")
public class GfDemoApplication {

	private static HashMap<String, String> values;

	@Autowired
	private GfDemoService service;

	public static void main(String[] args) {

		values = new HashMap<>();

		SpringApplication.run(GfDemoApplication.class, args);

	}

	@Bean
	Properties gemfireProperties(@Value("${gemfire.log.level:config}") String logLevel) {
		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("log-level", logLevel);

		return gemfireProperties;
	}

	@Bean
	PoolFactoryBean gemfirePool(@Value("${gemfire.cache.locator.host:localhost}") String host,
								@Value("${gemfire.cache.locator.port:10334}") int port) {
		PoolFactoryBean gemfirePool = new PoolFactoryBean();

		gemfirePool.setLocators(Collections.singletonList(new ConnectionEndpoint(host, port )));

		return gemfirePool;
	}

	@Bean
	ClientCacheFactoryBean gemfireCache(Properties gemfireProperties) {
		ClientCacheFactoryBean gemfireCache = new ClientCacheFactoryBean();

		// gemfireCache.setClose(true);
		gemfireCache.setProperties(gemfireProperties);

		return gemfireCache;
	}

	@Bean(name = "Values")
	ClientRegionFactoryBean<String, String> valuesRegion(ClientCacheFactoryBean gemfireCache, PoolFactoryBean gemfirePool) throws Exception {
		ClientRegionFactoryBean<String, String> valuesRegion = new ClientRegionFactoryBean<>();

		valuesRegion.setCache(gemfireCache.getObject());
		valuesRegion.setName("Values");
		valuesRegion.setPool(gemfirePool.getPool());
		valuesRegion.setShortcut(ClientRegionShortcut.PROXY);

		return valuesRegion;
	}

	@Bean
	GemfireCacheManager cacheManager(ClientCacheFactoryBean gemfireCache) throws Exception {
		GemfireCacheManager cacheManager = new GemfireCacheManager();
		cacheManager.setCache(gemfireCache.getObject());
		return cacheManager;
	}

/*	@Bean
	GemfireCacheManager cacheManager(GemFireCache gemfireCache) {
		GemfireCacheManager cacheManager = new GemfireCacheManager();
		cacheManager.setCache(gemfireCache);
		return cacheManager;
	}

	// define all Region beans required by the application including Regions
	// used specifically in Spring's Cache Abstraction
	*/

	@CachePut(cacheNames = "Values", key="#key")
	@RequestMapping(method = RequestMethod.POST)
	public String set_cache(@RequestParam(value="key") String key, @RequestParam(value="value") String value){
		System.out.println(String.format("Called to set key %1$s with value %2$s", key, value));
		values.put(key, value);
		return value;
	}

	@Cacheable(cacheNames = "Values", key="#key")
	@RequestMapping(method = RequestMethod.GET)
	public String get_cache(@RequestParam(value="key") String key){
		System.out.println(String.format("Called to get key %1$s", key));
		return values.get(key) == null ? "Unknown key!" : values.get(key);
	}

	@RequestMapping(path = "/repo", method = RequestMethod.POST)
	public String save(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value){
		GfDemoEntity entity = new GfDemoEntity();
		entity.key = key;
		entity.value = value;
		return service.save(entity);
	}

	@RequestMapping(path = "/repo", method = RequestMethod.GET)
	public String getByKey(@RequestParam(value = "key") String key){
		return service.getByKey(key);
	}
}
