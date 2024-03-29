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
package org.tinygroup.htmlparser;

import junit.framework.TestCase;
import org.tinygroup.htmlparser.node.HtmlNode;

public class INoteTest2 extends TestCase {
    HtmlNode node = null;

    protected void setUp() throws Exception {
        node = new HtmlNode(HtmlNodeType.COMMENT);
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetFooter() {
        StringBuffer sb = new StringBuffer();
        node.getFooter(sb);
        assertEquals("-->", sb.toString());
    }
}
