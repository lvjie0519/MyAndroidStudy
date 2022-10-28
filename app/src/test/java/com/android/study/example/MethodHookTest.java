package com.android.study.example;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MethodHookTest {

    /**
     * hook类需要实现的接口
     */
    interface IGetUserName{
        String getUserName();
    }

    /**
     * 接口实现
     */
    static class GetUserName implements IGetUserName{

        @Override
        public String getUserName() {
            return "teacher";
        }
    }

    /**
     * hook代理
     */
    static class GetUserNameProxy implements IGetUserName{

        public String getUserName(){
            IGetUserName iGetUserName = (IGetUserName) Proxy.newProxyInstance(GetUserName.class.getClassLoader(), GetUserName.class.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return "student";
                }
            });
            return iGetUserName.getUserName();
        }
    }
    //需要hook iGetUserName成员
    static IGetUserName iGetUserName = new GetUserName();

    @Test
    public void testHook(){
        try {
            System.out.println("===========  start ==============");
            System.out.println("before hook: "+iGetUserName.getUserName());
            Field field = MethodHookTest.class.getDeclaredField("iGetUserName");
            field.setAccessible(true);
            field.set(null,new GetUserNameProxy());
            System.out.println("after hook: "+iGetUserName.getUserName());
            System.out.println("===========  end ==============");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
