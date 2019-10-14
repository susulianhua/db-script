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
package org.tinygroup.template.loader;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.vfs.FileObject;

import java.io.InputStream;

/**
 * Created by luoguo on 2014/6/9.
 */
public class FileObjectResourceLoader extends AbstractResourceLoader<FileObject> {

    private FileResourceManager fileResourceManager;

    public FileObjectResourceLoader() {
        super(null, null, null);
    }

    public FileObjectResourceLoader(String templateExtName, String layoutExtName, String macroLibraryExtName) {
        super(templateExtName, layoutExtName, macroLibraryExtName);
    }

    public FileResourceManager getFileResourceManager() {
        return fileResourceManager;
    }

    public void setFileResourceManager(FileResourceManager fileResourceManager) {
        this.fileResourceManager = fileResourceManager;
    }

    public Template createTemplate(FileObject fileObject) throws TemplateException {
        if (fileObject != null) {
            return loadTemplate(fileObject);
        }
        return null;
    }

    protected Template loadTemplateItem(final String path) throws TemplateException {
        boolean tag = isLayout(path);
        return createTemplate(fileResourceManager.getFileObject(path, tag));
    }


    public boolean isModified(String path) {
        boolean tag = isLayout(path);
        return fileResourceManager.isModified(path, tag);
    }

    public void resetModified(String path) {
        boolean tag = isLayout(path);
        fileResourceManager.resetModified(path, tag);
    }

    public String getResourceContent(String path, String encode) throws TemplateException {
        FileObject fileObject = null;
        if (isLoadResource(path)) {
            boolean tag = isLayout(path);
            fileObject = fileResourceManager.getFileObject(path, tag);
        } else {
            fileObject = fileResourceManager.getOtherFileObject(path);
        }
        if (fileObject != null) {
            InputStream inputStream = fileObject.getInputStream();
            try {
                return IOUtils.readFromInputStream(inputStream, encode);
            } catch (Exception e) {
                throw new TemplateException(e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        throw new TemplateException(e);
                    }
                }
            }
        }
        return null;
    }

    private Template loadTemplate(FileObject fileObject) throws TemplateException {
        try {
            Template templateFromContext = TemplateLoadUtil.loadComponent((TemplateEngineDefault) getTemplateEngine(), fileObject.getPath(), fileObject.getAbsolutePath(), fileObject.getLastModifiedTime(), getResourceContent(fileObject.getPath(), getTemplateEngine().getEncode()));
            addTemplate(templateFromContext);
            return templateFromContext;
        } catch (Exception e) {
            throw new TemplateException(e);
        }
    }

}
