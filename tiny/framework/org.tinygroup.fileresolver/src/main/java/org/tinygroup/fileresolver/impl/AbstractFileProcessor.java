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
package org.tinygroup.fileresolver.impl;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.FileProcessor;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件处理器的抽象实现
 *
 * @author renhui
 */
public abstract class AbstractFileProcessor implements FileProcessor {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractFileProcessor.class);
    protected List<FileObject> fileObjects = new ArrayList<FileObject>();
    protected List<FileObject> changeList = new ArrayList<FileObject>();
    protected List<FileObject> deleteList = new ArrayList<FileObject>();
    protected FileResolver fileResolver;

    protected XmlNode applicationConfig;

    protected XmlNode componentConfig;

    protected Map<String, Object> caches = new HashMap<String, Object>();

    public boolean isMatch(FileObject fileObject) {
        if (fileObject.isFolder()) {
            return false;
        }
        return checkMatch(fileObject);
    }

    protected abstract boolean checkMatch(FileObject fileObject);

    public FileResolver getFileResolver() {
        return fileResolver;
    }

    public void setFileResolver(FileResolver fileResolver) {
        this.fileResolver = fileResolver;
    }

    public void add(FileObject fileObject) {
        fileObjects.add(fileObject);
        changeList.add(fileObject);
    }

    public void noChange(FileObject fileObject) {

    }

    public void modify(FileObject fileObject) {
        fileObjects.add(fileObject);
        changeList.add(fileObject);
    }

    public void delete(FileObject fileObject) {
        fileObjects.remove(fileObject);
        deleteList.add(fileObject);
    }

    public void clean() {
        fileObjects.clear();
        changeList.clear();
        deleteList.clear();
    }

    public boolean supportRefresh() {
        return true;
    }

    public String getApplicationNodePath() {
        return null;
    }

    public String getComponentConfigPath() {
        return null;
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        this.applicationConfig = applicationConfig;
        this.componentConfig = componentConfig;
    }

    public XmlNode getComponentConfig() {
        return componentConfig;
    }

    public XmlNode getApplicationConfig() {
        return applicationConfig;
    }

    public int getOrder() {
        return DEFAULT_PRECEDENCE;
    }

    /**
     * 转换XML为Xstream对象
     *
     * @param <T>
     * @param stream
     * @param fileObject
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> T convertFromXml(XStream stream, FileObject fileObject) {
        InputStream inputStream = null;
        try {
            inputStream = fileObject.getInputStream();
            return (T) stream.fromXML(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("转换XML文件成Xstream对象发生异常,路径:" + fileObject.getAbsolutePath(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    LOGGER.error("关闭文件流时出错,文件路径:{}", e, fileObject.getAbsolutePath());
                }
            }
        }
    }
}
