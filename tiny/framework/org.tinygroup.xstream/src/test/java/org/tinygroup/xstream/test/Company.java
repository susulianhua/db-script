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
package org.tinygroup.xstream.test;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.tinygroup.xstream.CustomEnumSingleNameConverter;

import java.util.List;


public class Company {
    String dontoutput;
    String name;
    String contory;
    int num;
    double money;

    List<Department> departments;
    Role[] roles;
    @XStreamConverter(value = CustomEnumSingleNameConverter.class)
    Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getDontoutput() {
        return dontoutput;
    }

    public void setDontoutput(String dontoutput) {
        this.dontoutput = dontoutput;
    }

    public Role[] getRoles() {
        return roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContory() {
        return contory;
    }

    public void setContory(String contory) {
        this.contory = contory;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }


}
