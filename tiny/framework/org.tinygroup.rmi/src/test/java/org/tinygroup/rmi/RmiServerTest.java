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
package org.tinygroup.rmi;

import junit.framework.TestCase;
import org.tinygroup.rmi.impl.RmiServerImpl;

/**
 * Created by luoguo on 14-1-24.
 */
public class RmiServerTest extends TestCase {
    static String SIP = "127.0.0.1";
    static String CIP = "127.0.0.1";
    static int SP = 18888;
    static int CP = 17777;
    RmiServer localServer;
    RmiServer remoteServer;

    public void setUp() throws Exception {
        super.setUp();
        localServer = new RmiServerImpl(SIP, SP);
        remoteServer = new RmiServerImpl(CIP, CP, SIP, SP);

    }

    public void tearDown() throws Exception {
        super.tearDown();
        localServer.unexportObjects();
        remoteServer.unexportObjects();
    }

    public void testGetRegistry() throws Exception {
        localServer.registerLocalObject(new HelloImpl(), "hello");
        assertEquals(localServer.getRegistry().list().length, 2);
        assertEquals(remoteServer.getRegistry().list().length, 1);
        Hello hello = remoteServer.getObject("hello");
        String info = hello.sayHello("abc");
        assertEquals(info, "Hello,abc");

        remoteServer.registerLocalObject(new HelloImpl(), "hello1");
        assertEquals(localServer.getRegistry().list().length, 2);
        assertEquals(remoteServer.getRegistry().list().length, 2);
        hello = localServer.getObject("hello1");
        info = hello.sayHello("def");
        assertEquals(info, "Hello,def");
    }


}
