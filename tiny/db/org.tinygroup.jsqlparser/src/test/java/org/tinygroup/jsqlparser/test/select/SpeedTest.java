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
package org.tinygroup.jsqlparser.test.select;

import junit.framework.TestCase;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.test.TestException;
import org.tinygroup.jsqlparser.test.simpleparsing.CCJSqlParserManagerTest;
import org.tinygroup.jsqlparser.util.TablesNamesFinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpeedTest extends TestCase {

    private final static int NUM_REPS = 500;
    private CCJSqlParserManager parserManager = new CCJSqlParserManager();

    public SpeedTest(String arg0) {
        super(arg0);
    }

    public void testSpeed() throws Exception {
        // all the statements in testfiles/simple_parsing.txt
        BufferedReader in = new BufferedReader(new InputStreamReader(SpeedTest.class.getResourceAsStream("/simple_parsing.txt")));
        CCJSqlParserManagerTest d;
        ArrayList statementsList = new ArrayList();

        while (true) {
            String statement = CCJSqlParserManagerTest.getStatement(in);
            if (statement == null) {
                break;
            }
            statementsList.add(statement);
        }
        in.close();
        in = new BufferedReader(new InputStreamReader(SpeedTest.class.getResourceAsStream("/RUBiS-select-requests.txt")));

        // all the statements in testfiles/RUBiS-select-requests.txt
        while (true) {
            String line = CCJSqlParserManagerTest.getLine(in);
            if (line == null) {
                break;
            }
            if (line.length() == 0) {
                continue;
            }

            if (!line.equals("#begin")) {
                break;
            }
            line = CCJSqlParserManagerTest.getLine(in);
            StringBuilder buf = new StringBuilder(line);
            while (true) {
                line = CCJSqlParserManagerTest.getLine(in);
                if (line.equals("#end")) {
                    break;
                }
                buf.append("\n");
                buf.append(line);
            }
            if (!CCJSqlParserManagerTest.getLine(in).equals("true")) {
                continue;
            }

            statementsList.add(buf.toString());

            String cols = CCJSqlParserManagerTest.getLine(in);
            String tables = CCJSqlParserManagerTest.getLine(in);
            String whereCols = CCJSqlParserManagerTest.getLine(in);
            String type = CCJSqlParserManagerTest.getLine(in);

        }
        in.close();

        String statement = "";
        int numTests = 0;
        // it seems that the very first parsing takes a while, so I put it aside
        Statement parsedStm = parserManager.parse(new StringReader(statement = (String) statementsList.get(0)));
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        ArrayList parsedSelects = new ArrayList(NUM_REPS * statementsList.size());
        long time = System.currentTimeMillis();

        // measure the time to parse NUM_REPS times all statements in the 2 files
        for (int i = 0; i < NUM_REPS; i++) {
            try {
                int j = 0;
                for (Iterator iter = statementsList.iterator(); iter.hasNext(); j++) {
                    statement = (String) iter.next();
                    parsedStm = parserManager.parse(new StringReader(statement));
                    numTests++;
                    if (parsedStm instanceof Select) {
                        parsedSelects.add(parsedStm);
                    }

                }
            } catch (JSQLParserException e) {
                throw new TestException("impossible to parse statement: " + statement, e);
            }
        }
        long elapsedTime = System.currentTimeMillis() - time;
        long statementsPerSecond = numTests * 1000 / elapsedTime;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(7);
        df.setMinimumFractionDigits(4);
        System.out.println(numTests + " statements parsed in " + elapsedTime + " milliseconds");
        System.out.println(" (" + statementsPerSecond + " statements per second,  "
                + df.format(1.0 / statementsPerSecond) + " seconds per statement )");

        numTests = 0;
        time = System.currentTimeMillis();
        // measure the time to get the tables names from all the SELECTs parsed before
        for (Iterator iter = parsedSelects.iterator(); iter.hasNext(); ) {
            Select select = (Select) iter.next();
            if (select != null) {
                numTests++;
                List tableListRetr = tablesNamesFinder.getTableList(select);
            }
        }
        elapsedTime = System.currentTimeMillis() - time;
        statementsPerSecond = numTests * 1000 / elapsedTime;
        System.out.println(numTests + " select scans for table name executed in " + elapsedTime + " milliseconds");
        System.out.println(" (" + statementsPerSecond + " select scans for table name per second,  "
                + df.format(1.0 / statementsPerSecond) + " seconds per select scans for table name)");

    }
}
