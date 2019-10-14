package org.tinygroup.tinysqldsl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.ScoreTable.TSCORE;
import static org.tinygroup.tinysqldsl.Select.select;
import static org.tinygroup.tinysqldsl.select.Join.innerJoin;

public class Junit4TestInnerJoin {
    @Test
    public void test(){
        assertEquals(
                select(CUSTOM.NAME, CUSTOM.AGE, TSCORE.SCORE).from(CUSTOM)
                        .join(innerJoin(TSCORE, CUSTOM.NAME.eq(TSCORE.NAME)))
                        .sql(),
                "SELECT custom.name,custom.age,score.score FROM custom INNER JOIN score ON custom.name = score.name");

    }
}
