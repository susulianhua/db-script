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
package org.tinygroup.ansjanalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;
import org.tinygroup.stopword.StopWordManager;

import java.io.Reader;

public abstract class LuceneAnalyzer extends Analyzer {

    private StopWordManager stopWordManager;

    public StopWordManager getStopWordManager() {
        return stopWordManager;
    }

    public void setStopWordManager(StopWordManager stopWordManager) {
        this.stopWordManager = stopWordManager;
    }

    protected TokenStreamComponents createComponents(String fieldName,
                                                     Reader reader) {
        final Tokenizer source = createTokenizer(reader);
        if (stopWordManager != null) {
            //走停止词过滤
            CharArraySet stopWords = new CharArraySet(Version.LUCENE_CURRENT, stopWordManager.getStopWords(), true);
            TokenStream result = new StopFilter(Version.LUCENE_CURRENT, source, stopWords);
            return new TokenStreamComponents(source, result);
        } else {
            //走原始逻辑
            return new TokenStreamComponents(source);
        }
    }

    /**
     * 生成Tokenizer
     * @param reader
     * @return
     */
    protected abstract Tokenizer createTokenizer(Reader reader);

}
