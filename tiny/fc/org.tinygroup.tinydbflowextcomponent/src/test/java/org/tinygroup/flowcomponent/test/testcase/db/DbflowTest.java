package org.tinygroup.flowcomponent.test.testcase.db;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.flowcomponent.test.testcase.NewBaseTest;

/**
 * Created by wangwy11342 on 2016/8/22.
 */
public class DbflowTest extends NewBaseTest {
    public void testPojo() {
        Context c = new ContextImpl();
        c.put("sql", "select * from tsys_user");
        c.put("mappedClassName", "org.tinygroup.flowcomponent.test.testcase.pojo.TsysUser");
        flowExecutor.execute("queryForObjectList", c);
        Object result = c.get("result");
        System.out.println(result);
    }

    public void testResultSet() {
        Context c = new ContextImpl();
        c.put("sql", "select * from tsys_user");
        flowExecutor.execute("queryforRowSet", c);
        SqlRowSet sqlRowSet = c.get("result");
        SqlRowSetMetaData sqlRowSetMetaData = sqlRowSet.getMetaData();
        while (sqlRowSet.next()) {
            for (int i = 0; i < sqlRowSetMetaData.getColumnCount(); i++) {
                System.out.print(sqlRowSetMetaData.getColumnName(i + 1));
                System.out.print("=");
                System.out.println(sqlRowSet.getObject(i + 1));
            }
        }
    }

    public void testSqlRowSet() {
        Context c = new ContextImpl();
        c.put("sql", "select * from tsys_user");
        flowExecutor.execute("queryforRowSet", c);
        SqlRowSet sqlRowSet = c.get("result");
        if (sqlRowSet == null) {
            return;
        }
        SqlRowSetMetaData sqlRowSetMetaData = sqlRowSet.getMetaData();
        while (sqlRowSet.next()) {
            for (int i = 0; i < sqlRowSetMetaData.getColumnCount(); i++) {
                System.out.print(sqlRowSetMetaData.getColumnName(i + 1));
                System.out.print("=");
                System.out.println(sqlRowSet.getObject(i + 1));
            }
        }
    }

    public void testInteger() {
        Context c = new ContextImpl();
        c.put("sql", "select count(*) from tsys_user");
        flowExecutor.execute("queryForNumber", c);
        Number result = c.get("result");
        System.out.println(result);
    }

    public void testDelete() {
        Context c = new ContextImpl();
        c.put("sql", "delete from tsys_user where user_id='xxx'");
        flowExecutor.execute("delete", c);
    }

    public void testUpdate() {
        Context c = new ContextImpl();
        c.put("sql", "update tsys_user set user_id='xxx' where user_id='xxx'");
        flowExecutor.execute("update", c);
    }

    public void testInsert() {
        Context c = new ContextImpl();
        c.put("sql", "insert into tsys_user (user_id,user_name) values ('a','b')");
        flowExecutor.execute("insert", c);
    }

    public void testOneObj() {
        Context c = new ContextImpl();
        c.put("sql", "select * from tsys_user where user_id='u003'");
        c.put("mappedClassName", "org.tinygroup.flowcomponent.test.testcase.pojo.TsysUser");
        flowExecutor.execute("queryforObj", c);
    }
}
