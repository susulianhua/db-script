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
import org.tinygroup.httpvisitor.request.HttpRequestBuilder;
import org.tinygroup.httpvisitor.request.PostRequestBuilder;
import org.tinygroup.httpvisitor.request.PostRequestBuilderInterface;

import java.io.File;
import java.io.InputStream;

public class PostRequestClientBuilder extends RequestClientBuilder<PostRequestClientBuilder, PostRequestBuilder> implements PostRequestBuilderInterface<PostRequestClientBuilder> {

    private SingleClientBuilder singleClientBuilder;
    private PostRequestBuilder requestBuilder;

    /**
     * 通过工厂创建
     *
     * @param methodMode
     * @param url
     */
    PostRequestClientBuilder(MethodMode methodMode, String url, String templateId) {
        requestBuilder = new PostRequestBuilder(methodMode, url);
        singleClientBuilder = new SingleClientBuilder(templateId);
    }

    public PostRequestClientBuilder data(byte[] data) {
        requestBuilder.data(data);
        return self();
    }

    public PostRequestClientBuilder data(InputStream in) {
        requestBuilder.data(in);
        return self();
    }

    public PostRequestClientBuilder data(String body) {
        requestBuilder.data(body);
        return self();
    }

    public PostRequestClientBuilder data(File file) {
        requestBuilder.data(file);
        return self();
    }

    public PostRequestClientBuilder data(BodyElement element) {
        requestBuilder.data(element);
        return self();
    }


    public PostRequestClientBuilder multipart(BodyElement... elements) {
        requestBuilder.multipart(elements);
        return self();
    }

    protected PostRequestClientBuilder self() {
        return this;
    }

    protected ClientBuilder findClientBuilder() {
        return singleClientBuilder;
    }

    protected HttpRequestBuilder<PostRequestBuilder> findRequsetBuilder() {
        return requestBuilder;
    }


}
