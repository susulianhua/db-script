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
package org.tinygroup.templatespringext;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangll13383 on 2015/9/9.
 * 文件扫描抽象类
 */
public abstract class AbstractFileScanner implements FileScanner {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractFileScanner.class);

    private List<String> classPathList = new ArrayList<String>();

    private List<FileProcessor> fileProcessors = new ArrayList<FileProcessor>();

    public List<FileProcessor> getFileProcessors() {
        return fileProcessors;
    }

    public void setFileProcessors(List<FileProcessor> fileProcessors) {
        this.fileProcessors = fileProcessors;
    }

    protected List<String> getClassPathList() {
        return classPathList;
    }

    public void setClassPathList(List<String> classPathList) {
        this.classPathList = classPathList;
    }

    public void resolverFolder(FileObject file) {
        if (file.isFolder()) {
            for (FileProcessor fileProcessor : fileProcessors) {
                if (fileProcessor.isMatch(file.getFileName())) {
                    fileProcessor.addFile(file);
                }
            }
            for (FileObject f : file.getChildren()) {
                resolverFolder(f);
            }
        } else {
            for (FileProcessor fileProcessor : fileProcessors) {
                if (fileProcessor.isMatch(file.getFileName())) {
                    fileProcessor.addFile(file);
                }
            }
        }
    }


}
