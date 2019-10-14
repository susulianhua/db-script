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
package org.tinygroup.servicewrapper;


import java.util.List;

public class GeneratorService implements GeneratorServiceIn {

    public ServiceUser userObject(ServiceUser user, ServiceOrg org) {
        System.out.println(user.getName() + ":s" + user.getAge());
        System.out.println("org name=" + org.getName());
        ServiceUser newUser = new ServiceUser();
        newUser.setName("changerName");
        newUser.setAge(10);
        newUser.setMale(true);
        return newUser;
    }

    public List<ServiceUser> userList(ServiceUser user, List<ServiceUser> users) {
        users.add(user);
        return users;
    }

    public ServiceUser[] userArray(ServiceUser[] users) {
        return users;
    }

    @Override
    public ServiceUser userObject2(ServiceUser user, ServiceOrg org) {
        return userObject(user, org);
    }
}
