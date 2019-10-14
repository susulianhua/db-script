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
package org.tinygroup.weblayer.webcontext.parser.impl;

import org.apache.commons.fileupload.*;
import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.weblayer.webcontext.parser.fileupload.TinyFileItem;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 继承自commons-fileupload-1.2.1的同名类，改进了如下内容：
 * <ul>
 * <li>使用新的<code>ServletRequestContext</code>,以便在处理字符集编码时，和servlet规范相容。详见
 * {@link ServletRequestContext}。</li>
 * </ul>
 *
 * @author Michael Zhou
 */
public class ServletFileUpload extends org.apache.commons.fileupload.servlet.ServletFileUpload {
    private String fileNameKey[];

    public ServletFileUpload() {
        super();
    }

    public ServletFileUpload(FileItemFactory fileItemFactory) {
        super(fileItemFactory);
    }

    public String[] getFileNameKey() {
        if (ArrayUtil.isEmptyArray(fileNameKey)) {
            fileNameKey = new String[]{"filename"};
        }

        return fileNameKey;
    }

    public void setFileNameKey(String[] fileNameKey) {
        this.fileNameKey = fileNameKey;
    }


    public List<?/* FileItem */> parseRequest(HttpServletRequest request) throws FileUploadException {
        return parseRequest(new ServletRequestContext(request));
    }


    public List<?/* FileItem */> parseRequest(RequestContext ctx) throws FileUploadException {
        FileItemFactory itemFactory = getFileItemFactory();
        FileItemFactoryWrapper wrapper = (FileItemFactoryWrapper) itemFactory;
        FileItemFactory itemWrapperFactory = wrapper.getFileItemFactory();
        if (itemWrapperFactory instanceof DiskFileItemFactory) {//绑定此次请求对象
            DiskFileItemFactory factory = (DiskFileItemFactory) itemWrapperFactory;
            factory.setRequest(((ServletRequestContext) ctx).getRequest());
        }
        @SuppressWarnings("unchecked")
        List<FileItem> items = super.parseRequest(ctx);
        String charset = ctx.getCharacterEncoding();
        for (FileItem fileItem : items) {
            if (fileItem instanceof AbstractFileItem) {
                ((AbstractFileItem) fileItem).setCharset(charset);
            }
            if (fileItem instanceof TinyFileItem) {
                TinyFileItem tinyFileItem = (TinyFileItem) fileItem;
                tinyFileItem.setCharset(charset);
                tinyFileItem.storage();//存储
            }
        }
        return items;
    }


    @SuppressWarnings({"rawtypes", "deprecation"})
    protected String getFileName(Map /* String, String */headers) {
        return getFileName(getHeader(headers, CONTENT_DISPOSITION));
    }


    protected String getFileName(FileItemHeaders headers) {
        return getFileName(headers.getHeader(CONTENT_DISPOSITION));
    }

    private String getFileName(String pContentDisposition) {
        String fileName = null;

        if (pContentDisposition != null) {
            String cdl = pContentDisposition.toLowerCase();

            if (cdl.startsWith(FORM_DATA) || cdl.startsWith(ATTACHMENT)) {
                ParameterParser parser = new ParameterParser();
                parser.setLowerCaseNames(true);

                // Parameter parser can handle null input
                @SuppressWarnings("unchecked")
                Map<String, String> params = parser.parse(pContentDisposition, ';');

                // 解决类似Flash上传更改了filename 为 fname 的多客户端支持
                boolean isFileFiled = false;
                for (String key : getFileNameKey()) {
                    if (params.containsKey(key)) {
                        isFileFiled = true;
                    }
                    fileName = StringUtil.trimToNull(params.get(key));

                    if (fileName != null) {
                        break;
                    }
                }
                if (isFileFiled && fileName == null) {
                    // Even if there is no value, the parameter is present,
                    // so we return an empty file name rather than no file
                    fileName = "";
                }
            }
        }

        return fileName;
    }


    public FileItemIterator getItemIterator(HttpServletRequest request) throws FileUploadException, IOException {
        return super.getItemIterator(new ServletRequestContext(request));
    }
}
