package com.javaguides.clothesbabies.oauth.google;

import com.javaguides.clothesbabies.oauth.AccessTokenValidationResult;
import com.javaguides.clothesbabies.oauth.AccessTokenValidator;
import com.javaguides.clothesbabies.util.RestTemplateBuilderUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import java.util.Map;

public class GoogleTokenValidator implements AccessTokenValidator {

    @Override
    public AccessTokenValidationResult validate(String accessToken) {
        Map<String, ?> response = getGoogleResponse(accessToken);
        boolean validationResult = validateResponse(response);
        return new AccessTokenValidationResult(validationResult, response);
    }

    private boolean validateResponse(Map<String, ?> response) throws AuthenticationException {
        String aud = (String) response.get("aud");
        if (!StringUtils.hasText(aud)) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> getGoogleResponse(String accessToken) {
        final String API_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=%s";
        String fullUrl = String.format(API_URL, accessToken);
        return RestTemplateBuilderUtil.restTemplateGetBuilder(fullUrl, "");
    }
}
