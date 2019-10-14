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
package org.tinygroup.tinypc;

import junit.framework.TestCase;
import org.tinygroup.tinypc.range.LongRangeSpliter;

import java.util.List;

/**
 * Created by luoguo on 14-3-3.
 */
public class RangeLongSpliterTest extends TestCase {
    public void testSplit() throws Exception {
        LongRangeSpliter spliter = new LongRangeSpliter();
        List<Range<Long>> list = spliter.split(1l, 100l, 10);
        for (Range range : list) {
            System.out.println(range.toString());
        }
    }

    public void testSplit1() throws Exception {
        LongRangeSpliter spliter = new LongRangeSpliter();
        List<Range<Long>> list = spliter.split(0l, 100l, 10);
        for (Range range : list) {
            System.out.println(range.toString());
        }
    }

    public void testSplit2() throws Exception {
        LongRangeSpliter spliter = new LongRangeSpliter();
        List<Range<Long>> list = spliter.split(-2l, 10l, 4);
        for (Range range : list) {
            System.out.println(range.toString());
        }
    }
}
