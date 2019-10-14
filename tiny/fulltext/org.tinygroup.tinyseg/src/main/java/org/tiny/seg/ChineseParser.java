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
package org.tiny.seg;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 中文分词<br>
 *
 * @author luoguo
 */
public interface ChineseParser {

    String CHINESE_PARSER_BEAN_NAME = "chineseParser";

    /**
     * 对内容进行分词，最大优先，此方法由于用了统计，因此效率较低
     *
     * @param content
     * @return 分的词及个数的键值对
     */
    void segmentWordMax(String content, Map<String, Integer> result);

    /**
     * 对内容进行分词，最小优先，此方法由于用了统计，因此效率较低
     *
     * @param content
     * @param result  分的词及个数的键值对
     */
    void segmentWordMin(String content, Map<String, Integer> result);

    /**
     * 对内容进行分词，最大优先
     *
     * @param content
     * @param result
     */
    void segmentWordMax(String content, List<String> result);

    /**
     * 对内容进行分词，最小优先
     *
     * @param content
     * @param result
     */
    void segmentWordMin(String content, List<String> result);

    /**
     * 设置找词时的事件
     *
     * @param event
     */

    void setFoundEvent(FoundEvent event);

    /**
     * 加载词库
     *
     * @param inputStream
     * @param encode
     */
    void loadDict(InputStream inputStream,
                  String encode);
}
