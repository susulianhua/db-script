/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.tinygroup.commons.spring.util;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Spring Aop工具类
 */
public class AopTargetUtil {

    /**
     * 获取目标对象
     * @param proxy 代理对象
     * @return
     */
    public static Object getTarget(Object proxy) {
        // 是否代理对象
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }

        try {
            // JDK动态代理
            if (AopUtils.isJdkDynamicProxy(proxy)) {
                return getJdkDynamicProxyTargetObject(proxy);
            // CGLIB动态代理
            } else {
                return getCglibProxyTargetObject(proxy);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    /**
     * 获取多层代理的实现类
     * @param proxy 代理对象
     * @return
     */
    public static Object getMixProxyTarget(Object proxy) {
        // 是否代理对象
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }

        try {
            // JDK动态代理
            Object target=null;
            if (AopUtils.isJdkDynamicProxy(proxy)) {
                target=getJdkDynamicProxyTargetObjectJVM(proxy);
            // CGLIB动态代理
            } else {
                target= getCglibProxyTargetObject(proxy);
            }
            if(target==null){
                throw new RuntimeException("获取代理对象不能为空");
            }
            if(AopUtils.isAopProxy(target)){
                return getMixProxyTarget(target);
            }
            return target;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    /**
     * 获取CGLIB动态代理目标对象
     * @param proxy
     * @return
     * @throws Exception
     */
    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        
        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        return target == null ? proxy : target;
    }
    
    /**
     * 获取JDK动态代理目标对象
     * @param proxy
     * @return
     * @throws Exception
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        return target == null ? proxy : target;
    }
    
    /**
     * 获取JDK动态代理目标对象
     * @param proxy
     * @return
     * @throws Exception
     */
    private static Object getJdkDynamicProxyTargetObjectJVM(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target =  ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        if(target != null){
        	return target;
        }
        ProxyFactory px = (ProxyFactory) advised.get(aopProxy);
        Method advice = px.getAdvisors()[0].getAdvice().getClass().getDeclaredMethod("getTarget", new Class[]{});
        advice.setAccessible(true);
        target =  advice.invoke(px.getAdvisors()[0].getAdvice(), new Object[]{});
        return target;
    }
}