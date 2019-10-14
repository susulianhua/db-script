package org.tinygroup.service.proxy.test;

import org.junit.Before;
import org.junit.Test;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.service.util.ServiceTestUtil;

import java.util.List;

public class ServiceMixProxyTest {

    @Test
    public void testMix(){
        Context context = new ContextImpl();

        context.put("abcimpl", "123");
        context.put("defimpl", 456);

        ServiceTestUtil.execute("org.tinygroup.service.proxy.service.HelloService.start", context);
    }
}
