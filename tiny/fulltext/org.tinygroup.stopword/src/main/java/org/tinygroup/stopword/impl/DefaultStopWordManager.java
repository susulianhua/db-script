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
package org.tinygroup.stopword.impl;

import org.tinygroup.stopword.StopWordManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 默认的停止词管理器
 *
 * @author yancheng11334
 *
 */
public class DefaultStopWordManager implements StopWordManager {

    private Set<String> stopWordSet = new HashSet<String>();

    public void addStopWord(String stopWord) {
        stopWordSet.add(stopWord);
    }

    public void addStopWords(List<String> stopWords) {
        if (stopWords != null) {
            for (String stopWord : stopWords) {
                addStopWord(stopWord);
            }
        }
    }

    public void removeStopWord(String stopWord) {
        stopWordSet.remove(stopWord);
    }

    public void removeStopWords(List<String> stopWords) {
        if (stopWords != null) {
            for (String stopWord : stopWords) {
                removeStopWord(stopWord);
            }
        }
    }

    public void cleanStopWords() {
        stopWordSet.clear();
    }

    public Set<String> getStopWords() {
        return stopWordSet;
    }

}
