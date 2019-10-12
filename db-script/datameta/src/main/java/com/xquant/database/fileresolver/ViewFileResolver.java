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
import com.xquant.database.config.view.Views;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.database.view.ViewProcessor;
import com.xquant.file.FileObject;
import com.xquant.fileresolver.impl.AbstractFileProcessor;
import com.xquant.xml.XStreamFactory;

public class ViewFileResolver extends AbstractFileProcessor {

    private static final String VIEW_EXTFILENAME = ".view.xml";
    ViewProcessor viewProcessor;

    public ViewProcessor getViewProcessor() {
        return viewProcessor;
    }

    public void setViewProcessor(ViewProcessor viewProcessor) {
        this.viewProcessor = viewProcessor;
    }


    public void process() {
        XStream stream = XStreamFactory
                .getXStream(DataBaseUtil.DATABASE_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.info("正在移除view文件[{0}]",
                    fileObject.getAbsolutePath());
            Views views = (Views) caches.get(fileObject.getAbsolutePath());
            if (views != null) {
                viewProcessor.removeViews(views);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.info( "移除view文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.info( "正在加载view文件[{0}]",
                    fileObject.getAbsolutePath());
            Views oldViews = (Views) caches.get(fileObject.getAbsolutePath());
            if (oldViews != null) {
                viewProcessor.removeViews(oldViews);
            }
            Views views = convertFromXml(stream, fileObject);
            viewProcessor.addViews(views);
            caches.put(fileObject.getAbsolutePath(), views);
            viewProcessor.registerModifiedTime(views, fileObject.getLastModifiedTime());
            LOGGER.info( "加载view文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        viewProcessor.dependencyInit();
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(VIEW_EXTFILENAME) || fileObject.getFileName().endsWith(".view");
    }

}
