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
package org.tinygroup.database;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.database.tablespace.TableSpaceProcessor;
import org.tinygroup.database.util.DataBaseUtil;

/**
 * Created by wangwy11342 on 2016/8/2.
 */
public class TableSpaceProcessorTest extends TestCase {
    static {
        TestInit.init();

    }

    TableSpaceProcessor tableSpaceProcessor;

    protected void setUp() throws Exception {
        super.setUp();
        tableSpaceProcessor = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean(DataBaseUtil.TABLE_SPACE_PROCESSOR_BEAN);
    }

    public void testProcessor() {
        assertEquals("ts2", tableSpaceProcessor.getTableSpace("bcd63471d4f14f83878a993efe97a109").getName());
    }

    public void testDataBaseUtil() {
        assertEquals("ts2", DataBaseUtil
                .getTableSpace(getClass().getClassLoader(), "bcd63471d4f14f83878a993efe97a109")
                .getName());
    }
}
