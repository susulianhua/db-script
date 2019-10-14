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
package org.tinygroup.database;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.database.config.SqlBody;
import org.tinygroup.database.config.view.View;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.database.view.ViewProcessor;

import java.util.List;

public class ViewProcessorTest extends TestCase {
    static {
        TestInit.init();

    }

    ViewProcessor viewProcessor;

    protected void setUp() throws Exception {
        super.setUp();
        viewProcessor = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                DataBaseUtil.VIEW_BEAN);
    }

    public void testGetTableStringString() {
        assertNotNull(viewProcessor.getView("user_view"));
    }

    public void testGetViewString() {
        assertNotNull(viewProcessor.getCreateSql("user_view", "oracle"));
    }

    public void testGetCreateSqlString() {
        System.out.println("view sql:");
        String tableSql = viewProcessor.getCreateSql("user_view", "oracle");
        System.out.print("oracle view:");
        System.out.println(tableSql);
        tableSql = viewProcessor.getCreateSql("user_view", "mysql");
        System.out.print("mysql view:");
        System.out.println(tableSql);
        tableSql = viewProcessor.getCreateSql("user_view", "h2");
        System.out.print("h2 view:");
        System.out.println(tableSql);

    }

    public void testGetCreateSqlString2() {
        System.out.println("sql:");
        List<String> tableSql = viewProcessor.getCreateSql("oracle");
        System.out.println(tableSql);

    }

    public void testViewSerializable() {
        List<View> views = viewProcessor.getViews();
        String viewSql = viewProcessor.getCreateSql("test000.org_user_view", "mysql");
        assertEquals("CREATE OR REPLACE VIEW role_user_view AS SELECT * FROM org_view o,user_view u where t.id=u.o_id"
                , viewSql);
        View view = viewProcessor.getView("test000.org_user_view");
        view.getName();
        assertEquals("视图user_view_id", view.getDescription());
        for (String refView : view.getRefViewIds().getRefViewIdList()) {
            System.out.println(refView);
        }
        for (SqlBody sqlBody : view.getSqlBodyList()) {
            System.out.println(sqlBody.getDialectTypeName());
            System.out.println(sqlBody.getContent());
        }
        assertEquals("用户视图", view.getTitle());

    }

}
