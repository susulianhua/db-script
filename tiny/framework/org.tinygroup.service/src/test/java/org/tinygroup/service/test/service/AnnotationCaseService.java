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
package org.tinygroup.service.test.service;

import org.tinygroup.service.annotation.ServiceComponent;
import org.tinygroup.service.annotation.ServiceMethod;
import org.tinygroup.service.annotation.ServiceParameter;
import org.tinygroup.service.annotation.ServiceResult;
import org.tinygroup.service.test.base.ServiceUser;

import java.util.List;

@ServiceComponent()
public class AnnotationCaseService {
    @ServiceMethod(serviceId = "annotationUserObject")
    @ServiceResult(name = "result")
    public ServiceUser userObject(ServiceUser user) {
        System.out.println(user.getName() + ":s" + user.getAge());
        return user;
    }

    @ServiceMethod(serviceId = "annotationUserList")
    @ServiceResult(name = "result")
    public List<ServiceUser> userList(ServiceUser user, List<ServiceUser> users) {
        users.add(user);
        return users;
    }

    @ServiceMethod(serviceId = "annotationUserArray")
    @ServiceResult(name = "result")
    public ServiceUser[] userArray(ServiceUser[] users) {
        System.out.println(users.length);
        return users;
    }
    @ServiceMethod(serviceId = "timeoutService",requestTimeout = 18000)
    @ServiceResult(name = "result")
    public String timeoutService(
            @ServiceParameter(name = "name") String name) {

        return name;
    }
}
