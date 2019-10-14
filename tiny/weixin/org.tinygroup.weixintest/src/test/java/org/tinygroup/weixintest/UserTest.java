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
import org.tinygroup.weixin.WeiXinConnector;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.common.ToServerMessage;
import org.tinygroup.weixin.impl.WeiXinContextDefault;
import org.tinygroup.weixin.result.AccessToken;
import org.tinygroup.weixinuser.message.UserInfoMessage;
import org.tinygroup.weixinuser.message.UserListMessage;
import org.tinygroup.weixinuser.result.UserInfoResult;
import org.tinygroup.weixinuser.result.UserListResult;

public class UserTest {

    private static WeiXinConnector weiXinConnector;

    static {
        AbstractTestUtil.init(null, false);
        weiXinConnector = BeanContainerFactory.getBeanContainer(KfMessageTest.class.getClassLoader()).getBean("weiXinConnector");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        AccessToken token = weiXinConnector.getAccessToken();
        Assert.assertNotNull(token);

        testGetUserInfo();
        testGetUserList();
    }

    public static void testGetUserInfo() {
        UserInfoMessage message = new UserInfoMessage();
        message.setLang("zh_CN");
        message.setOpenId("oH7YcuDMbvNuwRMpWRu5BNOS21vU");
        UserInfoResult result = send(message);
        Assert.assertNotNull(result);
    }

    public static void testGetUserList() {
        UserListMessage message = new UserListMessage();
        UserListResult result = send(message);
        Assert.assertNotNull(result);
    }

    private static <OUTPUT> OUTPUT send(ToServerMessage message) {
        WeiXinContext context = new WeiXinContextDefault();
        context.put(WeiXinConnector.ACCESS_TOKEN, weiXinConnector.getAccessToken().getAccessToken());
        weiXinConnector.getWeiXinSender().send(message, context);
        return context.getOutput();
    }

}
