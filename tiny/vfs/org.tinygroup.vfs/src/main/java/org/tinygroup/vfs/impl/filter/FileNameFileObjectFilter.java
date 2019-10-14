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
package org.tinygroup.vfs.impl.filter;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luoguo on 14-2-26.
 */
public class FileNameFileObjectFilter implements FileObjectFilter {
    private Pattern pattern;
    /**
     * 是否完全匹配，默认是局部区域即可
     */
    private boolean fullMatch = false;

    public FileNameFileObjectFilter(String fileNamePattern) {
        pattern = Pattern.compile(fileNamePattern);
    }

    public FileNameFileObjectFilter(String fileNamePattern, boolean fullMatch) {
        this(fileNamePattern);
        this.fullMatch = fullMatch;
    }

    public boolean accept(FileObject fileObject) {
        String fileName = fileObject.getFileName();//取得文件名
        if (fullMatch) {
            //完全匹配，不仅对文件名进行匹配，还要对匹配组(group)进行对比。
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                return matcher.group().equals(fileName);
            } else {
                return false;
            }
        } else {
            //局部匹配，直接用设置的正则表达式对文件名进行匹配
            Matcher matcher = pattern.matcher(fileName);
            return matcher.find();
        }
    }
}
