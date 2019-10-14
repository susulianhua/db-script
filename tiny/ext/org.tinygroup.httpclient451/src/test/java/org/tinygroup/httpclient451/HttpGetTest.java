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
package org.tinygroup.httpclient451;

import org.tinygroup.commons.tools.StringEscapeUtil;
import org.tinygroup.httpvisitor.Cookie;
import org.tinygroup.httpvisitor.Response;
import org.tinygroup.httpvisitor.builder.HttpFactory;
import org.tinygroup.httpvisitor.struct.SimpleCookie;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpGetTest extends ServerTestCase {

    public void testGet() throws IOException {

        // GBK编码
        Response response = HttpFactory
                .get("http://127.0.0.1:" + MockUtil.HTTP_PORT + "/charset1.do").execute();
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("中文,GBK", response.charset("GBK").text());
        response.close();

        // 默认编码
        response = HttpFactory.get("http://127.0.0.1:" + MockUtil.HTTP_PORT + "/charset2.do")
                .execute();
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("中文,utf-8", response.text());
        response.close();

        // 请求URL参数
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("p2", "56");
        maps.put("p3", 789);
        response = HttpFactory.get("http://127.0.0.1:" + MockUtil.HTTP_PORT + "/param.do?p0=123")
                .param("p1", 4).params(maps).execute();
        assertEquals("123456789", response.text());
        response.close();

        // 请求头操作
        Map<String, String> headMaps = new HashMap<String, String>();
        headMaps.put("Pragma", "no-cache");
        headMaps.put("Cache-Control", "no-cache");
        headMaps.put("Referer", "http://download.google.com/");
        headMaps.put("User-Agent",
                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");

        response = HttpFactory.get("http://127.0.0.1:" + MockUtil.HTTP_PORT + "/header.do")
                .header("Host", "download.google.com").headers(headMaps)
                .execute();
        assertEquals(headMaps.get("User-Agent"), response.text());

        // 响应头操作
        assertEquals("Jetty(8.1.17.v20150415)", response.getHeader("Server")
                .getValue());
        response.close();

        // cookie操作
        Map<String, Cookie> cookieMaps = new HashMap<String, Cookie>();
        cookieMaps.put("tinyage", new SimpleCookie("tinyage", "2014", new Date(
                System.currentTimeMillis() + 1000000L), null, null, false));
        response = HttpFactory.get("http://127.0.0.1:" + MockUtil.HTTP_PORT + "/cookie.do")
                .cookie(null, "tiny_version", "2.1.1").cookies(cookieMaps)
                .execute();
        assertEquals(cookieMaps.get("tinyage").getValue(), response.text());

        assertEquals("2.1.1", response.getCookie("tiny_version").getValue());
        response.close();

        // 重定向
        response = HttpFactory.get("http://127.0.0.1:" + MockUtil.HTTP_PORT + "/redirect.do")
                .execute();
        assertEquals("hello world", response.text()); // 默认支持跳转，转到默认的hello world
        response.close();
        response = HttpFactory.get("http://127.0.0.1:" + MockUtil.HTTP_PORT + "/redirect.do")
                .allowRedirects(false).execute();
        assertEquals("", response.text()); // 禁止重定向，停留当前页
        response.close();

        // 设置User-Agent
        response = HttpFactory.get("http://127.0.0.1:" + MockUtil.HTTP_PORT + "/agent.do").execute();
        assertEquals("HttpClient4.5.1", response.text()); // 默认的User-Agent
        response.close();

        response = HttpFactory.get("http://127.0.0.1:" + MockUtil.HTTP_PORT + "/agent.do")
                .userAgent("IE10.0").execute();
        assertEquals("IE10.0", response.text()); // 通过接口设置
        response.close();

        response = HttpFactory.get("http://127.0.0.1:" + MockUtil.HTTP_PORT + "/agent.do")
                .header("User-Agent", "Chrome45.0").execute();
        assertEquals("Chrome45.0", response.text()); // 通过Header设置
        response.close();

        //测试支持xml格式的header
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Finance><Message id=\"abc\"><name>标准协议</name></Message></Finance>";
        String newxml = StringEscapeUtil.escapeURL(xml, "UTF-8");
        response = HttpFactory.get("http://127.0.0.1:" + MockUtil.HTTP_PORT + "/xmlheader.do").header("tiny-xmlsignature", newxml).execute();
        assertEquals(xml, StringEscapeUtil.unescapeURL(response.text(), "UTF-8")); // 服务端成功解析xml
        response.close();
    }
}
