package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.model.pojo.HouseholdMember;
import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.DBService;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import com.example.sbchainssioicdoauth2.service.ResourceService;
import com.example.sbchainssioicdoauth2.utils.FormType;
import com.example.sbchainssioicdoauth2.utils.LogoutUtils;
import com.example.sbchainssioicdoauth2.utils.RandomIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/multi/personalInfo")
public class PersonalInformationController {

    @Autowired
    CacheService cacheService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    PopulateInfoService infoService;

    @Autowired
    DBService dbServ;


    @GetMapping("/view")
    protected ModelAndView personalInfo(@RequestParam(value = "uuid", required = false) String uuid, ModelMap model, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {

        model.addAttribute("firstPage", true);

        if (StringUtils.isEmpty(uuid)) {
            uuid = RandomIdGenerator.GetBase36(16);
        }

        infoService.populateFetchInfo(model, request, uuid);
        SsiApplication ssiApp = cacheService.get(uuid);
        model.addAttribute("uuid", uuid);

        infoService.populateSsiApp(ssiApp, request, FormType.PERSONAL_DECLARATION.value, uuid);
        ssiApp = infoService.updateModelfromCacheMergeDB(ssiApp, model, request, uuid);
        cacheService.putInfo(ssiApp, uuid);
        //is this is a previous application
        if (ssiApp.getHospitalized() != null) {
            model.addAttribute("old", true);
        }


        return new ModelAndView("personalInfo");
    }

    ////    @PreAuthorize("hasAuthority('personal_info')")
//    @GetMapping("/results")
//    protected ModelAndView personalInfoResults(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model,
//            HttpServletRequest request) {
//
//        infoService.populateFetchInfo(model, request, uuid);
//        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
//        context.getIdToken().getOtherClaims();
//
//        return new ModelAndView("personalInfo");
//    }
//    @PreAuthorize("hasAuthority('personal_info')")
    @GetMapping("/continue")
    protected ModelAndView personalInfoSubmit(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
                                              @RequestParam(value = "principal", required = false) String principalAFM,
                                              @RequestParam(value = "household", required = false) String householdAFM,
                                              ModelMap model, HttpServletRequest request, HttpSession session) {

        SsiApplication ssiApp = cacheService.get(uuid);

        if ((ssiApp.getHouseholdPrincipal() != null && StringUtils.isEmpty(ssiApp.getHouseholdPrincipal().getAfm()))
                || !StringUtils.isEmpty(principalAFM)) {
            HouseholdMember principalApplicant = new HouseholdMember();
            principalApplicant.setAfm(principalAFM);
            ssiApp.setHouseholdPrincipal(principalApplicant);
            Optional<SsiApplication> principalApplications = dbServ.getByTaxisAfm(principalApplicant.getAfm());
            if (principalApplications.isPresent()) {
                principalApplicant.setSurname(principalApplications.get().getSurnameLatin());
                principalApplicant.setName(principalApplications.get().getNameLatin());
                principalApplicant.setDateOfBirth(principalApplications.get().getTaxisDateOfBirth());
            }

            if (!StringUtils.isEmpty(householdAFM)) {
                Arrays.stream(householdAFM.split(",")).forEach(afm -> {
                    HouseholdMember member = new HouseholdMember();
                    member.setAfm(principalAFM);
                    member.setName("N/A");
                    member.setSurname("N/A");
                    member.setDateOfBirth("01/01/1970");
                    member.setRelationship("N/A");
                    if (ssiApp.getHouseholdComposition() == null) {
                        ssiApp.setHouseholdComposition(new ArrayList<HouseholdMember>());
                    }
                    ssiApp.getHouseholdComposition().add(member);

                });
            }


        }

        LogoutUtils.forceRelogIfNotCondition(request, ssiApp.getHospitalized());
        return new ModelAndView("redirect:/multi/disqualifyingCrit/view?uuid=" + uuid);
    }

    @GetMapping("/nextCompleted")
    protected ModelAndView nextComplete(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
                                        ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/disqualifyingCrit/view?uuid=" + uuid);
    }

}
