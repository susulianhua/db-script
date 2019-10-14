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

import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.i18n.I18nMessageFactory;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * 功能说明: i18n文件处理器,优先级别最高
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-11-20 <br>
 * <br>
 */
public class I18nFileProcessor extends AbstractFileProcessor {
    private static final String PROPERTIES_FILE_EXTENSION = ".properties";
    private static final String CUSTOMIZE_PROPERTIES_FILE_EXTENSION = ".cproperties";
    private static final String I18N_FOLDER_NAME = "i18n";
    private String i18nFolderName = I18N_FOLDER_NAME;

    public String getI18nFolderName() {
        return i18nFolderName;
    }

    public void setI18nFolderName(String i18nFolderName) {
        this.i18nFolderName = i18nFolderName;
    }

    protected boolean checkMatch(FileObject fileObject) {
        return (fileObject.getFileName()
                .endsWith(PROPERTIES_FILE_EXTENSION) || fileObject.getFileName()
                .endsWith(CUSTOMIZE_PROPERTIES_FILE_EXTENSION))
                && fileObject.getParent().getFileName()
                .equals(I18N_FOLDER_NAME);
    }

    private void process(FileObject fileObject) {
        LOGGER.logMessage(LogLevel.INFO, "找到国际化资源配置文件[{0}]，并开始加载...",
                fileObject.getAbsolutePath());
        Locale locale = getLocale(fileObject);
        Properties oldProperties = (Properties) caches.get(fileObject.getAbsolutePath());
        if (oldProperties != null) {
            if (fileObject.getFileName().endsWith(PROPERTIES_FILE_EXTENSION)) {
                I18nMessageFactory.removeResource(locale, oldProperties);
            } else {
                I18nMessageFactory.removeCustomizeResource(locale, oldProperties);
            }
        }
        try {
            Properties properties = convertXml(fileObject);
            if (fileObject.getFileName().endsWith(PROPERTIES_FILE_EXTENSION)) {
                I18nMessageFactory.addResource(locale, properties);
            } else {
                I18nMessageFactory.addCustomizeResource(locale, properties);
            }
            caches.put(fileObject.getAbsolutePath(), properties);
            LOGGER.logMessage(LogLevel.INFO, "国际化资源配置文件[{0}]，加载完毕。",
                    fileObject.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.errorMessage("载入资源文件[{}]出现异常！", e, fileObject.getAbsolutePath());
        }
    }

    private Properties convertXml(FileObject fileObject) {
        InputStream inputStream = null;
        try {
            Properties properties = new Properties();
            inputStream = fileObject.getInputStream();
            properties.load(inputStream);
            return properties;
        } catch (Exception e) {
            LOGGER.errorMessage("载入资源文件[{0}]出现异常", e, fileObject.getAbsolutePath());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    LOGGER.errorMessage("关闭文件流时出错,文件路径:{0}", e, fileObject.getAbsolutePath());
                }
            }
        }
        return null;
    }

    private Locale getLocale(FileObject fileObject) {
        String baseName = fileObject.getFileName();
        int start = baseName.indexOf("_") + 1;
        int end = baseName.length() - PROPERTIES_FILE_EXTENSION.length();
        String localeStr = null;
        if (start > 0) {
            localeStr = baseName.substring(start, end);
        }
        Locale locale = LocaleUtil.getContext().getLocale();
        if (localeStr != null && localeStr.length() > 0) {
            String[] loc = localeStr.split("_");
            if (loc.length >= 2) {
                //取最后两位
                locale = new Locale(loc[loc.length - 2], loc[loc.length - 1]);
            } else {
                locale = new Locale(loc[0]);
            }
        }
        return locale;
    }

    public void process() {

        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "开始移除国际化资源配置文件[{0}]",
                    fileObject.getAbsolutePath());
            Locale locale = getLocale(fileObject);
            Properties properties = (Properties) caches.get(fileObject.getAbsolutePath());
            if (properties != null) {
                if (fileObject.getFileName().endsWith(PROPERTIES_FILE_EXTENSION)) {
                    I18nMessageFactory.removeResource(locale, properties);
                } else {
                    I18nMessageFactory.removeCustomizeResource(locale, properties);
                }
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除国际化资源配置文件[{0}],完成",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            process(fileObject);
        }
    }

    public void setFileResolver(FileResolver fileResolver) {
        //do nothing
    }


    public int getOrder() {
        return HIGHEST_PRECEDENCE + 10;
    }


}
