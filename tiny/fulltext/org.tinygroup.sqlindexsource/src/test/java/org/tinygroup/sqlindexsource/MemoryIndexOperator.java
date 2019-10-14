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
package org.tinygroup.sqlindexsource;

import org.tinygroup.fulltext.DocumentFilter;
import org.tinygroup.fulltext.IndexOperator;
import org.tinygroup.fulltext.Pager;
import org.tinygroup.fulltext.SearchRule;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.fulltext.field.Field;

import java.util.*;
import java.util.Map.Entry;

public class MemoryIndexOperator implements IndexOperator {

    private Map<String, Document> results = new HashMap<String, Document>();

    public void createIndex(List<Document> docs) {
        for (Document doc : docs) {
            results.put((String) doc.getId().getValue(), doc);
        }
    }

    public void deleteIndex(List<Field> docIds) {
        for (Field field : docIds) {
            results.remove(field.getValue());
        }
    }

    public Pager<Document> search(String searchCondition, DocumentFilter filter, int start, int limit) {
        List<Document> list = new ArrayList<Document>();
        String[] keys = searchCondition.split(",");
        for (Entry<String, Document> entry : results.entrySet()) {
            boolean tag = isMatch(keys, entry.getValue());
            if (tag) {
                list.add(entry.getValue());
            }
        }
        int total = list.size();
        if (start < total && start > 0) {
            list = list.subList(start, total);
        }
        if (limit < list.size() && limit > 0) {
            list = list.subList(0, limit);
        }

        Pager<Document> page = new Pager<Document>(total, start, limit, list);
        return page;
    }

    private boolean isMatch(String[] keys, Document doc) {
        Iterator<Field> it = doc.iterator();
        while (it.hasNext()) {
            Field field = it.next();
            String content = field.getValue().toString();
            for (String key : keys) {
                if (content.contains(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Pager<Document> search(SearchRule searchRule, DocumentFilter filter, int start, int limit) {
        return null;
    }

    @Override
    public void deleteAllIndexes() {
        results.clear();
    }

}
