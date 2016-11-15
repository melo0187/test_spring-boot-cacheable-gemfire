package org.melo;

import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by CSCOLLO on 15.11.2016.
 */
@Repository
public interface GfDemoRepo extends GemfireRepository<GfDemoEntity, String> {

    Optional<GfDemoEntity> findByKey(String key);

}
