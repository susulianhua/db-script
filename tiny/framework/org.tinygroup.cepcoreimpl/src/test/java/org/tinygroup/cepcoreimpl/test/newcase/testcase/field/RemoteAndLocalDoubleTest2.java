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

public class RemoteAndLocalDoubleTest2 extends FieldBaseTest {
    /**
     * @description 一个本地，一个远程，二者服务一样
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testSameRRUU() {
        CEPCore cepcore = new CEPCoreImpl();
        // 远程初始化数据
        String remoteIds = "a,b,c";
        String remoteProssorId = "remote";
        EventProcessor remoteProcessor = getEventProcessor(remoteProssorId,
                EventProcessor.TYPE_REMOTE, remoteIds);
        // 远程注册
        cepcore.registerEventProcessor(remoteProcessor);
        // 创建远程注册结果数据校验对象
        DataObject remoteDob = new DataObject();
        remoteDob.createData(remoteProcessor, remoteProssorId, remoteIds,
                EventProcessor.TYPE_REMOTE);
        // 远程注册结果断言
        myAssertEventProcessorServices(remoteDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(remoteDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(remoteDob.getLocalServices(), cepcore);
        myAssertProcessorMap(remoteDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(remoteDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(remoteDob.getRemoteServiceMap(), cepcore);

        // 本地初始化数据
        String localIds = "a,b,c";
        String localProssorId = "local";
        EventProcessor localProcessor = getEventProcessor(localProssorId,
                EventProcessor.TYPE_LOCAL, localIds);
        // 本地注册
        cepcore.registerEventProcessor(localProcessor);
        // 注册之后信息数据增加，此时数据为前面注册结果的合集
        DataObject dob = new DataObject();
        dob.createData(remoteProcessor, remoteProssorId, remoteIds,
                EventProcessor.TYPE_REMOTE);
        dob.createData(localProcessor, localProssorId, localIds,
                EventProcessor.TYPE_LOCAL);
        // 都注册完毕之后断言
        myAssertEventProcessorServices(dob.getEventProcessorServices(), cepcore);
        myAssertLocalServiceMap(dob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(dob.getLocalServices(), cepcore);
        myAssertProcessorMap(dob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(dob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(dob.getRemoteServiceMap(), cepcore);
        // 本地注销
        cepcore.unregisterEventProcessor(localProcessor);
        // 本地注销之后，剩余数据应该为远程服务
        myAssertEventProcessorServices(remoteDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(remoteDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(remoteDob.getLocalServices(), cepcore);
        myAssertProcessorMap(remoteDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(remoteDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(remoteDob.getRemoteServiceMap(), cepcore);
        // 远程注销
        cepcore.unregisterEventProcessor(remoteProcessor);
        // 注销完毕之后数据应该为空
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
     * @description 一个本地，一个远程，二者服务一样，注册注销顺序改变
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testSameRRUU2() {
        CEPCore cepcore = new CEPCoreImpl();
        // 初始化本地数据
        String localIds = "a,b,c";
        String localProssorId = "local";
        EventProcessor localProcessor = getEventProcessor(localProssorId,
                EventProcessor.TYPE_LOCAL, localIds);
        // 本地注册
        cepcore.registerEventProcessor(localProcessor);
        // 创建本地注册数据校验对象
        DataObject localDob = new DataObject();
        localDob.createData(localProcessor, localProssorId, localIds,
                EventProcessor.TYPE_LOCAL);
        // 本地注册结果断言
        myAssertEventProcessorServices(localDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(localDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(localDob.getLocalServices(), cepcore);
        myAssertProcessorMap(localDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(localDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(localDob.getRemoteServiceMap(), cepcore);
        // 初始化远程数据
        String remoteIds = "a,b,c";
        String remoteProssorId = "remote";
        EventProcessor remoteProcessor = getEventProcessor(remoteProssorId,
                EventProcessor.TYPE_REMOTE, remoteIds);
        // 远程注册
        cepcore.registerEventProcessor(remoteProcessor);
        // 注册之后信息数据增加，此时数据为前面注册数据的合集
        DataObject dob = new DataObject();
        dob.createData(remoteProcessor, remoteProssorId, remoteIds,
                EventProcessor.TYPE_REMOTE);
        dob.createData(localProcessor, localProssorId, localIds,
                EventProcessor.TYPE_LOCAL);
        // 两个processor注册完毕之后结果断言
        myAssertEventProcessorServices(dob.getEventProcessorServices(), cepcore);
        myAssertLocalServiceMap(dob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(dob.getLocalServices(), cepcore);
        myAssertProcessorMap(dob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(dob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(dob.getRemoteServiceMap(), cepcore);
        // 远程注销
        cepcore.unregisterEventProcessor(remoteProcessor);
        // 远程注销之后，剩余数据应该为本地服务
        myAssertEventProcessorServices(localDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(localDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(localDob.getLocalServices(), cepcore);
        myAssertProcessorMap(localDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(localDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(localDob.getRemoteServiceMap(), cepcore);
        // 本地注销
        cepcore.unregisterEventProcessor(localProcessor);
        // 注销之后数据结果应该为空
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
     * @description 一个本地，一个远程，二者服务一样，依次先注册再注销
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testSameRURU() {
        CEPCore cepcore = new CEPCoreImpl();
        // 远程初始化数据
        String remoteIds = "a,b,c";
        String remoteProssorId = "remote";
        EventProcessor remoteProcessor = getEventProcessor(remoteProssorId,
                EventProcessor.TYPE_REMOTE, remoteIds);
        // 远程注册
        cepcore.registerEventProcessor(remoteProcessor);
        // 创建远程注册数据校验对象
        DataObject remoteDob = new DataObject();
        remoteDob.createData(remoteProcessor, remoteProssorId, remoteIds,
                EventProcessor.TYPE_REMOTE);
        // 远程注册结果断言
        myAssertEventProcessorServices(remoteDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(remoteDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(remoteDob.getLocalServices(), cepcore);
        myAssertProcessorMap(remoteDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(remoteDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(remoteDob.getRemoteServiceMap(), cepcore);
        // 远程注销
        cepcore.unregisterEventProcessor(remoteProcessor);
        // 远程注销结果断言
        myAssertEventProcessorServices(
                new HashMap<String, List<ServiceInfo>>(), cepcore);
        myAssertLocalServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
        myAssertLocalServices(new ArrayList<ServiceInfo>(), cepcore);
        myAssertProcessorMap(new HashMap<String, EventProcessor>(), cepcore);
        myAssertServiceIdMap(new HashMap<String, List<EventProcessor>>(),
                cepcore);
        myAssertRemoteServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
        // 本地初始化数据
        String localIds = "a,b,c";
        String localProssorId = "local";
        EventProcessor localProcessor = getEventProcessor(localProssorId,
                EventProcessor.TYPE_LOCAL, localIds);
        // 本地注册
        cepcore.registerEventProcessor(localProcessor);
        // 创建本地注册结果数据校验对象
        DataObject localDob = new DataObject();
        localDob.createData(localProcessor, localProssorId, localIds,
                EventProcessor.TYPE_LOCAL);
        // 本地注册结果断言
        myAssertEventProcessorServices(localDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(localDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(localDob.getLocalServices(), cepcore);
        myAssertProcessorMap(localDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(localDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(localDob.getRemoteServiceMap(), cepcore);
        // 本地注销
        cepcore.unregisterEventProcessor(localProcessor);
        // 本地 注销结果断言
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
     * @description 一个本地，一个远程，二者服务不一样
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testDifferRRUU() {
        CEPCore cepcore = new CEPCoreImpl();
        // 远程初始化数据
        String remoteIds = "a,b,c";
        String remoteProssorId = "remote";
        EventProcessor remoteProcessor = getEventProcessor(remoteProssorId,
                EventProcessor.TYPE_REMOTE, remoteIds);
        // 远程注册
        cepcore.registerEventProcessor(remoteProcessor);
        // 创建远程注册结果数据校验对象
        DataObject remoteDob = new DataObject();
        remoteDob.createData(remoteProcessor, remoteProssorId, remoteIds,
                EventProcessor.TYPE_REMOTE);
        // 远程注册结果断言
        myAssertEventProcessorServices(remoteDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(remoteDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(remoteDob.getLocalServices(), cepcore);
        myAssertProcessorMap(remoteDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(remoteDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(remoteDob.getRemoteServiceMap(), cepcore);

        // 本地初始化数据
        String localIds = "d,e,f";
        String localProssorId = "local";
        EventProcessor localProcessor = getEventProcessor(localProssorId,
                EventProcessor.TYPE_LOCAL, localIds);
        // 本地注册
        cepcore.registerEventProcessor(localProcessor);

        // 注册之后信息数据增加，此时校验数据应该为注册数据的合集
        DataObject dob = new DataObject();
        dob.createData(remoteProcessor, remoteProssorId, remoteIds,
                EventProcessor.TYPE_REMOTE);
        dob.createData(localProcessor, localProssorId, localIds,
                EventProcessor.TYPE_LOCAL);
        // 两个processor注册完毕之后结果校验
        myAssertEventProcessorServices(dob.getEventProcessorServices(), cepcore);
        myAssertLocalServiceMap(dob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(dob.getLocalServices(), cepcore);
        myAssertProcessorMap(dob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(dob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(dob.getRemoteServiceMap(), cepcore);
        // 本地注销
        cepcore.unregisterEventProcessor(localProcessor);
        // 本地注销之后剩余数据应该为远程processor的数据
        myAssertEventProcessorServices(remoteDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(remoteDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(remoteDob.getLocalServices(), cepcore);
        myAssertProcessorMap(remoteDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(remoteDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(remoteDob.getRemoteServiceMap(), cepcore);
        // 远程注销
        cepcore.unregisterEventProcessor(remoteProcessor);
        // 远程注销之后数据应该为空
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
     * @description 一个本地，一个远程，二者服务有重复
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testCrossRRUU() {
        CEPCore cepcore = new CEPCoreImpl();
        // 远程初始化数据
        String remoteIds = "a,b,c";
        String remoteProssorId = "remote";
        EventProcessor remoteProcessor = getEventProcessor(remoteProssorId,
                EventProcessor.TYPE_REMOTE, remoteIds);
        // 远程注册
        cepcore.registerEventProcessor(remoteProcessor);
        // 创建远程注册结果校验对象
        DataObject remoteDob = new DataObject();
        remoteDob.createData(remoteProcessor, remoteProssorId, remoteIds,
                EventProcessor.TYPE_REMOTE);
        // 远程注册结果断言
        myAssertEventProcessorServices(remoteDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(remoteDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(remoteDob.getLocalServices(), cepcore);
        myAssertProcessorMap(remoteDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(remoteDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(remoteDob.getRemoteServiceMap(), cepcore);
        // 本地初始化数据
        String localIds = "a,d,e";
        String localProssorId = "local";
        EventProcessor localProcessor = getEventProcessor(localProssorId,
                EventProcessor.TYPE_LOCAL, localIds);
        // 本地注册
        cepcore.registerEventProcessor(localProcessor);
        // 注册之后信息数据增加，此时数据为注册数据的合集
        DataObject dob = new DataObject();
        dob.createData(remoteProcessor, remoteProssorId, remoteIds,
                EventProcessor.TYPE_REMOTE);
        dob.createData(localProcessor, localProssorId, localIds,
                EventProcessor.TYPE_LOCAL);
        // 两个processor注册完毕之后断言
        myAssertEventProcessorServices(dob.getEventProcessorServices(), cepcore);
        myAssertLocalServiceMap(dob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(dob.getLocalServices(), cepcore);
        myAssertProcessorMap(dob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(dob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(dob.getRemoteServiceMap(), cepcore);
        // 本地注销
        cepcore.unregisterEventProcessor(localProcessor);
        // 本地 注销之后断言，剩余数据应该为远程服务数据
        myAssertEventProcessorServices(remoteDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(remoteDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(remoteDob.getLocalServices(), cepcore);
        myAssertProcessorMap(remoteDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(remoteDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(remoteDob.getRemoteServiceMap(), cepcore);
        // 远程注销
        cepcore.unregisterEventProcessor(remoteProcessor);
        // 远程注销之后，剩余数据应该为空
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
     * @description 一个本地，一个远程，二者服务有重复，依次先注册再注销
     * @author: qiucn
     * @version: 2016年8月11日
     */
    public void testCrossRURU() {
        CEPCore cepcore = new CEPCoreImpl();
        // 远程初始化数据
        String remoteIds = "a,b,c";
        String remoteProssorId = "remote";
        EventProcessor remoteProcessor = getEventProcessor(remoteProssorId,
                EventProcessor.TYPE_REMOTE, remoteIds);
        // 远程注册
        cepcore.registerEventProcessor(remoteProcessor);
        // 创建远程注册结果校验对象
        DataObject remoteDob = new DataObject();
        remoteDob.createData(remoteProcessor, remoteProssorId, remoteIds,
                EventProcessor.TYPE_REMOTE);
        // 远程注册结果断言
        myAssertEventProcessorServices(remoteDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(remoteDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(remoteDob.getLocalServices(), cepcore);
        myAssertProcessorMap(remoteDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(remoteDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(remoteDob.getRemoteServiceMap(), cepcore);
        // 远程注销
        cepcore.unregisterEventProcessor(remoteProcessor);
        // 远程注销之后，数据应该为空
        myAssertEventProcessorServices(
                new HashMap<String, List<ServiceInfo>>(), cepcore);
        myAssertLocalServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
        myAssertLocalServices(new ArrayList<ServiceInfo>(), cepcore);
        myAssertProcessorMap(new HashMap<String, EventProcessor>(), cepcore);
        myAssertServiceIdMap(new HashMap<String, List<EventProcessor>>(),
                cepcore);
        myAssertRemoteServiceMap(new HashMap<String, ServiceInfo>(), cepcore);
        // 本地初始化数据
        String localIds = "a,d,e";
        String localProssorId = "local";
        EventProcessor localProcessor = getEventProcessor(localProssorId,
                EventProcessor.TYPE_LOCAL, localIds);
        // 本地注册
        cepcore.registerEventProcessor(localProcessor);
        // 创建本地注册数据校验对象
        DataObject localDob = new DataObject();
        localDob.createData(localProcessor, localProssorId, localIds,
                EventProcessor.TYPE_LOCAL);
        // 本地注册结果断言
        myAssertEventProcessorServices(localDob.getEventProcessorServices(),
                cepcore);
        myAssertLocalServiceMap(localDob.getLocalServiceMap(), cepcore);
        myAssertLocalServices(localDob.getLocalServices(), cepcore);
        myAssertProcessorMap(localDob.getProcessorMap(), cepcore);
        myAssertServiceIdMap(localDob.getServiceIdMap(), cepcore);
        myAssertRemoteServiceMap(localDob.getRemoteServiceMap(), cepcore);
        // 本地注销
        cepcore.unregisterEventProcessor(localProcessor);
        // 本地注销结果断言
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
