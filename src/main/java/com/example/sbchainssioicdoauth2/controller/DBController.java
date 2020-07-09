package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.DBService;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/db")
public class DBController {

    @Autowired
    private DBService submitService;

    @Autowired
    CacheService cacheService;

    @Autowired
    PopulateInfoService infoService;

    @PostMapping("/save")
    protected @ResponseBody
    String save(@AuthenticationPrincipal OidcUser oidcUser, @RequestBody(required = true) String uuid) throws IllegalAccessException, IllegalArgumentException, IntrospectionException, InvocationTargetException {
        SsiApplication ssiApp = cacheService.get(uuid);
        submitService.submit(ssiApp);
        return "OK";
    }

    @PostMapping("/temp")
    protected @ResponseBody
    String tempSave(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam(value = "uuid", required = true) String uuid) throws IllegalAccessException, IllegalArgumentException, IntrospectionException, InvocationTargetException {
        SsiApplication ssiApp = cacheService.get(uuid);
        submitService.temp(ssiApp);
        return "OK";
    }

}
