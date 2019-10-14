/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.weixintest;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.weixin.WeiXinSession;
import org.tinygroup.weixin.WeiXinSessionManager;
import org.tinygroup.weixin.impl.WeiXinSessionDefault;

/**
 * 测试微信的Session会话接口
 *
 * @author yancheng11334
 */
public class WeiXinSessionTest {

    private static WeiXinSessionManager weiXinSessionManager;

    static {
        AbstractTestUtil.init(null, false);
        weiXinSessionManager = BeanContainerFactory.getBeanContainer(KfMessageTest.class.getClassLoader()).getBean("weiXinSessionManager");
        weiXinSessionManager.clear();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        String userId = "yc_293404327";

        addSession(userId);
        removeSession(userId);

        expireSession();
    }

    public static void addSession(String userId) {
        WeiXinSession session = new WeiXinSessionDefault(userId);
        String s = "abcdefig";
        Integer num = 100;
        session.setParameter("string", s);
        session.setParameter("int", num);
        weiXinSessionManager.addWeiXinSession(session);

        WeiXinSession result = weiXinSessionManager.getWeiXinSession(userId);
        Assert.assertNotNull(result);
        Assert.assertTrue(s.equals(result.getParameter("string")));
        Assert.assertTrue(num.equals(result.getParameter("int")));
    }

    public static void removeSession(String userId) {
        weiXinSessionManager.removeWeiXinSession(userId);
        Assert.assertNull(weiXinSessionManager.getWeiXinSession(userId));
    }

    public static void expireSession() {
        weiXinSessionManager.addWeiXinSession(new WeiXinSessionDefault("no_expire"));
        weiXinSessionManager.addWeiXinSession(new WeiXinSessionDefault("fast_expire", 1));
        WeiXinSession[] array = weiXinSessionManager.getWeiXinSessions();
        Assert.assertNotNull(array);
        Assert.assertTrue(2 == array.length);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //忽略异常
        }

        weiXinSessionManager.expireWeiXinSessions();
        array = weiXinSessionManager.getWeiXinSessions();
        Assert.assertNotNull(array);
        Assert.assertTrue(1 == array.length);
    }

}
