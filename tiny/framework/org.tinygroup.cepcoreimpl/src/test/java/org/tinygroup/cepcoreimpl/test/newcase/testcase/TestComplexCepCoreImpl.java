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
package org.tinygroup.cepcoreimpl.test.newcase.testcase;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcoreimpl.CEPCoreImpl;

/**
 * @author: qiucn
 * @version: 2016年7月29日下午3:08:23
 */
public class TestComplexCepCoreImpl extends BaseNewCase {

    /**
     * @description：本地和远程有重复的服务
     * @author: qiucn
     * @version: 2016年8月1日上午9:57:37
     */
    public void testTwoRepeat() {
        CEPCore cepcore = new CEPCoreImpl();

        registerEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "a,b", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "");
        assertEventProcessor(cepcore, "local-1");
        registerEventProcessor(cepcore, "remote-1", EventProcessor.TYPE_REMOTE,
                "a,c");
        assertServiceExist(cepcore, "a,b,c", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "a,c", "");
        assertEventProcessor(cepcore, "local-1,remote-1");

        // 注销远程服务
        unRegisterEventProcessor(cepcore, "remote-1",
                EventProcessor.TYPE_REMOTE, "a,c");
        assertServiceExist(cepcore, "a,b", "c");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "a,c");
        assertEventProcessor(cepcore, "local-1");

        // 注销本地服务
        unRegisterEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "", "a,b,c");
        assertLocalService(cepcore, "", "a,b");
        assertRemoteService(cepcore, "", "a,c");
        assertEventProcessor(cepcore, "");
    }

    /**
     * @description：本地和远程没有重复的服务
     * @author: qiucn
     * @version: 2016年8月1日上午9:57:24
     */
    public void testTwoNoRepeat() {
        CEPCore cepcore = new CEPCoreImpl();

        registerEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "a,b", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "");
        assertEventProcessor(cepcore, "local-1");
        registerEventProcessor(cepcore, "remote-1", EventProcessor.TYPE_REMOTE,
                "c,d");
        assertServiceExist(cepcore, "a,b,c,d", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "c,d", "");
        assertEventProcessor(cepcore, "local-1,remote-1");

        // 注销远程服务
        unRegisterEventProcessor(cepcore, "remote-1",
                EventProcessor.TYPE_REMOTE, "c,d");
        assertServiceExist(cepcore, "a,b", "c,d");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "c,d");
        assertEventProcessor(cepcore, "local-1");

        // 注销本地服务
        unRegisterEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "", "a,b,c,d");
        assertLocalService(cepcore, "", "a,b");
        assertRemoteService(cepcore, "", "c,d");
        assertEventProcessor(cepcore, "");
    }

    /**
     * @description：两个本地服务，且两者之间有重复服务，一个远程服务，不与本地服务重复
     * @author: qiucn
     * @version: 2016年8月1日上午9:57:24
     */
    public void testThreeAndLocalRepeat() {
        CEPCore cepcore = new CEPCoreImpl();

        registerEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "a,b", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "");
        assertEventProcessor(cepcore, "local-1");
        registerEventProcessor(cepcore, "local-2", EventProcessor.TYPE_LOCAL,
                "a,c");
        assertServiceExist(cepcore, "a,b,c", "");
        assertLocalService(cepcore, "a,b,c", "");
        assertRemoteService(cepcore, "", "");
        assertEventProcessor(cepcore, "local-1,local-2");
        registerEventProcessor(cepcore, "remote-1", EventProcessor.TYPE_REMOTE,
                "d,e");
        assertServiceExist(cepcore, "a,b,c,d,e", "");
        assertLocalService(cepcore, "a,b,c", "");
        assertRemoteService(cepcore, "d,e", "");
        assertEventProcessor(cepcore, "local-1,local-2,remote-1");

        // 注销远程服务
        unRegisterEventProcessor(cepcore, "remote-1",
                EventProcessor.TYPE_REMOTE, "d,e");
        assertServiceExist(cepcore, "a,b,c", "d,e");
        assertLocalService(cepcore, "a,b,c", "");
        assertRemoteService(cepcore, "", "d,e");
        assertEventProcessor(cepcore, "local-1,local-2");

        // 注销本地服务local-2,因为服务a是重复服务，所以a服务依旧存在，注销掉的服务实际只有c
        unRegisterEventProcessor(cepcore, "local-2", EventProcessor.TYPE_LOCAL,
                "a,c");
        assertServiceExist(cepcore, "a,b", "c,d,e");
        assertLocalService(cepcore, "a,b", "c");
        assertRemoteService(cepcore, "", "d,e");
        assertEventProcessor(cepcore, "local-1");

        // 注销本地服务local-1
        unRegisterEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "", "a,b,c,d,e");
        assertLocalService(cepcore, "", "a,b,c");
        assertRemoteService(cepcore, "", "d,e");
        assertEventProcessor(cepcore, "");
    }

    /**
     * @description：两个本地服务，且两者之间有重复服务，一个远程服务，且与本地服务重复
     * @author: qiucn
     * @version: 2016年8月1日上午9:57:24
     */
    public void testThreeAndRemoteRepeat() {
        CEPCore cepcore = new CEPCoreImpl();

        registerEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "a,b", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "");
        assertEventProcessor(cepcore, "local-1");
        registerEventProcessor(cepcore, "local-2", EventProcessor.TYPE_LOCAL,
                "a,c");
        assertServiceExist(cepcore, "a,b,c", "");
        assertLocalService(cepcore, "a,b,c", "");
        assertRemoteService(cepcore, "", "");
        assertEventProcessor(cepcore, "local-1,local-2");
        registerEventProcessor(cepcore, "remote-1", EventProcessor.TYPE_REMOTE,
                "a,d");
        assertServiceExist(cepcore, "a,b,c,d", "");
        assertLocalService(cepcore, "a,b,c", "");
        assertRemoteService(cepcore, "a,d", "");
        assertEventProcessor(cepcore, "local-1,local-2,remote-1");

        // 注销远程服务,因为服务a是重复服务，所以a服务依旧存在，注销掉的服务实际只有d;但是在远程服务中a,d均不存在
        unRegisterEventProcessor(cepcore, "remote-1",
                EventProcessor.TYPE_REMOTE, "a,d");
        assertServiceExist(cepcore, "a,b,c", "d");
        assertLocalService(cepcore, "a,b,c", "");
        assertRemoteService(cepcore, "", "a,d");
        assertEventProcessor(cepcore, "local-1,local-2");

        // 注销本地服务local-2,因为服务a是重复服务，所以a服务依旧存在，注销掉的服务实际只有c
        unRegisterEventProcessor(cepcore, "local-2", EventProcessor.TYPE_LOCAL,
                "a,c");
        assertServiceExist(cepcore, "a,b", "d,c");
        assertLocalService(cepcore, "a,b", "c");
        assertRemoteService(cepcore, "", "a,d");
        assertEventProcessor(cepcore, "local-1");

        // 注销本地服务local-1
        unRegisterEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "", "d,c,a,b");
        assertLocalService(cepcore, "", "a,b,c");
        assertRemoteService(cepcore, "", "a,d");
        assertEventProcessor(cepcore, "");
    }

    /**
     * @description：一个本地服务；两个远程服务，且与本地服务重复
     * @author: qiucn
     * @version: 2016年8月1日上午9:57:24
     */
    public void testThreeAndRepeat() {
        CEPCore cepcore = new CEPCoreImpl();

        registerEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "a,b", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "");
        assertEventProcessor(cepcore, "local-1");
        registerEventProcessor(cepcore, "remote-1", EventProcessor.TYPE_REMOTE,
                "a,c");
        assertServiceExist(cepcore, "a,b,c", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "a,c", "");
        assertEventProcessor(cepcore, "local-1,remote-1");
        registerEventProcessor(cepcore, "remote-2", EventProcessor.TYPE_REMOTE,
                "a,d");
        assertServiceExist(cepcore, "a,b,c,d", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "a,c,d", "");
        assertEventProcessor(cepcore, "local-1,remote-1,remote-2");

        // 注销远程服务remote-2,因为服务a是重复服务，所以a服务依旧存在，注销掉的服务实际只有d
        unRegisterEventProcessor(cepcore, "remote-2",
                EventProcessor.TYPE_REMOTE, "a,d");
        assertServiceExist(cepcore, "a,b,c", "d");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "a,c", "d");
        assertEventProcessor(cepcore, "local-1,remote-1");

        // 注销本地服务remote-1,因为服务a是重复服务，所以a服务依旧存在，注销掉的服务实际只有c
        unRegisterEventProcessor(cepcore, "remote-1",
                EventProcessor.TYPE_REMOTE, "a,c");
        assertServiceExist(cepcore, "a,b", "d,c");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "a,d,c");
        assertEventProcessor(cepcore, "local-1");

        // 注销本地服务
        unRegisterEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "", "a,b,d,c");
        assertLocalService(cepcore, "", "a,b");
        assertRemoteService(cepcore, "", "a,d,c");
        assertEventProcessor(cepcore, "");
    }

    /**
     * @description：两个远程服务，且两者之间有重复服务，不与本地服务重复
     * @author: qiucn
     * @version: 2016年8月1日上午9:57:24
     */
    public void testThreeRemoteRepeat() {
        CEPCore cepcore = new CEPCoreImpl();

        registerEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "a,b", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "");
        assertEventProcessor(cepcore, "local-1");
        registerEventProcessor(cepcore, "remote-1", EventProcessor.TYPE_REMOTE,
                "c,d");
        assertServiceExist(cepcore, "a,b,c,d", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "c,d", "");
        assertEventProcessor(cepcore, "local-1,remote-1");
        registerEventProcessor(cepcore, "remote-2", EventProcessor.TYPE_REMOTE,
                "c,e");
        assertServiceExist(cepcore, "a,b,c,d,e", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "c,d,e", "");
        assertEventProcessor(cepcore, "local-1,remote-1,remote-2");

        // 注销远程服务remote-2,因为服务c是重复服务，所以c服务依旧存在，注销掉的服务实际只有e
        unRegisterEventProcessor(cepcore, "remote-2",
                EventProcessor.TYPE_REMOTE, "c,e");
        assertServiceExist(cepcore, "a,b,c,d", "e");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "c,d", "e");
        assertEventProcessor(cepcore, "local-1,remote-1");

        // 注销远程服务remote-1
        unRegisterEventProcessor(cepcore, "remote-1",
                EventProcessor.TYPE_LOCAL, "c,d");
        assertServiceExist(cepcore, "a,b", "c,d,e");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "c,d,e");
        assertEventProcessor(cepcore, "local-1");
        // 注销本地服务local-1
        unRegisterEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "", "a,b,c,d,e");
        assertLocalService(cepcore, "", "a,b");
        assertRemoteService(cepcore, "", "c,d,e");
        assertEventProcessor(cepcore, "");
    }

    /**
     * @description：两个远程服务，且两者之间有重复服务，且与本地服务重复
     * @author: qiucn
     * @version: 2016年8月1日上午9:57:24
     */
    public void testThreeLComplexRepeat() {
        CEPCore cepcore = new CEPCoreImpl();

        registerEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "a,b", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "");
        assertEventProcessor(cepcore, "local-1");
        registerEventProcessor(cepcore, "remote-1", EventProcessor.TYPE_REMOTE,
                "a,c");
        assertServiceExist(cepcore, "a,b,c", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "a,c", "");
        assertEventProcessor(cepcore, "local-1,remote-1");
        registerEventProcessor(cepcore, "remote-2", EventProcessor.TYPE_REMOTE,
                "c,d");
        assertServiceExist(cepcore, "a,b,c,d", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "a,c,d", "");
        assertEventProcessor(cepcore, "local-1,remote-1,remote-2");

        // 注销远程服务remote-2,因为服务c是重复服务，所以c服务依旧存在，注销掉的服务实际只有e
        unRegisterEventProcessor(cepcore, "remote-2",
                EventProcessor.TYPE_REMOTE, "c,d");
        assertServiceExist(cepcore, "a,b,c", "d");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "a,c", "d");
        assertEventProcessor(cepcore, "local-1,remote-1");

        // 注销远程服务remote-1
        unRegisterEventProcessor(cepcore, "remote-1",
                EventProcessor.TYPE_LOCAL, "a,c");
        assertServiceExist(cepcore, "a,b", "c,d");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "a,c,d");
        assertEventProcessor(cepcore, "local-1");

        // 注销本地服务local-1
        unRegisterEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "", "a,b,c,d");
        assertLocalService(cepcore, "", "a,b");
        assertRemoteService(cepcore, "", "a,c,d");
        assertEventProcessor(cepcore, "");
    }

    /**
     * @description：两个远程服务，两个本地服务，互相之间均有重复
     * @author: qiucn
     * @version: 2016年8月1日上午9:57:24
     */
    public void testFourComplexRepeat() {
        CEPCore cepcore = new CEPCoreImpl();

        registerEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "a,b", "");
        assertLocalService(cepcore, "a,b", "");
        assertRemoteService(cepcore, "", "");
        assertEventProcessor(cepcore, "local-1");

        registerEventProcessor(cepcore, "local-2", EventProcessor.TYPE_LOCAL,
                "a,c");
        assertServiceExist(cepcore, "a,b,c", "");
        assertLocalService(cepcore, "a,b,c", "");
        assertRemoteService(cepcore, "", "");
        assertEventProcessor(cepcore, "local-1,local-2");

        registerEventProcessor(cepcore, "remote-1", EventProcessor.TYPE_REMOTE,
                "a,d");
        assertServiceExist(cepcore, "a,b,c,d", "");
        assertLocalService(cepcore, "a,b,c", "");
        assertRemoteService(cepcore, "a,d", "");
        assertEventProcessor(cepcore, "local-1,local-2,remote-1");

        registerEventProcessor(cepcore, "remote-2", EventProcessor.TYPE_REMOTE,
                "a,e");
        assertServiceExist(cepcore, "a,b,c,d,e", "");
        assertLocalService(cepcore, "a,b,c", "");
        assertRemoteService(cepcore, "a,d,e", "");
        assertEventProcessor(cepcore, "local-1,local-2,remote-1,remote-2");

        // 注销远程服务remote-2,因为服务c是重复服务，所以c服务依旧存在，注销掉的服务实际只有e
        unRegisterEventProcessor(cepcore, "remote-2",
                EventProcessor.TYPE_REMOTE, "a,e");
        assertServiceExist(cepcore, "a,b,c,d", "e");
        assertLocalService(cepcore, "a,b,c", "");
        assertRemoteService(cepcore, "a,d", "e");
        assertEventProcessor(cepcore, "local-1,local-2,remote-1");

        // 注销远程服务remote-1
        unRegisterEventProcessor(cepcore, "remote-1",
                EventProcessor.TYPE_REMOTE, "a,d");
        assertServiceExist(cepcore, "a,b,c", "d,e");
        assertLocalService(cepcore, "a,b,c", "");
        assertRemoteService(cepcore, "", "a,d,e");
        assertEventProcessor(cepcore, "local-1,local-2");


        // 注销本地服务local-2
        unRegisterEventProcessor(cepcore, "local-2", EventProcessor.TYPE_LOCAL,
                "a,c");
        assertServiceExist(cepcore, "a,b", "c,d,e");
        assertLocalService(cepcore, "a,b", "c");
        assertRemoteService(cepcore, "", "a,d,e");
        assertEventProcessor(cepcore, "local-1");


        // 注销本地服务local-1
        unRegisterEventProcessor(cepcore, "local-1", EventProcessor.TYPE_LOCAL,
                "a,b");
        assertServiceExist(cepcore, "", "a,b,c,d,e");
        assertLocalService(cepcore, "", "a,b,c");
        assertRemoteService(cepcore, "", "a,d,e");
        assertEventProcessor(cepcore, "");
    }
}
