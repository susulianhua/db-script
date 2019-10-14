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
package org.tinygroup.cepcoreimpl.test.newcase.testcase.field;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcoreimpl.CEPCoreImpl;
import org.tinygroup.event.ServiceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocalDoubleTest2 extends FieldBaseTest {
    public void testSameRRUU() {
        CEPCore cepcore = new CEPCoreImpl();
        EventProcessor processor = getEventProcessor("local",
                EventProcessor.TYPE_LOCAL, "a,b");
        // ------------------------------
        // 注册
        // ------------------------------
        cepcore.registerEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except = createEventProcessorServices();
        addEventProcessorServices(except, "local", "a,b");
        myAssertEventProcessorServices(except, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except2 = createProcessorMap();
        addProcessorMap(except2, "local", EventProcessor.TYPE_LOCAL, "a,b");
        myAssertProcessorMap(except2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except3 = createServiceIdMap(cepcore);
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(processor);
        except3.put("a", list);
        except3.put("b", list);
        myAssertServiceIdMap(except3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("a,b"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("a,b"), cepcore);

        // ------------------------------
        // 注册
        // ------------------------------
        EventProcessor processor2 = getEventProcessor("local1",
                EventProcessor.TYPE_LOCAL, "a,b");
        cepcore.registerEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except4 = createEventProcessorServices();
        addEventProcessorServices(except4, "local1", "a,b");
        addEventProcessorServices(except4, "local", "a,b");
        myAssertEventProcessorServices(except4, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except5 = createProcessorMap();
        addProcessorMap(except5, "local", EventProcessor.TYPE_LOCAL, "a,b");
        addProcessorMap(except5, "local1", EventProcessor.TYPE_LOCAL, "a,b");
        myAssertProcessorMap(except5, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except6 = createServiceIdMap(cepcore);
        List<EventProcessor> list6 = new ArrayList<EventProcessor>();
        list6.add(processor2);
        list6.add(processor);
        except6.put("a", list6);
        except6.put("b", list6);
        myAssertServiceIdMap(except6, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("a,b"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("a,b"), cepcore);

        // ------------------------------
        // 注销
        // ------------------------------
        cepcore.unregisterEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu1 = createEventProcessorServices();
        addEventProcessorServices(exceptu1, "local1", "a,b");
        myAssertEventProcessorServices(exceptu1, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> exceptu2 = createProcessorMap();
        addProcessorMap(exceptu2, "local1", EventProcessor.TYPE_LOCAL, "a,b");
        myAssertProcessorMap(exceptu2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> exceptu3 = createServiceIdMap(cepcore);
        List<EventProcessor> listu3 = new ArrayList<EventProcessor>();
        listu3.add(processor2);
        exceptu3.put("a", listu3);
        exceptu3.put("b", listu3);
        myAssertServiceIdMap(exceptu3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("a,b"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("a,b"), cepcore);
        // ------------------------------
        // 注销
        // ------------------------------
        cepcore.unregisterEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu4 = createEventProcessorServices();
        myAssertEventProcessorServices(exceptu4, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> exceptu5 = createProcessorMap();
        myAssertProcessorMap(exceptu5, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> exceptu6 = createServiceIdMap(cepcore);
        myAssertServiceIdMap(exceptu6, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);
    }

    public void testSameRURU() {
        CEPCore cepcore = new CEPCoreImpl();
        EventProcessor processor = getEventProcessor("local",
                EventProcessor.TYPE_LOCAL, "a,b");
        // ------------------------------
        // 注册
        // ------------------------------
        cepcore.registerEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except = createEventProcessorServices();
        addEventProcessorServices(except, "local", "a,b");
        myAssertEventProcessorServices(except, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except2 = createProcessorMap();
        addProcessorMap(except2, "local", EventProcessor.TYPE_LOCAL, "a,b");
        myAssertProcessorMap(except2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except3 = createServiceIdMap(cepcore);
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(processor);
        except3.put("a", list);
        except3.put("b", list);
        myAssertServiceIdMap(except3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("a,b"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("a,b"), cepcore);
        // ------------------------------
        // 注销
        // ------------------------------
        cepcore.unregisterEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu1 = createEventProcessorServices();
        myAssertEventProcessorServices(exceptu1, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> exceptu2 = createProcessorMap();
        myAssertProcessorMap(exceptu2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> exceptu3 = createServiceIdMap(cepcore);
        myAssertServiceIdMap(exceptu3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);
        // ------------------------------
        // 注册
        // ------------------------------
        EventProcessor processor2 = getEventProcessor("local1",
                EventProcessor.TYPE_LOCAL, "a,b");
        cepcore.registerEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except4 = createEventProcessorServices();
        addEventProcessorServices(except4, "local1", "a,b");
        myAssertEventProcessorServices(except4, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except5 = createProcessorMap();
        addProcessorMap(except5, "local1", EventProcessor.TYPE_LOCAL, "a,b");
        myAssertProcessorMap(except5, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except6 = createServiceIdMap(cepcore);
        List<EventProcessor> list6 = new ArrayList<EventProcessor>();
        list6.add(processor2);
        except6.put("a", list6);
        except6.put("b", list6);
        myAssertServiceIdMap(except6, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("a,b"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("a,b"), cepcore);
        // ------------------------------
        // 注销
        // ------------------------------
        cepcore.unregisterEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu4 = createEventProcessorServices();
        myAssertEventProcessorServices(exceptu4, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> exceptu5 = createProcessorMap();
        myAssertProcessorMap(exceptu5, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> exceptu6 = createServiceIdMap(cepcore);
        myAssertServiceIdMap(exceptu6, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);
    }

    public void testDifferRRUU() {
        CEPCore cepcore = new CEPCoreImpl();

        // ------------------------------
        // 注册
        // ------------------------------
        EventProcessor processor = getEventProcessor("local",
                EventProcessor.TYPE_LOCAL, "a,b");
        cepcore.registerEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except = createEventProcessorServices();
        addEventProcessorServices(except, "local", "a,b");
        myAssertEventProcessorServices(except, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except2 = createProcessorMap();
        addProcessorMap(except2, "local", EventProcessor.TYPE_LOCAL, "a,b");
        myAssertProcessorMap(except2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except3 = createServiceIdMap(cepcore);
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(processor);
        except3.put("a", list);
        except3.put("b", list);
        myAssertServiceIdMap(except3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("a,b"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("a,b"), cepcore);

        // ------------------------------
        // 注册
        // ------------------------------
        EventProcessor processor2 = getEventProcessor("local1",
                EventProcessor.TYPE_LOCAL, "c,d");
        cepcore.registerEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except4 = createEventProcessorServices();
        addEventProcessorServices(except4, "local1", "c,d");
        addEventProcessorServices(except4, "local", "a,b");
        myAssertEventProcessorServices(except4, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except5 = createProcessorMap();
        addProcessorMap(except5, "local", EventProcessor.TYPE_LOCAL, "a,b");
        addProcessorMap(except5, "local1", EventProcessor.TYPE_LOCAL, "c,d");
        myAssertProcessorMap(except5, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except6 = createServiceIdMap(cepcore);
        List<EventProcessor> list6 = new ArrayList<EventProcessor>();
        list6.add(processor);
        except6.put("a", list6);
        except6.put("b", list6);
        List<EventProcessor> list7 = new ArrayList<EventProcessor>();
        list7.add(processor2);
        except6.put("c", list7);
        except6.put("d", list7);
        myAssertServiceIdMap(except6, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("a,b,c,d"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("a,b,c,d"), cepcore);
        // ------------------------------
        // 注销
        // ------------------------------
        cepcore.unregisterEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu1 = createEventProcessorServices();
        addEventProcessorServices(exceptu1, "local1", "c,d");
        myAssertEventProcessorServices(exceptu1, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> exceptu2 = createProcessorMap();
        addProcessorMap(exceptu2, "local1", EventProcessor.TYPE_LOCAL, "c,d");
        myAssertProcessorMap(exceptu2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> exceptu3 = createServiceIdMap(cepcore);
        List<EventProcessor> listu3 = new ArrayList<EventProcessor>();
        listu3.add(processor2);
        exceptu3.put("c", listu3);
        exceptu3.put("d", listu3);
        myAssertServiceIdMap(exceptu3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("c,d"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("c,d"), cepcore);
        // ------------------------------
        // 注销
        // ------------------------------
        cepcore.unregisterEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu4 = createEventProcessorServices();
        myAssertEventProcessorServices(exceptu4, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> exceptu5 = createProcessorMap();
        myAssertProcessorMap(exceptu5, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> exceptu6 = createServiceIdMap(cepcore);
        myAssertServiceIdMap(exceptu6, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);
    }

    public void testCrossRRUU() {
        CEPCore cepcore = new CEPCoreImpl();
        EventProcessor processor = getEventProcessor("local",
                EventProcessor.TYPE_LOCAL, "a,b");
        // ------------------------------
        // 注册
        // ------------------------------
        cepcore.registerEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except = createEventProcessorServices();
        addEventProcessorServices(except, "local", "a,b");
        myAssertEventProcessorServices(except, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except2 = createProcessorMap();
        addProcessorMap(except2, "local", EventProcessor.TYPE_LOCAL, "a,b");
        myAssertProcessorMap(except2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except3 = createServiceIdMap(cepcore);
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(processor);
        except3.put("a", list);
        except3.put("b", list);
        myAssertServiceIdMap(except3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("a,b"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("a,b"), cepcore);

        // ------------------------------
        // 注册
        // ------------------------------
        EventProcessor processor2 = getEventProcessor("local1",
                EventProcessor.TYPE_LOCAL, "a,c");
        cepcore.registerEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except4 = createEventProcessorServices();
        addEventProcessorServices(except4, "local1", "a,c");
        addEventProcessorServices(except4, "local", "a,b");
        myAssertEventProcessorServices(except4, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except5 = createProcessorMap();
        addProcessorMap(except5, "local", EventProcessor.TYPE_LOCAL, "a,b");
        addProcessorMap(except5, "local1", EventProcessor.TYPE_LOCAL, "a,c");
        myAssertProcessorMap(except5, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except6 = createServiceIdMap(cepcore);
        List<EventProcessor> list6 = new ArrayList<EventProcessor>();
        list6.add(processor2);
        list6.add(processor);
        except6.put("a", list6);
        List<EventProcessor> list61 = new ArrayList<EventProcessor>();
        list61.add(processor);
        except6.put("b", list61);
        List<EventProcessor> list62 = new ArrayList<EventProcessor>();
        list62.add(processor2);
        except6.put("c", list62);
        myAssertServiceIdMap(except6, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("a,b,c"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("a,b,c"), cepcore);

        // ------------------------------
        // 注销
        // ------------------------------
        cepcore.unregisterEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu1 = createEventProcessorServices();
        addEventProcessorServices(exceptu1, "local1", "a,c");
        myAssertEventProcessorServices(exceptu1, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> exceptu2 = createProcessorMap();
        addProcessorMap(exceptu2, "local1", EventProcessor.TYPE_LOCAL, "a,c");
        myAssertProcessorMap(exceptu2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> exceptu3 = createServiceIdMap(cepcore);
        List<EventProcessor> listu3 = new ArrayList<EventProcessor>();
        listu3.add(processor2);
        exceptu3.put("a", listu3);
        exceptu3.put("c", listu3);
        myAssertServiceIdMap(exceptu3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("a,c"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("a,c"), cepcore);
        // ------------------------------
        // 注销
        // ------------------------------
        cepcore.unregisterEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu4 = createEventProcessorServices();
        myAssertEventProcessorServices(exceptu4, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> exceptu5 = createProcessorMap();
        myAssertProcessorMap(exceptu5, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> exceptu6 = createServiceIdMap(cepcore);
        myAssertServiceIdMap(exceptu6, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);
    }

    public void testCrossRURU() {
        CEPCore cepcore = new CEPCoreImpl();
        EventProcessor processor = getEventProcessor("local",
                EventProcessor.TYPE_LOCAL, "a,b");
        // ------------------------------
        // 注册
        // ------------------------------
        cepcore.registerEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except = createEventProcessorServices();
        addEventProcessorServices(except, "local", "a,b");
        myAssertEventProcessorServices(except, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except2 = createProcessorMap();
        addProcessorMap(except2, "local", EventProcessor.TYPE_LOCAL, "a,b");
        myAssertProcessorMap(except2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except3 = createServiceIdMap(cepcore);
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(processor);
        except3.put("a", list);
        except3.put("b", list);
        myAssertServiceIdMap(except3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("a,b"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("a,b"), cepcore);
        // ------------------------------
        // 注销
        // ------------------------------
        cepcore.unregisterEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu1 = createEventProcessorServices();
        myAssertEventProcessorServices(exceptu1, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> exceptu2 = createProcessorMap();
        myAssertProcessorMap(exceptu2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> exceptu3 = createServiceIdMap(cepcore);
        myAssertServiceIdMap(exceptu3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);
        // ------------------------------
        // 注册
        // ------------------------------
        EventProcessor processor2 = getEventProcessor("local1",
                EventProcessor.TYPE_LOCAL, "a,c");
        cepcore.registerEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except4 = createEventProcessorServices();
        addEventProcessorServices(except4, "local1", "c,a");
        myAssertEventProcessorServices(except4, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except5 = createProcessorMap();
        addProcessorMap(except5, "local1", EventProcessor.TYPE_LOCAL, "c,a");
        myAssertProcessorMap(except5, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except6 = createServiceIdMap(cepcore);
        List<EventProcessor> list6 = new ArrayList<EventProcessor>();
        list6.add(processor2);
        except6.put("c", list6);
        except6.put("a", list6);
        myAssertServiceIdMap(except6, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices("c,a"), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap("c,a"), cepcore);
        // ------------------------------
        // 注销
        // ------------------------------
        cepcore.unregisterEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu4 = createEventProcessorServices();
        myAssertEventProcessorServices(exceptu4, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> exceptu5 = createProcessorMap();
        myAssertProcessorMap(exceptu5, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap(""), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> exceptu6 = createServiceIdMap(cepcore);
        myAssertServiceIdMap(exceptu6, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);
    }
}
