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
package org.tinygroup.fulltext.impl;

import org.tinygroup.fulltext.DocumentListCreator;
import org.tinygroup.fulltext.document.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 支持集合类型的对象操作
 *
 * @author yancheng11334
 */
public class CollectionDocumentCreator implements DocumentListCreator<Collection<Document>> {

    public boolean isMatch(Collection<Document> data) {
        return !(data == null || data.isEmpty());
    }

    public List<Document> getDocument(String type, Collection<Document> data,
                                      Object... arguments) {
        return new ArrayList<Document>(data);
    }


}
