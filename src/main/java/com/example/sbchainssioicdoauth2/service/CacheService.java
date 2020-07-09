package com.example.sbchainssioicdoauth2.service;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheService {

    @Autowired
    private CacheManager manager;

//    @CachePut(value = "ssiApp", key = "#uuid")
    public SsiApplication putInfo(SsiApplication ssiApp, String uuid) {
        log.debug("put cache with uuid :{}", uuid);

        manager.getCache("ssiApp").put(uuid, ssiApp);
        manager.getCache("ssiApp").get(uuid, SsiApplication.class);
        return manager.getCache("ssiApp").get(uuid, SsiApplication.class); //ssiApp;
    }

//    @Cacheable(value = "ssiApp", key = "#uuid")
    public SsiApplication get(String uuid) {
        SsiApplication result = manager.getCache("ssiApp").get(uuid, SsiApplication.class);
//        log.info("FETCHING From CACHE " + uuid);
        if (result != null) {
            return result;
        }
        SsiApplication ssiApp = new SsiApplication();

        return ssiApp;
    }

    @CacheEvict(value = "ssiApp", key = "#uuid")
    public void evictSsiAppCache(String uuid) {
        log.debug("cache with uuid :{} , evicted ", uuid);
    }
}
