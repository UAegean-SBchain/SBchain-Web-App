package com.example.sbchainssioicdoauth2.service;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        if (result != null) {
            return result;
        }
        SsiApplication ssiApp = new SsiApplication();
        ssiApp.setCredentialIds(new ArrayList());
        String municipality = !StringUtils.isEmpty(ssiApp.getMunicipality()) ? ssiApp.getMunicipality() : "tmpMunicipality";
        ssiApp.setSubmittedMunicipality(municipality);

        return ssiApp;
    }

    @CacheEvict(value = "ssiApp", key = "#uuid")
    public void evictSsiAppCache(String uuid) {
        log.debug("cache with uuid :{} , evicted ", uuid);
    }

    public String putNonce(String nonce) {
        log.info("put cache with nonce:{}", nonce);
        manager.getCache("nonce").put(nonce, nonce);
        return manager.getCache("nonce").get(nonce, String.class);
    }

    public boolean isNonce(String nonce) {
        log.info("looking in  cache for nonce:{}", nonce);
        return manager.getCache("nonce").get(nonce, String.class) != null;
    }

}
