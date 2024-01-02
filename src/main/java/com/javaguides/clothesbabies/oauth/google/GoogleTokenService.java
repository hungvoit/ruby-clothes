package com.javaguides.clothesbabies.oauth.google;

import com.javaguides.clothesbabies.dto.enums.RoleEnum;
import com.javaguides.clothesbabies.model.Role;
import com.javaguides.clothesbabies.model.User;
import com.javaguides.clothesbabies.oauth.AccessTokenValidationResult;
import com.javaguides.clothesbabies.oauth.OAuthPrincipal;
import com.javaguides.clothesbabies.repository.UserRepository;
import com.javaguides.clothesbabies.service.PropertyService;
import com.javaguides.clothesbabies.util.RestTemplateBuilderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.singleton;

public class GoogleTokenService implements ResourceServerTokenServices {

    @Autowired
    PropertyService propertyService;

    private final GoogleTokenValidator tokenValidator;
    private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

    private UserRepository userRepository;

    public GoogleTokenService(GoogleTokenValidator tokenValidator, UserRepository userRepository) {
        this.tokenValidator = tokenValidator;
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        AccessTokenValidationResult validationResult = tokenValidator.validate(accessToken);
        if (!validationResult.isValid()) {
            throw new UnapprovedClientAuthenticationException(this.propertyService.getMessage("loadAuthentication.UnapprovedClientAuthenticationException"));
        }
        Map<String, ?> tokenInfo = validationResult.getTokenInfo();
        return getAuthentication(tokenInfo, accessToken);
    }

    private OAuth2Authentication getAuthentication(Map<String, ?> tokenInfo, String accessToken) {
        OAuth2Request request = tokenConverter.extractAuthentication(tokenInfo).getOAuth2Request();
        Authentication authentication = getAuthenticationToken(accessToken);
        return new OAuth2Authentication(request, authentication);
    }

    private Authentication getAuthenticationToken(String accessToken) {
        Map<String, ?> userInfo = getUserInfo(accessToken);
        String principalId = (String) userInfo.get("sub");
        if (principalId == null) {
            throw new InternalAuthenticationServiceException(this.propertyService.getMessage("getAuthenticationToken.InternalAuthenticationServiceException"));
        }
        User user = this.userRepository.findByPrincipalId(principalId);
        String email = (String) userInfo.get("email");
        if (user == null) {
            user = userRepository.findByEmail(email);
        }
        if (user == null) {
            user = new User();
            user.setPrincipalId(principalId);
            user.setCreateDate(new Date());
            user.setEmail(email);
            user.setFirstName((String) userInfo.get("given_name"));
            user.setLastName((String) userInfo.get("family_name"));
            user.setPassword("");
            user.setRole(new Role(RoleEnum.CUSTOMER.name()));
        } else {
            user.setPrincipalId(principalId);
        }
        try {
            this.userRepository.save(user);
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(this.propertyService.getMessage("getAuthenticationToken.InternalAuthenticationServiceException.err.CreateUserAC"));
        }
        return new UsernamePasswordAuthenticationToken(new OAuthPrincipal(new BigInteger(principalId)), null, singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        final String fullUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        return RestTemplateBuilderUtil.restTemplateGetBuilder(fullUrl, accessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException(this.propertyService.getMessage("readAccessToken.UnsupportedOperationException"));
    }
}
