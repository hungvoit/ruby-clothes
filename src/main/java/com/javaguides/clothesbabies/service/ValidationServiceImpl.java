package com.javaguides.clothesbabies.service;

import com.javaguides.clothesbabies.dto.UserRegistrationDto;
import com.javaguides.clothesbabies.repository.UserRepository;
import com.javaguides.clothesbabies.util.RestTemplateBuilderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.Map;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    PropertyService propertyService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BindingResult validateUser(UserRegistrationDto request, BindingResult result) {
        Errors errors = new BeanPropertyBindingResult(request, "COMMAND_NAME");
        String message = null;
        if (StringUtils.hasText(request.getEmail())) {
            final String API_URL = "https://emailvalidation.abstractapi.com/v1/?api_key=%s&auto_correct=false&email=%s";
            String fullUrl = String.format(API_URL, "9588c442f4bd4e38932e6dba06a42d25", request.getEmail().trim());
            Map<String, Object> map = null;
            try {
                map = RestTemplateBuilderUtil.restTemplateGetBuilder(fullUrl, "");
                if (map != null && map.get("deliverability") != null && map.get("quality_score") != null
                        && (!"DELIVERABLE".equalsIgnoreCase(map.get("deliverability").toString())
                        || Float.parseFloat(map.get("quality_score").toString()) < 0.6f)) {
                    message = this.propertyService.getMessage("email.real");
                } else {
                    if (this.checkIfEmailExist(request.getEmail())) {
                        message = this.propertyService.getMessage("email.exist");
                    }
                }
                if (StringUtils.hasText(message)) errors.rejectValue("email", message);
            } catch (Exception ex) {
                throw new UnsupportedOperationException(ex.getMessage());
            }
        }
        return result;
    }

    private boolean checkIfEmailExist(String email) {
        return this.userRepository.findByEmail(email) != null;
    }
}
