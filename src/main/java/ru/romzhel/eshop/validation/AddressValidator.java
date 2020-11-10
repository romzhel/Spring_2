package ru.romzhel.eshop.validation;

import org.springframework.stereotype.Component;
import ru.romzhel.eshop.entities.DeliveryAddress;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class AddressValidator implements ConstraintValidator<ValidAddress, DeliveryAddress> {
    public AddressValidator() {
    }

    @Override
    public boolean isValid(final DeliveryAddress address, final ConstraintValidatorContext context) {
        return address != null && address.getAddress() != null && !address.getAddress().isEmpty();
    }
}