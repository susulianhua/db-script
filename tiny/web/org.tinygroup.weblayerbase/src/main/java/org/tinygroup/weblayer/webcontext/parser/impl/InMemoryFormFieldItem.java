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
package org.tinygroup.weblayer.webcontext.parser.impl;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 用来存储form field的<code>FileItem</code>实现。
 * <p>
 * 避免了<code>DiskFileItem.finalize()</code>方法的开销。
 * </p>
 *
 * @author Michael Zhou
 */
public class InMemoryFormFieldItem extends AbstractFileItem {
    private static final long serialVersionUID = -103002370072467461L;

    public InMemoryFormFieldItem(String fieldName, String contentType, boolean isFormField, boolean saveInFile, String fileName,
                                 int sizeThreshold, boolean keepFormFieldInMemory, File repository, HttpServletRequest request) {
        super(fieldName, contentType, isFormField, saveInFile, fileName, sizeThreshold, keepFormFieldInMemory, repository, request);
    }
}
