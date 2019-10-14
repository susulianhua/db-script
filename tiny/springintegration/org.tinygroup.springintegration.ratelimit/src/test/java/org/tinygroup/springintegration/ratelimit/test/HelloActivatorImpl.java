package org.tinygroup.springintegration.ratelimit.test;

public class HelloActivatorImpl {
    public String sayHello(String name) {
        return "hello," + name;
    }
    public String sayDiscard(String name){
        return "limited,"+name;
    }
}