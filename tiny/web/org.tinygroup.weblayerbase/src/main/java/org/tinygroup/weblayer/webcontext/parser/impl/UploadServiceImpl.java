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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.tinygroup.commons.tools.HumanReadableSize;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.support.BeanSupport;
import org.tinygroup.weblayer.webcontext.parser.exception.UploadException;
import org.tinygroup.weblayer.webcontext.parser.exception.UploadSizeLimitExceededException;
import org.tinygroup.weblayer.webcontext.parser.upload.FileUploadReName;
import org.tinygroup.weblayer.webcontext.parser.upload.UploadParameters;
import org.tinygroup.weblayer.webcontext.parser.upload.UploadService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * 这个service可以处理<code>multipart/form-data</code>格式的HTTP
 * POST请求，并将它们转换成form字段或是文件。
 *
 * @author renhui
 */
public class UploadServiceImpl extends BeanSupport implements UploadService {
    private static Logger logger = LoggerFactory
            .getLogger(UploadServiceImpl.class);
    private final UploadParameters params = new UploadParameters();
    private ServletFileUpload fileUpload;
    /**
     * 上传文件重命名接口
     */
    private FileUploadReName rename = new FileUploadRenameImpl();

    public FileUploadReName getRename() {
        return rename;
    }

    public void setRename(FileUploadReName rename) {
        this.rename = rename;
    }

    public File getRepository() {
        return params.getRepository();
    }

    public void setRepository(File repository) {
        params.setRepository(repository);
    }

    public HumanReadableSize getSizeMax() {
        return params.getSizeMax();
    }

    public void setSizeMax(HumanReadableSize sizeMax) {
        params.setSizeMax(sizeMax);
    }

    public HumanReadableSize getFileSizeMax() {
        return params.getFileSizeMax();
    }

    public void setFileSizeMax(HumanReadableSize fileSizeMax) {
        params.setFileSizeMax(fileSizeMax);
    }

    public HumanReadableSize getSizeThreshold() {
        return params.getSizeThreshold();
    }

    public void setSizeThreshold(HumanReadableSize sizeThreshold) {
        params.setSizeThreshold(sizeThreshold);
    }

    public boolean isKeepFormFieldInMemory() {
        return params.isKeepFormFieldInMemory();
    }

    public void setKeepFormFieldInMemory(boolean keepFormFieldInMemory) {
        params.setKeepFormFieldInMemory(keepFormFieldInMemory);
    }

    public boolean isSaveInFile() {
        return params.isSaveInFile();
    }

    public void setSaveInFile(boolean saveInFile) {
        params.setSaveInFile(saveInFile);
    }

    public boolean isDiskItemFactory() {
        return params.isDiskItemFactory();
    }

    public void setDiskItemFactory(boolean isDiskItemFactory) {
        params.setDiskItemFactory(isDiskItemFactory);
    }

    public String getItemStorageBeanName() {
        return params.getItemStorageBeanName();
    }

    public void setItemStorageBeanName(String itemStorageBeanName) {
        params.setItemStorageBeanName(itemStorageBeanName);
    }

    public String[] getFileNameKey() {
        return params.getFileNameKey();
    }

    public void setFileNameKey(String[] fileNameKey) {
        params.setFileNameKey(fileNameKey);
    }

    public boolean isTemporary() {
        return params.isTemporary();
    }

    public void setTemporary(boolean isTemporary) {
        params.setTemporary(isTemporary);
    }

    protected void init() {
        params.applyDefaultValues();
        logger.logMessage(LogLevel.INFO, "Upload Parameters: {}", params);

        fileUpload = getFileUpload(params, false);
    }

    /**
     * 判断是否是符合<a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>标准的
     * <code>multipart/form-data</code>类型的HTTP请求。
     *
     * @param request HTTP请求
     * @return 如果是，则返回<code>true</code>
     */
    public boolean isMultipartContent(HttpServletRequest request) {
        return org.apache.commons.fileupload.servlet.ServletFileUpload
                .isMultipartContent(request);
    }

    /**
     * 解析符合<a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>标准的
     * <code>multipart/form-data</code>类型的HTTP请求。
     *
     * @param request HTTP请求
     * @return <code>FileItem</code>的列表，按其输入的顺序罗列
     * @throws UploadException 如果解析时出现异常
     */
    public FileItem[] parseRequest(HttpServletRequest request) {
        return parseRequest(request, null);
    }

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
    public FileItem[] parseRequest(HttpServletRequest request,
                                   UploadParameters params) {
        assertInitialized();

        ServletFileUpload fileUpload;

        if (params == null || params.equals(this.params)) {
            fileUpload = this.fileUpload;
        } else {
            fileUpload = getFileUpload(params, true);
        }

        List<?> fileItems;

        try {
            fileItems = fileUpload.parseRequest(request);
        } catch (FileUpload.SizeLimitExceededException e) {
            throw new UploadSizeLimitExceededException(e);
        } catch (FileUpload.FileSizeLimitExceededException e) {
            throw new UploadSizeLimitExceededException(e);
        } catch (FileUploadException e) {
            throw new UploadException(e);
        }

        return fileItems.toArray(new FileItem[fileItems.size()]);
    }

    /**
     * 根据参数创建<code>FileUpload</code>对象。
     */
    private ServletFileUpload getFileUpload(UploadParameters params,
                                            boolean applyDefaultValues) {
        if (applyDefaultValues) {
            params.applyDefaultValues();
            logger.logMessage(LogLevel.DEBUG, "Upload Parameters: {}", params);
        }

        FileItemFactory itemFactory = new FileItemFactoryWrapper(params, this);
        // 用于解析multipart request的参数
        ServletFileUpload fileUpload = new ServletFileUpload(itemFactory);

        fileUpload.setSizeMax(params.getSizeMax().getValue());
        fileUpload.setFileSizeMax(params.getFileSizeMax().getValue());
        fileUpload.setFileNameKey(params.getFileNameKey());

        return fileUpload;
    }


}
