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
import org.tinygroup.sequence.impl.MultipleSequenceDao;
import org.tinygroup.sequence.impl.MultipleSequenceFactory;
import org.tinygroup.tinydb.dialect.impl.MySQLDialect;
import org.tinygroup.tinydb.exception.TinyDbException;

import javax.sql.DataSource;
import java.util.List;

/**
 * 多数据源方式获取分布式主键值
 * @author renhui
 *
 */
public class MultiDistributedMySQLDialect extends MySQLDialect implements
        InitializingBean {

    private List<DataSource> dataSources;

    private String sequenceName;

    private MultipleSequenceFactory sequenceFactory;

    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    @Override
    public int getNextKey() {
        try {
            return new Long(sequenceFactory.getNextValue(sequenceName))
                    .intValue();
        } catch (Exception e) {
            throw new TinyDbException("获取分布式主键值出错", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.assertTrue(dataSources == null ? false : dataSources.size() > 1,
                "为保证高可用，数据源的个数建议设置多于两个");
        Assert.assertTrue(!StringUtil.isBlank(sequenceName),
                "sequenceName must not null or empty");
        sequenceFactory = new MultipleSequenceFactory();
        MultipleSequenceDao multipleSequenceDao = new MultipleSequenceDao();
        multipleSequenceDao.setDataSourceList(dataSources);
        sequenceFactory.setMultipleSequenceDao(multipleSequenceDao);
        sequenceFactory.init();
    }

}
