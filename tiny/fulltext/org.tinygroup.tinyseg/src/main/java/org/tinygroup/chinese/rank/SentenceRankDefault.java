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
package org.tinygroup.chinese.rank;

import org.tinygroup.chinese.Character;
import org.tinygroup.chinese.*;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.List;

/**
 * Created by luog on 15/4/20.
 */
public class SentenceRankDefault implements SentenceRank {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SentenceRankDefault.class);

    private WordParserManager wordParserManager;

    public void setWordParserManager(WordParserManager wordParserManager) {
        this.wordParserManager = wordParserManager;
    }

    public int rank(List<Token> tokenList) {
        int rank = 0;
        for (Token token : tokenList) {
            if (token.getTokenType() != TokenType.WORD) {
                continue;
            }
            String wordString = token.getWord();
            if (wordString.length() == 1) {
                try {
                    Character character = wordParserManager.getCharacter(wordString.charAt(0));
                    if (character.isSingleWord()) {
                        //如果有单字
                        rank += 1;
                        continue;
                    }
                } catch (ParserException e) {
                    LOGGER.errorMessage("评价发生异常:", e);
                }
                rank -= 1;
            } else {
                try {
                    Word word = wordParserManager.getWord(wordString);
                    if (word.getWeight() != 0) {
                        rank += word.getWeight();
                        continue;
                    }

                } catch (ParserException e) {
                    LOGGER.errorMessage("评价发生异常:", e);
                }
                rank += getDefaultWeight(wordString.length());
            }
        }
        return rank;
    }

    private int getDefaultWeight(int length) {
        return length * 2 - 1;
    }
}
