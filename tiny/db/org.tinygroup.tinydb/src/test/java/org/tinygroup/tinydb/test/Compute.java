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
package org.tinygroup.tinydb.test;

public class Compute {

    public static void main(String[] args) {
        Compute c = new Compute();
        int oneBegin = 100;
        int oneTotal = c.deal(oneBegin);
        int twoBegin = oneTotal / 6;
        int twoTotal = c.deal(twoBegin);
        int threeBegin = twoTotal / 6;
        int threeTotal = c.deal(threeBegin);
        System.out.println(threeTotal);
        System.out.println(37.8 * 6 + "");
    }


    public int deal(int num) {
        int p1NextNum = num - 10;
        return p1NextNum * 5 + 20;
    }
}
