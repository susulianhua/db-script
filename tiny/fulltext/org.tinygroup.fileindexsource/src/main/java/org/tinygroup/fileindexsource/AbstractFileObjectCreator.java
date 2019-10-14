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
package org.tinygroup.fileindexsource;

import org.tinygroup.fileindexsource.impl.DefaultFileObjectFilter;
import org.tinygroup.fulltext.DocumentCreator;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.fulltext.impl.AbstractCreator;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectFilter;
import org.tinygroup.vfs.FileObjectProcessor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFileObjectCreator extends AbstractCreator {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Document> getDocument(final String type, final FileObject data,
                                      final Object... arguments) {

        FileObjectFilter filter = new DefaultFileObjectFilter();

        if (arguments != null && arguments.length >= 1) {
            // 设置用户的规则
            filter = (FileObjectFilter) arguments[0];
        }
        final List<Document> docs = new ArrayList<Document>();
        data.foreach(filter, new FileObjectProcessor() {

            public void process(FileObject fileObject) {

                for (DocumentCreator creator : getDocumentCreators()) {
                    if (creator.isMatch(fileObject)) {
                        Document doc = creator.getDocument(type, fileObject, arguments);
                        docs.add(doc);
                    }
                }
            }
        });
        return docs;
    }
}
