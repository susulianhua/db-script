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
package org.tinygroup.httpvisitor.struct;

import org.tinygroup.httpvisitor.BodyElement;
import org.tinygroup.httpvisitor.BodyElementMode;

/**
 * 正文分段的抽象类
 *
 * @author yancheng11334
 */
public abstract class AbstractBodyElement implements BodyElement {

    BodyElementMode type;
    private String name;
    private Object element;
    private String contentType;
    private String charset;

    public AbstractBodyElement(Object element) {
        this(null, element, null, null);
    }

    public AbstractBodyElement(Object element, String charset) {
        this(null, element, null, charset);
    }

    public AbstractBodyElement(Object element, String contentType,
                               String charset) {
        this(null, element, contentType, charset);
    }

    public AbstractBodyElement(String name, Object element, String contentType,
                               String charset) {
        super();
        this.name = name;
        this.element = element;
        this.contentType = contentType;
        this.charset = charset;
    }

    public String getName() {
        return name;
    }

    public Object getElement() {
        return element;
    }

    public String getContentType() {
        return contentType;
    }

    public String getCharset() {
        return charset;
    }

}
