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
package org.tinygroup.xmlparser.node;

import junit.framework.TestCase;
import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xmlparser.XmlDocument;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.io.File;
import java.util.List;

public class XmlNodePathTest extends TestCase {
    XmlDocument doc = null;

    protected void setUp() throws Exception {
        File file1 = new File("src/test/resources/dtd.xml");
        System.out.println(file1.getAbsolutePath());
        XmlStringParser parser = new XmlStringParser();
        FileObject file = VFS.resolveFile("file:" + file1.getAbsolutePath());
        doc = parser.parse(IOUtils.readFromInputStream(file.getInputStream(),
                "utf-8"));
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetRootSubNodes() {
        XmlNode node = doc.getRoot();
        List<XmlNode> subNodes = node.getSubNodesByPath("/note/to");
        assertEquals(2, subNodes.size());
        logOut(subNodes);
        List<XmlNode> subNodes1 = node.getSubNodesByPath("/note/from");
        assertEquals(5, subNodes1.size());
        logOut(subNodes1);
        List<XmlNode> subNodes2 = node.getSubNodesByPath("/note/from/from");
        assertEquals(1, subNodes2.size());
        logOut(subNodes2);
        List<XmlNode> subNodes3 = node.getSubNodesByPath("/note/from/from/from");
        assertEquals(1, subNodes3.size());
        logOut(subNodes3);
        List<XmlNode> subNodes4 = node.getSubNodesByPath("/note/body/a");
        assertEquals(2, subNodes4.size());
        logOut(subNodes4);
    }

    public void testGetSubSubNodes() {
        XmlNode node = doc.getRoot();
        XmlNode subNode = node.getSubNode("zhang");
        List<XmlNode> subNodes = subNode.getSubNodesByPath("/zhang/zhang1/");
        assertEquals(1, subNodes.size());
        logOut(subNodes);
    }

    public void testGetErrorSubNodes() {
        XmlNode node = doc.getRoot();
        List<XmlNode> subNodes = node.getSubNodesByPath("/note/from/from/from1");
        assertNull(subNodes);
        List<XmlNode> subNodes1 = node.getSubNodesByPath("/note/from1/from/from");
        assertNull(subNodes1);
        List<XmlNode> subNodes2 = node.getSubNodesByPath("/note/from//from");
        assertNull(subNodes2);
    }

    private void logOut(List<XmlNode> subNodes) {
        if (subNodes == null) {
            return;
        }
        for (XmlNode xn : subNodes) {
            System.out.println(xn.toString());
        }
    }

}
