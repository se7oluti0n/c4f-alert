package com.c4f.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class JSON2Object {

    private Class<?> cls;
    private JSONObject json;

    public JSON2Object(Class<?> cls, JSONObject json) {
        this.cls = cls;
        this.json = json;
    }

    @SuppressLint("DefaultLocale")
    public Object parse() {
        try {
            Constructor<?> ctor = cls.getConstructor();
            Object obj = ctor.newInstance();

            Map<String, Class<?>[]> methodPamarsMap = new HashMap<String, Class<?>[]>();

            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.startsWith("set")) {
                    Class<?>[] params = method.getParameterTypes();
                    methodPamarsMap.put(methodName, params);
                }
            }

            Iterator<?> i = json.keys();
            while (i.hasNext()) {
                String key = (String) i.next();
                String chars[] = key.split("_");
                String methodName = "";
                for (String s : chars) {
                    methodName += s.substring(0, 1).toUpperCase() + s.substring(1);
                }

                methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
                Field field = null;

                try {

                    field = obj.getClass().getDeclaredField(methodName);
                    field.setAccessible(true);
                    Object fieldValue = json.get(key);

                    if (fieldValue != null && !"null".equals(fieldValue.toString())) {
                        if (Integer.TYPE.equals(field.getType())) {
                            field.setInt(obj, Integer.parseInt(fieldValue.toString()));
                        } else if (Long.TYPE.equals(field.getType())) {
                            field.setLong(obj, Long.parseLong(fieldValue.toString()));
                        } else if (String.class.equals(field.getType())) {
                            field.set(obj, fieldValue.toString());
                        } else if (ArrayList.class.equals(field.getType())) {
                            JSONArray arr = (JSONArray) fieldValue;

                            String className = methodName.replace("s", "");
                            className = className.substring(0, 1).toUpperCase() + className.substring(1);
                            className = cls.getPackage().getName() + "." + className;

                            ArrayList<Object> lst = new ArrayList<Object>();
                            for (int idx = 0; idx < arr.length(); idx++) {
                                JSONObject jo = arr.getJSONObject(idx);
                                JSON2Object j2o = new JSON2Object(Class.forName(className), jo);
                                Object o = j2o.parse();
                                lst.add(o);
                            }
                            field.set(obj, lst);
                        } else {
                            field.set(obj, fieldValue);
                        }
                    }

                } catch (NoSuchFieldException e) {
                    Log.d("JSON2Object", field == null ? "" : field.getName() + ": " + e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    Log.d("JSON2Object", field == null ? "" : field.getName() + ": " + e.getMessage(), e);
                } catch (IllegalArgumentException e) {
                    Log.d("JSON2Object", field == null ? "" : field.getName() + ": " + e.getMessage(), e);
                }
            }
            return obj;
        } catch (Exception e) {
            Log.d("JSON2Object", e.getMessage(), e);
        }
        return null;
    }

    public static Object getObjectFromJSON(JSONObject JSON, Class C) throws Throwable {

        Object o = C.newInstance();
        JSONObject j = JSON;
        Iterator<?> keys = j.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String val = (String) j.get(key);

            try {

                Field f = C.getDeclaredField(key);
                f.set(o, val);

            } catch (Exception err) {
                throw new Exception(err.getMessage());
            }
        }
        return o;

    }

}
