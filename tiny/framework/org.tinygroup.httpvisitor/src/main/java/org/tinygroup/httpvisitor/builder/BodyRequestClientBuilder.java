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
package org.tinygroup.httpvisitor.builder;

import org.tinygroup.httpvisitor.BodyElement;
import org.tinygroup.httpvisitor.MethodMode;
import org.tinygroup.httpvisitor.client.ClientBuilder;
import org.tinygroup.httpvisitor.client.SingleClientBuilder;
import org.tinygroup.httpvisitor.request.BodyRequestBuilder;
import org.tinygroup.httpvisitor.request.BodyRequestBuilderInterface;
import org.tinygroup.httpvisitor.request.HttpRequestBuilder;

import java.io.File;
import java.io.InputStream;

public class BodyRequestClientBuilder extends RequestClientBuilder<BodyRequestClientBuilder, BodyRequestBuilder> implements BodyRequestBuilderInterface<BodyRequestClientBuilder> {

    private SingleClientBuilder singleClientBuilder;
    private BodyRequestBuilder requestBuilder;

    /**
     * 通过工厂构造
     *
     * @param methodMode
     * @param url
     */
    BodyRequestClientBuilder(MethodMode methodMode, String url, String templateId) {
        requestBuilder = new BodyRequestBuilder(methodMode, url);
        singleClientBuilder = new SingleClientBuilder(templateId);
    }

    protected BodyRequestClientBuilder self() {
        return this;
    }

    protected ClientBuilder findClientBuilder() {
        return singleClientBuilder;
    }

    protected HttpRequestBuilder<BodyRequestBuilder> findRequsetBuilder() {
        return requestBuilder;
    }

    public BodyRequestClientBuilder data(byte[] data) {
        requestBuilder.data(data);
        return self();
    }

    public BodyRequestClientBuilder data(InputStream in) {
        requestBuilder.data(in);
        return self();
    }

    public BodyRequestClientBuilder data(String body) {
        requestBuilder.data(body);
        return self();
    }

    public BodyRequestClientBuilder data(File file) {
        requestBuilder.data(file);
        return self();
    }

    public BodyRequestClientBuilder data(BodyElement element) {
        requestBuilder.data(element);
        return self();
    }

}
