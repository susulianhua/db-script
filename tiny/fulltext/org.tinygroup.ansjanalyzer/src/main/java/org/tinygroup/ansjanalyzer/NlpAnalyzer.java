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

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.apache.lucene.analysis.Tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * 对应NlpAnalysis 带有新词发现功能的分词
 * @author yancheng11334
 *
 */
public class NlpAnalyzer extends LuceneAnalyzer {

    protected Tokenizer createTokenizer(Reader reader) {
        return new NlpAnalysisTokenizer(reader);
    }

    final class NlpAnalysisTokenizer extends LuceneTokenStream {

        protected NlpAnalysisTokenizer(Reader input) {
            super(input);
        }

        protected TokenStreamWrapper createTokenStreamWrapper(Reader input) throws IOException {
            return new NlpAnalysisWrapper(input);
        }

    }

    class NlpAnalysisWrapper extends AbstractTokenStreamWrapper {

        public NlpAnalysisWrapper(Reader input) throws IOException {
            super(input);
        }

        public List<Term> parse(String str) {
            return NlpAnalysis.parse(str).getTerms();
        }

    }

}
