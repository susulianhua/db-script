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

public class ConnectTest {
    public static void main(String[] args) {
        Jedis j = new Jedis("testdb", 11112);
        j.auth(IJedisConstant.PASSWOPD);
        int times = 1000;
        for (int i = 0; i < times; i++) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            try {

                j.connect();
                System.out.println("连接成功");
            } catch (Exception e) {
                System.out.println("连接失败");
            }
            long time = System.currentTimeMillis();
            try {
                System.out.println("--" + j.ping());
                if (j.ping().equals("PONG")) {
                    System.out.println("获取成功");
                    System.out.println((System.currentTimeMillis() - time));
                } else {
                    System.out.println((System.currentTimeMillis() - time));
                    System.out.println("获取失败");
                    j.disconnect();
                }
            } catch (Exception e) {
                System.out.println((System.currentTimeMillis() - time));
                System.out.println("获取失败");
                j.disconnect();
            }
        }

    }
}
