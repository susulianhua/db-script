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
package org.tinygroup.docgen;

import org.tinygroup.context.Context;
import org.tinygroup.vfs.FileObject;

import java.io.File;
import java.io.OutputStream;

/**
 * 功能说明: 文档生成器
 * <p>
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-7-25 <br>
 * <br>
 */
public interface DocumentGenerater<T> {
    String DOCUMENT_GENERATE_BEAN_NAME = "documentGenerater";

    /**
     * 根据tinyTemplate模板文件生成文件
     *
     * @param fileObject   tinyTemplate模板文件
     * @param context      默认文件需要用到的上下文环境
     * @param outputStream 输出的流
     */
    void generate(FileObject fileObject, Context context, OutputStream outputStream);

    /**
     * 根据tinyTemplate模板文件生成文件
     *
     * @param path         tinyTemplate模板文件
     * @param context      默认文件需要用到的上下文环境
     * @param outputStream 输出的流
     */
    void generate(String path, Context context, OutputStream outputStream);

    /**
     * 根据tinyTemplate模板文件生成文件
     *
     * @param path       tinyTemplate模板文件
     * @param context    默认文件需要用到的上下文环境
     * @param outputFile 输出文件
     */
    void generate(String path, Context context, File outputFile);

    /**
     * 增加宏文件模板
     *
     * @param fileObject 宏模板文件
     */
    void addMacroFile(FileObject fileObject);

    /**
     * 移除宏文件模板
     *
     * @param fileObject 宏模板文件
     */
    void removeMacroFile(FileObject fileObject);

    /**
     * 获得模板引擎执行器
     *
     * @return
     */
    T getTemplateGenerater();

    /**
     * 设置模板引擎执行器
     *
     * @param generater
     */
    void setTemplateGenerater(T generater);

    /**
     * 转化输入的字符串
     *
     * @param context
     * @param inputString
     */
    String evaluteString(Context context, String inputString);
}
