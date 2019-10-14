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
package org.tinygroup.service.annotation;

import java.util.ArrayList;
import java.util.List;

public class AnnotationLoader extends AbstractAnnotationServiceLoader {

    private List<String> classNames = new ArrayList<String>();


    public void setConfigPath(String path) {

    }


    protected List<String> getClassNames() {
        return classNames;
    }

    public void addClassName(String className) {
        classNames.add(className);
    }


}
