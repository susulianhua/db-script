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
package org.tinygroup.validate;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.validate.impl.ValidateResultImpl;

public class ValidateTest extends TestCase {
    private static ValidatorManager annotationValidatorManager;

    static {
        AbstractTestUtil.init(null, true);
        annotationValidatorManager = BeanContainerFactory.getBeanContainer(ValidateTest.class.getClassLoader()).
                getBean("annotationValidatorManager");
    }

    public void testAnnotationValidate() {
        UserBean bean = new UserBean();
        bean.setUserName("12");
        bean.setPasswd("1234");
        bean.setObid("123456");
        ValidateResult result = new ValidateResultImpl();
        annotationValidatorManager.validate(bean, result);

        for (ErrorDescription errorMsg : result.getErrorList()) {
            System.out.println(errorMsg.getDescription());
        }

        assertFalse(result.hasError());
    }
}
