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
 * 产生异常的组件
 *
 * @author zhangliang08072
 * @version $Id: CreateExceptionComponent.java, v 0.1 2016年4月27日 下午9:19:37 zhangliang08072 Exp $
 */
public class ExceptionSwitchComponent implements ComponentInterface {
    int exceptionNo;

    public int getExceptionNo() {
        return exceptionNo;
    }

    /**
     * 根据入参exceptionNo生成不同的异常
     *
     * @param exceptionNo
     */
    public void setExceptionNo(int exceptionNo) {
        this.exceptionNo = exceptionNo;
    }

    public void execute(Context context) {
        switch (exceptionNo) {
            case 1:
                throw new ComponentException1();
            case 2:
                throw new ComponentException2();
            case 3:
                throw new ComponentException3();
            case 4:
                throw new ComponentException4();
            default:
                throw new ComponentException5();
        }
    }

}
