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
package org.tinygroup.jsqlparser.test.drop;

import junit.framework.TestCase;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.statement.drop.Drop;

import java.io.StringReader;

public class DropTest extends TestCase {

    CCJSqlParserManager parserManager = new CCJSqlParserManager();

    public DropTest(String arg0) {
        super(arg0);
    }

    public void testDrop() throws JSQLParserException {
        String statement = "DROP TABLE mytab";
        Drop drop = (Drop) parserManager.parse(new StringReader(statement));
        assertEquals("TABLE", drop.getType());
        assertEquals("mytab", drop.getName());
        assertEquals(statement, "" + drop);

        statement = "DROP INDEX myindex CASCADE";
        drop = (Drop) parserManager.parse(new StringReader(statement));
        assertEquals("INDEX", drop.getType());
        assertEquals("myindex", drop.getName());
        assertEquals("CASCADE", drop.getParameters().get(0));
        assertEquals(statement, "" + drop);
    }
}
