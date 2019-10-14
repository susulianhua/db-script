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
package org.tinygroup.sequence.exception;

import org.junit.Assert;
import org.junit.Test;
import org.tinygroup.dbunit.AbstractDBUnitTest;
import org.tinygroup.sequence.impl.MultipleSequenceDao;
import org.tinygroup.sequence.impl.MultipleSequenceFactory;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 测试初始异常
 * Created by wangwy11342 on 2016/6/21.
 */
public class InitExceptionTest extends AbstractDBUnitTest {
    @Override
    protected List<String> getSchemaFiles() {
        return Arrays.asList("integrate/schema/db_sequence1.sql",
                "integrate/schema/db_sequence2.sql");
    }

    @Override
    protected List<String> getDataSetFiles() {
        return Arrays.asList("integrate/dataset/exception/init/db_sequence1.xml",
                "integrate/dataset/exception/init/db_sequence2.xml");
    }

    /**
     * sequenceFactory未指定dao异常
     */
    @Test
    public void testDaoNull() {
        MultipleSequenceFactory sequenceFactory = new MultipleSequenceFactory();
        try {
            sequenceFactory.init();
            Assert.fail("dao不能为空");
        } catch (IllegalArgumentException e) {
            //断言异常
            assertEquals("The sequenceDao is null!", e.getMessage());
        }
    }

    /**
     * 重复初始化异常
     */
    @Test
    public void testRepeatInit() {
        MultipleSequenceFactory sequenceFactory = new MultipleSequenceFactory();
        MultipleSequenceDao multipleSequenceDao = new MultipleSequenceDao();
        List<DataSource> dataSources = createDataSourceList();
        multipleSequenceDao.setDataSourceList(dataSources);
        sequenceFactory.setMultipleSequenceDao(multipleSequenceDao);
        sequenceFactory.init();
        try {
            sequenceFactory.init();
            Assert.fail("两次初始化应报异常");
        } catch (SequenceException e) {
            //断言异常
            assertEquals("ERROR ## the MultipleSequenceDao has inited", e.getMessage());
        }
    }

}
