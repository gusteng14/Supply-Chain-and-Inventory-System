package com.barkstore.Barkstore.Security;

import com.barkstore.Barkstore.appuser.MyUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication
    ) throws IOException, ServletException {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        System.out.println("Username: " + userDetails.getUsername());
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        authorities.forEach(auth -> System.out.println(auth.getAuthority()));
        super.onAuthenticationSuccess(request, response, chain, authentication);
    }
}
