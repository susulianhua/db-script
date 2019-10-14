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
package org.tinygroup.sequence.multi;

import org.junit.Assert;
import org.junit.Test;
import org.tinygroup.dbunit.AbstractDBUnitTest;
import org.tinygroup.sequence.impl.MultipleSequenceDao;
import org.tinygroup.sequence.impl.MultipleSequenceFactory;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * 多数据源序列号生成测试
 *
 * @author renhui
 */
public class MultipleSequenceTest extends AbstractDBUnitTest {

    @Test
    public void testSequence() {
        MultipleSequenceFactory sequenceFactory = new MultipleSequenceFactory();
        MultipleSequenceDao multipleSequenceDao = new MultipleSequenceDao();
        List<DataSource> dataSources = createDataSourceList();
        multipleSequenceDao.setDataSourceList(dataSources);
        sequenceFactory.setMultipleSequenceDao(multipleSequenceDao);
        sequenceFactory.init();
        long curSeqId = 0;//当前id
        try {
            for (int i = 0; i < 2; i++) {
                int step = 10;
                long temp = sequenceFactory.getNextValue("user");
                if (temp % step == 0) {
                    curSeqId = temp;
                } else {
                    curSeqId++;
                }
                Assert.assertEquals(curSeqId, temp);
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Override
    protected List<String> getSchemaFiles() {
        return Arrays.asList(
                "integrate/schema/db_sequence1.sql",
                "integrate/schema/db_sequence2.sql");
    }

    @Override
    protected List<String> getDataSetFiles() {
        return Arrays.asList(
                "integrate/dataset/multi/init/db_sequence1.xml",
                "integrate/dataset/multi/init/db_sequence2.xml");
    }


}
