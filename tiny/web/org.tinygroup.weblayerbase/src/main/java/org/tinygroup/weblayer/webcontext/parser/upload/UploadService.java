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
package org.tinygroup.weblayer.webcontext.parser.upload;

import org.apache.commons.fileupload.FileItem;
import org.tinygroup.weblayer.webcontext.parser.exception.UploadException;

import javax.servlet.http.HttpServletRequest;

/**
 * 用来处理<code>multipart/form-data</code>格式的HTTP POST请求，并将它们转换成form字段或文件。
 *
 * @author renhui
 */
public interface UploadService extends UploadConfiguration {
    /**
     * 判断是否是符合<a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>标准的
     * <code>multipart/form-data</code>类型的HTTP请求。
     *
     * @param request HTTP请求
     * @return 如果是，则返回<code>true</code>
     */
    boolean isMultipartContent(HttpServletRequest request);

    /**
     * 解析符合<a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>标准的
     * <code>multipart/form-data</code>类型的HTTP请求。
     *
     * @param request HTTP请求
     * @return <code>FileItem</code>的列表，按其输入的顺序罗列
     * @throws UploadException 如果解析时出现异常
     */
    FileItem[] parseRequest(HttpServletRequest request);

    /**
     * 解析符合<a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>标准的
     * <code>multipart/form-data</code>类型的HTTP请求。
     * <p>
     * 此方法覆盖了service的默认设置，适合于在action或servlet中手工执行。
     * </p>
     *
     * @param request HTTP请求
     * @param params  upload参数
     * @return <code>FileItem</code>的列表，按其输入的顺序罗列
     * @throws UploadException 如果解析时出现异常
     */
    FileItem[] parseRequest(HttpServletRequest request, UploadParameters params);


}
