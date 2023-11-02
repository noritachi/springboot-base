package com.base.auth.validation.impl;


import com.base.auth.constant.UserBaseConstant;
import com.base.auth.validation.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class StatusValidation  implements ConstraintValidator<Status, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(Status constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }
    @Override
    public boolean isValid(Integer status, ConstraintValidatorContext constraintValidatorContext) {
        if(status == null && allowNull){
            return true;
        }
        if(!Objects.equals(status, UserBaseConstant.STATUS_ACTIVE)
                && !Objects.equals(status, UserBaseConstant.STATUS_LOCK)
                && !Objects.equals(status, UserBaseConstant.STATUS_DELETE)
                && !Objects.equals(status, UserBaseConstant.STATUS_PENDING)){
            return false;
        }
        return true;
    }
}
