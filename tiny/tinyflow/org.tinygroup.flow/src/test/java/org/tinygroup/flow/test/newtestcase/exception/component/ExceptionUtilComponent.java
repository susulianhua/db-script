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
package org.tinygroup.flow.test.newtestcase.exception.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;

/**
 * 辅助测试流程异常的组件
 *
 * @author zhangliang08072
 * @version $Id: ExceptionUtilComponent.java, v 0.1 2016年4月27日 下午10:11:45 zhangliang08072 Exp $
 */
public class ExceptionUtilComponent implements ComponentInterface {
    Integer no;

    public void execute(Context context) {
        context.put("result", no);
    }

    /**
     * Getter method for property <tt>no</tt>.
     *
     * @return property value of no
     */
    public Integer getNo() {
        return no;
    }

    /**
     * Setter method for property <tt>no</tt>.
     *
     * @param no value to be assigned to property no
     */
    public void setNo(Integer no) {
        this.no = no;
    }
}
