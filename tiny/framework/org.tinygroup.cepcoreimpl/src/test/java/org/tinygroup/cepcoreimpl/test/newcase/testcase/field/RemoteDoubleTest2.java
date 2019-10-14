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
import java.util.HashMap;
import java.util.List;

public class RemoteDoubleTest2 extends FieldBaseTest {
    /**
     * @description 两个processor, 二者服务一样，先都注册后依次注销
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testSameRRUU() {
        CEPCore cepcore = new CEPCoreImpl();

        // 第一个processor初始化数据
        String firstIds = "a,b,c";
        String firstEventProssorId = "remote-1";
        EventProcessor firstProcessor = getEventProcessor(firstEventProssorId,
                EventProcessor.TYPE_REMOTE, firstIds);
        // 第一个processor注册
        cepcore.registerEventProcessor(firstProcessor);
        // 创建第一个processor数据校验对象
        DataObject firstDob = new DataObject();
        firstDob.createData(firstProcessor, firstEventProssorId, firstIds,
                EventProcessor.TYPE_REMOTE);
        // 第一个processor注册结果断言
        myAssertEventProcessorServices(firstDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(firstDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(firstDob.getLocalServices(), cepcore);
        myAssertProcessorMap(firstDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(firstDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(firstDob.getRemoteServiceMap(), cepcore);

        // 第二个processor初始化数据
        String secIds = "a,b,c";
        String secEventProssorId = "remote-2";
        EventProcessor secProcessor = getEventProcessor(secEventProssorId,
                EventProcessor.TYPE_REMOTE, secIds);
        // 第二个processor注册
        cepcore.registerEventProcessor(secProcessor);
        // 创建第二个processor数据校验对象
        DataObject secDob = new DataObject();
        secDob.createData(secProcessor, secEventProssorId, secIds,
                EventProcessor.TYPE_REMOTE);

        // 注册之后信息数据增加，为了防止对已有数据校验对象产生影响，这里重新创建一个新的对象，包含了原来所有的数据
        DataObject threeDob = new DataObject();
        threeDob.createData(secProcessor, secEventProssorId, secIds,
                EventProcessor.TYPE_REMOTE);
        threeDob.createData(firstProcessor, firstEventProssorId, firstIds,
                EventProcessor.TYPE_REMOTE);
        // 两个processor注册完毕之后断言
        myAssertEventProcessorServices(threeDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(threeDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(threeDob.getLocalServices(), cepcore);
        myAssertProcessorMap(threeDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(threeDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(threeDob.getRemoteServiceMap(), cepcore);

        // 第一个processor注销
        cepcore.unregisterEventProcessor(firstProcessor);
        // 第一个processor注销断言，此时剩余数据为第二个processor的注册数据
        myAssertEventProcessorServices(secDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(secDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(secDob.getLocalServices(), cepcore);
        myAssertProcessorMap(secDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(secDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(secDob.getRemoteServiceMap(), cepcore);

        // 第二个processor注销
        cepcore.unregisterEventProcessor(secProcessor);
        // 第二个processor注销断言，因为都已注销，所以数据结果应该没空
        myAssertEventProcessorServices(
                new HashMap<String, List<ServiceInfo>>(), cepcore);
        myAssertLocalServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
        myAssertLocalServices(new ArrayList<ServiceInfo>(), cepcore);
        myAssertProcessorMap(new HashMap<String, EventProcessor>(), cepcore);
        myAssertServiceIdMap(new HashMap<String, List<EventProcessor>>(),
                cepcore);
        myAssertRemoteServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
    }

    /**
     * @description 两个processor, 二者服务一样，各自先注册再注销
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testSameRURU() {
        CEPCore cepcore = new CEPCoreImpl();

        // 第一个processor初始化数据
        String firstIds = "a,b,c";
        String firstEventProssorId = "remote-1";
        EventProcessor firstProcessor = getEventProcessor(firstEventProssorId,
                EventProcessor.TYPE_REMOTE, firstIds);
        // 第一个processor注册
        cepcore.registerEventProcessor(firstProcessor);
        // 创建第一个processor数据校验对象
        DataObject firstDob = new DataObject();
        firstDob.createData(firstProcessor, firstEventProssorId, firstIds,
                EventProcessor.TYPE_REMOTE);
        // 第一个processor注册结果断言
        myAssertEventProcessorServices(firstDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(firstDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(firstDob.getLocalServices(), cepcore);
        myAssertProcessorMap(firstDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(firstDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(firstDob.getRemoteServiceMap(), cepcore);
        // 第一个processor注销
        cepcore.unregisterEventProcessor(firstProcessor);
        // 第一个processor注销结果断言
        myAssertEventProcessorServices(
                new HashMap<String, List<ServiceInfo>>(), cepcore);
        myAssertLocalServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
        myAssertLocalServices(new ArrayList<ServiceInfo>(), cepcore);
        myAssertProcessorMap(new HashMap<String, EventProcessor>(), cepcore);
        myAssertServiceIdMap(new HashMap<String, List<EventProcessor>>(),
                cepcore);
        myAssertRemoteServiceMap(new HashMap<String, ServiceInfo>(), cepcore);

        // 第二个processor初始化数据
        String secIds = "a,b,c";
        String secEventProssorId = "remote-2";
        EventProcessor secProcessor = getEventProcessor(secEventProssorId,
                EventProcessor.TYPE_REMOTE, secIds);
        // 第二个processor注册
        cepcore.registerEventProcessor(secProcessor);
        // 创建第二个processor数据校验对象
        DataObject secDob = new DataObject();
        secDob.createData(secProcessor, secEventProssorId, secIds,
                EventProcessor.TYPE_REMOTE);
        // 第二个processor注册结果断言
        myAssertEventProcessorServices(secDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(secDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(secDob.getLocalServices(), cepcore);
        myAssertProcessorMap(secDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(secDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(secDob.getRemoteServiceMap(), cepcore);
        // 第二个processor注销
        cepcore.unregisterEventProcessor(secProcessor);
        // 第二个processor注销断言
        myAssertEventProcessorServices(
                new HashMap<String, List<ServiceInfo>>(), cepcore);
        myAssertLocalServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
        myAssertLocalServices(new ArrayList<ServiceInfo>(), cepcore);
        myAssertProcessorMap(new HashMap<String, EventProcessor>(), cepcore);
        myAssertServiceIdMap(new HashMap<String, List<EventProcessor>>(),
                cepcore);
        myAssertRemoteServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
    }

    /**
     * @description 两个processor, 二者之间服务不一样
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testDifferRRUU() {
        CEPCore cepcore = new CEPCoreImpl();

        // 第一个processor初始化数据
        String firstIds = "a,b,c";
        String firstEventProssorId = "remote-1";
        EventProcessor firstProcessor = getEventProcessor(firstEventProssorId,
                EventProcessor.TYPE_REMOTE, firstIds);
        // 第一个processor注册
        cepcore.registerEventProcessor(firstProcessor);
        // 创建第一个processor数据校验对象
        DataObject firstDob = new DataObject();
        firstDob.createData(firstProcessor, firstEventProssorId, firstIds,
                EventProcessor.TYPE_REMOTE);
        // 第一个processor注册结果断言
        myAssertEventProcessorServices(firstDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(firstDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(firstDob.getLocalServices(), cepcore);
        myAssertProcessorMap(firstDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(firstDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(firstDob.getRemoteServiceMap(), cepcore);

        // 第二个processor初始化数据
        String secIds = "d,e,f";
        String secEventProssorId = "remote-2";
        EventProcessor secProcessor = getEventProcessor(secEventProssorId,
                EventProcessor.TYPE_REMOTE, secIds);
        // 第二个processor注册
        cepcore.registerEventProcessor(secProcessor);
        // 创建第二个processor数据校验对象
        DataObject secDob = new DataObject();
        secDob.createData(secProcessor, secEventProssorId, secIds,
                EventProcessor.TYPE_REMOTE);

        // 第二个注册之后数据有增加，此时数据为两个processor的合集
        DataObject threeDob = new DataObject();
        threeDob.createData(secProcessor, secEventProssorId, secIds,
                EventProcessor.TYPE_REMOTE);
        threeDob.createData(firstProcessor, firstEventProssorId, firstIds,
                EventProcessor.TYPE_REMOTE);
        // 第二个processor注册完毕之后结果断言
        myAssertEventProcessorServices(threeDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(threeDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(threeDob.getLocalServices(), cepcore);
        myAssertProcessorMap(threeDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(threeDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(threeDob.getRemoteServiceMap(), cepcore);

        // 第一个processor注销
        cepcore.unregisterEventProcessor(firstProcessor);
        // 第一个processor注销之后，剩余数据应该是第二个processor的数据
        myAssertEventProcessorServices(secDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(secDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(secDob.getLocalServices(), cepcore);
        myAssertProcessorMap(secDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(secDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(secDob.getRemoteServiceMap(), cepcore);

        // 第二个processor注销
        cepcore.unregisterEventProcessor(secProcessor);
        // 第二个processor注销之后，数据结果应该没空
        myAssertEventProcessorServices(
                new HashMap<String, List<ServiceInfo>>(), cepcore);
        myAssertLocalServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
        myAssertLocalServices(new ArrayList<ServiceInfo>(), cepcore);
        myAssertProcessorMap(new HashMap<String, EventProcessor>(), cepcore);
        myAssertServiceIdMap(new HashMap<String, List<EventProcessor>>(),
                cepcore);
        myAssertRemoteServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
    }

    /**
     * @description 两个processor, 二者之间有重复服务
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testCrossRRUU() {
        CEPCore cepcore = new CEPCoreImpl();

        // 第一个processor初始化数据
        String firstIds = "a,b,c";
        String firstEventProssorId = "remote-1";
        EventProcessor firstProcessor = getEventProcessor(firstEventProssorId,
                EventProcessor.TYPE_REMOTE, firstIds);
        // 第一个processor注册
        cepcore.registerEventProcessor(firstProcessor);
        // 创建第一个processor数据校验对象
        DataObject firstDob = new DataObject();
        firstDob.createData(firstProcessor, firstEventProssorId, firstIds,
                EventProcessor.TYPE_REMOTE);
        // 第一个processor注册结果断言
        myAssertEventProcessorServices(firstDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(firstDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(firstDob.getLocalServices(), cepcore);
        myAssertProcessorMap(firstDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(firstDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(firstDob.getRemoteServiceMap(), cepcore);

        // 第二个processor初始化数据
        String secIds = "a,d,e";
        String secEventProssorId = "remote-2";
        EventProcessor secProcessor = getEventProcessor(secEventProssorId,
                EventProcessor.TYPE_REMOTE, secIds);
        // 第二个processor注册
        cepcore.registerEventProcessor(secProcessor);
        // 创建第二个processor数据校验对象
        DataObject secDob = new DataObject();
        secDob.createData(secProcessor, secEventProssorId, secIds,
                EventProcessor.TYPE_REMOTE);

        // 注册之后信息数据增加，此时数据为前面两个processor合集
        DataObject threeDob = new DataObject();
        threeDob.createData(secProcessor, secEventProssorId, secIds,
                EventProcessor.TYPE_REMOTE);
        threeDob.createData(firstProcessor, firstEventProssorId, firstIds,
                EventProcessor.TYPE_REMOTE);
        // 第二个processor注册完毕之后结果断言
        myAssertEventProcessorServices(threeDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(threeDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(threeDob.getLocalServices(), cepcore);
        myAssertProcessorMap(threeDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(threeDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(threeDob.getRemoteServiceMap(), cepcore);

        // 第一个processor注销
        cepcore.unregisterEventProcessor(firstProcessor);
        // 第一个processor注销之后剩余数据应该为第二个processor的注册数据
        myAssertEventProcessorServices(secDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(secDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(secDob.getLocalServices(), cepcore);
        myAssertProcessorMap(secDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(secDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(secDob.getRemoteServiceMap(), cepcore);

        // 第二个processor注销
        cepcore.unregisterEventProcessor(secProcessor);
        // 全部注销数据应该为空
        myAssertEventProcessorServices(
                new HashMap<String, List<ServiceInfo>>(), cepcore);
        myAssertLocalServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
        myAssertLocalServices(new ArrayList<ServiceInfo>(), cepcore);
        myAssertProcessorMap(new HashMap<String, EventProcessor>(), cepcore);
        myAssertServiceIdMap(new HashMap<String, List<EventProcessor>>(),
                cepcore);
        myAssertRemoteServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
    }

    /**
     * @description 两个processor, 二者之间有重复服务，各自先注册再注销
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testCrossRURU() {
        CEPCore cepcore = new CEPCoreImpl();

        // 第一个processor初始化数据
        String firstIds = "a,b,c";
        String firstEventProssorId = "remote-1";
        EventProcessor firstProcessor = getEventProcessor(firstEventProssorId,
                EventProcessor.TYPE_REMOTE, firstIds);
        // 第一个processor注册
        cepcore.registerEventProcessor(firstProcessor);
        // 创建第一个processor数据校验对象
        DataObject firstDob = new DataObject();
        firstDob.createData(firstProcessor, firstEventProssorId, firstIds,
                EventProcessor.TYPE_REMOTE);
        // 第一个processor注册结果断言
        myAssertEventProcessorServices(firstDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(firstDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(firstDob.getLocalServices(), cepcore);
        myAssertProcessorMap(firstDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(firstDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(firstDob.getRemoteServiceMap(), cepcore);
        // 第一个processor注销
        cepcore.unregisterEventProcessor(firstProcessor);
        // 第一个processor注销结果断言
        myAssertEventProcessorServices(
                new HashMap<String, List<ServiceInfo>>(), cepcore);
        myAssertLocalServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
        myAssertLocalServices(new ArrayList<ServiceInfo>(), cepcore);
        myAssertProcessorMap(new HashMap<String, EventProcessor>(), cepcore);
        myAssertServiceIdMap(new HashMap<String, List<EventProcessor>>(),
                cepcore);
        myAssertRemoteServiceMap(new HashMap<String, ServiceInfo>(), cepcore);

        // 第二个processor初始化数据
        String secIds = "a,d,e";
        String secEventProssorId = "remote-2";
        EventProcessor secProcessor = getEventProcessor(secEventProssorId,
                EventProcessor.TYPE_REMOTE, secIds);
        // 第二个processor注册
        cepcore.registerEventProcessor(secProcessor);
        // 创建第二个processor数据校验对象
        DataObject secDob = new DataObject();
        secDob.createData(secProcessor, secEventProssorId, secIds,
                EventProcessor.TYPE_REMOTE);
        // 第二个processor注册结果断言
        myAssertEventProcessorServices(secDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(secDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(secDob.getLocalServices(), cepcore);
        myAssertProcessorMap(secDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(secDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(secDob.getRemoteServiceMap(), cepcore);
        // 第二个processor注销
        cepcore.unregisterEventProcessor(secProcessor);
        // 第二个processor注销结果断言
        myAssertEventProcessorServices(
                new HashMap<String, List<ServiceInfo>>(), cepcore);
        myAssertLocalServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
        myAssertLocalServices(new ArrayList<ServiceInfo>(), cepcore);
        myAssertProcessorMap(new HashMap<String, EventProcessor>(), cepcore);
        myAssertServiceIdMap(new HashMap<String, List<EventProcessor>>(),
                cepcore);
        myAssertRemoteServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
    }
}
