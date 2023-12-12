package com.javaguides.clothesbabies.oauth;

public interface AccessTokenValidator {
    AccessTokenValidationResult validate(String accessToken);
}
