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
import org.tinygroup.parser.filter.FastNameFilter;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.parser.filter.QuickNameFilter;

public class NameFilterTest extends TestCase {
    HtmlNode node = null;

    public NameFilterTest() {
        node = new HtmlNode("root");
        for (int i = 0; i < 10; i++) {
            HtmlNode a = node.addNode(new HtmlNode("a" + i));
            for (int j = 0; j < 10; j++) {
                HtmlNode b = a.addNode(new HtmlNode("b" + j));
                for (int k = 0; k < 10; k++) {
                    b.addNode(new HtmlNode("c" + k));
                }
            }
        }
    }

    protected void setUp() throws Exception {
        super.setUp();

    }

    protected void tearDown() throws Exception {
        super.tearDown();
        node = null;
    }

    public void testSpeed() {
        long t21 = System.currentTimeMillis();
        QuickNameFilter quick = new QuickNameFilter(node);
        long t22 = System.currentTimeMillis();
        System.out.println("quick用时" + (t22 - t21));
        long t1 = System.currentTimeMillis();
        String nodeName = null;
        for (int x = 0; x < 1; x++) {
            nodeName = quick.findNode("b6").toString();
        }
        // System.out.println(nodeName);
        long t2 = System.currentTimeMillis();
        System.out.println("QuickNameFilter用时" + (t2 - t1));
    }

    public void testSpeed1() {
        long t21 = System.currentTimeMillis();
        FastNameFilter fast = new FastNameFilter(node);
        long t22 = System.currentTimeMillis();
        System.out.println("fast用时" + (t22 - t21));
        long t1 = System.currentTimeMillis();
        String nodeName = null;
        for (int x = 0; x < 1; x++) {
            nodeName = fast.findNode("b6").getNodeName();
        }
        // System.out.println(nodeName);
        long t2 = System.currentTimeMillis();
        System.out.println("FastNameFilter用时" + (t2 - t1));
    }

    public void testSpeed2() {
        long t11 = System.currentTimeMillis();
        NameFilter filter = new NameFilter(node);
        long t12 = System.currentTimeMillis();
        System.out.println("Name用时" + (t12 - t11));
        long t1 = System.currentTimeMillis();
        String nodeName = null;
        for (int x = 0; x < 10; x++) {
            nodeName = filter.findNode("b6").toString();
        }
        long t2 = System.currentTimeMillis();
        // System.out.println(nodeName);
        System.out.println("NameFilter用时" + (t2 - t1));
    }

}
