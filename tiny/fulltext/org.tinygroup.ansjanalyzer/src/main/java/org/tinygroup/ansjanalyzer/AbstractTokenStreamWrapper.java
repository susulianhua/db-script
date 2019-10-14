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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTokenStreamWrapper implements TokenStreamWrapper {

    private int p = -1;
    private List<Term> termList = null;

    public AbstractTokenStreamWrapper(Reader input) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(input);
            String line = null;
            termList = new ArrayList<Term>();
            while ((line = reader.readLine()) != null) {
                termList.addAll(parse(line));
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public void reset() throws IOException {
        p = -1;
    }

    public Term next() throws IOException {
        p++;
        if (p < termList.size()) {
            return termList.get(p);
        }
        return null;
    }

}
