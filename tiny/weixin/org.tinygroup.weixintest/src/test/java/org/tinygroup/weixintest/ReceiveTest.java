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
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.weixinhttp.WeiXinHttpConnector;

import java.io.File;

/**
 * 测试演示工程的推送消息
 *
 * @author yancheng11334
 */
public class ReceiveTest {

    private static WeiXinHttpConnector weiXinHttpConnector;

    static {
        AbstractTestUtil.init(null, false);
        weiXinHttpConnector = BeanContainerFactory.getBeanContainer(KfMessageTest.class.getClassLoader()).getBean(WeiXinHttpConnector.DEFAULT_BEAN_NAME);
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String url = "http://127.0.0.1:8080/org.tinygroup.weixinservice/service.do";
        String xml = FileUtil.readFileContent(new File("src/test/resources/TextMessage.xml"), "utf-8");
        String result = weiXinHttpConnector.postUrl(url, xml, null);
        Assert.assertNotNull(result);
    }

}
