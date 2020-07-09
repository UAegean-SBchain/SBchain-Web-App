package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import com.example.sbchainssioicdoauth2.utils.FormType;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/multi/electricityBill")
public class ElectricityBillController {

    @Autowired
    CacheService cacheService;

    @Autowired
    PopulateInfoService infoService;

    @GetMapping("/view")
    protected ModelAndView electricityBillInfo(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        model.addAttribute("uuid", uuid);
        infoService.populateFetchInfo(model, request, uuid);
        SsiApplication ssiApp = cacheService.get(uuid);
        infoService.populateSsiApp(ssiApp, request, FormType.PERSONAL_DECLARATION.value, uuid);
        infoService.mergeModelFromCache(ssiApp, model, request);
        cacheService.putInfo(ssiApp, uuid);

        return new ModelAndView("electricityBill");
    }

//    @GetMapping("/results")
//    protected ModelAndView electricityBillInfoResults(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {
//
//        infoService.populateFetchInfo(model, request, uuid);
//        return new ModelAndView("electricityBill");
//
//    }
    @GetMapping("/continue")
    protected ModelAndView electricityBillInfoSubmit(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {

//        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
//        SsiApplication ssiApp = cacheService.get(uuid);
//        infoService.populateSsiApp(ssiApp, context, FormType.ELECTRICITY_BILL_INFO.value, uuid);
//        cacheService.putInfo(ssiApp, uuid);
//        attr.addAttribute("uuid", uuid);
//        try {
//            request.logout();
//        } catch (ServletException e) {
//            log.error(e.getMessage());
//        }
        return new ModelAndView("redirect:/multi/residenceInfo/view?uuid=" + uuid);
    }

    @GetMapping("/nextCompleted")
    protected ModelAndView nextComplete(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
            ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/residenceInfo/view?uuid=" + uuid);
    }

    @GetMapping("/back")
    protected ModelAndView back(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
            ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/householdInfo/view?uuid=" + uuid);
    }
}
