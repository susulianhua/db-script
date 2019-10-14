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
package testcase;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.databasebuinstaller.DatabaseInstallerProcessor;
import org.tinygroup.databasechange.TableSqlChangeUtil;
import org.tinygroup.databasechange.TableSqlDropUtil;
import org.tinygroup.databasechange.TableSqlFullUtil;
import org.tinygroup.fileresolver.FileResolverFactory;
import org.tinygroup.tinyrunner.Runner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SqlChangeTest extends TestCase {


    public void testSqlChange() throws IOException {
        URL url = getClass().getResource("/");
        if (url == null) {
            url = getClass().getClassLoader().getResource("");
        }
        String filePath = url.getFile() + "change.sql";
        TableSqlChangeUtil.main(new String[]{filePath});

    }

    public void testFullSql() throws IOException {
        URL url = getClass().getResource("/");
        if (url == null) {
            url = getClass().getClassLoader().getResource("");
        }
        String filePath = url.getFile() + "full.sql";
        TableSqlFullUtil.main(new String[]{filePath});
    }

    public void testDropSql() throws IOException {
        URL url = getClass().getResource("/");
        if (url == null) {
            url = getClass().getClassLoader().getResource("");
        }
        String filePath = url.getFile() + "drop.sql";
        TableSqlDropUtil.main(new String[]{filePath});
    }

    public void testInstaller() {
        FileResolverFactory.destroyFileResolver();
        Runner.init(null, new ArrayList<String>());
        System.out.println("start creating change sql no cache...");
        DatabaseInstallerProcessor databaseInstallerProcessor
                = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean("databaseInstallerProcessor");
        System.out.println("all sql=" + databaseInstallerProcessor.getChangeSqls());
    }

}
