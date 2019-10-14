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
package org.tinygroup.weixinpay;

import junit.framework.TestCase;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.weixin.WeiXinConvertMode;
import org.tinygroup.weixin.impl.WeiXinContextDefault;
import org.tinygroup.weixin.util.WeiXinParserUtil;
import org.tinygroup.weixinpay.result.QueryRefundResult;

import java.io.File;

/**
 * 动态XML报文解析
 *
 * @author yancheng11334
 */
public class DynamicXmlTest extends TestCase {

    protected void setUp() throws Exception {
        AbstractTestUtil.init(null, false);
    }

    public void testQueryRefundResult() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/QueryRefundResult.xml"), "utf-8");
        QueryRefundResult result = parse(xml);

        assertNotNull(result);
        assertEquals(1, result.getRefundNumber());
        assertEquals(1, result.getRefundResultList().size());
    }

    private <T> T parse(String xml) {
        return WeiXinParserUtil.parse(xml, new WeiXinContextDefault(), WeiXinConvertMode.SEND);
    }
}
