package com.example.sbchainssioicdoauth2.config.security;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Slf4j
@KeycloakConfiguration
public class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
        grantedAuthorityMapper.setPrefix("ROLE_");

        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    @Override
    @ConditionalOnMissingBean(HttpSessionManager.class)
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/rest/nonce");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .csrf().disable().cors()
                .and()
                .authorizeRequests()
                .antMatchers("/db/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/rest/**").permitAll()
                .antMatchers("/rest/nonce/**").permitAll()
                .antMatchers("/multi/review/**").hasRole("personal_info")
                .antMatchers("/multi/personalInfo/**").hasRole("personal_info")
                .antMatchers("/multi/disqualifyingCrit/**").hasRole("personal_info")
                .antMatchers("/multi/employment/**").hasRole("personal_info")
                .antMatchers("/multi/financialInfo/**").hasRole("personal_info")
                .antMatchers("/multi/assetInfo/**").hasRole("personal_info")
                .antMatchers("/multi/householdInfo/**").hasRole("personal_info")
                .antMatchers("/multi/electricityBill/**").hasRole("personal_info")
                .antMatchers("/multi/residenceInfo/**").hasRole("personal_info")
                .antMatchers("/multi/contactDetails/**").hasRole("personal_info")
                .antMatchers("/multi/parenthood/**").hasRole("personal_info")
                .antMatchers("/multi/fead/**").hasRole("personal_info")
                .antMatchers("/multi/amounts/**").hasRole("personal_info")
                .anyRequest().permitAll()
                .and()
                .sessionManagement()
                .sessionFixation().migrateSession()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

    }

    //request debuger, remove
    // @Override
    // public void configure(WebSecurity web) throws Exception {
    //     web.debug(true);
    // }
    // private GrantedAuthoritiesMapper userAuthoritiesMapper() {
    //   return (authorities) -> {
    //     Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
    //     authorities.forEach(
    //         authority -> {
    //           if (authority instanceof OidcUserAuthority) {
    //             OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
    //             OidcIdToken idToken = oidcUserAuthority.getIdToken();
    //             OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
    //           //   List<SimpleGrantedAuthority> groupAuthorities =
    //           //       userInfo.getClaimAsStringList("groups").stream()
    //           //           .map(g -> new SimpleGrantedAuthority("ROLE_" + g.toUpperCase()))
    //           //           .collect(Collectors.toList());
    //            mappedAuthorities.add(new SimpleGrantedAuthority("user"));
    //           // List<SimpleGrantedAuthority> groupAuthorities =
    //           //       userInfo.getClaimAsStringList("claims").stream()
    //           //           .map(g -> new SimpleGrantedAuthority("ROLE_" + g.toUpperCase()))
    //           //           .collect(Collectors.toList());
    //           // mappedAuthorities.addAll(groupAuthorities);
    //           }
    //         });
    //     return mappedAuthorities;
    //   };
    // }
}
