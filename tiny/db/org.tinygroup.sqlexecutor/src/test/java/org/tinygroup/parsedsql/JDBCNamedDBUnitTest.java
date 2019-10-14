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

import org.dbunit.DatabaseUnitException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.parsedsql.exception.ParsedSqlException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test.beans.xml")
public class JDBCNamedDBUnitTest extends AbstractDBUnitTest {

    private DataSource dataSource;
    @Autowired
    private JDBCNamedSqlExecutor sqlExecutor;

    @Override
    protected List<String> getSchemaFiles() {
        return Collections.singletonList("integrate/schema/db_test.sql");
    }

    @Override
    protected List<String> getDataSetFiles() {
        return Collections.singletonList("integrate/dataset/init/db_test.xml");
    }

    @Before
    public void initDataSource() throws SQLException {
        dataSource = createDataSource("integrate/schema/db_test.sql");
    }

    @Test
    public void testSimpleSelect() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("user_id", "102");
        paramMap.put("user_name", "testName2");
        Context context = new ContextImpl(paramMap);
        sqlExecutor
                .extractResultSetCallback(
                        "select * from t_user where user_id=@user_id and user_name=@user_name",
                        dataSource, context, new ResultSetCallback() {
                            @Override
                            public void callback(ResultSet rs)
                                    throws SQLException {
                                try {
                                    assertDataSet(
                                            rs,
                                            dataSource.getConnection(),
                                            "integrate/dataset/expect/simple_select.xml",
                                            "t_user");
                                } catch (DatabaseUnitException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

    }

    @Test
    public void testSimpleSelectNoUserId() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("user_name", "testName2");
        Context context = new ContextImpl(paramMap);
        sqlExecutor
                .extractResultSetCallback(
                        "select * from t_user where user_id=@user_id and user_name=@user_name",
                        dataSource, context, new ResultSetCallback() {
                            @Override
                            public void callback(ResultSet rs)
                                    throws SQLException {
                                try {
                                    assertDataSet(
                                            rs,
                                            dataSource.getConnection(),
                                            "integrate/dataset/expect/simple_select.xml",
                                            "t_user");
                                } catch (DatabaseUnitException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

    }

    @Test
    public void testSimpleSelectGreater() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("user_id", "102");
        Context context = new ContextImpl(paramMap);
        sqlExecutor
                .extractResultSetCallback(
                        "select * from t_user where user_id>@user_id and user_name=@user_name",
                        dataSource, context, new ResultSetCallback() {
                            @Override
                            public void callback(ResultSet rs)
                                    throws SQLException {
                                try {
                                    assertDataSet(
                                            rs,
                                            dataSource.getConnection(),
                                            "integrate/dataset/expect/greater_select.xml",
                                            "t_user");
                                } catch (DatabaseUnitException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

    }

//	@Test
//	public void testSimpleSelectLike() throws SQLException {
//		Map<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("user_id", "2");
//		paramMap.put("user_name", "testName");
//		Context context = new ContextImpl(paramMap);
//		sqlExecutor
//				.extractResultSetCallback(
//						"select * from t_user where user_id>@user_id and user_name like '%@user_name%'",
//						dataSource, context, new ResultSetCallback() {
//							@Override
//							public void callback(ResultSet rs)
//									throws SQLException {
//								try {
//									assertDataSet(
//											rs,
//											dataSource.getConnection(),
//											"integrate/dataset/expect/greater_select.xml",
//											"t_user");
//								} catch (DatabaseUnitException e) {
//									throw new RuntimeException(e);
//								}
//							}
//						});
//
//	}

    @Test
    public void testSimpleSelectGreaterNoUserId() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("user_name", "testName2");
        Context context = new ContextImpl(paramMap);
        sqlExecutor
                .extractResultSetCallback(
                        "select * from t_user where user_id>@user_id and user_name=@user_name",
                        dataSource, context, new ResultSetCallback() {
                            @Override
                            public void callback(ResultSet rs)
                                    throws SQLException {
                                try {
                                    assertDataSet(
                                            rs,
                                            dataSource.getConnection(),
                                            "integrate/dataset/expect/simple_select.xml",
                                            "t_user");
                                } catch (DatabaseUnitException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

    }

    @Test
    public void testUpdate() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("user_id", "105");
        paramMap.put("user_name", "updateName");
        paramMap.put("age", "15");
        Context context = new ContextImpl(paramMap);
        int affectNum = sqlExecutor.execute("update t_user set age=@age,user_name=@user_name where user_id=@user_id", dataSource, context);
        Assert.assertEquals(1, affectNum);
    }

    @Test
    public void testUpdateNoUserName() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("user_id", "105");
        paramMap.put("age", "15");
        Context context = new ContextImpl(paramMap);
        int affectNum = sqlExecutor.execute("update t_user set age=@age,user_name=@user_name where user_id=@user_id", dataSource, context);
        Assert.assertEquals(1, affectNum);
    }

    @Test
    public void testUpdateNoCondtion() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("age", "15");
        Context context = new ContextImpl(paramMap);
        int affectNum = sqlExecutor.execute("update t_user set age=@age,user_name=@user_name where user_id=@user_id", dataSource, context);
        Assert.assertEquals(10, affectNum);
    }

    @Test(expected = ParsedSqlException.class)
    public void testUpdateNoUpdateItem() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("user_id", "5");
        Context context = new ContextImpl(paramMap);
        sqlExecutor.execute("update t_user set age=@age,user_name=@user_name where user_id=@user_id", dataSource, context);
    }

    @Test
    public void testDelete() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("user_id", "102");
        paramMap.put("user_name", "testName2");
        Context context = new ContextImpl(paramMap);
        int affectNum = sqlExecutor.execute("delete from t_user where user_id=@user_id and user_name=@user_name", dataSource, context);
        Assert.assertEquals(1, affectNum);
    }

    @Test
    public void testDeleteNoUserName() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("user_id", "102");
        Context context = new ContextImpl(paramMap);
        int affectNum = sqlExecutor.execute("delete from t_user where user_id=@user_id and user_name=@user_name", dataSource, context);
        Assert.assertEquals(1, affectNum);
    }

    @Test
    public void testDeleteNoCondition() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        Context context = new ContextImpl(paramMap);
        int affectNum = sqlExecutor.execute("delete from t_user where user_id=@user_id and user_name=@user_name", dataSource, context);
        Assert.assertEquals(10, affectNum);
    }

    @Test
    public void testInsert() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("user_id", "1000");
        paramMap.put("user_name", "testName100");
        paramMap.put("age", "100");
        Context context = new ContextImpl(paramMap);
        int affectNum = sqlExecutor.execute("insert into t_user(user_id,user_name,age) values(@user_id,@user_name,@age)", dataSource, context);
        Assert.assertEquals(1, affectNum);
    }
    
    @Test
    public void testInsertWithNoPrimaryKey() throws SQLException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("user_name", "testName10001");
        paramMap.put("age", "100");
        Context context = new ContextImpl(paramMap);
        int affectNum = sqlExecutor.execute("insert into t_user(user_name,age) values(@user_name,@age)", dataSource, context);
        Assert.assertEquals(1, affectNum);
    }

}
