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
package org.tinygroup.redis.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PartenTest {
    public static void main(String[] args) {

        String str = "Hello,World! in Java.";
        Pattern pattern = Pattern.compile("W(or)(ld!)");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println("Group 0:" + matcher.group(0));// 得到第0组——整个匹配
            System.out.println("Group 1:" + matcher.group(1));// 得到第一组匹配——与(or)匹配的
            System.out.println("Group 2:" + matcher.group(2));// 得到第二组匹配——与(ld!)匹配的，组也就是子表达式
            System.out.println("Start 0:" + matcher.start(0) + " End 0:"
                    + matcher.end(0));// 总匹配的索引
            System.out.println("Start 1:" + matcher.start(1) + " End 1:"
                    + matcher.end(1));// 第一组匹配的索引
            System.out.println("Start 2:" + matcher.start(2) + " End 2:"
                    + matcher.end(2));// 第二组匹配的索引
            System.out.println(str.substring(matcher.start(0), matcher.end(1)));// 从总匹配开始索引到第1组匹配的结束索引之间子串——Wor
        }
    }

    public static String getKeyTag(Pattern tagPattern, String key) {
        if (tagPattern != null) {
            Matcher m = tagPattern.matcher(key);
            if (m.find())
                return m.group(1);
        }
        return key;
    }
}
