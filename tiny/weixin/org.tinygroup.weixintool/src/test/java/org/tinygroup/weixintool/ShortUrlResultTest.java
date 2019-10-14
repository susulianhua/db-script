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
package org.tinygroup.weixintool;

import junit.framework.TestCase;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.weixin.WeiXinConvertMode;
import org.tinygroup.weixin.util.WeiXinParserUtil;
import org.tinygroup.weixintool.convert.json.ShortUrlResultConvert;
import org.tinygroup.weixintool.message.ShortUrl;
import org.tinygroup.weixintool.result.ShortUrlResult;

import java.io.File;

public class ShortUrlResultTest extends TestCase {

    /**
     * 测试生成JSON报文
     */
    public void testMessage() {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setAction("long2short");
        shortUrl.setLongUrl("http://wap.koudaitong.com/v2/showcase/goods?alias=128wi9shh&spm=h56083&redirect_count=1");

        assertEquals("{\"action\":\"long2short\",\"long_url\":\"http://wap.koudaitong.com/v2/showcase/goods?alias=128wi9shh&spm=h56083&redirect_count=1\"}", shortUrl.toString());
    }

    /**
     * 测试解析JSON报文
     *
     * @throws Exception
     */
    public void testResult() throws Exception {

        String json = FileUtil.readFileContent(new File("src/test/resources/json.txt"), "utf-8");

        WeiXinParserUtil.addJsonConvert(new ShortUrlResultConvert());

        ShortUrlResult result = WeiXinParserUtil.parse(json, null, WeiXinConvertMode.SEND);
        assertNotNull(result);
        assertEquals("http://w.url.cn/s/AvCo6Ih", result.getShortUrl());
    }
}
