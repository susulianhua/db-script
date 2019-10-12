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
package com.xquant.databasebuilstaller.impl;

import com.xquant.databasebuilstaller.InstallProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 功能说明:安装处理的抽象实现
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-15 <br>
 * <br>
 */
public abstract class AbstractInstallProcessor implements InstallProcessor {

    protected Logger logger = LoggerFactory
            .getLogger(AbstractInstallProcessor.class);

    public int getOrder() {
        return 0;
    }

    public List<String> getPreProcessSqls(boolean isFull, String language, Connection connection) throws SQLException {
        return null;
    }

    public List<String> getProcessSqls(boolean isFull, String language, Connection connection) throws SQLException {
        return null;
    }

    public List<String> getPostProcessSqls(boolean isFull, String language, Connection connection) throws SQLException {
        return null;
    }

}
