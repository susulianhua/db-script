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
package org.tinygroup.tinydb.testcase.operator;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.test.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestBatchOperator extends BaseTest {

    private String[] getBeanIds(Bean[] beans) {
        String[] ids = new String[2];
        Integer id1 = beans[0].getProperty("id");
        Integer id2 = beans[1].getProperty("id");
        ids[0] = String.valueOf(id1);
        ids[1] = String.valueOf(id2);
        return ids;
    }

    private List<String> getBeanIdList(Bean[] beans) {
        List<String> ids = new ArrayList<String>();
        Integer id1 = beans[0].getProperty("id");
        Integer id2 = beans[1].getProperty("id");
        ids.add(String.valueOf(id1));
        ids.add(String.valueOf(id2));
        return ids;
    }

    private Bean[] getBeans() {
        Bean bean = new Bean(ANIMAL);
        bean.setProperty("id", "beanId");
        bean.setProperty("name", "1234");
        bean.setProperty("length", "1234");

        Bean bean2 = new Bean(ANIMAL);
        bean2.setProperty("id", "beanId2");
        bean2.setProperty("name", "12345");
        bean2.setProperty("length", "12345");
        Bean[] beans = new Bean[2];
        beans[0] = bean;
        beans[1] = bean2;
        return beans;
    }

    private List<Bean> getBeanList() {
        Bean bean = new Bean(ANIMAL);
        bean.setProperty("id", "beanId");
        bean.setProperty("name", "1234");
        bean.setProperty("length", "1234");

        Bean bean2 = new Bean(ANIMAL);
        bean2.setProperty("id", "beanId2");
        bean2.setProperty("name", "12345");
        bean2.setProperty("length", "12345");

        List<Bean> list = new ArrayList<Bean>();
        list.add(bean2);
        list.add(bean);
        return list;

    }

    public void testArrayDelete() throws TinyDbException {
        Bean[] beans = getBeans();
        getOperator().batchDelete(beans);
        assertEquals(2, getOperator().batchInsert(beans).length);
        assertEquals(2, getOperator().batchDelete(beans).length);
    }

    public void testArrayUpdate() throws TinyDbException {
        Bean[] beans = getBeans();
        getOperator().batchInsert(beans);
        beans[0].setProperty("name", "123456");
        beans[1].setProperty("name", "123456");
        assertEquals(2, getOperator().batchUpdate(beans).length);
        assertEquals(2, getOperator().batchDelete(beans).length);

    }

    public void testArrayQuery() throws TinyDbException {
        Bean[] beans = getBeans();
        beans = getOperator().batchInsert(beans);
        String[] ids = getBeanIds(beans);
        Bean[] beans2 = getOperator().getBeansById(ids, ANIMAL);
        assertEquals(2, beans2.length);
        getOperator().batchDelete(beans);
    }

    public void testArrayDeleteById() throws TinyDbException {
        Bean[] beans = getBeans();
        beans = getOperator().batchInsert(beans);
        String[] ids = getBeanIds(beans);
        assertEquals(2, getOperator().deleteById(ids, ANIMAL).length);

    }

    public void testCollectionDelete() throws TinyDbException {
        List<Bean> beans = getBeanList();
        assertEquals(2, getOperator().batchInsert(beans).length);
        assertEquals(2, getOperator().batchDelete(beans).length);
    }

    public void testCollectionUpdate() throws TinyDbException {
        List<Bean> beans = getBeanList();
        getOperator().batchInsert(beans);
        beans.get(0).setProperty("name", "123456");
        beans.get(1).setProperty("name", "123456");
        assertEquals(2, getOperator().batchUpdate(beans).length);
        getOperator().batchDelete(beans);

    }

    public void testCollectionQuery() throws TinyDbException {
        List<Bean> beans = getBeanList();
        Bean[] insertBeans = getOperator().batchInsert(beans);
        List<String> ids = getBeanIdList(insertBeans);
        Bean[] beans2 = getOperator().getBeansById(ids, ANIMAL);
        assertEquals(2, beans2.length);
        getOperator().batchDelete(beans);
    }

    public void testCollectionDeleteById() throws TinyDbException {
        List<Bean> beans = getBeanList();
        Bean[] insertBeans = getOperator().batchInsert(beans);
        List<String> ids = getBeanIdList(insertBeans);
        assertEquals(2, getOperator().deleteById(ids, ANIMAL).length);
    }

    public void testBatchSqls() throws TinyDbException {
        Bean[] beans = getBeans(20);
        beans = getOperator().batchInsert(beans);
        List<String> sqls = new ArrayList<String>();
        for (int i = 0; i < beans.length; i++) {
            sqls.add("update animal set name='fdfs' where id='" + beans[i].get("id").toString() + "'");
        }
        int[] affects = getOperator().executeBatch(sqls);
        assertEquals(20, affects.length);
        assertEquals(1, affects[0]);
        getOperator().execute("delete from animal");
    }

    public void testBatchWithParams() throws TinyDbException {
        Bean[] beans = getBeans(20);
        beans = getOperator().batchInsert(beans);
        String sql = "update animal set name=? where id=?";
        List<List<Object>> params = new ArrayList<List<Object>>();
        for (int i = 0; i < beans.length; i++) {
            Bean bean = beans[i];
            List<Object> param = new ArrayList<Object>();
            param.add("name" + i);
            param.add(bean.get("id").toString());
            params.add(param);
        }
        int[] affects = getOperator().executeBatchByList(sql, params);
        assertEquals(20, affects.length);
        assertEquals(1, affects[0]);
        String sqlMap = "delete from animal where id=@id";
        List<Map<String, Object>> paramsMap = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < beans.length; i++) {
            Bean bean = beans[i];
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("id", bean.get("id"));
            paramsMap.add(param);
        }
        affects = getOperator().executeBatchByMap(sqlMap, paramsMap);
        assertEquals(20, affects.length);
        assertEquals(1, affects[0]);
        getOperator().execute("delete from animal");
    }

    public void testInsertDiffType() throws TinyDbException {
        List<Bean> beans = getBeanDiffList();
        try {
            getOperator().batchInsert(beans);
            getOperator().batchDelete(beans);
        } catch (Exception e) {
            assertTrue(true);
        }
        assertEquals(2, getOperator().insertBean(beans.toArray(new Bean[0])).length);
        assertEquals(2, getOperator().deleteBean(beans.toArray(new Bean[0])).length);


    }

    private List<Bean> getBeanDiffList() {
        Bean bean = new Bean(ANIMAL);
        bean.setProperty("id", "beanId");
        bean.setProperty("name", "1234");
        bean.setProperty("length", "1234");

        Bean bean2 = new Bean(BRANCH);
        bean2.setProperty("branchId", "branch1");
        bean2.setProperty("branchName", "branchNmae");

        List<Bean> list = new ArrayList<Bean>();
        list.add(bean);
        list.add(bean2);
        return list;
    }

}
