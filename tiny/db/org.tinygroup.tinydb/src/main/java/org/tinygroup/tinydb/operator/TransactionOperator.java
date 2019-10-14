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
package org.tinygroup.tinydb.operator;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * 事务操作api
 *
 * @author renhui
 */
public interface TransactionOperator {

    /**
     * 开始事务
     */
    void beginTransaction();

    /**
     * 提交事务
     */
    void commitTransaction();

    /**
     * 回滚事务
     */
    void rollbackTransaction();

    /**
     * 以事务方式执行回调处理
     *
     * @param callback
     * @return
     */
    Object execute(TransactionCallBack callback);

    /**
     * 设置事务管理对象
     *
     * @param transactionManager
     */
    void setTransactionManager(
            PlatformTransactionManager transactionManager);

    /**
     * 设置事务定义，默认为PROPAGATION_REQUIRED,ISOLATION_DEFAULT
     *
     * @param transactionDefinition
     */
    void setTransactionDefinition(
            TransactionDefinition transactionDefinition);


}
