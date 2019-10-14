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
package org.tinygroup.tinypc.pi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class TestRelease extends UnicastRemoteObject {
    Registry registry = null;

    protected TestRelease() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        TestRelease t = null;
        try {
            t = new TestRelease();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
        try {
            t.init();
            Thread.sleep(5000);

        } catch (Exception e) {
            e.printStackTrace();
        }
        t.stop();

    }

    public void stop() {
        try {
            System.out.println("stop");
            UnicastRemoteObject.unexportObject(this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() throws NotBoundException {

        try {
            registry = LocateRegistry.createRegistry(2222);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            registry.rebind("aaaaaaaa", this);

        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
