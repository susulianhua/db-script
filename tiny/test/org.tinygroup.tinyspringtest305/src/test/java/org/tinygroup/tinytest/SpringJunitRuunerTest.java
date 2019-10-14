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
package org.tinygroup.tinytest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;

import javax.annotation.Resource;
import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test.beans.xml")
public class SpringJunitRuunerTest extends BaseTest {
    @Resource(name = "derbyDataSource")
    private DataSource derbyDataSource;
    private JdbcTemplate jdbcTemplate;


    @Before
    public void before() {
        jdbcTemplate = new JdbcTemplate(derbyDataSource);
    }

    @AfterTransaction
    public void after() {
        int count = jdbcTemplate.queryForObject("select count(*) from custom",
                Integer.class);
        assertEquals(0, count);
    }


    @Test
    public void testTransaction() {
        jdbcTemplate.execute("insert into custom(id,name,age) values(10,'sds',16)");
        jdbcTemplate.execute("insert into custom(id,name,age) values(11,'sds',16)");
        int count = jdbcTemplate.queryForObject("select count(*) from custom", Integer.class);
        assertEquals(2, count);
    }

}
