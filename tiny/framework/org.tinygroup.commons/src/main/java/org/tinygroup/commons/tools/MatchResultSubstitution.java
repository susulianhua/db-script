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
package org.tinygroup.commons.tools;

import org.tinygroup.commons.tools.ToStringBuilder.MapBuilder;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.tinygroup.commons.tools.ArrayUtil.isEmptyArray;
import static org.tinygroup.commons.tools.Assert.assertTrue;


/**
 * 用<code>MatchResult</code>来替换字符串中的变量。通常变量以<code>'$'</code>开始，例如：
 * <code>$1</code>，<code>$2</code>等，但<code>MatchResultSubstitution</code>类支持对多个
 * <code>MatchResult</code>变量进行替换，分别对应不同的前缀。
 *
 * @author renhui
 */
public class MatchResultSubstitution extends Substitution {
    /**
     * 代表一个成功但无内容的匹配结果。
     */
    public static final MatchResult EMPTY_MATCH_RESULT = createEmptyMatchResult();

    private final MatchResult[] results;

    /**
     * 创建一个替换。替换所有<code>$num</code>所代表的变量。
     */
    public MatchResultSubstitution() {
        this("$", EMPTY_MATCH_RESULT);
    }

    /**
     * 创建一个替换。替换所有<code>$num</code>所代表的变量。
     */
    public MatchResultSubstitution(MatchResult result) {
        this("$", result);
    }

    /**
     * 创建一个替换。将所有指定前缀所代表的变量替换成相应<code>MatchResult.group(num)</code>的值。
     */
    public MatchResultSubstitution(String replacementPrefixes, MatchResult... results) {
        super(replacementPrefixes);
        this.results = new MatchResult[this.replacementPrefixes.length()];

        setMatchResults(results);
    }

    private static MatchResult createEmptyMatchResult() {
        Matcher matcher = Pattern.compile("^$").matcher("");

        assertTrue(matcher.find());

        return matcher.toMatchResult();
    }

    /**
     * 设置新匹配。
     */
    public void setMatchResult(MatchResult result) {
        if (results.length != 1) {
            new IllegalArgumentException("expected " + this.results.length + " MatchResults");
        }

        results[0] = result;
    }

    /**
     * 设置新匹配。
     */
    public void setMatchResults(MatchResult... results) {
        assertTrue(!isEmptyArray(results), "results");

        if (this.results.length != results.length) {
            throw new IllegalArgumentException("expected " + this.results.length + " MatchResults");
        }

        for (int i = 0; i < results.length; i++) {
            this.results[i] = results[i];
        }
    }

    /**
     * 取得匹配。
     */
    public MatchResult getMatch() {
        return getMatch(0);
    }

    /**
     * 取得匹配。
     */
    public MatchResult getMatch(int index) {
        return results[index];
    }


    protected String group(int index, int groupNumber) {
        MatchResult result = getMatch(index);

        if (groupNumber <= result.groupCount()) {
            return result.group(groupNumber);
        }

        return null;
    }


    public String toString() {
        MapBuilder mb = new MapBuilder();

        for (int i = 0; i < replacementPrefixes.length(); i++) {
            mb.append(replacementPrefixes.charAt(i) + "n", results[i]);
        }

        return new ToStringBuilder().append(getClass().getSimpleName()).append(mb).toString();
    }
}
