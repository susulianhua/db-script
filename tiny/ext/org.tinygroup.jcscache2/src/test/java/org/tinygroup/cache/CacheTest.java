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
package org.tinygroup.cache;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cache.exception.CacheException;
import org.tinygroup.fileresolver.FileResolverFactory;
import org.tinygroup.fileresolver.FileResolverUtil;
import org.tinygroup.fileresolver.impl.I18nFileProcessor;
import org.tinygroup.fileresolver.impl.XStreamFileProcessor;
import org.tinygroup.springutil.SpringBeanContainer;
import org.tinygroup.springutil.fileresolver.SpringBeansFileProcessor;

import java.util.concurrent.atomic.AtomicInteger;

public class CacheTest extends TestCase {
    static {
        BeanContainerFactory.initBeanContainer(SpringBeanContainer.class
                .getName());
        FileResolverFactory.getFileResolver().addFileProcessor(
                new XStreamFileProcessor());
        FileResolverFactory.getFileResolver().addFileProcessor(
                new I18nFileProcessor());
        FileResolverFactory.getFileResolver().addFileProcessor(
                new SpringBeansFileProcessor());
        FileResolverUtil.addClassPathPattern(FileResolverFactory
                .getFileResolver());
        FileResolverFactory.getFileResolver().addResolvePath(
                FileResolverUtil.getClassPath(FileResolverFactory
                        .getFileResolver()));
        // FileResolverFactory.getFileResolver().addResolvePath(FileResolverUtil.getWebClasses());

        FileResolverFactory.getFileResolver().resolve();
    }

    Cache cache;

    public static void main(String[] args) throws InterruptedException {
        // final byte[] lock = new byte[0];
        //
        // Thread thread = new Thread(new Runnable() {
        // public void run() {
        // CompositeCacheManager cacheManager = CompositeCacheManager
        // .getInstance("/cache1.ccf");
        // CacheManager manager2 = JcsCacheManager
        // .getInstance(cacheManager);
        // Cache cache = manager2.createCache("testCache1");
        // System.out.println(cache.get("test"));
        // while (true) {
        // synchronized (lock) {
        // try {
        // lock.wait(100);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // System.out.println(cache.get("test"));
        // lock.notifyAll();
        // }
        // }
        // }
        // });
        // thread.start();
        //
        // CacheManager manager1 = JcsCacheManager.getInstance();
        // Cache cache = manager1.createCache("testCache1");
        // Map<String, String> map = new HashMap<String, String>();
        // map.put("key", "init value");
        // cache.put("test", map);
        // for (int i = 0; i < 100000; i++) {
        // synchronized (lock) {
        // map.put("key", "changeValue" + i);
        // cache.remove("test");
        // cache.put("test", map);
        // lock.notifyAll();
        // lock.wait(100);
        // }
        // }
        //

        // CompositeCacheManager cacheManager = CompositeCacheManager
        // .getInstance("/cache.ccf");
        // CacheManager manager2 = JcsCacheManager
        // .getInstance(cacheManager);
        // Cache cache = manager2.createCache("testCache1");
        // System.out.println(cache.get("test"));
        // while (true) {
        // System.out.println(cache.get("test"));
        // Thread.sleep(1000);
        // }
        // JCS cache=JCS.getInstance("testCache1");
        // while(true){
        // System.out.println(cache.get("test"));
        // Thread.sleep(1000);
        // }
    }

    protected void setUp() throws Exception {
        super.setUp();
        cache = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean("jcsCache");
        cache.init("testCache1");
        // cache.clear();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        // cache.destory();
    }

    public void testGetString() throws CacheException {
        cache.put("aa", "123");
        assertEquals("123", cache.get("aa"));
    }

    public void testPutSafe() {
        try {
            cache.putSafe("aa", 123);
            cache.putSafe("aa", "bb");
            fail();
        } catch (CacheException e) {
        }
    }

    public void testPutStringStringObject() throws CacheException {
        cache.put("group", "aa", "123");
        assertEquals("123", cache.get("group", "aa"));
    }

    public void testGetGroupKeys() throws CacheException {
        cache.put("groupa", "aa1", "123");
        cache.put("groupa", "aa2", "123");
        cache.put("groupa", "aa3", "123");
        assertEquals(3, cache.getGroupKeys("groupa").size());
    }

    public void testCleanGroup() throws CacheException {
        cache.put("bb", "123");
        cache.put("group", "aa1", "123");
        cache.put("group", "aa2", "123");
        cache.put("group", "aa3", "123");
        cache.cleanGroup("group");
        cache.get("group", "aa1");
    }

    public void testClear() throws CacheException {
        cache.put("bb", "123");
        assertEquals("123", cache.get("bb"));
        cache.clear();
        cache.get("bb");
    }

    public void testRemoveStringString() throws CacheException {
        cache.put("group", "bb", "123");
        assertEquals("123", cache.get("group", "bb"));
        cache.remove("group", "bb");
        cache.get("group", "bb");
    }

    public void testRemove() throws CacheException {
        cache.put("bb", "123");
        assertEquals("123", cache.get("bb"));
        cache.remove("bb");
        cache.get("bb");
    }

    public void testGetStrings() throws CacheException {
        cache.put("aa", "123");
        cache.put("bb", "456");
        assertEquals(2, cache.get(new String[]{"aa", "bb"}).length);
    }

    public void testGetGroupStrings() throws CacheException {
        cache.put("group", "aa", "111");
        cache.put("group", "bb", "222");
        cache.put("group1", "cc", "333");
        cache.put("group1", "dd", "444");
        assertEquals(2, cache.get("group", new String[]{"aa", "bb"}).length);
        assertEquals(2, cache.get("group1", new String[]{"cc", "dd"}).length);
    }

    public void testRemoveStrings() throws CacheException {
        cache.put("aa", "111");
        cache.put("bb", "222");
        cache.remove(new String[]{"aa", "bb"});
        assertNull(cache.get(new String[]{"aa", "bb"})[0]);
        assertNull(cache.get(new String[]{"aa", "bb"})[1]);
    }

    public void testRemoveGroupStrings() throws CacheException {
        cache.put("group", "aa", "111");
        cache.put("group", "bb", "222");
        cache.put("group1", "cc", "333");
        cache.put("group1", "dd", "444");
        cache.remove("group", new String[]{"aa", "bb"});
        assertNull(cache.get("group", new String[]{"aa", "bb"})[0]);
        assertNull(cache.get("group", new String[]{"aa", "bb"})[1]);
    }

    public void testGetStats() {
        System.out.println(System.getProperty("user.dir"));
        System.out.println(cache.getStats());
    }

    public void testFreeMemoryElements() throws CacheException {
        cache.put("aa", "aa");
        for (int i = 0; i < 100; i++) {
            cache.put("aa" + i, i);
        }
        for (int j = 0; j < 500; j++) {
            cache.get("aa");
        }
        try {
            cache.freeMemoryElements(100);
            assertEquals("aa", cache.get("aa"));
            cache.get("aa1");
            // /fail();
        } catch (Exception e) {

        }

    }

    public void testConcurrent() throws InterruptedException {
        cache.put("group", "aa", "111");
        cache.put("group", "bb", "222");
        final AtomicInteger num = new AtomicInteger(0);
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    cache.put("group", "aa" + i, i);
                }
            }
        });
        thread1.setDaemon(true);
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cache.cleanGroup("group");
                } catch (Exception e) {
                    num.getAndIncrement();
                }
            }
        });
        thread2.setDaemon(true);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertTrue(num.intValue() == 0);

    }

}
