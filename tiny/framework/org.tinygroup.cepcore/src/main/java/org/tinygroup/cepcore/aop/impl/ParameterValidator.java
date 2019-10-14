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
package org.tinygroup.cepcore.aop.impl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.exception.RequestParamValidateException;
import org.tinygroup.event.Parameter;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.ValidatorManager;
import org.tinygroup.validate.impl.ValidateResultImpl;

import java.util.List;

public class ParameterValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterValidator.class);

    private ParameterValidator() {

    }

    public static void validate(Object[] args, List<Parameter> inputParameters,
                                ClassLoader loader) {
        ValidateResult result = new ValidateResultImpl();
        for (int i = 0; i < inputParameters.size(); i++) {
            Parameter p = inputParameters.get(i);
            Object value = args[i];
            String scene = p.getValidatorSence();
            if (scene != null && !"".equals(scene)) {
                ValidatorManager xmlValidatorManager = BeanContainerFactory
                        .getBeanContainer(loader).getBean(
                                ValidatorManager.VALIDATOR_MANAGER_BEAN_NAME);
                if (p.isArray()) {// 如果是数组
                    Object[] array = (Object[]) value;
                    validateArray(scene, array, result, xmlValidatorManager);
                } else if (value instanceof List) { // 如果是List
                    validateList(scene, (List<?>) value, result,
                            xmlValidatorManager);
                } else {
                    validateObject(scene, value, result, xmlValidatorManager);
                }
            }
        }
        if (result.hasError()) {
            LOGGER.errorMessage("参数验证失败");
            throw new RequestParamValidateException(result);
        }
    }

    private static void validateList(String scene, List<?> value,
                                     ValidateResult result, ValidatorManager xmlValidatorManager) {
        Object[] array = value.toArray();
        for (Object o : array) {
            validateObject(scene, o, result, xmlValidatorManager);
        }
    }

    private static void validateArray(String scene, Object[] value,
                                      ValidateResult result, ValidatorManager xmlValidatorManager) {
        for (Object o : value) {
            validateObject(scene, o, result, xmlValidatorManager);
        }
    }

    private static void validateObject(String scene, Object value,
                                       ValidateResult result, ValidatorManager xmlValidatorManager) {
        xmlValidatorManager.validate(scene, value, result);
    }
}
