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
package org.tinygroup.sequence.single;

import org.junit.Assert;
import org.junit.Test;
import org.tinygroup.dbunit.AbstractDBUnitTest;
import org.tinygroup.sequence.impl.DefaultSequence;
import org.tinygroup.sequence.impl.DefaultSequenceDao;

import java.util.Collections;
import java.util.List;

/**
 * 单库序列号生成测试
 *
 * @author renhui
 */
public class SingleSequenceTest extends AbstractDBUnitTest {


    @Test
    public void testSingle() {
        DefaultSequenceDao sequenceDao = new DefaultSequenceDao();
        sequenceDao.setDataSource(createDataSourceList().get(0));
        sequenceDao.setStep(10);
        DefaultSequence defaultSequence = new DefaultSequence();
        defaultSequence.setSequenceDao(sequenceDao);
        defaultSequence.setName("user");
        for (int i = 0; i < 100; i++) {
            long actualValue = defaultSequence.nextValue();
            Assert.assertEquals(i + 1, actualValue);
        }
    }

    @Override
    protected List<String> getSchemaFiles() {
        return Collections.singletonList("integrate/schema/db_sequence1.sql");
    }

    @Override
    protected List<String> getDataSetFiles() {
        return Collections.singletonList("integrate/dataset/single/init/db_sequence1.xml");
    }

}
