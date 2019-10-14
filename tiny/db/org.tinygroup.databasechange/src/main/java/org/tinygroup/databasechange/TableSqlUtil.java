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
package org.tinygroup.databasechange;

import java.io.IOException;
import java.util.List;

public class TableSqlUtil {
    private static final String lINE_SEPARATOR = System
            .getProperty("line.separator");

    public static void appendSqlText(StringBuilder builder,
                                     List<String> processSqls) throws IOException {
        builder.append("/*-------start-----*/").append(lINE_SEPARATOR);
        for (String sql : processSqls) {
            if (sql == null) {
                continue;
            }
            builder.append(sql);
            if (!sql.endsWith("\n/\n")) {
                builder.append(";");
                builder.append(lINE_SEPARATOR);
            }
        }
        builder.append("/*-------end-----*/").append(lINE_SEPARATOR);
    }
}
