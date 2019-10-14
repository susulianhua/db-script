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
package org.tinygroup.cache.redis;

import junit.framework.Assert;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cache.Cache;
import org.tinygroup.cache.CacheManager;
import org.tinygroup.tinyrunner.Runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证RedisCache的性能<br>
 * 具体的redis配置地址请修改sample.jedisconfig.xml
 */
public class RedisCachePerformanceTest {

    public static void main(String[] args) {
        Runner.init(null, new ArrayList<String>());
        CacheManager manager = BeanContainerFactory.getBeanContainer(RedisCacheTest.class.getClassLoader()).getBean("redisCacheManager");
        Cache cache = manager.createCache("server01");
        Map<String, Food> foods = new HashMap<String, Food>();
        foods.put("food1", new Food("大蒜", "蔬菜", 7.0));
        foods.put("food2", new Food("炸酱面", "面食", 20.99));
        foods.put("food3", new Food("德国火腿", "干货", 225.0));

        long time = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            cache.put("food", foods.get("food1"));
            Assert.assertEquals(cache.get("food"), foods.get("food1"));
            cache.remove("food");
        }
        System.out.println("cost time:" + (System.currentTimeMillis() - time) + "ms");
    }

}
