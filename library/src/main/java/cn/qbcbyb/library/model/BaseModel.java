package cn.qbcbyb.library.model;

import android.database.Cursor;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import cn.qbcbyb.library.util.LinkedCachedHashMap;

public class BaseModel implements Serializable {

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
                break;
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
                try {
                    method = clazz.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                } catch (NoSuchMethodException e) {
                    try {
                        method = clazz.getMethod("is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                    } catch (NoSuchMethodException e1) {
                        try {
                            method = clazz.getMethod("has" + fieldName.substring(0, 1).toUpperCase()
                                    + fieldName.substring(1));
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

    public void fromCursor(Cursor cursor) {
        String[] fieldNames = cursor.getColumnNames();
        for (int j = 0; j < fieldNames.length; j++) {
            String fieldName = fieldNames[j];
            int i = cursor.getColumnIndex(fieldName);
            try {
                Class<?> clazz = this.getClass();
                Class<?> type = clazz.getDeclaredField(fieldName).getType();
                Method method = clazz.getMethod(
                        "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), type);
                if (type == String.class) {
                    method.invoke(this, cursor.getString(i));
                } else if (type == double.class || type == Double.class) {
                    method.invoke(this, cursor.getDouble(i));
                } else if (type == int.class || type == Integer.class) {
                    method.invoke(this, cursor.getInt(i));
                } else if (type == float.class || type == Float.class) {
                    method.invoke(this, cursor.getFloat(i));
                } else if (type == long.class || type == Long.class) {
                    method.invoke(this, cursor.getLong(i));
                } else if (type == short.class || type == Short.class) {
                    method.invoke(this, cursor.getShort(i));
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
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
