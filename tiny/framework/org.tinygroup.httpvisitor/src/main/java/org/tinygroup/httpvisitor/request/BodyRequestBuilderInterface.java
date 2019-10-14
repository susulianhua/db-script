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
package org.tinygroup.httpvisitor.request;

import org.tinygroup.httpvisitor.BodyElement;

import java.io.File;
import java.io.InputStream;


/**
 * 包含Body的HTTP链式构造器接口
 *
 * @param <T>
 * @author yancheng11334
 */
public interface BodyRequestBuilderInterface<T> extends HttpRequestBuilderInterface<T> {

    /**
     * Set http body data for Post/Put request
     *
     * @param data the data to post
     */
    T data(byte[] data);

    /**
     * Set http data from inputStream for Post/Put request
     */
    T data(InputStream in);

    /**
     * Set http data with text.
     * The text string will be encoded, default using utf-8, set charset with charset(Charset charset) method
     */
    T data(String body);

    /**
     * Set http data from file for Post/Put request
     */
    T data(File file);

    /**
     * Set http data from warpper object for Post/Put request
     */
    T data(BodyElement element);
}
