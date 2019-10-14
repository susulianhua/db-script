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
package org.tinygroup.exception;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 泛型Result结果类。
 */
public class Result<T> implements Serializable {

    /**
     * serial
     */
    private static final long serialVersionUID = -757959713205186605L;

    /**
     * 是否成功。
     */
    private boolean success;

    /**
     * 错误上下文。
     */
    private ErrorContext errorContext;

    /**
     * 请求执行结果。
     */
    private T resultObj;

    /**
     * 构造函数。
     */
    public Result() {
    }

    /**
     * 构造函数。
     *
     * @param success      是否成功。
     * @param errorContext 错误上下文。
     * @param resultObj    对象实例。
     */
    public Result(boolean success, ErrorContext errorContext, T resultObj) {

        this.success = success;
        this.errorContext = errorContext;
        this.resultObj = resultObj;
    }

    /**
     * Getter method for property <tt>success</tt>.
     *
     * @return property value of success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Setter method for property <tt>success</tt>.
     *
     * @param success value to be assigned to property success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Getter method for property <tt>errorContext</tt>.
     *
     * @return property value of errorContext
     */
    public ErrorContext getErrorContext() {
        return errorContext;
    }

    /**
     * Setter method for property <tt>errorContext</tt>.
     *
     * @param errorContext value to be assigned to property errorContext
     */
    public void setErrorContext(ErrorContext errorContext) {
        this.errorContext = errorContext;
    }

    /**
     * Getter method for property <tt>resultObj</tt>.
     *
     * @return property value of resultObj
     */
    public T getResultObj() {
        return resultObj;
    }

    /**
     * Setter method for property <tt>resultObj</tt>.
     *
     * @param resultObj value to be assigned to property resultObj
     */
    public void setResultObj(T resultObj) {
        this.resultObj = resultObj;
    }

    /**
     * 必须是ToStringStyle.SHORT_PREFIX_STYLE，形式如下:</br>
     * Person[name=John Doe,age=33,smoker=false]</br>
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
