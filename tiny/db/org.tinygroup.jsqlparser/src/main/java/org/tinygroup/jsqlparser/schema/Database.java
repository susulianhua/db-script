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
package org.tinygroup.jsqlparser.schema;

import java.io.Serializable;

public final class Database implements MultiPartName, Serializable {
    private Server server;
    private String databaseName;

    public Database() {

    }

    public Database(String databaseName) {
        setDatabaseName(databaseName);
    }

    public Database(Server server, String databaseName) {
        setServer(server);
        setDatabaseName(databaseName);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }


    public String getFullyQualifiedName() {
        String fqn = "";

        if (server != null) {
            fqn += server.getFullyQualifiedName();
        }
        if (!(fqn == null || fqn.equals(""))) {
            fqn += ".";
        }

        if (databaseName != null) {
            fqn += databaseName;
        }

        return fqn;
    }


    public String toString() {
        return getFullyQualifiedName();
    }
}
