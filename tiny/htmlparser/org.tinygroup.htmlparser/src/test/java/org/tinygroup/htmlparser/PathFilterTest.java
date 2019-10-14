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
import org.tinygroup.htmlparser.parser.HtmlStringParser;
import org.tinygroup.parser.NodeFilter;
import org.tinygroup.parser.filter.PathFilter;

public class PathFilterTest extends TestCase {
    NodeFilter filter;

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFindNodeList() {
        HtmlNode node = new HtmlNode("aa");
        filter = new PathFilter(node);
        assertEquals(1, filter.findNodeList("/aa").size());
    }

    public void testFindNodeList1() {
        HtmlNode node = new HtmlNode("aa");
        node.addNode(new HtmlNode("b"));
        node.addNode(new HtmlNode("b"));
        filter = new PathFilter(node);
        assertEquals(2, filter.findNodeList("/AA/B").size());
    }

    public void testFindNodeList3() {
        HtmlNode node = new HtmlStringParser().parse("<a><b></b></a><c></c>").getRoot();
        System.out.println(node.toString());
        filter = new PathFilter(node);
        filter.setIncludeByNode("a");
//		assertEquals(1, filter.findNodeList("/a").size());
        assertEquals(1, filter.findNodeList("/B").size());
    }

    public void testFindNodeList2() {
        HtmlNode node = new HtmlNode("aa");
        // node.addNode(new HtmlNode("b"));
        HtmlNode n = node.addNode(new HtmlNode("b"));
        n.addNode(new HtmlNode("c"));
        HtmlNode c = n.addNode(new HtmlNode("c"));
        c.setAttribute("a", "a");
        filter = new PathFilter(c);
        assertEquals(2, filter.findNodeList("../../aa/b/c").size());

        filter = new PathFilter(c);
        filter.setIncludeAttributes("a");
        assertEquals(1, filter.findNodeList("../../aa/b/c").size());
    }
}
