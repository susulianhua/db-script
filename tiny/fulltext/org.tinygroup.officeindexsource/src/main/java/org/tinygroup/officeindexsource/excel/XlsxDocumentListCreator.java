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
package org.tinygroup.officeindexsource.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.tinygroup.fulltext.DocumentListCreator;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.vfs.FileObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 对应office2007的excel
 *
 * @author yancheng11334
 */
public class XlsxDocumentListCreator extends AbstractExcelIndexSource implements DocumentListCreator<FileObject> {

    public boolean isMatch(FileObject data) {
        return !data.isFolder() && data.getExtName().equals("xlsx");
    }

    public List<Document> getDocument(String type, FileObject data,
                                      Object... arguments) {
        return this.getDocument(type, data.getAbsolutePath(),
                data.getInputStream());
    }

    protected Workbook createWorkbook(InputStream input) throws IOException {
        return new XSSFWorkbook(input);
    }

}
