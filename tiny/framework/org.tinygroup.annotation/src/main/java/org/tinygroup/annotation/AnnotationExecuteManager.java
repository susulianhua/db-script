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
package org.tinygroup.annotation;

import org.tinygroup.annotation.config.AnnotationClassMatchers;
import org.tinygroup.vfs.FileObject;

/**
 * annotation执行管理接口
 *
 * @author renhui
 */
public interface AnnotationExecuteManager {

    String ANNOTATION_MANAGER_BEAN_NAME = "annotationExecuteManager";

    String XSTEAM_PACKAGE_NAME = "annotation";

    /**
     * 增加annotation配置对象信息
     *
     * @param annotationClassMatchers
     */
    void addAnnotationClassMatchers(AnnotationClassMatchers annotationClassMatchers);

    /**
     * 对class文件进行处理
     *
     * @param fileObject
     */
    void processClassFileObject(FileObject fileObject);

    /**
     * 移除annotation配置信息
     *
     * @param annotationClassMatchers
     */
    void removeAnnotationClassMatchers(AnnotationClassMatchers annotationClassMatchers);


}
