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
package com.xquant.database.fileresolver;

import com.thoughtworks.xstream.XStream;
import com.xquant.vfs.FileObject;
import com.xquant.fileresolver.FileResolver;
import com.xquant.fileresolver.impl.AbstractFileProcessor;
import com.xquant.xml.XStreamConfiguration;
import com.xquant.xml.XStreamFactory;
import java.io.InputStream;

/**
 * 功能说明:xstream文件处理器.优先级别低于i18n,但是高于其他处理器
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-11-20 <br>
 * <br>
 */
public class XStreamFileProcessor extends AbstractFileProcessor {

    private static final String XSTREAM_FILE_EXTENSION = ".xstream.xml";
    private String xstreamFileExtension = XSTREAM_FILE_EXTENSION;

    public String getXstreamFileExtension() {
        return xstreamFileExtension;
    }

    public void setXstreamFileExtension(String xstreamFileExtension) {
        this.xstreamFileExtension = xstreamFileExtension;
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(XSTREAM_FILE_EXTENSION);
    }

    private void process(FileObject fileObject) {
        try {
            LOGGER.info( "找到XStream配置文件[{0}]，并开始加载...",
                    fileObject.getAbsolutePath());
            XStream loadXStream = XStreamFactory.getXStream();
            InputStream inputStream = fileObject.getInputStream();
            XStreamConfiguration xstreamConfiguration = (XStreamConfiguration) loadXStream
                    .fromXML(inputStream);
            try {
                inputStream.close();
            } catch (Exception e) {
                LOGGER.error("关闭文件流时出错,文件路径:{}"+ e, fileObject.getAbsolutePath());
            }
            XStreamFactory.parse(xstreamConfiguration);

            LOGGER.info( "XStream配置文件[{0}]，加载完毕。",
                    fileObject.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.error(
                    String.format("processing file <%s>",
                            fileObject.getAbsolutePath()), e);
        }
    }


    public void process() {
        for (FileObject fileObject : fileObjects) {
            process(fileObject);
        }

    }

    public void setFileResolver(FileResolver fileResolver) {
        //do nothing
    }


    public int getOrder() {
        return HIGHEST_PRECEDENCE + 20;
    }


}
