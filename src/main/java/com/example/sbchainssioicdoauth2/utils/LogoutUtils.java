/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sbchainssioicdoauth2.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nikos
 */
@Slf4j
public class LogoutUtils {

    private final static Logger log = LoggerFactory.getLogger(LogoutUtils.class);

    public static void forceRelogIfNotCondition(HttpServletRequest request, Object controlObject) {
        if (controlObject == null) {
            try {
                request.logout();
            } catch (ServletException e) {
                log.error(e.getMessage());
            }
        }
    }

}
