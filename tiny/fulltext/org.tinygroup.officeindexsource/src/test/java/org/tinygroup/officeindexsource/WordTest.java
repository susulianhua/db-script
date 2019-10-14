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
package org.tinygroup.officeindexsource;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.officeindexsource.word.DocDocumentCreator;
import org.tinygroup.officeindexsource.word.DocxDocumentCreator;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

public class WordTest extends TestCase {

    private BeanContainer<?> BeanContainer = null;

    protected void setUp() throws Exception {
        super.setUp();
        Runner.init("application.xml", null);
        BeanContainer = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader());
    }

    public void testDoc() {
        DocDocumentCreator creator = BeanContainer.getBean("docDocumentCreator");
        FileObject fileObejct = VFS.resolveFile("src/test/resources/word/data.doc");
        Document doc = creator.getDocument("传记", fileObejct);
        assertEquals("传记", doc.getType().getValue());
        assertEquals("Word文档输出结果，我爱北京", doc.getAbstract().getValue());
    }

    public void testDocx() {
        DocxDocumentCreator creator = BeanContainer.getBean("docxDocumentCreator");
        FileObject fileObejct = VFS.resolveFile("src/test/resources/word/data.docx");
        Document doc = creator.getDocument("传记2", fileObejct);
        assertEquals("传记2", doc.getType().getValue());
        assertEquals("Word文档输出结果，我爱北京", doc.getAbstract().getValue());
    }
}
