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
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.fulltext.FullText;
import org.tinygroup.fulltext.FullTextHelper;
import org.tinygroup.fulltext.Pager;
import org.tinygroup.fulltext.UserIndexOperator;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.fulltext.impl.AbstractFullText;
import org.tinygroup.fulltext.impl.DefaultSearchRule;
import org.tinygroup.lucene472.config.LuceneConfig;
import org.tinygroup.tinyrunner.Runner;

import java.io.File;

/**
 * 动态配置测试用例
 *
 * @author yancheng11334
 */
public class DynamicLuceneTest extends TestCase {

    private LuceneDynamicManager luceneDynamicManager;

    protected void setUp() throws Exception {
        super.setUp();
        Runner.init("dynamic.xml", null);
        luceneDynamicManager = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean(LuceneDynamicManager.DEFAULT_BEAN_NAME);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        //清理索引目录
        FileUtil.delete(new File("dynamic"));
    }

    //测试对象构建
    public void testInstance() throws Exception {
        FullText f1 = FullTextHelper.getFullText("hello1");
        FullText f2 = FullTextHelper.getFullText("hello2", "config2");

        //相同用户返回相同实例
        assertEquals(f1, FullTextHelper.getFullText("hello1"));
        //不同用户返回不同实例
        assertNotSame(f1, f2);

        //测试UserIndexOperator的userId
        UserIndexOperator uop1 = getUserIndexOperator(f1);
        UserIndexOperator uop2 = getUserIndexOperator(f2);
        assertEquals("hello1", uop1.getUserId());
        assertEquals("hello2", uop2.getUserId());

        //测试配置ID
        assertEquals(null, uop1.getConfigId());
        assertEquals("config2", uop2.getConfigId());
    }

    //测试动态配置加载
    public void testDynamicConfig() throws Exception {
        assertNotNull(luceneDynamicManager);

        LuceneConfig config = null;

        config = luceneDynamicManager.getFullTextConfig("john");
        assertEquals("index001", config.getId());
        assertEquals("dynamic/index01/john", config.getDirectory());

        config = luceneDynamicManager.getFullTextConfig("alert", "index002");
        assertEquals("index002", config.getId());
        assertEquals("dynamic/index02/alert", config.getDirectory());

        config = luceneDynamicManager.getFullTextConfig("xxxx", "index002");
        assertEquals("index002", config.getId());
        assertEquals("dynamic/index02/xxxx", config.getDirectory());
    }

    //测试基于动态配置的全文检索
    public void testDynamicLecene() throws Exception {
        //测试相同配置下不同的租户
        FullText cat = FullTextHelper.getFullText("cat", "index002");
        FullText dog = FullTextHelper.getFullText("dog", "index002");
        File cmsDir = new File("src/test/resources/cms/");  //cat用户记录3篇文章
        File dmsDir = new File("src/test/resources/dms/");  //dog用户记录1篇文章

        cat.deleteIndex("cms", cmsDir); //清理该数据源的索引
        cat.createIndex("cms", cmsDir);//创建数据源的索引
        dog.deleteIndex("dms", dmsDir); //清理该数据源的索引
        dog.createIndex("dms", dmsDir);//创建数据源的索引

        Pager<Document> pager = null;


        DefaultSearchRule searchRule = new DefaultSearchRule();
        searchRule.addField("type", "abc");
        pager = cat.search(searchRule, 0, 10);
        assertEquals(2, pager.getTotalCount());

        pager = dog.search(searchRule, 0, 10);
        assertEquals(1, pager.getTotalCount());
    }

    public void testDeleteAllIndexes() throws Exception {
        FullText fox = FullTextHelper.getFullText("fox", "index002");
        File cmsDir = new File("src/test/resources/cms/");
        File dmsDir = new File("src/test/resources/dms/");

        fox.createIndex("cms", cmsDir); //先创建cms

        Pager<Document> pager = null;


        DefaultSearchRule searchRule = new DefaultSearchRule();
        searchRule.addField("type", "abc");
        pager = fox.search(searchRule, 0, 10);
        assertEquals(2, pager.getTotalCount());

        //清理全部索引
        fox.deleteAllIndexes();

        pager = fox.search(searchRule, 0, 10);
        assertEquals(0, pager.getTotalCount());

        fox.createIndex("dms", dmsDir); //再创建dms
        pager = fox.search(searchRule, 0, 10);
        assertEquals(1, pager.getTotalCount());

        fox.deleteAllIndexes();
    }

    private UserIndexOperator getUserIndexOperator(FullText fullText) {
        if (fullText instanceof AbstractFullText) {
            AbstractFullText abstractFullText = (AbstractFullText) fullText;
            if (abstractFullText.getIndexOperator() != null) {
                return (UserIndexOperator) abstractFullText.getIndexOperator();
            }
        }
        return null;
    }
}
