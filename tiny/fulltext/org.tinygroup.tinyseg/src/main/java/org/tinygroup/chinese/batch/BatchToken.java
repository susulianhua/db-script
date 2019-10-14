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
package org.tinygroup.chinese.batch;

import org.tinygroup.chinese.Token;
import org.tinygroup.chinese.TokenType;
import org.tinygroup.chinese.single.SingleToken;

/**
 * Created by luog on 15/4/13.
 */
public class BatchToken implements Token {
    SingleToken singleToken;
    /**
     * 出现次数
     */
    private int count;

    public BatchToken(SingleToken singleToken) {
        this.singleToken = singleToken;
        this.count = 1;
    }

    public BatchToken(SingleToken singleToken, int count) {
        this.singleToken = singleToken;
        this.count = count;
    }

    public SingleToken getSingleToken() {
        return singleToken;
    }

    public void setSingleToken(SingleToken singleToken) {
        this.singleToken = singleToken;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public TokenType getTokenType() {
        return singleToken.getTokenType();
    }

    public String getWord() {
        return singleToken.getWord();
    }
}
