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

import org.springframework.validation.annotation.Validated;
import org.tinygroup.servicewrapper.annotation.ServiceWrapper;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface GeneratorServiceIn {
    @ServiceWrapper(serviceId = "serviceUserObject")
    ServiceUser userObject(ServiceUser user, ServiceOrg org);

    @ServiceWrapper(serviceId = "serviceUserObject2")
    ServiceUser userObject2(@Valid ServiceUser user, ServiceOrg org);

    @ServiceWrapper(serviceId = "serviceUserList")
    List<ServiceUser> userList(ServiceUser user, List<ServiceUser> users);

    @ServiceWrapper(serviceId = "serviceUserArray")
    ServiceUser[] userArray(ServiceUser[] users);
}
