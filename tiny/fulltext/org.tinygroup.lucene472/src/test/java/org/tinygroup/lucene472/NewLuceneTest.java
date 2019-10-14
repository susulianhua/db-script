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
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fulltext.*;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.fulltext.impl.DefaultSearchRule;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinyrunner.Runner;

import java.io.File;

public class NewLuceneTest extends TestCase {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(NewLuceneTest.class);
    private FullText fullText;

    protected void setUp() throws Exception {
        super.setUp();
        LOGGER.logMessage(LogLevel.INFO, "开始初始化应用配置application.xml");
        Runner.init("application.xml", null);
        fullText = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean("luceneFullText");
        LOGGER.logMessage(LogLevel.INFO, String.format("初始化应用配置application.xml结束,FULLTEXT_CONFIG_ID=%s", ConfigurationUtil.getConfigurationManager()
                .getConfiguration(FullTextConfigManager.FULLTEXT_CONFIG_ID)));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        //清理索引目录
        FileUtil.delete(new File("index2"));
    }

    public void testScene2() {
        Pager<Document> pager = null;
        File dir1 = new File("src/test/resources/ams/");
        File dir2 = new File("src/test/resources/bms/");

        fullText.createIndex("ams", dir1);
        fullText.createIndex("bms", dir2);

        //pager = fullText.search("abc", 0, 10);
        //assertEquals(2, pager.getTotalCount());

        pager = fullText.search("_type:ams AND abc", 0, 10);
        assertEquals(1, pager.getTotalCount());

        pager = fullText.search(FullTextHelper.getStoreType() + ":bms AND abc", 0, 10);
        assertEquals(1, pager.getTotalCount());

    }

    /**
     * 测试全文检索自定义字段的接口
     */
    public void testScene3() {
        Pager<Document> pager = null;
        //默认文章c1/c2/c3
        File dir1 = new File("src/test/resources/cms/");
        fullText.createIndex("news", dir1);

        DefaultSearchRule searchRule = new DefaultSearchRule();
        searchRule.addField("type", "abc");

        //精确匹配abc
        pager = fullText.search(searchRule, 0, 10);
        assertEquals(2, pager.getTotalCount());

        //通配符匹配abc*
        searchRule = new DefaultSearchRule();
        searchRule.addField("type", "abc*", QueryType.WILDCARD);
        pager = fullText.search(searchRule, 0, 10);
        assertEquals(3, pager.getTotalCount());

        //通配符匹配*bcd
        searchRule = new DefaultSearchRule();
        searchRule.addField("type", "*bcd", QueryType.WILDCARD);
        pager = fullText.search(searchRule, 0, 10);
        assertEquals(1, pager.getTotalCount());

        //通配符匹配*bc*
        searchRule = new DefaultSearchRule();
        searchRule.addField("type", "*bc*", QueryType.WILDCARD);
        pager = fullText.search(searchRule, 0, 10);
        assertEquals(3, pager.getTotalCount());

        //仅设置默认字段
        searchRule = new DefaultSearchRule();
        searchRule.setDefault("神舟");
        pager = fullText.search(searchRule, 0, 10);
        assertEquals(2, pager.getTotalCount());

        //仅设置默认字段
        searchRule = new DefaultSearchRule();
        searchRule.setDefault("神舟 OR KTL");
        pager = fullText.search(searchRule, 0, 10);
        assertEquals(3, pager.getTotalCount());

        //自定义字段与默认字段结合
        searchRule = new DefaultSearchRule();
        searchRule.addField("type", "*bcd", QueryType.WILDCARD).setDefault("神舟");
        pager = fullText.search(searchRule, 0, 10);
        assertEquals(1, pager.getTotalCount());

        //自定义字段与默认字段结合
        searchRule = new DefaultSearchRule();
        searchRule.addField("type", "*bcd", QueryType.WILDCARD).setDefault("德国");
        pager = fullText.search(searchRule, 0, 10);
        assertEquals(0, pager.getTotalCount());

        //不同字段设置不同规则
        searchRule = new DefaultSearchRule();
        searchRule.addField("type", "*bc", QueryType.WILDCARD).addField("city", "酒泉  OR 北京市");
        pager = fullText.search(searchRule, 0, 10);
        assertEquals(1, pager.getTotalCount());

        //设置结果过滤
        searchRule = new DefaultSearchRule();
        searchRule.addField("type", "*bc*", QueryType.WILDCARD);
        pager = fullText.search(searchRule, new Fileter1(), 0, 10);
        assertEquals(1, pager.getTotalCount());

        //设置结果过滤
        searchRule = new DefaultSearchRule();
        searchRule.setDefault("神舟 OR KTL");
        pager = fullText.search(searchRule, new Fileter1(), 0, 10);
        assertEquals(1, pager.getTotalCount());
    }

    public void testScene4() {
        Pager<Document> pager = null;
        File dir1 = new File("src/test/resources/ems/");
        String type = "english";
        fullText.createIndex(type, dir1);

        DefaultSearchRule searchRule = new DefaultSearchRule();
        searchRule.addField("_type", type).setDefault("共和");
        // searchRule.addField("_type", type).setDefault("hell | hell*");
        // searchRule.addField("_type", type).setDefault("T恤 | T恤*");
        // searchRule.addField("_type", type).setDefault("T | T*");
        //searchRule.addField("_type", type).setDefault("EXO组合 | EXO组合*");
        //searchRule.addField("_type", type).setDefault("中文ppt");
        //searchRule.addField("_type", type).setDefault("中华人民共和国 | 中华人民共和国*");
        // String word ="A恤";
//         String word ="民";
//         if(word.indexOf("*")>=0){
//        	 searchRule.addField("_type", type).setDefault(word);
//         }else{
//        	 searchRule.addField("_type", type).setDefault(word+" | "+word+"*");
//         }


        pager = fullText.search(searchRule, 0, 10);
        assertEquals(1, pager.getTotalCount());
    }

    class Fileter1 implements DocumentFilter {

        /**
         * 用户自己定义的业务逻辑
         */
        public boolean accept(Document document) {
            String content = (String) document.getAbstract().getValue();
            if (content != null && content.indexOf("神舟") > -1) {
                return false;
            }
            return true;
        }

    }
}
