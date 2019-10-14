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
import org.tinygroup.sqlindexsource.config.SqlConfigs;
import org.tinygroup.templateindex.config.BaseIndexConfig;
import org.tinygroup.tinyrunner.Runner;

import java.util.List;
import java.util.Set;

public class LuceneConfigTest extends TestCase {

    private LuceneConfigManager luceneConfigManager;

    protected void setUp() throws Exception {
        super.setUp();
        Runner.init("application.xml", null);
        luceneConfigManager = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean("luceneConfigManager");
    }

    public void testIndexInstall() throws Exception {
        assertNotNull(luceneConfigManager);

        List<BaseIndexConfig> list = luceneConfigManager.getIndexConfigList();
        assertEquals(1, list.size());

        //SQL
        BaseIndexConfig config = list.get(0);
        assertEquals(SqlConfigs.class, config.getClass());

        assertEquals("sqlConfigsIndexOperator", config.getBeanName());

        Set<String> fields = config.getQueryFields();
        assertEquals(5, fields.size());
    }
}
