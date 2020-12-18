package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import com.example.sbchainssioicdoauth2.utils.FormType;
import com.example.sbchainssioicdoauth2.utils.LogoutUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
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
import org.thymeleaf.util.StringUtils;

@Slf4j
@Controller
@RequestMapping("/multi/financialInfo")
public class FinancialInfoController {

    @Autowired
    CacheService cacheService;

    @Autowired
    PopulateInfoService infoService;

    @GetMapping("/view")
    protected ModelAndView financialInfo(@RequestParam(value = "uuid", required = true) String uuid,
                                         @RequestParam(value = "update", required = false) String update, ModelMap model, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException, JsonProcessingException {
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
        Map<String, String>[] householdComposition = ssiApp.getHouseholdComposition();
        model.addAttribute("houseHoldInfo", householdComposition);
        return new ModelAndView("financialInfo");
    }

    @GetMapping("/results")
    protected ModelAndView financialInfoResults(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {

        infoService.populateFetchInfo(model, request, uuid);
        return new ModelAndView("financialInfo");

    }

    @GetMapping("/update")
    protected ModelAndView logoutAndUpdate(@RequestParam(value = "uuid", required = true) String uuid, RedirectAttributes attr, ModelMap model, HttpServletRequest request) {
        SsiApplication ssiApp = cacheService.get(uuid);
        LogoutUtils.forceRelogIfNotCondition(request, null);
        return new ModelAndView("redirect:/multi/financialInfo/view?uuid=" + uuid + "&update=" + true);
    }


    @GetMapping("/continue")
    protected ModelAndView residenceInfoSave(@RequestParam(value = "uuid", required = true) String uuid, RedirectAttributes attr, ModelMap model, HttpServletRequest request) {
        SsiApplication ssiApp = cacheService.get(uuid);
        LogoutUtils.forceRelogIfNotCondition(request, ssiApp.getEmail());
        return new ModelAndView("redirect:/multi/electricityBill/view?uuid=" + uuid);
    }

    @GetMapping("/nextCompleted")
    protected ModelAndView nextComplete(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
                                        ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/electricityBill/view?uuid=" + uuid);
    }


    @GetMapping("/back")
    protected ModelAndView back(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
                                ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/disqualifyingCrit/view?uuid=" + uuid);
    }

}
