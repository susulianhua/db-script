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
package org.tinygroup.pdfindexsource;

import junit.framework.TestCase;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

/**
 * PdfDocumentCreator测试用例
 *
 * @author yancheng11334
 */
public class PdfDocumentCreatorTest extends TestCase {


    public void testPdfContent() {
        PdfDocumentCreator creator = new PdfDocumentCreator();
        FileObject pdfFile = VFS.resolveFile("src/test/resources/TF-120416-0832-3.pdf");

        assertEquals(true, creator.isMatch(pdfFile));

        Document document = creator.getDocument("tiny框架", pdfFile);

        assertEquals("TF-120416-0832-3.pdf", document.getTitle().getValue());
        assertEquals("tiny框架", document.getType().getValue());
        String content = (String) document.getAbstract().getValue();
        assertTrue(content.indexOf("Tiny框架提出了一个在资产库的概念，它不是一个新概念") > 0);
    }

    public void testPdfWithPassword() {

        PdfDocumentCreator creator = new PdfDocumentCreator();
        creator.setPassword("tinyabc");
        FileObject pdfFile = VFS.resolveFile("src/test/resources/password.pdf");

        assertEquals(true, creator.isMatch(pdfFile));

        Document document = creator.getDocument("杂文", pdfFile);

        assertEquals("password.pdf", document.getTitle().getValue());
        assertEquals("杂文", document.getType().getValue());
        String content = (String) document.getAbstract().getValue();
        assertTrue(content.indexOf("明天") > 0);
    }

}
