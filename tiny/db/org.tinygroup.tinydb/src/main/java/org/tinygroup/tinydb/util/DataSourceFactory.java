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
package org.tinygroup.tinydb.util;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源工厂
 *
 * @author luoguo
 */
public final class DataSourceFactory {

    private static Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

    private DataSourceFactory() {

    }

    public static PlatformTransactionManager getTransactionManager(
            String dataSourceName, ClassLoader loader) {

        return getTransactionTemplate(dataSourceName, loader).getTransactionManager();
    }

    public static TransactionTemplate getTransactionTemplate(
            String dataSourceName, ClassLoader loader) {
        DataSourceProxy dataSource = BeanContainerFactory.getBeanContainer(loader).getBean("dataSourceProxy");
        return dataSource.getTransactionTemplate();
    }

    public static Connection getConnection(String dataSourceName, ClassLoader loader) {

        try {
            DataSource dataSource = BeanContainerFactory.getBeanContainer(loader).getBean(dataSourceName);
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.errorMessage(e.getMessage(), e);
        }
        return null;
    }
}
