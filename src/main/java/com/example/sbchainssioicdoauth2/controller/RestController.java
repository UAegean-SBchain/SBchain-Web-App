/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.DBService;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author nikos
 */
@Slf4j
@Controller
@RequestMapping("/rest")
public class RestController {

    @Autowired
    private DBService dbServ;

    @Autowired
    private CacheService cs;

    @RequestMapping(value = "/nonce", method = {RequestMethod.GET}, produces = "application/json")
    public @ResponseBody
    String getNonce() throws UnsupportedEncodingException {
        String nonce = generateNonce();
        //cache it
        cs.putNonce(nonce);
        return nonce;

    }

    @RequestMapping(value = "/isBeneficiary", method = {RequestMethod.GET}, produces = "application/json")
    public @ResponseBody
    boolean isBeneficiary(@RequestParam String afm, @RequestParam String nonce, @RequestParam String hashed) {
        String salt = System.getenv("SALT") != null ? System.getenv("SALT") : "salt";
        String sha256hex = DigestUtils.sha256Hex(nonce + salt);
        if (hashed.equals(sha256hex) && cs.isNonce(nonce) && dbServ.getByTaxisAfm(afm.trim()).isPresent()) {
            return true;
        }
        return false;
    }

    private static String generateNonce() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
