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
package org.tinygroup.tinydb.testcase.relation;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.test.BaseTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RelationTest extends BaseTest {

    private static final String ONE_TO_MORE1 = "oneToMore1";
    private static final String ONE_TO_MORE2 = "oneToMore2";
    private static final String ONE_TO_MORE3 = "oneToMore3";

    private static final String MORE_TO_ONE1 = "moreToOne1";
    private static final String MORE_TO_ONE2 = "moreToOne2";
    private static final String MORE_TO_ONE3 = "moreToOne3";
    private static final String MORE_TO_ONE4 = "moreToOne4";


    @SuppressWarnings("unchecked")
    public void testOneToMore() throws TinyDbException {
        DBOperator<String> operator = (DBOperator<String>) factory.getDBOperator();
        Bean bean = getOneToMoreBean();
        bean = operator.insert(bean);
        DBOperator<String> operator2 = (DBOperator<String>) factory.getDBOperator();
        List<Bean> beans = bean.getProperty("relationIdList");
        Bean bean2 = beans.get(0);
        Integer id = bean2.getProperty("id");
        Bean queryBean2 = operator2.getBean(String.valueOf(id), ONE_TO_MORE2);//ONE_TO_MORE2的记录值
        assertEquals(bean2.getProperty("name"), queryBean2.getProperty("name"));
        Integer id2 = bean.getProperty("id");
        Bean queryBean = operator.getBean(String.valueOf(id2), ONE_TO_MORE1);
        Bean bean3 = ((List<Bean>) queryBean.getProperty("relationIdList")).get(0);
        assertEquals(bean3.getProperty("name"), queryBean2.getProperty("name"));
        operator.delete(bean);
    }

    @SuppressWarnings("unchecked")
    public void testMoreToOne() throws TinyDbException {
        DBOperator<String> operator = (DBOperator<String>) factory.getDBOperator();
        Bean bean = getMoreToOneBean();
        bean = operator.insert(bean);

        DBOperator<String> operator2 = (DBOperator<String>) factory.getDBOperator();
        String relationId1 = bean.getProperty("relationId1");
        Bean bean2 = bean.getProperty("moreToOne2");
        assertEquals(relationId1, bean2.getProperty("relationId").toString());
        Integer id = bean2.getProperty("id");
        Bean queryBean = operator2.getBean(String.valueOf(id), MORE_TO_ONE2);
        assertEquals(queryBean.getProperty("name"), bean2.getProperty("name"));

        DBOperator<String> operator4 = (DBOperator<String>) factory.getDBOperator();
        List<Bean> beans = bean2.getProperty("relationIdList");
        Bean bean4 = beans.get(0);
        Integer id2 = bean4.getProperty("id");
        Bean queryBean4 = operator4.getBean(String.valueOf(id2), MORE_TO_ONE4);
        assertEquals(bean4.getProperty("name"), queryBean4.getProperty("name"));

        Integer id3 = bean.getProperty("id");
        Bean bean1 = operator.getBean(String.valueOf(id3), MORE_TO_ONE1);
        bean2 = bean1.getProperty("moreToOne2");
        assertEquals(bean2.getProperty("name"), "name2");


        operator.delete(bean);


    }

    private Bean getOneToMoreBean() {
        Bean bean1 = new Bean(ONE_TO_MORE1);
        bean1.setProperty("name", ONE_TO_MORE1);
        Bean bean11 = new Bean(ONE_TO_MORE2);
        bean11.setProperty("name", "name1");
        Bean bean12 = new Bean(ONE_TO_MORE2);
        bean12.setProperty("name", "name2");
        List<Bean> beans1 = new ArrayList<Bean>();
        beans1.add(bean12);
        beans1.add(bean11);
        bean1.setProperty("relationIdList", beans1);
        Bean bean21 = new Bean(ONE_TO_MORE3);
        bean21.setProperty("name", "name3");
        Bean bean22 = new Bean(ONE_TO_MORE3);
        bean22.setProperty("name", "name4");
        Set<Bean> beans2 = new HashSet<Bean>();
        beans2.add(bean21);
        beans2.add(bean22);
        bean11.setProperty("relationIdSet", beans2);
        return bean1;
    }

    private Bean getMoreToOneBean() {
        Bean bean1 = new Bean(MORE_TO_ONE1);

        bean1.setProperty("name", "name1");
        bean1.setProperty("relationId1", "1");
        bean1.setProperty("relationId2", "2");

        Bean bean2 = new Bean(MORE_TO_ONE2);
        bean2.setProperty("name", "name2");
        bean1.setProperty("moreToOne2", bean2);
//		bean2.setProperty("relationId", "1");//可以不指定关联，没有关联存在则新增该条记录
        Bean bean3 = new Bean(MORE_TO_ONE3);
        bean3.setProperty("name", "name3");
        bean1.setProperty("moreToOne3", bean3);
//		bean2.setProperty("relationId", "2");//可以不指定关联，没有关联存在则新增该条记录
        Bean bean41 = new Bean(MORE_TO_ONE4);
        bean41.setProperty("name", "name41");
        Bean bean42 = new Bean(MORE_TO_ONE4);
        bean42.setProperty("name", "name42");
        List<Bean> beans = new ArrayList<Bean>();
        beans.add(bean41);
        beans.add(bean42);
        bean2.setProperty("relationIdList", beans);
        return bean1;
    }


}
