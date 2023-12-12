package com.javaguides.clothesbabies.util;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
   public void initialize(ValidPassword constraint) {
   }

   public boolean isValid(String password, ConstraintValidatorContext context) {
      PasswordValidator validator = new PasswordValidator(Arrays.asList(
              new LengthRule(8, 30),
              new UppercaseCharacterRule(1),
              new SpecialCharacterRule(1),
              new WhitespaceRule()));

      RuleResult result = validator.validate(new PasswordData(password));
      if (result.isValid()) {
         return true;
      }
      List<String> messages = validator.getMessages(result);
      context.buildConstraintViolationWithTemplate(String.join(", ", messages))
              .addConstraintViolation()
              .disableDefaultConstraintViolation();
      return false;
   }
}
