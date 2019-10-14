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

import org.tinygroup.httpvisitor.builder.HttpFactory;

public class HttpPatchTest extends ServerTestCase {

    public void testPatch() {
        try {
            HttpFactory.patch("http://127.0.0.1:" + MockUtil.HTTP_PORT).execute();
        } catch (Exception e) {
            assertEquals("本HttpVisitor实现不支持PATCH操作!", e.getMessage());
        }

    }
}
