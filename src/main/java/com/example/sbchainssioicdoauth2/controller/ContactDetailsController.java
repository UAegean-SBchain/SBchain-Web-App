package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import com.example.sbchainssioicdoauth2.utils.FormType;
import com.example.sbchainssioicdoauth2.utils.LogoutUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

@Slf4j
@Controller
@RequestMapping("/multi/contactDetails")
public class ContactDetailsController {

    @Autowired
    CacheService cacheService;

    @Autowired
    PopulateInfoService infoService;

    @GetMapping("/view")
    protected ModelAndView contactDetailsView(@RequestParam(value = "uuid", required = true) String uuid,
                                              @RequestParam(value = "update", required = false) String update,
                                              ModelMap model, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        model.addAttribute("uuid", uuid);
        infoService.populateFetchInfo(model, request, uuid);
        SsiApplication ssiApp = cacheService.get(uuid);
        infoService.populateSsiApp(ssiApp, request, FormType.PERSONAL_DECLARATION.value, uuid);
        ssiApp = infoService.updateModelfromCacheMergeDB(ssiApp, model, request, uuid);
        if (!StringUtils.isEmpty(update)) {
            if (update.equals("true")) {
                infoService.mergeNoNCrucialCredential(ssiApp, request, FormType.PERSONAL_DECLARATION.value, uuid);
            }
        }
        cacheService.putInfo(ssiApp, uuid);
        return new ModelAndView("contactDetails");
    }

    @GetMapping("/results")
    protected ModelAndView disqualifyingInfoResults(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {
        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

        infoService.populateFetchInfo(model, request, uuid);
        model.addAttribute("email", context.getIdToken().getEmail());

        return new ModelAndView("contactDetails");
    }

    @GetMapping("/continue")
    protected ModelAndView contactDetailsSave(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {
        SsiApplication ssiApp = cacheService.get(uuid);
        LogoutUtils.forceRelogIfNotCondition(request, ssiApp.getGender());
        return new ModelAndView("redirect:/multi/parenthood/view?uuid=" + uuid);

    }

    @GetMapping("/nextCompleted")
    protected ModelAndView nextComplete(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
                                        ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/parenthood/view?uuid=" + uuid);
    }

    @GetMapping("/back")
    protected ModelAndView back(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
                                ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/electricityBill/view?uuid=" + uuid);
    }


    @GetMapping("/update")
    protected ModelAndView logoutAndUpdate(@RequestParam(value = "uuid", required = true) String uuid, RedirectAttributes attr, ModelMap model, HttpServletRequest request) {
        SsiApplication ssiApp = cacheService.get(uuid);
        LogoutUtils.forceRelogIfNotCondition(request,null);
        return new ModelAndView("redirect:/multi/contactDetails/view?uuid=" + uuid + "&update=" + true);
    }
}
