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
package org.tinygroup.dict.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.dict.DictLoader;
import org.tinygroup.dict.DictManager;
import org.tinygroup.dict.config.DictLoaderConfig;
import org.tinygroup.dict.config.DictLoaderConfigs;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;

/**
 * 字典加载器配置的文件搜索处理器
 *
 * @author renhui
 */
public class DictLoadFileProcessor extends AbstractFileProcessor {

    private static final String DICT_LOAND_EXT_NAME = ".dictloader.xml";
    private DictManager manager;

    public DictManager getManager() {
        return manager;
    }

    public void setManager(DictManager manager) {
        this.manager = manager;
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(DICT_LOAND_EXT_NAME);
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(DictManager.XSTEAM_PACKAGE_NAME);
        LOGGER.logMessage(LogLevel.INFO, "字典加载器配置文件处理开始");
        for (FileObject fileObject : fileObjects) {
            LOGGER.logMessage(LogLevel.INFO, "找到字典加载配置文件:[{}]",
                    fileObject.getAbsolutePath());
            InputStream inputStream = fileObject.getInputStream();
            DictLoaderConfigs configs = (DictLoaderConfigs) stream
                    .fromXML(inputStream);
            try {
                inputStream.close();
            } catch (Exception e) {
                LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e, fileObject.getAbsolutePath());
            }
            for (DictLoaderConfig config : configs.getConfigs()) {
                DictLoader dictLoader = BeanContainerFactory.getBeanContainer(
                        this.getClass().getClassLoader()).getBean(
                        config.getBeanName());
                dictLoader.setGroupName(config.getGroupName());
                dictLoader.setLanguage(config.getLanguage());
                manager.addDictLoader(dictLoader);
            }
        }
        // manager.load();
        LOGGER.logMessage(LogLevel.INFO, "字典加载器配置文件处理结束");
    }

    public void setFileResolver(FileResolver fileResolver) {
        //do nothing
    }

}
