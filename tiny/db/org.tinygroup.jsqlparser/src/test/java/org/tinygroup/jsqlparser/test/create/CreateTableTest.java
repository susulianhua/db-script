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
package org.tinygroup.jsqlparser.test.create;

import junit.framework.TestCase;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.statement.create.table.ColumnDefinition;
import org.tinygroup.jsqlparser.statement.create.table.CreateTable;
import org.tinygroup.jsqlparser.statement.create.table.Index;
import org.tinygroup.jsqlparser.test.TestException;
import org.tinygroup.jsqlparser.util.TablesNamesFinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import static org.tinygroup.jsqlparser.test.TestUtils.assertSqlCanBeParsedAndDeparsed;

public class CreateTableTest extends TestCase {

    CCJSqlParserManager parserManager = new CCJSqlParserManager();

    public CreateTableTest(String arg0) {
        super(arg0);
    }

    public void testCreateTable2() throws JSQLParserException {
        String statement = "CREATE TABLE testtab (\"test\" varchar (255))";
        assertSqlCanBeParsedAndDeparsed(statement);
    }

    public void testCreateTable3() throws JSQLParserException {
        String statement = "CREATE TABLE testtab (\"test\" varchar (255), \"test2\" varchar (255))";
        assertSqlCanBeParsedAndDeparsed(statement);
    }

    public void testCreateTable() throws JSQLParserException {
        String statement = "CREATE TABLE mytab (mycol a (10, 20) c nm g, mycol2 mypar1 mypar2 (23,323,3) asdf ('23','123') dasd, "
                + "PRIMARY KEY (mycol2, mycol)) type = myisam";
        CreateTable createTable = (CreateTable) parserManager.parse(new StringReader(statement));
        assertEquals(2, createTable.getColumnDefinitions().size());
        assertEquals("mycol", createTable.getColumnDefinitions().get(0).getColumnName());
        assertEquals("mycol2", createTable.getColumnDefinitions().get(1).getColumnName());
        assertEquals("PRIMARY KEY", createTable.getIndexes().get(0).getType());
        assertEquals("mycol", createTable.getIndexes().get(0).getColumnsNames().get(1));
        assertEquals(statement, "" + createTable);
    }

    public void testCreateTableForeignKey() throws JSQLParserException {
        String statement = "CREATE TABLE test (id INT UNSIGNED NOT NULL AUTO_INCREMENT, string VARCHAR (20), user_id INT UNSIGNED, PRIMARY KEY (id), FOREIGN KEY (user_id) REFERENCES ra_user(id))";
        assertSqlCanBeParsedAndDeparsed(statement);
    }

    public void testCreateTableForeignKey2() throws JSQLParserException {
        String statement = "CREATE TABLE test (id INT UNSIGNED NOT NULL AUTO_INCREMENT, string VARCHAR (20), user_id INT UNSIGNED, PRIMARY KEY (id), CONSTRAINT fkIdx FOREIGN KEY (user_id) REFERENCES ra_user(id))";
        assertSqlCanBeParsedAndDeparsed(statement);
    }

    public void testRUBiSCreateList() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(CreateTableTest.class.getResourceAsStream("/RUBiS-create-requests.txt")));
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();

        try {
            int numSt = 1;
            while (true) {
                String line = getLine(in);
                if (line == null) {
                    break;
                }

                if (!line.equals("#begin")) {
                    break;
                }
                line = getLine(in);
                StringBuilder buf = new StringBuilder(line);
                while (true) {
                    line = getLine(in);
                    if (line.equals("#end")) {
                        break;
                    }
                    buf.append("\n");
                    buf.append(line);
                }

                String query = buf.toString();
                if (!getLine(in).equals("true")) {
                    continue;
                }

                String tableName = getLine(in);
                String cols = getLine(in);
                try {
                    CreateTable createTable = (CreateTable) parserManager.parse(new StringReader(query));
                    String[] colsList = null;
                    if (cols.equals("null")) {
                        colsList = new String[0];
                    } else {
                        StringTokenizer tokenizer = new StringTokenizer(cols, " ");

                        List colsListList = new ArrayList();
                        while (tokenizer.hasMoreTokens()) {
                            colsListList.add(tokenizer.nextToken());
                        }

                        colsList = (String[]) colsListList.toArray(new String[colsListList.size()]);

                    }
                    List colsFound = new ArrayList();
                    if (createTable.getColumnDefinitions() != null) {
                        for (Iterator iter = createTable.getColumnDefinitions().iterator(); iter.hasNext(); ) {
                            ColumnDefinition columnDefinition = (ColumnDefinition) iter.next();
                            String colName = columnDefinition.getColumnName();
                            boolean unique = false;
                            if (createTable.getIndexes() != null) {
                                for (Iterator iterator = createTable.getIndexes().iterator(); iterator.hasNext(); ) {
                                    Index index = (Index) iterator.next();
                                    if (index.getType().equals("PRIMARY KEY") && index.getColumnsNames().size() == 1
                                            && index.getColumnsNames().get(0).equals(colName)) {
                                        unique = true;
                                    }

                                }
                            }

                            if (!unique) {
                                if (columnDefinition.getColumnSpecStrings() != null) {
                                    for (Iterator iterator = columnDefinition.getColumnSpecStrings().iterator(); iterator
                                            .hasNext(); ) {
                                        String par = (String) iterator.next();
                                        if (par.equals("UNIQUE")) {
                                            unique = true;
                                        } else if (par.equals("PRIMARY") && iterator.hasNext()
                                                && iterator.next().equals("KEY")) {
                                            unique = true;
                                        }
                                    }
                                }
                            }
                            if (unique) {
                                colName += ".unique";
                            }
                            colsFound.add(colName.toLowerCase());
                        }
                    }

                    assertEquals("stm:" + query, colsList.length, colsFound.size());

                    for (int i = 0; i < colsList.length; i++) {
                        assertEquals("stm:" + query, colsList[i], colsFound.get(i));

                    }
                } catch (Exception e) {
                    throw new TestException("error at stm num: " + numSt, e);
                }
                numSt++;

            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private String getLine(BufferedReader in) throws Exception {
        String line = null;
        while (true) {
            line = in.readLine();
            if (line != null) {
                line.trim();
                if ((line.length() != 0)
                        && ((line.length() < 2) || (line.length() >= 2)
                        && !(line.charAt(0) == '/' && line.charAt(1) == '/'))) {
                    break;
                }
            } else {
                break;
            }

        }

        return line;
    }
}
