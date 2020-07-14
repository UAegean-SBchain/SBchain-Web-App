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
@RequestMapping("/multi/residenceInfo")
public class ResidenceInfoController {

    @Autowired
    CacheService cacheService;

    @Autowired
    PopulateInfoService infoService;

    @GetMapping("/view")
    protected ModelAndView residenceInfo(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request, HttpSession session) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvocationTargetException, IntrospectionException {
        model.addAttribute("uuid", uuid);
        infoService.populateFetchInfo(model, request, uuid);
        SsiApplication ssiApp = cacheService.get(uuid);
        infoService.populateSsiApp(ssiApp, request, FormType.PERSONAL_DECLARATION.value, uuid);
        ssiApp = infoService.updateModelfromCacheMergeDB(ssiApp, model, request);
        cacheService.putInfo(ssiApp, uuid);
        return new ModelAndView("residenceInfo");
    }

    @GetMapping("/results")
    protected ModelAndView residenceInfoResults(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {

        infoService.populateFetchInfo(model, request, uuid);

        return new ModelAndView("residenceInfo");

    }

    @GetMapping("/continue")
    protected ModelAndView residenceInfoSave(@RequestParam(value = "uuid", required = true) String uuid, RedirectAttributes attr, ModelMap model, HttpServletRequest request) {
        SsiApplication ssiApp = cacheService.get(uuid);
        LogoutUtils.forceRelogIfNotCondition(request, ssiApp.getEmail());
        return new ModelAndView("redirect:/multi/contactDetails/view?uuid=" + uuid);
    }

    @GetMapping("/nextCompleted")
    protected ModelAndView nextComplete(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
            ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/contactDetails/view?uuid=" + uuid);
    }

    @GetMapping("/back")
    protected ModelAndView back(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
            ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/electricityBill/view?uuid=" + uuid);
    }
}
