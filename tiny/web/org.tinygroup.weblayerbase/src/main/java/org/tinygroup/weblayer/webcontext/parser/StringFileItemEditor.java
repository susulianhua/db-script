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
package org.tinygroup.weblayer.webcontext.parser;

import org.apache.commons.fileupload.FileItem;

import java.beans.PropertyEditorSupport;

/**
 * 将<code>FileItem</code>转换成字符串。
 *
 * @author renhui
 */
public class StringFileItemEditor extends PropertyEditorSupport {

    public void setAsText(String text) {
        setValue(text);
    }


    public void setValue(Object value) {
        if (value instanceof FileItem) {
            super.setValue(((FileItem) value).getName());
        } else {
            super.setValue(value);
        }
    }
}
