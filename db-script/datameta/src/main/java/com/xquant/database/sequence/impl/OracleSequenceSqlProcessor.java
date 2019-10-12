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
package com.xquant.database.sequence.impl;

import com.xquant.database.config.sequence.SeqCacheConfig;
import com.xquant.database.config.sequence.Sequence;
import com.xquant.database.config.sequence.ValueConfig;

public class OracleSequenceSqlProcessor extends AbstractSequenceSqlProcessor {

    public String getCreateSql(Sequence sequence) {
        StringBuffer seqBuffer = new StringBuffer();
        seqBuffer.append("CREATE SEQUENCE ").append(sequence.getName())
                .append(" INCREMENT BY ").append(sequence.getIncrementBy())
                .append(" START WITH ").append(sequence.getStartWith());
        ValueConfig maxValueConfig = sequence.getValueConfig();
        if (maxValueConfig == null) {
            seqBuffer.append(" NOMAXVALUE ");
        } else {
            seqBuffer.append(" MINVALUE ").append(maxValueConfig.getMinValue())
                    .append(" MAXVALUE ").append(maxValueConfig.getMaxValue());
        }
        if (sequence.isCycle()) {
            seqBuffer.append(" CYCLE ");
        } else {
            seqBuffer.append(" NOCYCLE ");
        }
        SeqCacheConfig cacheConfig = sequence.getSeqCacheConfig();
        if (cacheConfig == null || !cacheConfig.isCache()) {
            seqBuffer.append(" NOCACHE ");
        } else {
            seqBuffer.append(" CACHE ").append(cacheConfig.getNumber());
        }
        if (sequence.isOrder()) {
            seqBuffer.append(" ORDER ");
        } else {
            seqBuffer.append(" NOORDER ");
        }
        return seqBuffer.append(";").toString();
    }

    protected String getQuerySql(Sequence sequence) {
        String sql = "select  sequence_name  from  user_sequences  where  sequence_name= '"
                + sequence.getName() + "'";
        return sql;
    }

}
