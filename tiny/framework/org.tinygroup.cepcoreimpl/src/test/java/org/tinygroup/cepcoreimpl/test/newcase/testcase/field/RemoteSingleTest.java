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

public class RemoteSingleTest extends FieldBaseTest {

    public void testRegAndUnSame() {
        CEPCore cepcore = new CEPCoreImpl();

        EventProcessor processor = getEventProcessor("remote",
                EventProcessor.TYPE_REMOTE, "a,b");
        // ------------------------------
        // 注册
        // ------------------------------
        cepcore.registerEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except = createEventProcessorServices();
        addEventProcessorServices(except, "remote", "a,b");
        myAssertEventProcessorServices(except, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except2 = createProcessorMap();
        addProcessorMap(except2, "remote", EventProcessor.TYPE_REMOTE, "a,b");
        myAssertProcessorMap(except2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap("a,b"), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except3 = createServiceIdMap(cepcore);
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(processor);
        except3.put("a", list);
        except3.put("b", list);
        myAssertServiceIdMap(except3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);
        // ------------------------------
        // 注销
        // ------------------------------
        cepcore.unregisterEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu = createEventProcessorServices();
        myAssertEventProcessorServices(exceptu, cepcore);
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

    }

    public void testRegAndUnUnMore() {
        CEPCore cepcore = new CEPCoreImpl();

        EventProcessor processor = getEventProcessor("remote",
                EventProcessor.TYPE_REMOTE, "a,b");
        // ------------------------------
        // 注册
        // ------------------------------
        cepcore.registerEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except = createEventProcessorServices();
        addEventProcessorServices(except, "remote", "a,b");
        myAssertEventProcessorServices(except, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except2 = createProcessorMap();
        addProcessorMap(except2, "remote", EventProcessor.TYPE_REMOTE, "a,b");
        myAssertProcessorMap(except2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap("a,b"), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except3 = createServiceIdMap(cepcore);
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(processor);
        except3.put("a", list);
        except3.put("b", list);
        myAssertServiceIdMap(except3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);
        // ------------------------------
        // 注销
        // ------------------------------
        EventProcessor processor2 = getEventProcessor("remote",
                EventProcessor.TYPE_REMOTE, "a,b,c");
        cepcore.unregisterEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu = createEventProcessorServices();
        myAssertEventProcessorServices(exceptu, cepcore);
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
    }

    public void testRegAndUnUnLess() {
        CEPCore cepcore = new CEPCoreImpl();

        EventProcessor processor = getEventProcessor("remote",
                EventProcessor.TYPE_REMOTE, "a,b");
        // ------------------------------
        // 注册
        // ------------------------------
        cepcore.registerEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except = createEventProcessorServices();
        addEventProcessorServices(except, "remote", "a,b");
        myAssertEventProcessorServices(except, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except2 = createProcessorMap();
        addProcessorMap(except2, "remote", EventProcessor.TYPE_REMOTE, "a,b");
        myAssertProcessorMap(except2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap("a,b"), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except3 = createServiceIdMap(cepcore);
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(processor);
        except3.put("a", list);
        except3.put("b", list);
        myAssertServiceIdMap(except3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);
        // ------------------------------
        // 注销
        // ------------------------------
        EventProcessor processor2 = getEventProcessor("remote",
                EventProcessor.TYPE_REMOTE, "");
        cepcore.unregisterEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> exceptu = createEventProcessorServices();
        myAssertEventProcessorServices(exceptu, cepcore);
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
    }

    public void testDoubleRegMore() {
        CEPCore cepcore = new CEPCoreImpl();

        EventProcessor processor = getEventProcessor("remote",
                EventProcessor.TYPE_REMOTE, "a,b");
        // ------------------------------
        // 注册
        // ------------------------------
        cepcore.registerEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except = createEventProcessorServices();
        addEventProcessorServices(except, "remote", "a,b");
        myAssertEventProcessorServices(except, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except2 = createProcessorMap();
        addProcessorMap(except2, "remote", EventProcessor.TYPE_REMOTE, "a,b");
        myAssertProcessorMap(except2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap("a,b"), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except3 = createServiceIdMap(cepcore);
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(processor);
        except3.put("a", list);
        except3.put("b", list);
        myAssertServiceIdMap(except3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);

        // ------------------------------
        // 注册2
        // ------------------------------
        EventProcessor processor2 = getEventProcessor("remote",
                EventProcessor.TYPE_REMOTE, "a,b,c");
        cepcore.registerEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except4 = createEventProcessorServices();
        addEventProcessorServices(except4, "remote", "a,b,c");
        myAssertEventProcessorServices(except4, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except5 = createProcessorMap();
        addProcessorMap(except5, "remote", EventProcessor.TYPE_REMOTE, "a,b,c");
        myAssertProcessorMap(except5, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap("a,b,c"), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except6 = createServiceIdMap(cepcore);
        List<EventProcessor> list6 = new ArrayList<EventProcessor>();
        list6.add(processor2);
        except6.put("a", list6);
        except6.put("b", list6);
        except6.put("c", list6);
        myAssertServiceIdMap(except6, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);

    }

    public void testDoubleRegLess() {
        CEPCore cepcore = new CEPCoreImpl();

        EventProcessor processor = getEventProcessor("remote",
                EventProcessor.TYPE_REMOTE, "a,b");
        // ------------------------------
        // 注册
        // ------------------------------
        cepcore.registerEventProcessor(processor);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except = createEventProcessorServices();
        addEventProcessorServices(except, "remote", "a,b");
        myAssertEventProcessorServices(except, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except2 = createProcessorMap();
        addProcessorMap(except2, "remote", EventProcessor.TYPE_REMOTE, "a,b");
        myAssertProcessorMap(except2, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap("a,b"), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except3 = createServiceIdMap(cepcore);
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(processor);
        except3.put("a", list);
        except3.put("b", list);
        myAssertServiceIdMap(except3, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);

        // ------------------------------
        // 注册2
        // ------------------------------
        EventProcessor processor2 = getEventProcessor("remote",
                EventProcessor.TYPE_REMOTE, "a");
        cepcore.registerEventProcessor(processor2);
        // EventProcessorID：Service列表
        Map<String, List<ServiceInfo>> except4 = createEventProcessorServices();
        addEventProcessorServices(except4, "remote", "a");
        myAssertEventProcessorServices(except4, cepcore);
        // EventProcessorID：EventProcessor
        Map<String, EventProcessor> except5 = createProcessorMap();
        addProcessorMap(except5, "remote", EventProcessor.TYPE_REMOTE, "a");
        myAssertProcessorMap(except5, cepcore);
        // 远程服务列表
        myAssertRemoteServiceMap(createRemoteServiceMap("a"), cepcore);
        // 服务：处理器列表
        Map<String, List<EventProcessor>> except6 = createServiceIdMap(cepcore);
        List<EventProcessor> list6 = new ArrayList<EventProcessor>();
        list6.add(processor2);
        except6.put("a", list6);
        myAssertServiceIdMap(except6, cepcore);
        // 本地服务列表
        myAssertLocalServices(createLocalServices(""), cepcore);
        // 本地服务Map
        myAssertLocalServiceMap(createLocalServiceMap(""), cepcore);
    }

}
