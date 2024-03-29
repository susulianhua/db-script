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
package org.tinygroup.stopword;

import java.util.List;
import java.util.Set;

/**
 * 停止词管理器
 * @author yancheng11334
 *
 */
public interface StopWordManager {

    /**
     * 增加单个停止词
     * @param stopWord
     */
    void addStopWord(String stopWord);

    /**
     * 批量增加停止词
     * @param stopWords
     */
    void addStopWords(List<String> stopWords);

    /**
     * 删除单个停止词
     * @param stopWord
     */
    void removeStopWord(String stopWord);

    /**
     * 批量删除停止词
     * @param stopWords
     */
    void removeStopWords(List<String> stopWords);

    /**
     * 清理全部停止词
     */
    void cleanStopWords();

    /**
     * 获得停止词信息
     * @return
     */
    Set<String> getStopWords();
}
