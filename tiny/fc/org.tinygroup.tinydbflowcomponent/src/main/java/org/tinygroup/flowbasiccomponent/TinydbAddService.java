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
package org.tinygroup.flowbasiccomponent;

import org.tinygroup.context.Context;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.operator.DBOperator;

/**
 * tinydb的增删改查服务组件之添加服务
 *
 * @author renhui
 */
public class TinydbAddService extends AbstractTinydbService {


    public void tinyService(Bean bean, Context context, DBOperator operator) {
        if (bean != null) {
            try {
                Bean insertBean = operator.insert(bean);
                context.put(resultKey, insertBean);
            } catch (TinyDbException e) {
                throw new RuntimeException(e);
            }
        } else {
            logger.logMessage(LogLevel.WARN, "新增服务时,从上下文中找不到bean对象，其beantype:[{}]", beanType);
        }
    }

}
