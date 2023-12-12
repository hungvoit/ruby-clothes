package com.javaguides.clothesbabies.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    private final MessageSource messageSource;

    @Autowired
    public PropertyService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, Object... args) {
        String returnValue;
        try {
            returnValue = this.messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            returnValue = code;
        }
        return returnValue;
    }
}
