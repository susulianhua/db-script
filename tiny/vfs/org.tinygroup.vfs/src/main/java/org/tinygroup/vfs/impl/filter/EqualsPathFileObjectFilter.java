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

/**
 * Created by luoguo on 14-2-26.
 */
public class EqualsPathFileObjectFilter implements FileObjectFilter {
    private final String path; //匹配路径
    private boolean fullMatch = false; //是否全匹配

    //设置匹配的路径
    public EqualsPathFileObjectFilter(String path) {
        this.path = path;
    }

    public boolean accept(FileObject fileObject) {
        return path.equals(fileObject.getPath()); //判断fileObject的相对路径是否匹配
    }
}
