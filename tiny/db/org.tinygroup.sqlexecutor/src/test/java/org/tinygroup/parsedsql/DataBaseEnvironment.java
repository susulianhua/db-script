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
package org.tinygroup.parsedsql;

import org.tinygroup.parsedsql.base.DatabaseType;

import java.util.HashMap;
import java.util.Map;

public final class DataBaseEnvironment {

    private static final Map<DatabaseType, Class<?>> DRIVER_CLASS_NAME = new HashMap<DatabaseType, Class<?>>(2);

    private static final Map<DatabaseType, String> URL = new HashMap<DatabaseType, String>(2);

    private static final Map<DatabaseType, String> USERNAME = new HashMap<DatabaseType, String>(2);

    private static final Map<DatabaseType, String> PASSWORD = new HashMap<DatabaseType, String>(2);

    private final DatabaseType databaseType;

    public DataBaseEnvironment(final DatabaseType databaseType) {
        this.databaseType = databaseType;
        fillData();
    }

    private void fillData() {
        DRIVER_CLASS_NAME.put(DatabaseType.H2, org.h2.Driver.class);
        DRIVER_CLASS_NAME.put(DatabaseType.MySQL, com.mysql.jdbc.Driver.class);
        URL.put(DatabaseType.H2, "jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;MODE=MYSQL;ALIAS_COLUMN_NAME=true");
        URL.put(DatabaseType.MySQL, "jdbc:mysql://localhost:3306/%s?characterEncoding=utf-8&useOldAliasMetadataBehavior=true");
        USERNAME.put(DatabaseType.H2, "sa");
        USERNAME.put(DatabaseType.MySQL, "root");
        PASSWORD.put(DatabaseType.H2, "");
        PASSWORD.put(DatabaseType.MySQL, "123456");
    }

    public String getDriverClassName() {
        return DRIVER_CLASS_NAME.get(databaseType).getName();
    }

    public String getURL(final String dbName) {
        return String.format(URL.get(databaseType), dbName);
    }

    public String getUsername() {
        return USERNAME.get(databaseType);
    }

    public String getPassword() {
        return PASSWORD.get(databaseType);
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

}