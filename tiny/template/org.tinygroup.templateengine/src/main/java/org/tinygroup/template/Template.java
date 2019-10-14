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
package org.tinygroup.template;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 模板
 * Created by luoguo on 2014/6/4.
 */
public interface Template extends TemplateContextOperator, Updatable {
    /**
     * 返回宏的内容
     *
     * @return
     */
    Map<String, Macro> getMacroMap();

    /**
     * 返回宏文件中引入模板的顺序
     *
     * @return
     */
    List<String> getImportPathList();

    void addImport(Object importPath);

    /**
     * 进行渲染
     *
     * @param outputStream
     */
    void render(TemplateContext context, OutputStream outputStream) throws TemplateException;

    void render(TemplateContext context) throws TemplateException;

    void render() throws TemplateException;

    /**
     * 返回模板对应的路径
     *
     * @return
     */
    String getPath();

    /**
     * 返回模板引擎
     *
     * @return
     */
    TemplateEngine getTemplateEngine();

    /**
     * 设置对应的模板引擎
     *
     * @param templateEngine
     */
    void setTemplateEngine(TemplateEngine templateEngine);
}
