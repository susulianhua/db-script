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
package org.tinygroup.rmi.test;

import org.tinygroup.rmi.RmiServer;
import org.tinygroup.rmi.impl.RmiServerImpl;

import java.rmi.RemoteException;

public class NewRmiRunServer {

    private static String LOCALIP = "127.0.0.1";

    public static void main(String[] args) {
        RmiServer localServer = null;
        try {
            localServer = new RmiServerImpl(LOCALIP, 8888);
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
//		RmiUtil.start((Runnable)localServer);
        try {
            localServer.registerLocalObject(new MyHelloImpl(), "hello");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        NewRmiRunServer r = new NewRmiRunServer();
        r.runThread(localServer);

    }

    public void runThread(RmiServer localServer) {
        MyThread t = new NewRmiRunServer.MyThread(localServer);
        t.start();
    }

    class MyThread extends Thread {
        RmiServer localServer;
        private boolean end = false;

        public MyThread(RmiServer localServer) {
            this.localServer = localServer;
        }

        public void run() {
            while (!end) {
                try {
                    sleep(1000);
                    System.out.println(localServer.getObject("hello1").toString());
                    if (localServer.getObject("hello1") != null) {
                        MyHello hello = localServer.getObject("hello1");
                        String info = hello.sayHello("abc111111");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
