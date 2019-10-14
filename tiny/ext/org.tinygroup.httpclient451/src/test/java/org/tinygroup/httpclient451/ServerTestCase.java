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

import junit.framework.TestCase;
import org.tinygroup.httpclient451.mock.MockServer;
import org.tinygroup.tinyrunner.Runner;

import java.util.ArrayList;

/**
 * 扩展了Mock服务
 *
 * @author yancheng11334
 */
public abstract class ServerTestCase extends TestCase {

    private MockServer server = new MockServer();

    protected void setUp() {
        Runner.init("application.xml", new ArrayList<String>());
        server.start();
    }

    protected void tearDown() {
        server.stop();
    }

}
