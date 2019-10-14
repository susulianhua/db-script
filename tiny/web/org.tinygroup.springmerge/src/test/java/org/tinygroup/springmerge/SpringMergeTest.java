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
package org.tinygroup.springmerge;

import junit.framework.TestCase;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.tinygroup.springmerge.beanfactory.SpringMergeApplicationContext;

import java.util.*;

public class SpringMergeTest extends TestCase {

    private static SpringMergeApplicationContext applicationContext;
    private static boolean isInited;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if (!isInited) {
            applicationContext = new SpringMergeApplicationContext(
                    loadMergeBeanDefinition());
            applicationContext.setConfigLocation("classpath:test.xml");
            applicationContext.refresh();
            isInited = true;
        }
    }

    private BeanDefinitionRegistry loadMergeBeanDefinition() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("MERGE-INFO/merge.xml");
        BeanDefinitionRegistry registry = null;
        if (resource.exists()) {
            registry = new XmlBeanFactory(resource);
        } else {
            registry = new DefaultListableBeanFactory();
        }
        return registry;
    }

    public void testStringMerge() {
        MergeObject mergeObject = (MergeObject) applicationContext
                .getBean("stringMerge");
        assertEquals("jpg,docx,png", mergeObject.getSuffix());
    }

    /**
     * 测试Ref
     */
    public void testRefMerge() {
        MergeObject mergeObject = (MergeObject) applicationContext
                .getBean("mergeObject");
        RefObject refObject = mergeObject.getRefObject();
        assertEquals(refObject.getName(), "2");
    }

    /**
     * 测试集合的合并，包括map/set/list/property
     */
    public void testCollectionMerge() {
        MergeObject mergeObject = (MergeObject) applicationContext
                .getBean("mergeObject");
        Map<String, String> map = mergeObject.getParamsMap();
        assertEquals(5, map.size());
        assertEquals("value11", map.get("key1"));
        assertEquals("value5", map.get("key5"));

        Set<String> set = mergeObject.getParamSet();
        assertEquals(5, set.size());//按set规则合并
        Set<String> expectedset = new HashSet<String>();
        expectedset.add("svalue1");
        expectedset.add("svalue2");
        expectedset.add("svalue3");
        expectedset.add("svalue4");
        expectedset.add("svalue5");
        Iterator expectedsetiter = expectedset.iterator();
        while (expectedsetiter.hasNext()) {
            assertTrue(set.contains(expectedsetiter.next()));
        }

        List<String> list = mergeObject.getParamList();
        for (String l : list) {
            System.out.println(l);
        }
        assertEquals(6, list.size());//按set规则合并
        List<String> expectedlist = new ArrayList<String>();
        expectedlist.add("lvalue1");
        expectedlist.add("lvalue2");
        expectedlist.add("lvalue3");
        expectedlist.add("lvalue11");
        expectedlist.add("lvalue4");
        expectedlist.add("lvalue5");
        for (int i = 0; i < expectedlist.size(); i++) {
            assertEquals(list.get(i), expectedlist.get(i));
        }

        Properties properties = mergeObject.getProperties();
        assertEquals(5, properties.size());
        Properties expectedProp = new Properties();
        for (Object key : expectedProp.keySet()) {
            assertEquals(expectedProp.get(key), properties.get(key));
        }

    }

    /**
     * 测试集合的覆盖，包括map/set/list/property
     */
    public void testCollectionOverride() {
        MergeObject mergeObject = (MergeObject) applicationContext
                .getBean("mergeObjectOverride");
        Map<String, String> map = mergeObject.getParamsMap();
        assertEquals(map.size(), 3);
        assertEquals("value11", map.get("key1"));
        assertEquals("value4", map.get("key4"));
        assertEquals("value5", map.get("key5"));
        assertNull(map.get("key2"));
        assertNull(map.get("key3"));

        Set<String> set = mergeObject.getParamSet();
        assertEquals(3, set.size());//按set规则合并
        Set<String> expectedset = new HashSet<String>();
        expectedset.add("svalue11");
        expectedset.add("svalue4");
        expectedset.add("svalue5");
        Iterator expectedsetiter = expectedset.iterator();
        while (expectedsetiter.hasNext()) {
            assertTrue(set.contains(expectedsetiter.next()));
        }

        List<String> list = mergeObject.getParamList();
        assertEquals(3, list.size());//按set规则合并
        List<String> expectedlist = new ArrayList<String>();
        expectedlist.add("lvalue11");
        expectedlist.add("lvalue4");
        expectedlist.add("lvalue5");
        for (int i = 0; i < expectedlist.size(); i++) {
            assertEquals(list.get(i), expectedlist.get(i));
        }

        Properties properties = mergeObject.getProperties();
        assertEquals(3, properties.size());
        Properties expctedProp = new Properties();
        expctedProp.put("pkey1", "pvalue11");
        expctedProp.put("pkey4", "pvalue4");
        expctedProp.put("pkey5", "pvalue5");
        for (Object key : expctedProp.keySet()) {
            assertEquals(expctedProp.get(key), properties.get(key));
        }
    }


    public void testClassMerge() {
        assertTrue(applicationContext.getBean("classMerge") instanceof MergeObjectB);
        MergeObjectB mergeObject = (MergeObjectB) applicationContext.getBean("classMerge");
        MergeObjectB mergeObjectB = (MergeObjectB) applicationContext.getBean("classMerge");
        assertNotSame(mergeObject, mergeObjectB);//测试scope,属性合并后按照merge.xml配置

        assertEquals(mergeObject.getSuffix(), "jpg2,pdf2");
        Map<String, String> map = mergeObject.getParamsMap();
        assertEquals(map.size(), 3);
        assertEquals("value11", map.get("key1"));
        assertEquals("value4", map.get("key4"));
        assertEquals("value5", map.get("key5"));
    }

    public void testMethodReplacer() {
        RefObject refObject = (RefObject) applicationContext.getBean("replaceRefObject");
        assertEquals("ssdf", refObject.getName());
    }
}
