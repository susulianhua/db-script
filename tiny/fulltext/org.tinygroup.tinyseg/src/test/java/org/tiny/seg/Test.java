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
package org.tiny.seg;

import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {

    /**
     */
    private static int getValue(String s) {
        int l = s.length();
        int ret = 0;
        switch (l) {
            case 0:
                ret = 0;
                break;
            case 1:
                ret = 1;
                break;
            case 2:
                ret = 3;
                break;
            case 3:
                ret = 7;
                break;
            case 4:
                ret = 9;
                break;
            case 5:
                ret = 11;
                break;
            case 6:
                ret = 15;
                break;
            case 7:
                ret = 19;
                break;
            default:
                ret = 25;
                break;

        }
        return ret;
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        try {
            XmlStringParser parser = new XmlStringParser();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    Test.class.getClassLoader().getResourceAsStream(
                            "huijiasource.txt"), "UTF-8"));
            String str;
            Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]+");
            while ((str = in.readLine()) != null) {
                Matcher matcher = pattern.matcher(str);
                int start = 0;
                while (matcher.find(start)) {
                    System.out.println(matcher.group());
                    start = matcher.end();
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
