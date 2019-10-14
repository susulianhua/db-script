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
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.io.Reader;

public abstract class LuceneTokenStream extends Tokenizer {

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    private TokenStreamWrapper wrapper = null;

    protected LuceneTokenStream(Reader input) {
        super(input);
        try {
            wrapper = createTokenStreamWrapper(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reset() throws IOException {
        super.reset();
        wrapper.reset();
    }

    public boolean incrementToken() throws IOException {
        clearAttributes();
        Term term = wrapper.next();
        if (term != null && term.getName() != null) {
            termAtt.append(term.getName());
            offsetAtt.setOffset(term.getOffe(), term.toValue());
            typeAtt.setType("word");
            return true;
        } else {
            end();
            return false;
        }
    }

    protected abstract TokenStreamWrapper createTokenStreamWrapper(Reader input) throws IOException;

}
