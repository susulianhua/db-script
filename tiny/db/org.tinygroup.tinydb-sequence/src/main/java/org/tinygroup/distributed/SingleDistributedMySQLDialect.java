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
package org.tinygroup.distributed;

import org.springframework.beans.factory.InitializingBean;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.sequence.impl.DefaultSequence;
import org.tinygroup.sequence.impl.DefaultSequenceDao;
import org.tinygroup.tinydb.dialect.impl.MySQLDialect;

/**
 * 单数据源方式获取分布式主键值
 * @author renhui
 *
 */
public class SingleDistributedMySQLDialect extends MySQLDialect implements
        InitializingBean {

    private String sequenceName;// 分布式序列名称

    private DefaultSequence defaultSequence;

    private int step;// 每次缓存的步长


    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public int getNextKey() {
        return new Long(defaultSequence.nextValue()).intValue();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.assertNotNull(getDataSource(), "datasource must not null");
        Assert.assertTrue(!StringUtil.isBlank(sequenceName),
                "sequenceName must not null or empty");
        DefaultSequenceDao sequenceDao = new DefaultSequenceDao();
        sequenceDao.setDataSource(getDataSource());
        if (step > 0) {
            sequenceDao.setStep(step);
        }
        defaultSequence = new DefaultSequence();
        defaultSequence.setSequenceDao(sequenceDao);
        defaultSequence.setName(sequenceName);
    }

}
