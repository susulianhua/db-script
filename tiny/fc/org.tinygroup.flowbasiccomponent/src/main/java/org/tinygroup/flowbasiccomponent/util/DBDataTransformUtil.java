package org.tinygroup.flowbasiccomponent.util;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBDataTransformUtil {

    /**
     * RowSet转字符串
     *
     * @param sqlRowSet
     * @param separator
     * @return
     */
    public static String transformRowSet(SqlRowSet sqlRowSet, String separator) {
        SqlRowSetMetaData sqlRowSetMetaData = sqlRowSet.getMetaData();
        StringBuilder sb = new StringBuilder();
        while (sqlRowSet.next()) {
            for (int i = 0; i < sqlRowSetMetaData.getColumnCount(); i++) {
                sb.append(sqlRowSet.getObject(i + 1));
                if (i < sqlRowSetMetaData.getColumnCount() - 1) {
                    sb.append(separator);
                }
            }
        }
        return sb.toString();
    }

    /**
     * MapList转字符串
     *
     * @param mapList
     * @param separator
     * @return
     */
    public static String transformMapList(List<Map<String, Object>> mapList, String separator) {
        StringBuilder sb = new StringBuilder();
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                Set<String> sets = map.keySet();
                Iterator<String> iterator = sets.iterator();
                while (iterator.hasNext()) {
                    sb.append(map.get(iterator.next()));
                    sb.append(separator);
                }
            }
        }
        return sb.toString();
    }
}
