package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import com.example.sbchainssioicdoauth2.service.ResourceService;
import com.example.sbchainssioicdoauth2.utils.FormType;
import com.example.sbchainssioicdoauth2.utils.LogoutUtils;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private final static Logger log = LoggerFactory.getLogger(PersonalInformationController.class);

    @GetMapping("/view")
    protected ModelAndView personalInfo(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        model.addAttribute("uuid", uuid);
        infoService.populateFetchInfo(model, request, uuid);
        SsiApplication ssiApp = cacheService.get(uuid);
        infoService.populateSsiApp(ssiApp, request, FormType.PERSONAL_DECLARATION.value, uuid);
        infoService.mergeModelFromCache(ssiApp, model, request);
        cacheService.putInfo(ssiApp, uuid);
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
            ModelMap model, HttpServletRequest request, HttpSession session) {

        SsiApplication ssiApp = cacheService.get(uuid);
        LogoutUtils.forceRelogIfNotCondition(request, ssiApp.getHospitalized());
        return new ModelAndView("redirect:/multi/disqualifyingCrit/view?uuid=" + uuid);
    }

    @GetMapping("/nextCompleted")
    protected ModelAndView nextComplete(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
            ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/disqualifyingCrit/view?uuid=" + uuid);
    }

}
