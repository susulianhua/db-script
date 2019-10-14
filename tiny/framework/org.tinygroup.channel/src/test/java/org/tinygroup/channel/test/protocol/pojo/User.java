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
package org.tinygroup.channel.test.protocol.pojo;

import org.tinygroup.commons.tools.StringUtil;

public class User {
    String name;
    int age;
    int grade;
    int weight;
    String huhhu;

    public User(String name, int age, int grade) {
        super();
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    public static User parse(String userString) {
        if (StringUtil.isBlank(userString)) {
            return getNullUser();
        }
        String[] values = userString.split(":");
        if (values.length != 5) {
            return getNullUser();
        }
        User u = new User(values[0], parseInt(values[1]), parseInt(values[2]));
        u.setWeight(parseInt(values[3]));
        u.setHuhhu(values[4]);
        return u;

    }

    private static User getNullUser() {
        return new User(null, 0, 0);
    }

    private static int parseInt(String value) {
        int num = 0;
        try {
            num = Integer.parseInt(value);
        } catch (Exception e) {
        }
        return num;
    }

    public String getHuhhu() {
        return huhhu;
    }

    public void setHuhhu(String huhhu) {
        this.huhhu = huhhu;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(":").append(getAge());
        sb.append(":").append(getGrade());
        sb.append(":").append(getWeight());
        sb.append(":").append(getHuhhu());
        return sb.toString();
    }
}
