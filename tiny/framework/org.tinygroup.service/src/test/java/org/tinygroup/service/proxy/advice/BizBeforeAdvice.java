package org.tinygroup.service.proxy.advice;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class BizBeforeAdvice implements MethodBeforeAdvice {
    public void before(Method method, Object[] args, Object target) throws Throwable {

    }
}
