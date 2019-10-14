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
import org.tinygroup.officeindexsource.excel.XlsDocumentListCreator;
import org.tinygroup.officeindexsource.excel.XlsxDocumentListCreator;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import java.util.List;

/**
 * 测试Excel文件
 *
 * @author yancheng11334
 */
public class ExcelTest extends TestCase {

    private BeanContainer<?> BeanContainer = null;

    protected void setUp() throws Exception {
        super.setUp();
        Runner.init("application.xml", null);
        BeanContainer = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader());
    }

    public void testXls() {
        XlsDocumentListCreator creator = BeanContainer.getBean("xlsDocumentListCreator");
        FileObject fileObejct = VFS.resolveFile("src/test/resources/excel/data.xls");
        List<Document> docs = creator.getDocument("社会文学", fileObejct);
        assertEquals(3, docs.size());

        Document doc = docs.get(1);
        assertEquals("S002", doc.getId().getValue());
        assertEquals("中国百年展元明清建筑展示", doc.getTitle().getValue());
        assertEquals("社会文学", doc.getType().getValue());
        assertEquals("论南方园林的建筑演变历史", doc.getAbstract().getValue());
    }

    public void testXlsx() {
        XlsxDocumentListCreator creator = BeanContainer.getBean("xlsxDocumentListCreator");
        FileObject fileObejct = VFS.resolveFile("src/test/resources/excel/data.xlsx");
        List<Document> docs = creator.getDocument("历史文学", fileObejct);
        assertEquals(3, docs.size());

        Document doc = docs.get(1);
        assertEquals("S002", doc.getId().getValue());
        assertEquals("中国百年展元明清建筑展示", doc.getTitle().getValue());
        assertEquals("历史文学", doc.getType().getValue());
        assertEquals("论南方园林的建筑演变历史", doc.getAbstract().getValue());
    }
}
