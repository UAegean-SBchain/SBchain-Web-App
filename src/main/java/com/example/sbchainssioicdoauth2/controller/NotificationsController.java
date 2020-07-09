package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/multi/notifications")
public class NotificationsController {

    // @Autowired
    // CacheService cacheService;
    @Autowired
    CacheService cacheService;

    @Autowired
    PopulateInfoService infoService;

    private static final String SSI_REQUEST_PARAMS = "ssiInformation";

    @GetMapping("/view")
    protected ModelAndView notifications(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {
        model.addAttribute("uuid", uuid);
        return new ModelAndView("notifications");
    }

    @GetMapping("/results")
    protected ModelAndView notificationsResults(@AuthenticationPrincipal OAuth2User oidcUser, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {

        // log.info("zzzzzzzzzzzzzzzzzzzzzz user :{}", oidcUser);
        // //request.setAttribute("household", oidcUser.getAttributes());
        // Map<String,String> household = new HashMap<>();
        // household.put("key1", "value1");
        // household.put("key2", "value2");
        // request.setAttribute("household", household);
        infoService.populateFetchInfo(model, request, uuid);
        log.info("ssssssssssssssssss :{}", cacheService.get(uuid));

        return new ModelAndView("notifications");

    }

    @GetMapping("/continue")
    protected ModelAndView notificationsSubmit(@AuthenticationPrincipal OidcUser oidcUser, RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {

        //model.addAttribute("userAttr", oidcUser.getAttributes());
        // FinancialInfo financialInfo = fillFinancialInfo(oidcUser);
        // request.getSession().setAttribute("householdInfo", financialInfo);
        attr.addAttribute("uuid", uuid);
        try {
            request.logout();
        } catch (ServletException e) {
            log.error(e.getMessage());
        }

        return new ModelAndView("redirect:/multi/amounts/view");
    }

    @GetMapping("/nextCompleted")
    protected ModelAndView nextComplete(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
            ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/amounts/view?uuid=" + uuid);
    }

}
