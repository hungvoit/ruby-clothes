package com.javaguides.clothesbabies.util;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RestTemplateBuilderUtil {
    @Bean
    public static RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    public static Map<String, Object> restTemplateGetBuilder(String fullUrl, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.hasText(accessToken)) {
            headers.set("Authorization", "Bearer " + accessToken);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplateBuilder().build().exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class).getBody();
    }

    public static List<?> restTemplateGetBuilderList(String fullUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return restTemplateBuilder().build().exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<?>>() {}).getBody();
    }
}
