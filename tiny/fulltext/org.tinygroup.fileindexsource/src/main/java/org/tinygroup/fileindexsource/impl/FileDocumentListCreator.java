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
package org.tinygroup.fileindexsource.impl;

import org.tinygroup.fileindexsource.AbstractFileObjectCreator;
import org.tinygroup.fulltext.DocumentListCreator;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import java.io.File;
import java.util.List;

/**
 * 抽象的文件目录处理器
 *
 * @author yancheng11334
 */
public class FileDocumentListCreator extends AbstractFileObjectCreator implements
        DocumentListCreator<File> {

    public boolean isMatch(File data) {
        return data instanceof File;
    }

    public List<Document> getDocument(final String type, final File file,
                                      final Object... arguments) {
        final FileObject data = VFS.resolveFile(file.getAbsolutePath());
        return super.getDocument(type, data, arguments);
    }

}
