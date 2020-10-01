package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.DBService;
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

@Slf4j
@Controller
@RequestMapping("/multi/householdInfo")
public class HouseholdController {

    @Autowired
    CacheService cacheService;

    @Autowired
    PopulateInfoService infoService;

    @Autowired
    private DBService submitService;

    @GetMapping("/view")
    protected ModelAndView householdInfo(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) throws IllegalArgumentException, InvocationTargetException, IntrospectionException, IllegalAccessException, JsonProcessingException {
        model.addAttribute("uuid", uuid);
        infoService.populateFetchInfo(model, request, uuid);
        SsiApplication ssiApp = cacheService.get(uuid);
        infoService.populateSsiApp(ssiApp, request, FormType.PERSONAL_DECLARATION.value, uuid);
        ssiApp = infoService.updateModelfromCacheMergeDB(ssiApp, model, request, uuid);
        cacheService.putInfo(ssiApp, uuid);

        Map<String, String>[] householdComposition = ssiApp.getHouseholdComposition();
        model.addAttribute("houseHoldInfo", householdComposition);
        return new ModelAndView("householdInfo");
    }

//    @GetMapping("/results")
//    protected ModelAndView householdInfoResults(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
//        Map<String, String>[]
//        ] householdComposition = new HashMap<>();
//
//        householdComposition.put(String.valueOf(context.getIdToken().getOtherClaims().get("member")), String.valueOf(context.getIdToken().getOtherClaims().get("relation")));
//        model.addAttribute("houseHoldInfo", householdComposition);
//        model.addAttribute("uuid", uuid);
//
//        infoService.populateFetchInfo(model, request, uuid);
//        return new ModelAndView("householdInfo");
//
//    }
    @GetMapping("/continue")
    protected ModelAndView householdInfoSubmit(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {
        SsiApplication ssiApp = cacheService.get(uuid);
        LogoutUtils.forceRelogIfNotCondition(request, ssiApp.getSupplyType());
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
        return new ModelAndView("redirect:/multi/assetInfo/view?uuid=" + uuid);
    }

}
