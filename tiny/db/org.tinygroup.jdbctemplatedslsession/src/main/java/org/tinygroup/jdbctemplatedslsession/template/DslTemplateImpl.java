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
package org.tinygroup.jdbctemplatedslsession.template;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.jdbctemplatedslsession.callback.*;
import org.tinygroup.tinysqldsl.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * dsl模板实现类
 *
 * @author renhui
 */
public class DslTemplateImpl extends DslAccessor implements DslTemplate {

    public DslTemplateImpl() {
        super();
    }

    public DslTemplateImpl(DslSession dslSession) {
        setDslSession(dslSession);
        afterPropertiesSet();
    }

    public <T> T insert(T t, InsertGenerateCallback<T> callback) {
        Insert insert = callback.generate(t);
        dslSession.execute(insert);
        return t;
    }

    @SuppressWarnings("unchecked")
    public <T> T insertAndReturnKey(T t,
                                    InsertGenerateCallback<T> callback) {
        Insert insert = callback.generate(t);
        return (T) dslSession.executeAndReturnObject(insert, t.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> T insertAndReturnKey(boolean autoGeneratedKeys, T t,
                                    InsertGenerateCallback<T> callback) {
        Insert insert = callback.generate(t);
        return (T) dslSession.executeAndReturnObject(insert, t.getClass(), autoGeneratedKeys);
    }

    public <T> int update(T t, UpdateGenerateCallback<T> callback, boolean ignore) {
        Update update = callback.generate(t);
        return dslSession.execute(update, ignore);
    }

    public <T> int update(T t, UpdateGenerateCallback<T> callback) {
        return update(t, callback, true);
    }

    public int deleteByKey(Serializable pk, DeleteGenerateCallback<Serializable> callback) {
        Delete delete = callback.generate(pk);
        return dslSession.execute(delete);
    }

    @SuppressWarnings("rawtypes")
    public <T> T getByKey(Serializable pk, Class<T> requiredType, SelectGenerateCallback<Serializable> callback) {
        Select select = callback.generate(pk);
        return dslSession.fetchOneResult(select, requiredType);
    }

    public int deleteByKeys(DeleteGenerateCallback<Serializable[]> callback,
                            Serializable... pks) {
        Delete delete = callback.generate(pks);
        return dslSession.execute(delete);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> List<T> query(T t, SelectGenerateCallback<T> callback) {
        Select select = callback.generate(t);
        Class<T> requiredType = (Class<T>) t.getClass();
        return dslSession.fetchList(select, requiredType);
    }

    @SuppressWarnings({"rawtypes"})
    public <T> Pager<T> queryPager(int start, int limit, T t, boolean isCursor,
                                   SelectGenerateCallback<T> callback) {
        Select select = callback.generate(t);
        Class<T> requiredType = requiredType(t);
        return dslSession.fetchPage(select, start, limit, false, requiredType);
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> requiredType(T t) {
        Class<T> requiredType = (Class<T>) t.getClass();
        return requiredType;
    }

    public <T> int[] batchInsert(List<T> objs, NoParamInsertGenerateCallback callback) {
        if (CollectionUtil.isEmpty(objs)) {
            return new int[0];
        }
        Insert insert = callback.generate();
        boolean autoGeneratedKeys = insert.getContext().getTable().isAutoGeneratedKeys();
        Class<T> requiredType = requiredType(objs.get(0));
        return dslSession.batchInsert(insert, requiredType, objs, Integer.MAX_VALUE, autoGeneratedKeys);
    }

    public <T> int[] batchInsert(boolean autoGeneratedKeys, List<T> objs,
                                 NoParamInsertGenerateCallback callback) {
        if (CollectionUtil.isEmpty(objs)) {
            return new int[0];
        }
        Insert insert = callback.generate();
        Class<T> requiredType = requiredType(objs.get(0));
        return dslSession.batchInsert(insert, requiredType, objs, Integer.MAX_VALUE, autoGeneratedKeys);
    }

    public <T> int[] batchUpdate(List<T> objs, NoParamUpdateGenerateCallback callback) {
        if (CollectionUtil.isEmpty(objs)) {
            return new int[0];
        }
        Update update = callback.generate();
        Class<T> requiredType = requiredType(objs.get(0));
        return dslSession.batchUpdate(update, requiredType, objs);
    }

    public <T> int[] batchDelete(List<T> objs, NoParamDeleteGenerateCallback callback) {
        if (CollectionUtil.isEmpty(objs)) {
            return new int[0];
        }
        Delete delete = callback.generate();
        Class<T> requiredType = requiredType(objs.get(0));
        return dslSession.batchDelete(delete, requiredType, objs);
    }


    public int delete(NoParamDeleteGenerateCallback callback) {
        Delete delete = callback.generate();
        return dslSession.execute(delete);
    }

    public <T> int[] batchInsert(boolean autoGeneratedKeys, List<T> objects,
                                 InsertGenerateCallback<T> callback) {
        List<Insert> inserts = new ArrayList<Insert>();
        for (T object : objects) {
            inserts.add(callback.generate(object));
        }
        return dslSession.batchInsert(inserts, autoGeneratedKeys);
    }

    public <T> int[] batchUpdate(List<T> objects,
                                 UpdateGenerateCallback<T> callback) {
        List<Update> updates = new ArrayList<Update>();
        for (T object : objects) {
            updates.add(callback.generate(object));
        }
        return dslSession.batchUpdate(updates);
    }

    public <T> int[] batchDelete(List<T> objects,
                                 DeleteGenerateCallback<T> callback) {
        List<Delete> deletes = new ArrayList<Delete>();
        for (T object : objects) {
            deletes.add(callback.generate(object));
        }
        return dslSession.batchDelete(deletes);
    }


}
