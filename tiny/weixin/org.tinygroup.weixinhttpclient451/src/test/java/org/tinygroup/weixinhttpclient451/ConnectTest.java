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
package org.tinygroup.weixinhttpclient451;

import junit.framework.Assert;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.weixin.WeiXinConnector;
import org.tinygroup.weixin.result.AccessToken;

public class ConnectTest {

    private static WeiXinConnector weiXinConnector;

    static {
        AbstractTestUtil.init(null, false);
        weiXinConnector = BeanContainerFactory.getBeanContainer(ConnectTest.class.getClassLoader()).getBean("weiXinConnector");
    }

    public static void main(String[] args) {
        AccessToken token = weiXinConnector.getAccessToken();
        Assert.assertNotNull(token);
    }
}
