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
package org.tinygroup.chinese.single;

import org.tinygroup.chinese.Token;
import org.tinygroup.chinese.TokenType;

/**
 * BatchToken 用于在分词时返回Token的情况
 * Created by luog on 15/4/13.
 */
public class SingleToken implements Token {
    /**
     * 行
     */
    int line;
    /**
     * 开始列
     */
    int start;
    /**
     * 结束列
     */
    int end;

    /**
     * 文本内容
     */
    String word;
    /**
     * 类型
     */
    TokenType tokenType;

    public SingleToken(String word, TokenType tokenType, int line, int start, int end) {
        this.word = word;
        this.tokenType = tokenType;
        this.line = line;
        this.start = start;
        this.end = end;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getWord() {
        return word;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getLine() {
        return line;
    }

    public String toString() {
        return word + ":" + tokenType;
    }
}
