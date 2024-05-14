package com.example.demo.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("MANAGER"))) {
            response.sendRedirect("/manager/home");
        } else if (authorities.contains(new SimpleGrantedAuthority("BUILDER"))) {
            response.sendRedirect("/builder/home");
        } else {
            // Handle other roles or scenarios
            response.sendRedirect("/login"); // Redirect to login page by default
        }
    }

/*extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        if(response.isCommitted()) return;
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication){
        String url = "/login?error=true";
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = new ArrayList<>();
        for(GrantedAuthority a : authorities){
            roles.add(a.getAuthority());
        }
        if(roles.contains("MANAGER")){
            url = "/manager/home";
        }else if(roles.contains("BUILDER")) {
            url = "/builder/home";
        }
        return url;
    }*/
}
