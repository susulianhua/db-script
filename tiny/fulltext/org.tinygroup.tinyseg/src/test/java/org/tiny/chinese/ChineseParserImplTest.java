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
package org.tiny.chinese;

import junit.framework.TestCase;
import org.tiny.seg.impl.ChineseParserImpl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChineseParserImplTest extends TestCase {

    public void testChinese() {

        try {
            ChineseParserImpl cpi = new ChineseParserImpl();
//			URL u = this.getClass().getClassLoader().getResource("199801.dic");
//			FileInputStream f = new FileInputStream(new File(u.toURI()));
            FileInputStream f = new FileInputStream(new File("src/test/resources/199801.dic"));
            cpi.loadDict(f, "utf-8");
            List<String> result = new ArrayList<String>();
            Map<String, Integer> resultMap = new HashMap<String, Integer>();
            cpi.segmentWordMax("中华人民共和国成立拉国珠4国飞棋苹果6", result);
            cpi.segmentWordMax("中华人民共和国成立拉国珠4国飞棋苹果6", resultMap);
            System.out.println(result);
            System.out.println(resultMap);
            result.clear();
            resultMap.clear();
            cpi.segmentWordMin("饕餮", result);
            cpi.segmentWordMin("中华人民共和国成立拉国珠4国飞棋苹果6", result);
            cpi.segmentWordMin("中华人民共和国成立拉国珠4国飞棋苹果6", resultMap);
            System.out.println(result);
            System.out.println(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void testMatch() {
        Pattern pattern = Pattern
                .compile("[a-z|A-Z]+|[0-9]+|[\u4e00-\u9fa5]+");
        Matcher matcher = pattern.matcher("中华人民共和国成立拉国珠4国飞棋苹果6");
        int start = 0;
        while (matcher.find(start)) {
            String str = matcher.group();
            System.out.println("==========" + str);
            start = matcher.end();
        }
    }
}
