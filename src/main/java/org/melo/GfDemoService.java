package org.melo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public String save(GfDemoEntity entity) {
        return repo.save(entity).toString();

    }
}
