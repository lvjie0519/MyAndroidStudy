package com.android.study.example.androidapi.customstatusbar;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {
    final private static String TAG = "ReflectionUtil";

    /**
     * 通过反射机制执行某对象方法
     *
     * @param owner      对象
     * @param methodName 方法名
     * @param args       方法参数
     * @return
     * @throws Exception
     */
    public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
        return invokeMethodInternal(owner, methodName, false, args);
    }


    /**
     * 通过反射机制执行某对象hide方法
     *
     * @param owner      对象
     * @param methodName 方法名
     * @param args       方法参数
     * @return
     * @throws Exception
     */
    public static Object invokeHideMethod(Object owner, String methodName, Object[] args) throws Exception {
        return invokeMethodInternal(owner, methodName, true, args);

    }

    private static Object invokeMethodInternal(Object owner, String methodName, boolean isHide, Object[] args) throws Exception {
        Class ownerClass = owner.getClass();
        Method method = null;
        if (args != null && args.length > 0) {
            Class[] argsClass = new Class[args.length];
            for (int i = 0, j = args.length; i < j; i++) {
                //argsClass[i] = args[i].getClass();
                argsClass[i] = convertIfPrimitive(args[i]);
            }
            //使private方法或者hide的方法可以被调用
            if (isHide) {
                method = ownerClass.getDeclaredMethod(methodName, argsClass);
                method.setAccessible(true);
            } else {
                method = ownerClass.getMethod(methodName, argsClass);
            }
            return method.invoke(owner, args);
        } else {
            method = ownerClass.getMethod(methodName);
            //使private方法或者hide的方法可以被调用
            if (isHide) {
                method = ownerClass.getDeclaredMethod(methodName);
                method.setAccessible(true);
            }else{
                method = ownerClass.getMethod(methodName);
            }
            return method.invoke(owner);
        }

    }


    /**
     * 通过反射执行某类的静态方法
     *
     * @param className
     * @param methodName
     * @param args
     * @return
     * @throws Exception
     */
    public static Object invokeStaticMethod(String className, String methodName,
                                            Object[] args) throws Exception {
        Class ownerClass = Class.forName(className);

        if (args != null && args.length > 0) {
            Class[] argsClass = new Class[args.length];
            for (int i = 0, j = args.length; i < j; i++) {
                //argsClass[i] = args[i].getClass();
                argsClass[i] = convertIfPrimitive(args[i]);
            }
            Method method = ownerClass.getMethod(methodName, argsClass);
            return method.invoke(null, args);
        } else {
            Method method = ownerClass.getMethod(methodName);
            return method.invoke(null);
        }
    }

    private static Class convertIfPrimitive(Object object){
        Log.d(TAG,"arg0:"+object);
        Class objClass = object.getClass();
        if(!objClass.isPrimitive()){
            Log.d(TAG,"objClass:"+objClass);
            if(object instanceof Integer){
                Log.d(TAG,"arg0:"+object);
                objClass = int.class;
            }else if(object instanceof Long){
                objClass = long.class;
            }else if(object instanceof Double){
                objClass = double.class;
            }else if (object instanceof Float){
                objClass = float.class;
            }else if (object instanceof Boolean){
                objClass = boolean.class;
            }else if(object instanceof Byte){
                objClass = byte.class;
            }else if(object instanceof Character){
                objClass = char.class;
            }

        }
        return objClass;


    }
    public static void setFieldValue(Object targetObject, String filedName, Object filedvalue){
        try{
            Field field = targetObject.getClass().getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(targetObject, filedvalue);
        }catch (Exception e){
            Log.e(TAG, "setFieldValue->filedName:" + filedName);
            Log.e(TAG, "setFieldValue->value:" + filedvalue);
            Log.e(TAG, "setFieldValue->exception:" + e);
        }

    }

    public static Object getFieldValue(Object targetObject, String filedName){
        try{
            Field field = targetObject.getClass().getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(targetObject);
        }catch (Exception e){
            Log.e(TAG, "getFieldValue->filedName:" + filedName);
            Log.e(TAG, "getFieldValue->exception:" + e);
        }
        return null;
    }

    public static Object invokeMethod(Object object, String methodName, Class<?>[]paramTypes, Object[] values){
        try{
            Method method = object.getClass().getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method.invoke(object, values);
            //return method.invoke(object, paramTypes);
        }catch (Exception e){
            Log.e(TAG, "invokeMethod->methodName:" + methodName);
            Log.e(TAG, "invokeMethod->exception:" + e);
            e.printStackTrace();
        }
        return null;
    }

}
