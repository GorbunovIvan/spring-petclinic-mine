package com.example.owner;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PetValidator implements Validator {

    @Override
    public void validate(Object target, Errors errors) {

        Pet pet = (Pet) target;
        String name = pet.getName();

        // name
        if (!StringUtils.hasLength(name)) {
            errors.rejectValue("name", "required", "required");
        }

        // type
        if (pet.isNew() && pet.getType() == null) {
            errors.rejectValue("type", "required", "required");
        }

        // birth date
        if (pet.getBirthDate() == null) {
            errors.rejectValue("birthDate", "required", "required");
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Pet.class.isAssignableFrom(clazz);
    }
}
