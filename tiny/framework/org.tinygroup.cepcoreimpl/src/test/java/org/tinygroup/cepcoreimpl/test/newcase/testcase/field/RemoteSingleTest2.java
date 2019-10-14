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

public class RemoteSingleTest2 extends FieldBaseTest {

    /**
     * @description 先注册再注销processor，前后注册注销的服务是一样的
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testRegAndUnSame() {
        CEPCore cepcore = new CEPCoreImpl();
        // 初始化数据
        String ids = "a,b,c";
        String eventProssorId = "remote";
        EventProcessor processor = getEventProcessor(eventProssorId,
                EventProcessor.TYPE_REMOTE, ids);
        // 注册
        cepcore.registerEventProcessor(processor);
        //创建校验数据对象
        DataObject dob = new DataObject();
        dob.createData(processor, eventProssorId, ids,
                EventProcessor.TYPE_REMOTE);
        //注册结果断言
        myAssertEventProcessorServices(dob.getEventProcessorServices(), cepcore);
        myAssertLocalServiceMap(dob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(dob.getLocalServices(), cepcore);
        myAssertProcessorMap(dob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(dob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(dob.getRemoteServiceMap(), cepcore);

        // 注销
        cepcore.unregisterEventProcessor(processor);
        //注销结果断言
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
     * @description 先注册再注销processor，注销时服务变多
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testRegAndUnMore() {
        CEPCore cepcore = new CEPCoreImpl();

        // 初始化数据
        String regIds = "a,b,c";
        String eventProssorId = "remote";
        EventProcessor regProcessor = getEventProcessor(eventProssorId,
                EventProcessor.TYPE_REMOTE, regIds);
        //注册
        cepcore.registerEventProcessor(regProcessor);
        //创建数据校验对象
        DataObject regDob = new DataObject();
        regDob.createData(regProcessor, eventProssorId, regIds,
                EventProcessor.TYPE_REMOTE);
        //注册结果断言
        myAssertEventProcessorServices(regDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(regDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(regDob.getLocalServices(), cepcore);
        myAssertProcessorMap(regDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(regDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(regDob.getRemoteServiceMap(), cepcore);

        // 初始化注销数据，注销时处理器中的服务数量比注册时多
        String unRegIds = "a,b,c,d";
        EventProcessor unRegProcessor = getEventProcessor(eventProssorId,
                EventProcessor.TYPE_REMOTE, unRegIds);
        //注销
        cepcore.unregisterEventProcessor(unRegProcessor);
        //注销结果断言
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
     * @description 先注册再注销processor，注销时服务变少
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testRegAndUnLess() {
        CEPCore cepcore = new CEPCoreImpl();

        // 初始化注册数据
        String regIds = "a,b,c";
        String eventProssorId = "remote";
        EventProcessor regProcessor = getEventProcessor(eventProssorId,
                EventProcessor.TYPE_REMOTE, regIds);
        //注册
        cepcore.registerEventProcessor(regProcessor);
        //创建数据校验对象
        DataObject regDob = new DataObject();
        regDob.createData(regProcessor, eventProssorId, regIds,
                EventProcessor.TYPE_REMOTE);
        //注册断言
        myAssertEventProcessorServices(regDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(regDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(regDob.getLocalServices(), cepcore);
        myAssertProcessorMap(regDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(regDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(regDob.getRemoteServiceMap(), cepcore);

        // 初始化注销数据，注销时处理器中的服务数量比注册时少
        String unRegIds = "a,b";
        EventProcessor unRegProcessor = getEventProcessor(eventProssorId,
                EventProcessor.TYPE_REMOTE, unRegIds);
        cepcore.unregisterEventProcessor(unRegProcessor);
        //注销断言
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
     * @description 重复注册，第二次注册时服务变多
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testDoubleRegMore() {
        CEPCore cepcore = new CEPCoreImpl();

        // 初始化注册数据
        String regIds = "a,b,c";
        String eventProssorId = "remote";
        EventProcessor regProcessor = getEventProcessor(eventProssorId,
                EventProcessor.TYPE_REMOTE, regIds);
        //注册
        cepcore.registerEventProcessor(regProcessor);
        //创建数据校验对象
        DataObject regDob = new DataObject();
        regDob.createData(regProcessor, eventProssorId, regIds,
                EventProcessor.TYPE_REMOTE);
        //注册结果断言
        myAssertEventProcessorServices(regDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(regDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(regDob.getLocalServices(), cepcore);
        myAssertProcessorMap(regDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(regDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(regDob.getRemoteServiceMap(), cepcore);

        // 再次注册，注册的处理器中服务变多
        String doubleRegIds = "a,b,c,d";
        EventProcessor doubleRegProcessor = getEventProcessor(eventProssorId,
                EventProcessor.TYPE_REMOTE, doubleRegIds);
        //注册
        cepcore.registerEventProcessor(doubleRegProcessor);
        //创建数据校验对象
        DataObject doubleRegDob = new DataObject();
        doubleRegDob.createData(doubleRegProcessor, eventProssorId,
                doubleRegIds, EventProcessor.TYPE_REMOTE);
        //注册结果断言
        myAssertEventProcessorServices(
                doubleRegDob.getEventProcessorServices(), cepcore);
        myAssertLocalServiceMap(doubleRegDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(doubleRegDob.getLocalServices(), cepcore);
        myAssertProcessorMap(doubleRegDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(doubleRegDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(doubleRegDob.getRemoteServiceMap(), cepcore);
    }

    /**
     * @description 重复注册，第二次注册时服务变少
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testDoubleRegLess() {
        CEPCore cepcore = new CEPCoreImpl();

        // 初始化注册数据
        String regIds = "a,b,c";
        String eventProssorId = "remote";
        EventProcessor regProcessor = getEventProcessor(eventProssorId,
                EventProcessor.TYPE_REMOTE, regIds);
        //注册
        cepcore.registerEventProcessor(regProcessor);
        //创建数据校验对象
        DataObject regDob = new DataObject();
        regDob.createData(regProcessor, eventProssorId, regIds,
                EventProcessor.TYPE_REMOTE);
        //注册结果断言
        myAssertEventProcessorServices(regDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(regDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(regDob.getLocalServices(), cepcore);
        myAssertProcessorMap(regDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(regDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(regDob.getRemoteServiceMap(), cepcore);

        // 再次注册，注册的处理器中服务变少
        String doubleRegIds = "a,b";
        EventProcessor doubleRegProcessor = getEventProcessor(eventProssorId,
                EventProcessor.TYPE_REMOTE, doubleRegIds);
        //注册
        cepcore.registerEventProcessor(doubleRegProcessor);
        //创建数据处理对象
        DataObject doubleRegDob = new DataObject();
        doubleRegDob.createData(doubleRegProcessor, eventProssorId,
                doubleRegIds, EventProcessor.TYPE_REMOTE);
        //再次注册结果断言
        myAssertEventProcessorServices(
                doubleRegDob.getEventProcessorServices(), cepcore);
        myAssertLocalServiceMap(doubleRegDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(doubleRegDob.getLocalServices(), cepcore);
        myAssertProcessorMap(doubleRegDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(doubleRegDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(doubleRegDob.getRemoteServiceMap(), cepcore);
    }

}
