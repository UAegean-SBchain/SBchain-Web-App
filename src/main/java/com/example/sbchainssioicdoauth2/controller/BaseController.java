package com.example.sbchainssioicdoauth2.controller;

import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class BaseController {

    // @Value("${spring.security.oauth2.client.registration.keycloak.scope}")
    // private String scope;

    @GetMapping("/")
    protected ModelAndView landingView(ModelMap model, HttpServletRequest request, RedirectAttributes attr){

        try {
            request.logout();
        } catch (ServletException e) {
            log.error(e.getMessage());
        }
        UUID uuid = UUID.randomUUID();
        //attr.addAttribute("uuid", uuid);
        model.addAttribute("uuid", uuid);
        log.info("create new uuid :{}", uuid);

        //return new ModelAndView("redirect:/multi/personalInfo/view");
        return new ModelAndView("landingPage");
    }

    @GetMapping("/logout")
    protected ModelAndView logout(HttpServletRequest request){
        try {
            request.logout();
        } catch (ServletException e) {
            log.error(e.getMessage());
        }

        return new ModelAndView("redirect:/");

    }
    
}