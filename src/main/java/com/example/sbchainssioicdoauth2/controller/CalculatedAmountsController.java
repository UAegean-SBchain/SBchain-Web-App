package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.DBService;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import com.example.sbchainssioicdoauth2.utils.FormType;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/multi/amounts")
public class CalculatedAmountsController {

    @Autowired
    private DBService submitService;

    @Autowired
    CacheService cacheService;

    @Autowired
    PopulateInfoService infoService;

    @GetMapping("/view")
    protected ModelAndView calculatedAmountsView(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, IntrospectionException, InvocationTargetException {
        model.addAttribute("uuid", uuid);
        infoService.populateFetchInfo(model, request, uuid);
        SsiApplication ssiApp = cacheService.get(uuid);
        infoService.populateSsiApp(ssiApp, request, FormType.PERSONAL_DECLARATION.value, uuid);
        ssiApp = infoService.updateModelfromCacheMergeDB(ssiApp, model, request, uuid);
        cacheService.putInfo(ssiApp, uuid);

        String issueUrl = StringUtils.isEmpty(System.getenv("ISSUE_URL")) ? "https://dss.aegean.gr/sbchain/vc/issue/benefit" : System.getenv("ISSUE_URL");
        model.addAttribute("issueUrl", issueUrl);
        return new ModelAndView("calculatedAmounts");
    }

    @GetMapping("/results")
    protected ModelAndView calculatedAmountsResults(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {
        infoService.populateFetchInfo(model, request, uuid);
        return new ModelAndView("calculatedAmounts");
    }

    @GetMapping("/save")
    protected ModelAndView calculatedAmountsSave(@AuthenticationPrincipal OidcUser oidcUser, RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {

//        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
//        SsiApplication ssiApp = cacheService.get(uuid);
//        infoService.populateSsiApp(ssiApp, context, FormType.INCOME_GUARANTEE.value, uuid);
//        submitService.submit(ssiApp);
//        cacheService.evictSsiAppCache(uuid);
//        model.addAttribute("ssiInfo", context.getIdToken().getOtherClaims());
//        // cacheService.putInfo(ssiApp, uuid);
//        // attr.addAttribute("uuid", uuid);
//        // try {
//        //     request.logout();
//        // } catch (ServletException e) {
//        //     log.error(e.getMessage());
//        // }
        return new ModelAndView("calculatedAmounts");
    }

    @GetMapping("/back")
    protected ModelAndView back(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
            ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/parenthood/view?uuid=" + uuid);
    }
}
