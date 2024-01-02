package com.javaguides.clothesbabies.security;

import com.javaguides.clothesbabies.common.URI;
import com.javaguides.clothesbabies.model.User;
import com.javaguides.clothesbabies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
         String redirectUrl = null;
         //Forward page by role
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ADMIN")) {
                redirectUrl = "/dashboard";
                break;
            } else {
                redirectUrl = "/home";
                break;
            }
        }
        if (redirectUrl == null) {
            redirectUrl = "/403";
        }
        //Forward page change password
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String email = userDetail.getUsername();
        User user = this.userService.findByEmail(email);
        if (user != null) {
            boolean forwardChangePassword = false;
            if (user.getTmp_pwd_flag() > 0) {
                forwardChangePassword = true;
            }
            if (forwardChangePassword) redirectUrl = URI.USERS + "/showChangePassword";
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
