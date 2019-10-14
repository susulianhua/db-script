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
import org.tinygroup.chinese.single.SingleToken;
import org.tinygroup.chinese.single.SingleWordReaderParser;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by luog on 15/4/13.
 */
public class BatchWordReaderParser implements WordParser<BatchToken, Reader> {
    private SingleWordReaderParser singleWordReaderParser = new SingleWordReaderParser();
    private BatchToken[] batchTokens;
    private Map<String, BatchToken> batchTokenMap;
    private int position;


    public void parse(WordParserManager wordParserManager, Reader reader, WordParserType wordParserType, WordParserMode wordParserMode) throws IOException {
        singleWordReaderParser.parse(wordParserManager, reader, wordParserType, wordParserMode);
        SingleToken singleToken = null;
        while ((singleToken = singleWordReaderParser.nextToken()) != null) {
            BatchToken batchToken = batchTokenMap.get(singleToken.getWord());
            if (batchToken == null) {
                batchToken = new BatchToken(singleToken);
                batchTokenMap.put(singleToken.getWord(), batchToken);
            } else {
                batchToken.setCount(batchToken.getCount() + 1);
            }
        }
        batchTokens = batchTokenMap.values().toArray(new BatchToken[0]);
    }

    public Collection<BatchToken> tokens() {
        return batchTokenMap.values();
    }

    public BatchToken nextToken() {
        if (batchTokens != null && position < batchTokens.length) {
            return batchTokens[position++];
        }
        return null;
    }

    public List<BatchToken> nextSentenceTokens() throws IOException {
        return null;
    }
}
