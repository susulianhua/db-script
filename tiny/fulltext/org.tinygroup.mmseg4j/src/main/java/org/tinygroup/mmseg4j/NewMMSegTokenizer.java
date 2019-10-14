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
package org.tinygroup.mmseg4j;

import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.io.Reader;

public class NewMMSegTokenizer extends Tokenizer {

    private MMSeg mmSeg;
    private CharTermAttribute termAtt;
    private OffsetAttribute offsetAtt;
    private TypeAttribute typeAtt;

    public NewMMSegTokenizer(Seg seg, Reader input) {
        super(input);
        mmSeg = new MMSeg(input, seg);

        termAtt = addAttribute(CharTermAttribute.class);
        offsetAtt = addAttribute(OffsetAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
    }

    public void reset() throws IOException {
        super.reset();
        mmSeg.reset(input);
    }

    //lucene 2.9/3.0
    @Override
    public final boolean incrementToken() throws IOException {
        clearAttributes();
        Word word = mmSeg.next();
        if (word != null) {
            //lucene 3.0
            //termAtt.setTermBuffer(word.getSen(), word.getWordOffset(), word.getLength());
            //lucene 3.1
            termAtt.copyBuffer(word.getSen(), word.getWordOffset(), word.getLength());
            offsetAtt.setOffset(word.getStartOffset(), word.getEndOffset());
            typeAtt.setType(word.getType());
            return true;
        } else {
            end();
            return false;
        }
    }

}
