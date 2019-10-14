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
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import java.io.File;
import java.io.IOException;

/**
 * @author qiucn
 */
public class DBData2FileTest extends NewBaseTest {

    /**
     *
     */
    public void testData2File() {
    	FileObject fo = VFS.resolveFile("src/test/resources/Data2File.txt");
    	if(!fo.isExist()){
    		File file = new File(fo.getAbsolutePath());
    		try {
				file.createNewFile();
				Context context = ContextFactory.getContext();
				context.put("sql", "select * from tsys_user");
				context.put("filePath", fo.getAbsolutePath());
				context.put("colSeparator", ":");
				flowExecutor.execute("dbData2FileTest", context);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				fo.delete();
			}
    	}
    }

}
