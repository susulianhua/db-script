package org.tinygroup.flowbasiccomponent.test;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@SuppressWarnings("serial")
@XmlRootElement
public class User implements Serializable {

    private String id;
    private String name;
    private int age;
    private String address;
    private int tel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }
}
