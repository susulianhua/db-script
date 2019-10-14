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
package org.tinygroup.template.function;

import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by luoguo on 2014/6/9.
 */
public class ParseTemplateFunction extends AbstractTemplateFunction {

    public ParseTemplateFunction() {
        super("parse");
    }

    public Object execute(Template template, TemplateContext context,
                          Object... parameters) throws TemplateException {
        if (parameters.length == 0 || !(parameters[0] instanceof String)) {
            notSupported(parameters);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String path = parameters[0].toString();
        try {
            if (!path.startsWith("/")) {
                URL url = new URL("file:" + template.getPath());
                URL newUrl = new URL(url, path);
                path = newUrl.getPath();
            }
            template.getTemplateEngine().renderTemplateWithOutLayout(path,
                    context, outputStream);

            return new String(outputStream.toByteArray(), template
                    .getTemplateEngine().getEncode());
        } catch (UnsupportedEncodingException e) {
            throw new TemplateException(e);
        } catch (MalformedURLException e) {
            throw new TemplateException(e);
        }

    }
}
