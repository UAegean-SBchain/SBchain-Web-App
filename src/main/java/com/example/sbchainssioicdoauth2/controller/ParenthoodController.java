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
@RequestMapping("/multi/parenthood")
public class ParenthoodController {

    @Autowired
    CacheService cacheService;

    @Autowired
    PopulateInfoService infoService;

    @GetMapping("/view")
    protected ModelAndView parenthoodView(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, IntrospectionException, InvocationTargetException {
        model.addAttribute("uuid", uuid);
        infoService.populateFetchInfo(model, request, uuid);
        SsiApplication ssiApp = cacheService.get(uuid);
        infoService.populateSsiApp(ssiApp, request, FormType.PERSONAL_DECLARATION.value, uuid);
        ssiApp = infoService.updateModelfromCacheMergeDB(ssiApp, model, request, uuid);
        cacheService.putInfo(ssiApp, uuid);

        model.addAttribute("isMale", ssiApp.getGender().equals("male"));
        model.addAttribute("isFemale", ssiApp.getGender().equals("female"));
        model.addAttribute("singleParent", ssiApp.getParenthood().equals("true"));

        return new ModelAndView("parenthood");
    }

    @GetMapping("/results")
    protected ModelAndView parenthoodResults(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {

        infoService.populateFetchInfo(model, request, uuid);

        return new ModelAndView("parenthood");

    }

    @GetMapping("/continue")
    protected ModelAndView parenthoodSave(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {
//        try {
//            request.logout();
//        } catch (ServletException e) {
//            log.error(e.getMessage());
//        }
//        return new ModelAndView("redirect:/multi/financialInfo/view");
//        return new ModelAndView("redirect:/multi/residenceInfo/view?uuid=" + uuid);
        return new ModelAndView("redirect:/multi/amounts/view?uuid=" + uuid);

    }

    @GetMapping("/nextCompleted")
    protected ModelAndView nextComplete(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
            ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/amounts/view?uuid=" + uuid);
    }

    @GetMapping("/back")
    protected ModelAndView back(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
            ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/contactDetails/view?uuid=" + uuid);
    }
}
