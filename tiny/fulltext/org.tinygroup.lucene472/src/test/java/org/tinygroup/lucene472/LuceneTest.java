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
package org.tinygroup.lucene472;

import junit.framework.TestCase;
import org.apache.lucene.queryparser.classic.ParseException;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.fulltext.FullText;
import org.tinygroup.fulltext.FullTextHelper;
import org.tinygroup.fulltext.Pager;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.fulltext.document.HighlightDocument;
import org.tinygroup.fulltext.exception.FullTextException;
import org.tinygroup.fulltext.impl.DefaultSearchRule;
import org.tinygroup.tinyrunner.Runner;

import java.io.File;

public class LuceneTest extends TestCase {

    private FullText fullText;

    protected void setUp() throws Exception {
        super.setUp();
        Runner.init("application.xml", null);
        fullText = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean("luceneFullText");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        //清理索引目录
        FileUtil.delete(new File("index"));
    }

    public void testScene1() {
        File sourceDir = new File("src/test/resources/file/");
        String type = "word";

        //删除索引
        fullText.deleteIndex(type, sourceDir);

        //创建索引
        fullText.createIndex(type, sourceDir);


        Pager<Document> pager = fullText.search("上海 北京");
        String perfix = "<font color='red'>";
        String suffix = "</font>";

        for (Document doc : pager.getRecords()) {
            HighlightDocument newDoc = new HighlightDocument(doc, perfix, suffix);
            System.out.println(newDoc.getId());
            System.out.println(newDoc.getAbstract());
        }

        //测试分页
        pager = fullText.search("我们", 0, 10);
        for (Document doc : pager.getRecords()) {
            HighlightDocument newDoc = new HighlightDocument(doc, perfix, suffix);
            System.out.println(newDoc.getId());
        }
        assertEquals(4, pager.getTotalCount());
        assertEquals(4, pager.getRecords().size());
        assertEquals(1, pager.getCurrentPage());
        assertEquals(1, pager.getTotalPages());

        pager = fullText.search("我们", 0, 2);
        assertEquals(4, pager.getTotalCount());
        assertEquals(2, pager.getRecords().size());
        assertEquals(1, pager.getCurrentPage());
        assertEquals(2, pager.getTotalPages());

        pager = fullText.search("我们", 2, 2);
        assertEquals(4, pager.getTotalCount());
        assertEquals(2, pager.getRecords().size());
        assertEquals(2, pager.getCurrentPage());
        assertEquals(2, pager.getTotalPages());

        pager = fullText.search("我们", 4, 2);
        assertEquals(4, pager.getTotalCount());
        assertEquals(0, pager.getRecords().size());
        assertEquals(2, pager.getCurrentPage());
        assertEquals(2, pager.getTotalPages());

        //测试限定范围的查询
        pager = fullText.search(FullTextHelper.getStoreType() + ":word", 0, 10);
        assertEquals(6, pager.getTotalCount());

        pager = fullText.search("醉酒", 0, 10);
        assertEquals(1, pager.getTotalCount());

        pager = fullText.search(FullTextHelper.getStoreType() + ":word AND 醉酒", 0, 10);
        assertEquals(1, pager.getTotalCount());

        //测试转义
        String s = "org.tinygroup:org.tinygroup.ui.treeview:3.0.0";
        assertEquals("org.tinygroup\\:org.tinygroup.ui.treeview\\:3.0.0", fullText.escape(s));

        DefaultSearchRule searchRule = new DefaultSearchRule();

        searchRule.setDefault(s);
        try {
            fullText.search(searchRule, 0, 10);
        } catch (FullTextException e) {
            ////不转义查询报错
            assertEquals(ParseException.class, e.getCause().getClass());
        }

        //转义查询不报错
        searchRule.setDefault(fullText.escape(s));
        fullText.search(searchRule, 0, 10);
    }


}
