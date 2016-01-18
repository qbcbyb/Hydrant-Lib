package com.activeandroid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by qbcby on 2015/10/22.
 */
public class FieldProperty {
    private Field realField;
    private Method getterMethod;
    private Method setterMethod;

    public FieldProperty(Field field) {
        this.realField = field;
        Class<?> type = field.getType();
        Class clazz = field.getDeclaringClass();
        String fieldName = field.getName();
        final String firstChar = fieldName.substring(0, 1);
        String methodNameSuffix = firstChar.toUpperCase() + fieldName.substring(1);
        try {
            getterMethod = clazz.getMethod("get" + methodNameSuffix);
        } catch (NoSuchMethodException e) {
            try {
                getterMethod = clazz.getMethod("is" + methodNameSuffix);
            } catch (NoSuchMethodException e1) {
                try {
                    getterMethod = clazz.getMethod("has" + methodNameSuffix);
                } catch (NoSuchMethodException e2) {
                }
            }
        }
        try {
            setterMethod = clazz.getMethod("set" + methodNameSuffix, type);
        } catch (NoSuchMethodException e) {
        }
        if (getterMethod != null) {
            getterMethod.setAccessible(true);
        }
        if (setterMethod != null) {
            setterMethod.setAccessible(true);
        }
    }

    public Method getGetterMethod() {
        return getterMethod;
    }

    public Method getSetterMethod() {
        return setterMethod;
    }

    public String getName() {
        return realField.getName();
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return realField.getAnnotation(annotationType);
    }

    public Class<?> getType() {
        return realField.getType();
    }
}
