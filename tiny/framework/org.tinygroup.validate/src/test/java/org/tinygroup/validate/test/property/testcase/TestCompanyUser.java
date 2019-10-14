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
package org.tinygroup.validate.test.property.testcase;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.ValidatorManager;
import org.tinygroup.validate.XmlValidatorManager;
import org.tinygroup.validate.impl.ValidateResultImpl;
import org.tinygroup.validate.test.property.CompanyUser;

public class TestCompanyUser extends TestCase {
    protected ValidatorManager validatorManager;

    private void init() {
        AbstractTestUtil.init(null, true);
    }

    protected void setUp() throws Exception {
        super.setUp();
        init();
        validatorManager = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                XmlValidatorManager.VALIDATOR_MANAGER_BEAN_NAME);
    }

    public void testCompanyUserSucess() {
        ValidateResult result = new ValidateResultImpl();
        validatorManager.validate("", getSucessUser(), result);
        if (!result.hasError()) {
            assertTrue(true);
        } else {
            assertFalse(false);
        }
    }

    private CompanyUser getSucessUser() {
        CompanyUser u = new CompanyUser();
        u.setName("1234567890123");
        return u;
    }
}
