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

/**
 * 用于查找汉字的拼音
 *
 * @author luoguo
 */
public interface PinYin {
    String PINYIN_BEAN_NAME = "pinYin";

    /**
     * @param inputStream
     * @param encode
     */
    void loadPinFile(InputStream inputStream,
                     String encode);

    /**
     * 返回单字的拼音
     *
     * @param c
     * @return
     */
    List<String> getPinYin(char c);

    /**
     * 返回字符数组的每个字的拼音
     *
     * @param c
     * @return 每个List对应一个字的多音子列表
     */
    List<String>[] getPinYin(char[] c);

    /**
     * 返回字符串的拼音，如果有多音字，取第一个为准
     *
     * @param str
     * @return
     */
    List<String> getPinYin(String str);

    /**
     * 返回词组的拼音
     *
     * @param str
     * @param pinyin pinyin为str中每个字对应的拼音序号,从1开始
     * @return
     */
    List<String> getPinYin(String str, String pinyin);
}
