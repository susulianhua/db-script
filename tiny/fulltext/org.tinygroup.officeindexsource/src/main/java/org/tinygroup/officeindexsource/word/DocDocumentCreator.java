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
package org.tinygroup.officeindexsource.word;

import org.apache.poi.hwpf.HWPFDocument;
import org.tinygroup.fulltext.DocumentCreator;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.vfs.FileObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * 对应office2003的word
 *
 * @author yancheng11334
 */
public class DocDocumentCreator extends AbstractWordIndexSource implements DocumentCreator<FileObject> {

    public boolean isMatch(FileObject data) {
        return !data.isFolder() && data.getExtName().equals("doc");
    }

    public Document getDocument(String type, FileObject data,
                                Object... arguments) {
        return getDocument(type, data);
    }

    protected String readWordText(InputStream input) throws IOException {
        HWPFDocument doc = new HWPFDocument(input);
        return doc.getDocumentText().trim();
    }

}
