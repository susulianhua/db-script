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
package org.tinygroup.annotation.fileresolver;

import org.tinygroup.annotation.AnnotationExecuteManager;
import org.tinygroup.fileresolver.ProcessorCallBack;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.fileresolver.impl.MultiThreadFileProcessor;
import org.tinygroup.vfs.FileObject;

/**
 * 类文件搜索器
 *
 * @author luoguo
 */
public class AnnotationClassFileProcessor extends AbstractFileProcessor {
    private static final String CLASS_EXT_FILENAME = ".class";
    private AnnotationExecuteManager manager;

    protected boolean checkMatch(FileObject fileObject) {
        boolean isMatch = false;
        if (fileObject.getFileName().endsWith(CLASS_EXT_FILENAME)) {
            isMatch = true;
        }
        return isMatch;

    }

    public AnnotationExecuteManager getManager() {
        return manager;
    }

    public void setManager(AnnotationExecuteManager manager) {
        this.manager = manager;
    }

    public void process() {
        MultiThreadFileProcessor.multiProcessor(getFileResolver()
                        .getFileProcessorThreadNumber(), "annotation-muti", fileObjects,
                new ProcessorCallBack() {
                    public void callBack(FileObject fileObject) {
                        manager.processClassFileObject(fileObject);
                    }
                });
    }

}
