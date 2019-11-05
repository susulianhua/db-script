package com.xquant.script.service;

import com.xquant.script.pojo.MetaDataReturn.SqlResult;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SqlResultResolveService {

    public static String getParseOfIndex(String string , int index){
        StringTokenizer stringTokenizer = new StringTokenizer(string);
        for(int i = 1; i < index; i ++){
            stringTokenizer.nextToken();
        }
        return stringTokenizer.nextToken();
    }

    public static List<SqlResult> sortSql(List<SqlResult> sqlResultList){
        List<SqlResult> tableSqlList = new ArrayList<SqlResult>();
        List<SqlResult> indexList = new ArrayList<SqlResult>();
        List<SqlResult> viewSqlList = new ArrayList<SqlResult>();
        List<SqlResult> triggerList = new ArrayList<SqlResult>();
        List<SqlResult> procedurerList = new ArrayList<SqlResult>();
        List<SqlResult> sequenceList = new ArrayList<SqlResult>();
        for(SqlResult sqlResult: sqlResultList){
            if(sqlResult.getDatabaseType().equals("table")) tableSqlList.add(sqlResult);
            else if(sqlResult.getDatabaseType().equals("view")) viewSqlList.add(sqlResult);
            else if(sqlResult.getDatabaseType().equals("trigger")) triggerList.add(sqlResult);
            else if(sqlResult.getDatabaseType().equals("index")) indexList.add(sqlResult);
            else if(sqlResult.getDatabaseType().equals("procedure")) procedurerList.add(sqlResult);
            else if(sqlResult.getDatabaseType().equals("sequence")) sequenceList.add(sqlResult);
        }
        sqlResultList.clear();
        sqlResultList.addAll(tableSqlList);
        sqlResultList.addAll(indexList);
        sqlResultList.addAll(viewSqlList);
        sqlResultList.addAll(triggerList);
        sqlResultList.addAll(procedurerList);
        sqlResultList.addAll(sequenceList);
        return sqlResultList;
    }

    public static List<SqlResult> getChangeSql(List<String> processSqls){
        List<SqlResult> sqlResultList = new ArrayList<SqlResult>();
        for(String string : processSqls){
            resolveString(sqlResultList, string);
        }
        return sqlResultList;
    }

    public static void resolveString(List<SqlResult> sqlResultList, String string){
        SqlResult sqlResult = new SqlResult();
        if(string.startsWith("CREATE TABLE")){
            sqlResult.setDifferentDetail(string);
            sqlResult.setDatabaseType("table");
            sqlResult.setDatabaseObject(getParseOfIndex(string, 3));
            sqlResult.setDifferentType("缺失表");
            sqlResultList.add(sqlResult);
        }
        else if(string.startsWith("ALTER TABLE") &&
                getParseOfIndex(string, 4).equals("ADD") &&
                getParseOfIndex(string, 5).equals("CONSTRAINT")){
            sqlResult.setDifferentDetail(string);
            sqlResult.setDatabaseType("table");
            sqlResult.setDifferentType("缺失字段");
            sqlResult.setDatabaseObject(getParseOfIndex(string, 3));
            sqlResultList.add(sqlResult);
        }
        else if(string.startsWith("ALTER TABLE") &&
                getParseOfIndex(string, 4).equals("MODIFY")  &&
                getParseOfIndex(string, 8).equals("NULL")){
            sqlResult.setDatabaseObject(getParseOfIndex(string, 3));
            sqlResult.setDatabaseType("table");
            sqlResult.setDifferentType("调整");
            sqlResult.setDifferentDetail(string);
            sqlResultList.add(sqlResult);
        }
        else if(string.startsWith("CREATE UNIQUE INDEX")){
            String indexName = getParseOfIndex(string, 4);
            sqlResult.setDatabaseType("index");
            sqlResult.setDatabaseObject(indexName);
            sqlResult.setDifferentDetail(string);
            sqlResult.setDifferentType("缺失索引");
            sqlResultList.add(sqlResult);
        }
        if(string.startsWith("CREATE OR REPLACE VIEW")){
            sqlResult.setDatabaseType("view");
            sqlResult.setDatabaseObject(getParseOfIndex(string, 5));
            sqlResult.setDifferentType("缺失视图");
            sqlResult.setDifferentDetail(string);
            sqlResultList.add(sqlResult);
        }
        else if(string.startsWith("CREATE OR REPLACE PROCEDURE")){
            sqlResult.setDatabaseType("procedure");
            sqlResult.setDatabaseObject(getParseOfIndex(string, 5));
            sqlResult.setDifferentType("缺失存储过程");
            sqlResult.setDifferentDetail(string);
            sqlResultList.add(sqlResult);
        }
        else if(string.startsWith("CREATE OR REPLACE TRIGGER")){
            sqlResult.setDatabaseObject(getParseOfIndex(string, 5));
            sqlResult.setDatabaseType("trigger");
            sqlResult.setDifferentType("缺失触发器");
            sqlResult.setDifferentDetail(string);
            sqlResultList.add(sqlResult);
        }
        else if(string.startsWith("CREATE SEQUENCE")){
            sqlResult.setDatabaseObject(getParseOfIndex(string, 3));
            sqlResult.setDatabaseType("sequence");
            sqlResult.setDifferentType("缺失序列");
            sqlResult.setDifferentDetail(string);
            sqlResultList.add(sqlResult);
        }
    }

    public static List<SqlResult> getFullSql(List<String> processSqls){
        List<SqlResult> sqlResultList = new ArrayList<SqlResult>();
        for(String string : processSqls){
            resolveString(sqlResultList, string);
            SqlResult sqlResult = new SqlResult();
            if(string.startsWith("ALTER TABLE") && getParseOfIndex(string, 4).equals("DROP") &&
                    getParseOfIndex(string, 5).equals("COLUMN")){
                sqlResult.setDatabaseObject(getParseOfIndex(string, 3));
                sqlResult.setDifferentType("table");
                sqlResult.setDifferentType("字段多出");
                sqlResult.setDifferentDetail(string);
                sqlResultList.add(sqlResult);
            }
        }
        return sqlResultList;
    }
}
