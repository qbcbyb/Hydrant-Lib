package cn.qbcbyb.library.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import cn.qbcbyb.library.util.LinkedCachedHashMap;

public abstract class BaseModel implements Serializable {

    private final String TAG = this.getClass().getSimpleName();

    private static final long serialVersionUID = -8604228656515547902L;
    private static final String METHODNAME = "{0}#{1}";
    private static final LinkedCachedHashMap<String, Method> cacheMap;

    static {
        cacheMap = new LinkedCachedHashMap<String, Method>(25, 0.1f, true, null);
    }

    public Object get(String field) {
        Object value = null;
        String[] fields = field.split("\\.");
        Object sourceObj = this;
        int length = fields.length;
        for (int i = 0; i < length; i++) {
            String fieldName = fields[i];
            if (sourceObj == null) {
                return null;
            }
            Object result = getObject(sourceObj, fieldName);
            if (i != length - 1) {
                sourceObj = result;
            } else {
                value = result;
            }
        }
        return value;
    }

    private Object getObject(Object sourceObj, String fieldName) {
        Object value = null;
        try {
            Class<?> clazz = sourceObj.getClass();
            String methodName = MessageFormat.format(METHODNAME, clazz.getName(), fieldName);
            Method method = cacheMap.get(methodName);
            if (method == null) {
                String methodNameSuffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    method = clazz.getMethod("get" + methodNameSuffix);
                } catch (NoSuchMethodException e) {
                    try {
                        method = clazz.getMethod("is" + methodNameSuffix);
                    } catch (NoSuchMethodException e1) {
                        try {
                            method = clazz.getMethod("has" + methodNameSuffix);
                        } catch (NoSuchMethodException e2) {
                        }
                    }
                }
                cacheMap.put(methodName, method);
            }
            if (method != null) {
                value = method.invoke(sourceObj);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    public <T> T fromJson(String value) {
        T object = null;
        try {
            object = (T) JSON.parseObject(value, this.getClass());
        } catch (Exception e) {
        }
        return object;
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }
}
