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
package org.tinygroup.sequence.impl;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.sequence.DataSourceSelector;
import org.tinygroup.sequence.SequenceConstants;

import java.util.List;
import java.util.Random;

/**
 * 生成随机数，用于选择在哪个库上获取sequence
 *
 * @author renhui
 */
public class DataSourceRandomSelector implements DataSourceSelector {
    private static final Logger logger = LoggerFactory
            .getLogger(SequenceConstants.TINY_SEQUENCE_LOG_NAME);
    private final int dataSourceNum;
    private final int weights[];

    private final Random random = new Random();
    private final int DEFAULT_WEIGHT = 10;

    public DataSourceRandomSelector(int dataSourceNum) {
        this.dataSourceNum = dataSourceNum;
        weights = new int[dataSourceNum];
        for (int i = 0; i < dataSourceNum; i++) {
            weights[i] = DEFAULT_WEIGHT;
        }
    }

    public DataSourceRandomSelector(int[] weights) {
        this.weights = weights;
        this.dataSourceNum = weights.length;
    }

    public int getRandomDataSourceIndex(List<Integer> excludeIndexes) {
        int[] tempWeights = weights.clone();
        for (int i = 0; i < dataSourceNum; i++) {
            if (excludeIndexes.contains(i)) {
                tempWeights[i] = 0;
            }
        }
        int[] areaEnds = getAreaEnds(tempWeights);
        return select(areaEnds);
    }

    private int[] getAreaEnds(int[] weights) {
        if (weights == null) {
            return null;
        }
        int[] areaEnds = new int[weights.length];
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i];
            areaEnds[i] = sum;
        }
        return areaEnds;
    }

    private int select(int[] areaEnds) {
        int sum = areaEnds[areaEnds.length - 1];
        if (sum == 0) {
            logger.error("ERROR ## areaEnds: " + intArray2String(areaEnds));
            return -1;
        }
        int rand = random.nextInt(sum);
        for (int i = 0; i < areaEnds.length; i++) {
            if (rand < areaEnds[i]) {
                return i;
            }
        }
        return -1;
    }

    private String intArray2String(int[] inta) {
        if (inta == null) {
            return "null";
        } else if (inta.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(inta[0]);
        for (int i = 1; i < inta.length; i++) {
            sb.append(", ").append(inta[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}