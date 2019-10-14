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

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.httpvisitor.*;
import org.tinygroup.httpvisitor.struct.*;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 支持Head、Body的请求构建
 *
 * @author yancheng11334
 */
public class BodyRequestBuilder extends HttpRequestBuilder<BodyRequestBuilder> implements BodyRequestBuilderInterface<BodyRequestBuilder> {

    public BodyRequestBuilder(MethodMode methodMode, String url) {
        super(methodMode, url);
    }

    public Request execute() {
        List<Header> hs = CollectionUtil.createArrayList(headers.values());
        List<Cookie> cs = CollectionUtil.createArrayList(cookies.values());
        List<Parameter> ps = CollectionUtil.createArrayList(parameters.values());
        return new Request(methodMode, url, hs, cs, ps, bodyElements, charset);
    }

    protected BodyRequestBuilder self() {
        return this;
    }

    public BodyRequestBuilder data(byte[] data) {
        bodyElements.add(new ByteArrayElement(data));
        return self();
    }

    public BodyRequestBuilder data(InputStream in) {
        bodyElements.add(new InputStreamElement(in));
        return self();
    }

    public BodyRequestBuilder data(String body) {
        bodyElements.add(new StringElement(body));
        return self();
    }

    public BodyRequestBuilder data(File file) {
        bodyElements.add(new FileElement(file));
        return self();
    }

    public BodyRequestBuilder data(BodyElement element) {
        bodyElements.add(element);
        return self();
    }

}
