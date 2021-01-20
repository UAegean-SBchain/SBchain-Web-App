package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.model.pojo.HouseholdMember;
import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.DBService;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Optional;

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
    String save(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam(value = "uuid", required = true) String uuid) throws IllegalAccessException, IllegalArgumentException, IntrospectionException, InvocationTargetException {
        SsiApplication ssiApp = cacheService.get(uuid);
        // check if the is a principal application
        String principalAfm = ssiApp.getHouseholdPrincipal().getAfm();
        if (principalAfm.equals(ssiApp.getTaxisAfm())) {
            Optional<SsiApplication> principalApp = submitService.findAllByAFM(principalAfm).stream().filter(app -> {
                return app.getStatus().equals("accepted");
            }).findAny();
            if (principalApp.isPresent()) {
                HouseholdMember presentAppMember = new HouseholdMember();
                presentAppMember.setName(ssiApp.getTaxisFirstName());
                presentAppMember.setSurname(ssiApp.getTaxisFamilyName());
                presentAppMember.setAfm(ssiApp.getTaxisAfm());
                presentAppMember.setDateOfBirth(ssiApp.getTaxisDateOfBirth());
                if (principalApp.get().getHouseholdComposition().stream().noneMatch(hm -> {
                    return hm.getAfm().equals(presentAppMember.getAfm());
                })) {
                    principalApp.get().getHouseholdComposition().add(presentAppMember);
                    //add member to household
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    principalApp.get().getHouseholdCompositionHistory().put(timestamp.toString(),principalApp.get().getHouseholdComposition());
                    submitService.replace(principalApp.get());
                }
            }
        }


        return submitService.submit(ssiApp);
    }

    @PostMapping("/temp")
    protected @ResponseBody
    String tempSave(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam(value = "uuid", required = true) String uuid) throws IllegalAccessException, IllegalArgumentException, IntrospectionException, InvocationTargetException {
        SsiApplication ssiApp = cacheService.get(uuid);
        submitService.temp(ssiApp);
        return "OK";
    }

    @PostMapping("/delete")
    protected @ResponseBody
    String delete(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam(value = "uuid", required = true) String uuid) throws IllegalAccessException, IllegalArgumentException, IntrospectionException, InvocationTargetException {
//        SsiApplication ssiApp = cacheService.get(uuid);
        Optional<SsiApplication> ssiApp = submitService.getByUuid(uuid);
        if (ssiApp.isPresent()) {
            submitService.delete(ssiApp.get());

        }
        return "OK";
    }

}
