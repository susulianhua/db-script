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
import org.apache.commons.lang.StringUtils;

/**
 * db2 sequence sql处理
 *
 * @author renhui
 */
public class Db2SequenceSqlProcessor extends AbstractSequenceSqlProcessor {

    public String getCreateSql(Sequence sequence) {
        StringBuffer seqBuffer = new StringBuffer();
        seqBuffer.append("CREATE SEQUENCE ").append(sequence.getName());
        String dataType = sequence.getDataType();
        if (StringUtils.isBlank(dataType)) {
            seqBuffer.append(" AS INTEGER ");
        } else {
            seqBuffer.append(String.format(" AS %s ", dataType));
        }
        seqBuffer.append(" START WITH ")
                .append(sequence.getStartWith()).append(" INCREMENT BY ")
                .append(sequence.getIncrementBy());
        ValueConfig valueConfig = sequence.getValueConfig();
        if (valueConfig == null) {
            seqBuffer.append(" NO MINVALUE NO MAXVALUE  ");
        } else {
            Integer minValue = valueConfig.getMinValue();
            if (minValue == null) {
                seqBuffer.append("NO MINVALUE ");
            } else {
                seqBuffer.append(" MINVALUE ").append(valueConfig.getMinValue());
            }
            Integer maxValue = valueConfig.getMaxValue();
            if (maxValue == null) {
                seqBuffer.append(" NO MAXVALUE ");
            } else {
                seqBuffer.append(" MAXVALUE ").append(valueConfig.getMaxValue());
            }
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
            seqBuffer.append(" NO ORDER ");
        }
        return seqBuffer.append(";").toString();
    }

    protected String getQuerySql(Sequence sequence) {
        String sql = "SELECT *  FROM SYSCAT.SEQUENCES WHERE SEQNAME='" + sequence.getName() + "'";
        return sql;
    }

}
