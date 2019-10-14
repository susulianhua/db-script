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

import org.tinygroup.chinese.WordParser;
import org.tinygroup.chinese.WordParserManager;
import org.tinygroup.chinese.WordParserMode;
import org.tinygroup.chinese.WordParserType;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by luog on 15/4/13.
 */
public class BatchWordStringParser implements WordParser<BatchToken, String> {

    BatchWordReaderParser batchWordReaderParser = new BatchWordReaderParser();

    public void parse(WordParserManager wordParserManager, String string, WordParserType wordParserType, WordParserMode wordParserMode) throws IOException {
        batchWordReaderParser.parse(wordParserManager, new CharArrayReader(string.toCharArray()), wordParserType, wordParserMode);
    }

    public Collection<BatchToken> tokens() {
        return batchWordReaderParser.tokens();
    }

    public BatchToken nextToken() {
        return batchWordReaderParser.nextToken();
    }

    public List<BatchToken> nextSentenceTokens() throws IOException {
        return null;
    }
}
