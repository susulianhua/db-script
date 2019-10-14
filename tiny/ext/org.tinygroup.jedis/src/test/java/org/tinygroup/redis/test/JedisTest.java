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

import redis.clients.jedis.Jedis;

public class JedisTest {
    public static void main(String[] args) {
        Jedis j2 = new Jedis("testdb", 11111);
        j2.auth(IJedisConstant.PASSWOPD);
        j2.get("a");
        j2.connect();
        System.out.println(j2);
        System.out.println(j2.isConnected());
    }
}
