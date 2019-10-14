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
package org.tinygroup.jdbctemplatedslsession.execute;

import org.tinygroup.jdbctemplatedslsession.DslSessionFactory;
import org.tinygroup.jdbctemplatedslsession.SimpleDslSession;
import org.tinygroup.tinysqldsl.Delete;
import org.tinygroup.tinysqldsl.DslSession;
import org.tinygroup.tinysqldsl.Insert;
import org.tinygroup.tinysqldsl.Select;

import static org.tinygroup.jdbctemplatedslsession.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.Delete.delete;
import static org.tinygroup.tinysqldsl.Insert.insertInto;
import static org.tinygroup.tinysqldsl.Select.selectFrom;

public class TransactionTest extends BaseTest {

    public void testTransaction() {

        Delete delete = delete(CUSTOM);
        DslSession session = new SimpleDslSession(dataSource);
        session.execute(delete);

        try {
            session.beginTransaction();
            Insert customInsert = insertInto(CUSTOM).values(
                    CUSTOM.ID.value("10001"), CUSTOM.NAME.value("悠悠然然"),
                    CUSTOM.AGE.value(22));
            session.execute(customInsert);
            session.execute(customInsert);
            session.commitTransaction();
        } catch (Exception e) {
            session.rollbackTransaction();
        }
        Select select = selectFrom(CUSTOM);
        int count = session.count(select);
        assertEquals(0, count);
    }

    public void testNestedTransaction() {
        Delete delete = delete(CUSTOM);
        DslSession session = new SimpleDslSession(dataSource);
        session.execute(delete);
        try {
            session.beginTransaction();
            Insert customInsert = insertInto(CUSTOM).values(
                    CUSTOM.ID.value("10001"), CUSTOM.NAME.value("悠悠然然"),
                    CUSTOM.AGE.value(22));
            session.execute(customInsert);
            DslSession newSession = DslSessionFactory
                    .createSessionWithNewTransaction(dataSource);
            try {
                customInsert = insertInto(CUSTOM).values(
                        CUSTOM.ID.value("10002"), CUSTOM.NAME.value("flank"),
                        CUSTOM.AGE.value(23));
                newSession.beginTransaction();
                newSession.execute(customInsert);
                newSession.execute(customInsert);
                newSession.commitTransaction();
            } catch (Exception e) {
                newSession.rollbackTransaction();
            }
            session.commitTransaction();
        } catch (Exception e) {
            session.rollbackTransaction();
        }
        Select select = selectFrom(CUSTOM);
        int count = session.count(select);
        assertEquals(1, count);
    }

}
