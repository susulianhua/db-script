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

import junit.framework.TestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * 分词测试结果输出
 * @author yancheng11334
 *
 */
public class BaseTest extends TestCase {

    public static String word = "洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹";

    public void testBaseAnalyzer() throws Exception {
        Analyzer analyzer = new BaseAnalyzer();
        final TokenStream stream = analyzer.tokenStream("abc", word);
        CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
        while (stream.incrementToken()) {
            System.out.print("[" + cta + "]");
        }
        System.out.println();
    }

    public void testToAnalyzer() throws Exception {
        Analyzer analyzer = new ToAnalyzer();
        final TokenStream stream = analyzer.tokenStream("abc", word);
        CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
        while (stream.incrementToken()) {
            System.out.print("[" + cta + "]");
        }
        System.out.println();
    }

    public void testDicAnalyzer() throws Exception {
        Analyzer analyzer = new DicAnalyzer();
        final TokenStream stream = analyzer.tokenStream("abc", word);
        CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
        while (stream.incrementToken()) {
            System.out.print("[" + cta + "]");
        }
        System.out.println();
    }

    public void testIndexAnalyzer() throws Exception {
        Analyzer analyzer = new IndexAnalyzer();
        final TokenStream stream = analyzer.tokenStream("abc", word);
        CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
        while (stream.incrementToken()) {
            System.out.print("[" + cta + "]");
        }
        System.out.println();
    }

    public void testNlpAnalyzer() throws Exception {
        Analyzer analyzer = new NlpAnalyzer();
        final TokenStream stream = analyzer.tokenStream("abc", word);
        CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
        while (stream.incrementToken()) {
            System.out.print("[" + cta + "]");
        }
        System.out.println();
    }
}
