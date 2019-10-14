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
package org.tiny.chinese;

import org.tinygroup.chinese.WordParserManager;
import org.tinygroup.chinese.WordParserMode;
import org.tinygroup.chinese.WordParserType;
import org.tinygroup.chinese.parsermanager.WordParserManagerImpl;
import org.tinygroup.chinese.single.SingleToken;
import org.tinygroup.chinese.single.SingleWordStringParser;

import java.io.IOException;
import java.util.List;

/**
 * Created by luoguo on 2015/4/16.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        WordParserManager wordParserManager = new WordParserManagerImpl();
        wordParserManager.addWordString("帽子");
        wordParserManager.addWordString("帽子和");
        wordParserManager.addWordString("和服");
        wordParserManager.addWordString("服装");
        SingleWordStringParser singleWordStringParser = new SingleWordStringParser();
        List<SingleToken> token;
        singleWordStringParser.parse(wordParserManager, "abc 123 def\n帽子和服装", WordParserType.ASC, WordParserMode.MAX);
        while ((token = singleWordStringParser.nextSentenceTokens()) != null) {
            System.out.println(token.size());
        }
        singleWordStringParser.parse(wordParserManager, "abc 123 def\n" +
                "帽子和服装", WordParserType.DESC, WordParserMode.MAX);
        while ((token = singleWordStringParser.nextSentenceTokens()) != null) {
            System.out.println(token.size());
        }
    }
}
