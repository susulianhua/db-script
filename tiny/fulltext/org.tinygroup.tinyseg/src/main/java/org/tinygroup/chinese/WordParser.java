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
package org.tinygroup.chinese;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by luog on 15/4/13.
 */
public interface WordParser<T extends Token, Source> {
    /**
     * 解析资源，调用此方法时，会清空缓冲
     *
     * @param manager
     * @param source  表示来源
     */
    void parse(WordParserManager manager, Source source, WordParserType wordParserType, WordParserMode wordParserMode) throws IOException;

    /**
     * 返回所有解析出来的Token列表，调用时，会把nextToken置为开头
     *
     * @return
     */
    Collection<T> tokens() throws IOException;

    /**
     * 返回下一个Token，如果返回值为空，表示已经以结尾
     *
     * @return
     */
    T nextToken() throws IOException;

    /**
     * 返回下一个句子中的Token列表
     *
     * @return
     * @throws IOException
     */
    List<T> nextSentenceTokens() throws IOException;
}
