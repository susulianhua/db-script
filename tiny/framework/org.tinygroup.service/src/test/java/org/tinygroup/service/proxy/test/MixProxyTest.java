package org.tinygroup.service.proxy.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tinygroup.commons.beanutil.BeanUtil;
import org.tinygroup.commons.spring.util.AopTargetUtil;
import org.tinygroup.service.proxy.service.HelloService;

import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MixProxyTest {

    private ApplicationContext applicationContext;

    @Before
    public void init(){
        applicationContext =
                new ClassPathXmlApplicationContext("fixproxy.beans.xml");
    }

    @Test
    public void testProxy() throws NoSuchMethodException {

        HelloService helloService = applicationContext.getBean("helloServiceImpl",HelloService.class);
        Method originMethod = AopTargetUtil.getMixProxyTarget(helloService).getClass()
                .getMethod("start",String.class,Integer.class);
        String[] originParams =
                BeanUtil.getMethodParameterName(originMethod);
        assertNotNull(originParams);
        assertEquals(originParams.length,2);
        assertEquals(originParams[0],"abcimpl");
        assertEquals(originParams[1],"defimpl");

        HelloService helloProxy = applicationContext.getBean("helloProxy",HelloService.class);
        Method method = AopTargetUtil.getMixProxyTarget(helloProxy).getClass().getMethod("start",String.class,Integer.class);
        String[] parameterNames =
                BeanUtil.getMethodParameterName(method);
        assertNotNull(parameterNames);
        assertEquals(parameterNames.length,2);
        assertEquals(parameterNames[0],"abcimpl");
        assertEquals(parameterNames[1],"defimpl");

        HelloService cgProxy = applicationContext.getBean("cgProxy",HelloService.class);
        method = AopTargetUtil.getMixProxyTarget(cgProxy).getClass().getMethod("start",String.class,Integer.class);
        String[] cgProxyParams =
                BeanUtil.getMethodParameterName(method);
        assertArrayEquals(cgProxyParams,parameterNames);

        //两层代理
        HelloService mixProxy = applicationContext.getBean("mixProxy",HelloService.class);
        method = AopTargetUtil.getMixProxyTarget(mixProxy).getClass().getMethod("start",String.class,Integer.class);
        String fixProxyParams[] =
                BeanUtil.getMethodParameterName(method);
        assertArrayEquals(fixProxyParams,parameterNames);

        //三层代理
        HelloService mixMixProxy = applicationContext.getBean("mixMixProxy",HelloService.class);
        method = AopTargetUtil.getMixProxyTarget(mixMixProxy).getClass().getMethod("start",String.class,Integer.class);
        String[] fixFixProxyParams =
                BeanUtil.getMethodParameterName(method);
        assertArrayEquals(fixFixProxyParams,parameterNames);
        assertArrayEquals(originParams,fixFixProxyParams);
    }


}
