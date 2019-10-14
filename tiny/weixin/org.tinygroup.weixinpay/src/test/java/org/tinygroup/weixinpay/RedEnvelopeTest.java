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
import org.tinygroup.weixinpay.result.CommonRedEnvelopeResult;
import org.tinygroup.weixinpay.result.GroupRedEnvelopeResult;
import org.tinygroup.weixinpay.result.QueryRedEnvelopeResult;

import java.io.File;

/**
 * 测试红包相关接口
 *
 * @author yancheng11334
 */
public class RedEnvelopeTest extends TestCase {

    protected void setUp() throws Exception {
        AbstractTestUtil.init(null, false);
    }

    /**
     * 测试发送普通红包结果的解析
     *
     * @throws Exception
     */
    public void testCommonRedEnvelopeResult() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/CommonRedEnvelopeResult.xml"), "utf-8");
        CommonRedEnvelopeResult result = parse(xml);
        assertEquals(100, result.getTotalAmount());
        assertEquals("1244859502", result.getMchId());
    }

    /**
     * 测试发送裂变红包结果的解析
     *
     * @throws Exception
     */
    public void testGroupRedEnvelopeResult() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/GroupRedEnvelopeResult.xml"), "utf-8");
        GroupRedEnvelopeResult result = parse(xml);
        assertEquals(300, result.getTotalAmount());
        assertEquals(3, result.getTotalNum());
        assertEquals("100493101201512182159997298", result.getOrderId());
    }

    /**
     * 测试查询普通红包结果的解析
     *
     * @throws Exception
     */
    public void testQueryCommonRedEnvelope() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/QueryCommonRedEnvelope.xml"), "utf-8");
        QueryRedEnvelopeResult result = parse(xml);
        assertNotNull(result.getRedEnvelopeInfoList());
        assertEquals("NORMAL", result.getRedEnvelopeType());
        assertEquals("API", result.getSendType());
        assertEquals("0010244084201601150422687235", result.getRedEnvelopeId());
        assertEquals(1, result.getRedEnvelopeInfoList().getIntoList().size());
    }

    /**
     * 测试查询裂变红包结果的解析
     *
     * @throws Exception
     */
    public void testQueryGroupRedEnvelope() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/QueryGroupRedEnvelope.xml"), "utf-8");
        QueryRedEnvelopeResult result = parse(xml);
        assertNotNull(result.getRedEnvelopeInfoList());
        assertEquals("GROUP", result.getRedEnvelopeType());
        assertEquals("API", result.getSendType());
        assertEquals("0010244084201601160427757215", result.getRedEnvelopeId());
        assertEquals(2, result.getRedEnvelopeInfoList().getIntoList().size());
    }

    private <T> T parse(String xml) {
        return WeiXinParserUtil.parse(xml, new WeiXinContextDefault(), WeiXinConvertMode.SEND);
    }
}
