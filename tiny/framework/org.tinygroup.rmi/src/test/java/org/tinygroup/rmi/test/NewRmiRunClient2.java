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

public class NewRmiRunClient2 {
    private static String SERVERIP = "127.0.0.1";
    private static String LOCALIP = "127.0.0.1";
    private RmiServer remoteServer;

    public NewRmiRunClient2(RmiServer remoteServer) {
        this.remoteServer = remoteServer;
    }

    public static void main(String[] args) {
        RmiServer remoteServer = null;

        try {
            remoteServer = new RmiServerImpl(LOCALIP, 9999, SERVERIP, 8888);

        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            remoteServer.registerLocalObject(new MyHelloImpl(), "hello2");

        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        NewRmiRunClient2 c = new NewRmiRunClient2(remoteServer);
        c.run();

    }

    public void run() {
        MyThread t = new MyThread();
        t.run();
    }

    class MyThread extends Thread {
        boolean end = false;

        public void run() {
            while (!end) {
                hello();
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        public void hello() {
            MyHello hello = null;
            try {
                hello = remoteServer.getObject("hello1");
            } catch (Exception e) {
                e.printStackTrace();
                // throw new RuntimeException("获取对象失败"+e.getMessage());
            }

            try {
                String info = hello.sayHello("abc1");
//				System.out.println(info);
                if (!"Hello,abc1".equals(info)) {
                    throw new RuntimeException("执行结果的字符串不匹配");
                }
            } catch (Exception e) {
                e.printStackTrace();
                // throw new RuntimeException("执行方法失败:"+e.getMessage());
            }
        }
    }
}
