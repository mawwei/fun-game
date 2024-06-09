package com.trun.fun.common.validtor;

import com.trun.fun.common.annotations.EnumValidAnnotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 枚举类型检验实现方法类
 * @author  mawei
 */
public class EnumValidtor implements ConstraintValidator<EnumValidAnnotation,String> {

    Class<?>[] cls; //枚举类

    @Override
    public void initialize(EnumValidAnnotation constraintAnnotation) {
        cls = constraintAnnotation.target();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if(cls.length>0 ){
            for (Class<?> cl : cls
            ) {
                try {
                    if(cl.isEnum()){
                        //枚举类验证
                        Object[] objs = cl.getEnumConstants();
                        Method  method = cl.getMethod("name");
                        for (Object obj : objs
                        ) {
                            Object code = method.invoke(obj,null);
                            if(value.equals(code.toString())){
                                return true;
                            }
                        }
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }


}