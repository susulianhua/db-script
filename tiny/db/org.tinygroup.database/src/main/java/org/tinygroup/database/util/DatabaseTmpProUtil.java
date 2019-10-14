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
package org.tinygroup.database.util;

import java.io.*;
import java.util.Properties;

/**
 * Created by wangwy11342 on 2016/8/8.
 */
public class DatabaseTmpProUtil {

    protected static final String TEMP_DIR = System.getProperty("user.home");

    protected static final String USER_DIR = new File(System.getProperty("user.dir")).getName();

    /**
     * 保存属性文件信息
     *
     * @param proFile
     * @param tmpFileProperties 属性临时文件
     * @param newProperties     属性增量
     * @param title
     */
    public static void saveTmpFileProperties(File proFile, Properties tmpFileProperties, Properties newProperties, String title) {
        if (newProperties == null || newProperties.size() == 0) {
            return;
        }
        tmpFileProperties.putAll(newProperties);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(proFile);
            tmpFileProperties.store(outputStream, title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static Properties loadFileProperties(File proFile) {
        Properties fileProperties = new Properties();
        if (!proFile.exists()) {
            try {
                proFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(proFile);
            fileProperties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return fileProperties;
    }
}
