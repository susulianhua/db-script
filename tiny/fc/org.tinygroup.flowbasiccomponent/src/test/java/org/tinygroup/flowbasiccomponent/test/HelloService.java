package org.tinygroup.flowbasiccomponent.test;

public class HelloService {

    public void hello() {
        System.out.println("say hello");
    }

    public void exceptionService() {
        throw new RuntimeException();
    }

    public void exceptionNodeService(int a) {
        switch (a) {
            case 1:
                throw new Exception1();
            case 2:
                throw new Exception2();
            case 3:
                throw new Exception3();
            default:
                throw new RuntimeException();
        }
    }
}
