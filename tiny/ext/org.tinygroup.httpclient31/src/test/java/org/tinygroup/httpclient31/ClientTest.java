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
package org.tinygroup.httpclient31;

import org.tinygroup.httpvisitor.Response;
import org.tinygroup.httpvisitor.builder.HttpFactory;

import java.io.IOException;

/**
 * 客户端配置的一些测试
 *
 * @author yancheng11334
 */
public class ClientTest extends ServerTestCase {

    public void testHttps() throws IOException {
        Response response = HttpFactory
                .get("https://127.0.0.1:" + MockUtil.HTTPS_PORT + "/ssl").execute();
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("hello world", response.text());
        response.close();
    }

//	public void testProxy() {
//		Response response = HttpFactory
//				.get("http://www.youtobe.com").proxy("1.193.162.91", 8000).execute();
//		System.out.println(response.text());
//	}
}
