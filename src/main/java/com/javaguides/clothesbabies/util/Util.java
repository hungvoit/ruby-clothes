package com.javaguides.clothesbabies.util;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Locale;


@Component
public class Util {
    public String[] generateRandomString() {
        char[] tempPwd="0123456789abcdefghijklmnopqrstuvwxyz!@#$%&_+".toCharArray();
        String tempPassword = RandomStringUtils.random(8, 0, tempPwd.length, true, true,tempPwd, new SecureRandom());
        String hashPassword = new BCryptPasswordEncoder().encode(tempPassword);
        String results[] = {hashPassword,tempPassword};
        return results;
    }

    public static String getMessageString(String msgText, Object params[]) {
        Locale locale = new Locale(Locale.ENGLISH.getDisplayName());
        if (params != null) {
            MessageFormat mf = new MessageFormat(msgText, locale);
            msgText = mf.format(params, new StringBuffer(), null).toString();
        }
        return msgText;
    }
}
