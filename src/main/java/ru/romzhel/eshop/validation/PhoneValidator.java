package ru.romzhel.eshop.validation;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {
    public static final String PHONE_PATTERN = "^(\\+?[7-8])?(\\d{10})$";
    private Pattern pattern;
    private Matcher matcher;

    public PhoneValidator() {
        pattern = Pattern.compile(PHONE_PATTERN);
    }

    @Override
    public boolean isValid(final String phone, final ConstraintValidatorContext context) {
        if (phone == null) {
            return false;
        }

        return checkPhone(phone);
    }

    private boolean checkPhone(String phone) {
        matcher = pattern.matcher(getNumbers(phone));
        return matcher.matches() && matcher.group(2) != null;
    }

    public String normalize(String phone) {
        if (checkPhone(phone)) {
            return "+7".concat(matcher.group(2));
        } else {
            throw new RuntimeException("Ошибка получения номера телефона");
        }
    }

    public String getNumbers(String phone) {
        return phone.replaceAll("[+\\s-)(]", "");
    }
}