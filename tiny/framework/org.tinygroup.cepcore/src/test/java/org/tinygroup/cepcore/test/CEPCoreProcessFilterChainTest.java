package org.tinygroup.cepcore.test;

import junit.framework.TestCase;
import org.tinygroup.cepcore.CEPCoreProcessFilter;
import org.tinygroup.cepcore.CEPCoreProcessFilterChain;
import org.tinygroup.cepcore.impl.CEPCoreProcessFilterChainImpl;

import java.util.ArrayList;
import java.util.List;

public class CEPCoreProcessFilterChainTest extends TestCase {

    /**
     * 测试处理链是否是按照过滤器在数组中的顺序 顺序执行
     */
    public synchronized void testSimple() {
        List<CEPCoreProcessFilter> filters = new ArrayList<CEPCoreProcessFilter>();
        filters.add(new ProcessTestFilter("a"));
        filters.add(new ProcessTestFilter("b"));
        filters.add(new ProcessTestFilter("c"));
        CEPCoreProcessFilterChain chain = new CEPCoreProcessFilterChainImpl(filters);
        chain.getDealer(new ProcessTestDealer("d"), null, null, null).process(null, null, null);
        assertEquals("abcd", ProcessTestFilter.getValue());
    }

    /**
     * 测试处理器添加功能，添加到队列的倒数第二位
     */
    public synchronized void testAdd() {
        List<CEPCoreProcessFilter> filters = new ArrayList<CEPCoreProcessFilter>();
        filters.add(new ProcessTestFilter("a"));
        filters.add(new ProcessTestFilter("b"));
        CEPCoreProcessFilterChain chain = new CEPCoreProcessFilterChainImpl(filters);
        chain.insertFilterToBegin(new ProcessTestFilter("c"));
        chain.getDealer(new ProcessTestDealer("d"), null, null, null).process(null, null, null);
        assertEquals("cabd", ProcessTestFilter.getValue());
    }

    /**
     * 测试处理器添加功能，添加到队列的倒数第二位
     */
    public synchronized void testAdd2() {
        List<CEPCoreProcessFilter> filters = new ArrayList<CEPCoreProcessFilter>();
        filters.add(new ProcessTestFilter("a"));
        filters.add(new ProcessTestFilter("b"));
        filters.add(new ProcessTestFilter("c"));
        CEPCoreProcessFilterChain chain = new CEPCoreProcessFilterChainImpl(filters);
        chain.addFilter(new ProcessTestFilter("d"));
        chain.addFilter(new ProcessTestFilter("e"));
        chain.addFilter(new ProcessTestFilter("f"));
        chain.getDealer(new ProcessTestDealer("d"), null, null, null).process(null, null, null);
        assertEquals("abcdefd", ProcessTestFilter.getValue());
    }

    /**
     * 测试处理器添加功能，在队首添加处理器
     */
    public synchronized void testInsert() {
        List<CEPCoreProcessFilter> filters = new ArrayList<CEPCoreProcessFilter>();
        filters.add(new ProcessTestFilter("a"));
        filters.add(new ProcessTestFilter("b"));
        filters.add(new ProcessTestFilter("c"));
        CEPCoreProcessFilterChain chain = new CEPCoreProcessFilterChainImpl(filters);
        chain.insertFilterToBegin(new ProcessTestFilter("d"));
        chain.getDealer(new ProcessTestDealer("d"), null, null, null).process(null, null, null);
        assertEquals("dabcd", ProcessTestFilter.getValue());
    }

    /**
     * 测试处理器添加功能，在队首添加处理器
     */
    public synchronized void testInsert2() {
        List<CEPCoreProcessFilter> filters = new ArrayList<CEPCoreProcessFilter>();
        filters.add(new ProcessTestFilter("a"));
        filters.add(new ProcessTestFilter("b"));
        filters.add(new ProcessTestFilter("c"));
        CEPCoreProcessFilterChain chain = new CEPCoreProcessFilterChainImpl(filters);
        chain.insertFilterToBegin(new ProcessTestFilter("d"));
        chain.insertFilterToBegin(new ProcessTestFilter("e"));
        chain.insertFilterToBegin(new ProcessTestFilter("f"));
        chain.getDealer(new ProcessTestDealer("d"), null, null, null).process(null, null, null);
        assertEquals("fedabcd", ProcessTestFilter.getValue());
    }

}
