package com.javaguides.clothesbabies.service;

import com.javaguides.clothesbabies.dto.UserDto;
import com.javaguides.clothesbabies.model.User;
import com.javaguides.clothesbabies.repository.UserRepository;
import com.javaguides.clothesbabies.util.RestTemplateBuilderUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.Map;
import java.util.Optional;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    PropertyService propertyService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BindingResult validateUser(UserDto request, BindingResult result, String objectName, boolean isRegister) {
        Errors errors = new BeanPropertyBindingResult(request, objectName);
        String message = null;
        if (StringUtils.isNotEmpty(request.getEmail())) {
            /*final String API_URL = "https://emailvalidation.abstractapi.com/v1/?api_key=%s&auto_correct=false&email=%s";
            String fullUrl = String.format(API_URL, "9588c442f4bd4e38932e6dba06a42d25", userDto.getEmail().trim());
            Map<String, Object> map = null;
            try {
                map = RestTemplateBuilderUtil.restTemplateGetBuilder(fullUrl, "");
                if (map != null && map.get("deliverability") != null && map.get("quality_score") != null
                        && (!"DELIVERABLE".equalsIgnoreCase(map.get("deliverability").toString())
                        || Float.parseFloat(map.get("quality_score").toString()) < 0.6f)) {
                    message = this.propertyService.getMessage("email.real");
                } else {
                    if (this.checkIfEmailExist(userDto.getEmail())) {
                        message = this.propertyService.getMessage("email.exist");
                    }
                }
                if (StringUtils.hasText(message)) errors.rejectValue("email", message);
            } catch (Exception ex) {
                throw new UnsupportedOperationException(ex.getMessage());
            }*/

            if (request.getId() == null && this.checkIfEmailExist(request.getEmail())) {
                message = this.propertyService.getMessage("email.exist");
            }
            if (StringUtils.isNotEmpty(message)) {
                errors.rejectValue("email", "email", message);
            }
        }
        if (StringUtils.isNotEmpty(request.getPhone())) {
            if (this.checkIfPhoneExist(request.getPhone())) {
                errors.rejectValue("phone", "phone", this.propertyService.getMessage("phone.exist"));
            }
            String phone = request.getPhone().replaceAll("[^\\d]", "");
            if (phone.length() < 11) {
                errors.rejectValue("phone", "phone", this.propertyService.getMessage("min.phone"));
            } else if (phone.length() > 11) {
                errors.rejectValue("phone", "phone", this.propertyService.getMessage("max.phone"));
            }
        } else {
            if (isRegister)
                errors.rejectValue("phone", "phone", this.propertyService.getMessage("phone.required"));
        }
        if (StringUtils.isNotEmpty(request.getOldPin())) {
            if (this.checkPasswordOld(request.getId(), request.getOldPin()))
                errors.rejectValue("oldPin", "oldPin", this.propertyService.getMessage("oldpin.incorrect"));
        }
        if (StringUtils.isNotEmpty(request.getNewPin())
                && StringUtils.isNotEmpty(request.getRenterNewPin())
                && !request.getRenterNewPin().equalsIgnoreCase(request.getNewPin())) {
            errors.rejectValue("renterNewPin", "renterNewPin", this.propertyService.getMessage("compare.pin"));
        }
        result.addAllErrors(errors);
        return result;
    }

    @Override
    public BindingResult validateForgotPassword(UserDto request, BindingResult result) {
        Errors errors = new BeanPropertyBindingResult(request, "user");
        String emailTmp = request.getEmail();
        String phoneTmp = request.getPhone();
        String messageCode = null;
        if (StringUtils.isNotEmpty(emailTmp) || StringUtils.isNotEmpty(phoneTmp)) {
            User user = this.userRepository.findByPhoneOrEmail(phoneTmp, emailTmp);
            if (user == null) {
                if (StringUtils.isNotEmpty(phoneTmp) && StringUtils.isNotEmpty(emailTmp)) {
                    messageCode = "both.email.phone.exist.not";
                } else if (StringUtils.isNotEmpty(phoneTmp)) {
                    messageCode = "phone.exist.not";
                } else {
                    messageCode = "email.exist.not";
                }
            } else {
                if (user.getTmp_pwd_flag() > 0) {
                    messageCode = "change.many.time.pass";
                }
            }
        } else {
            messageCode = "both.email.phone.required";
        }
        if (StringUtils.isNotEmpty(messageCode)) {
            errors.reject("globalError", this.propertyService.getMessage(messageCode));
        }
        result.addAllErrors(errors);
        return result;
    }


    private boolean checkIfEmailExist(String email) {
        return this.userRepository.findByPhoneOrEmail(null, email) != null;
    }

    private boolean checkIfPhoneExist(String phone) {
        return this.userRepository.findByPhoneOrEmail(phone, null) != null;
    }

    private boolean checkPasswordOld(Long id, String oldPassword) {
        Optional<User> userOpt = this.userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String passwordDB = user.getPassword();
            return !new BCryptPasswordEncoder().matches(oldPassword, passwordDB);
        }
        return false;
    }
}
