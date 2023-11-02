package com.base.auth.validation.impl;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.validation.Gender;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class GenderValidation implements ConstraintValidator<Gender, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(Gender constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer gender, ConstraintValidatorContext constraintValidatorContext) {
        if(gender == null && allowNull){
            return true;
        }
        if(!Objects.equals(gender, UserBaseConstant.GENDER_FEMALE)
                && !Objects.equals(gender, UserBaseConstant.GENDER_MALE)
                && !Objects.equals(gender, UserBaseConstant.GENDER_OTHER)){
            return false;
        }
        return true;
    }
}
