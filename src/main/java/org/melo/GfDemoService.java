package org.melo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by CSCOLLO on 15.11.2016.
 */
@Service
public class GfDemoService {

    private GfDemoRepo repo;

    @Autowired
    public GfDemoService(GfDemoRepo repo) {
        this.repo = repo;
    }

    public void save(GfDemoEntity entity) {
        repo.save(entity);
    }

    public Optional<GfDemoEntity> getByKey(String key) {
        Optional<GfDemoEntity> result = repo.findByKey(key);
        return result;
    }
}
