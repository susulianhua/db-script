/**
 * Copyright (c) 2012-2016, www.tinygroup.org (luo_guo@icloud.com).
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
package org.tinygroup.flowcomponent.test.testcase.file;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.flowcomponent.test.testcase.NewBaseTest;
import org.tinygroup.vfs.VFS;



/**
 * @author qiucn
 */
public class FileData2DbTest extends NewBaseTest {

    /**
     *
     */
    public void testFile2Db() {
        Context context = ContextFactory.getContext();
        context.put("sql", "insert into tsys_custom (cust_code,cust_name,gmt_create) values (?,?,?)");
        context.put("filePath", VFS.resolveFile("src/test/resources/File2Db.txt"));
        context.put("colSeparator", ":");
        flowExecutor.execute("fileData2DbTest", context);
    }

}
