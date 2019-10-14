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
package org.tinygroup.validatecomponent.test;

import junit.framework.TestCase;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.ValidatorManager;
import org.tinygroup.validate.impl.ValidateResultImpl;

import java.util.*;

public abstract class AbstractValidatorManagerTest extends TestCase {

    protected ValidatorManager validatorManager;

    void init() {
        AbstractTestUtil.init("application.xml", true);
    }


    public void testValidator() {
        User user = new User();
        user.setAge(10);
        user.setEmail("abc@ad.com");
        user.setName("renhui");
        Address address = new Address();
        address.setName("武林门新村");
        // address.setUrl("http://www.sina.com");
        user.setAddress(address);
        Address[] addressArray = new Address[5];
        addressArray[0] = address;
        user.setAddressArray(addressArray);
        List<String> strList = new ArrayList<String>();
        strList.add("str1");
        user.setStrList(strList);
        List<Address> addresses = new ArrayList<Address>();
        addresses.add(address);
        user.setAdds(addresses);
        Set<Address> addressSet = new HashSet<Address>();
        addressSet.add(address);
        user.setAddressSet(addressSet);
        Map<String, Address> addressMap = new HashMap<String, Address>();
        addressMap.put("hangzhou", address);
        user.setAddressMap(addressMap);
        ValidateResult result = new ValidateResultImpl();
        validatorManager.validate(user, result);
//		for (ErrorDescription description : result.getErrorList()) {
//			System.out.println(description.getDescription());
//			System.out.println(description.getFieldName());
//		}
    }
}
