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
package com.xquant.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

@XStreamAlias("xstream-configuration")
public class XStreamConfiguration {

    @XStreamAsAttribute
    @XStreamAlias("package-name")
    private String packageName;
    @XStreamAlias("xstream-class-aliases")
    private XStreamClassAliases xStreamClassAliases;
    @XStreamAlias("xstream-annotation-classes")
    private List<XStreamAnnotationClass> xStreamAnnotationClasses;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public XStreamClassAliases getxStreamClassAliases() {
        return xStreamClassAliases;
    }

    public void setxStreamClassAliases(XStreamClassAliases xStreamClassAliases) {
        this.xStreamClassAliases = xStreamClassAliases;
    }

    public List<XStreamAnnotationClass> getxStreamAnnotationClasses() {
        return xStreamAnnotationClasses;
    }

    public void setxStreamAnnotationClasses(
            List<XStreamAnnotationClass> xStreamAnnotationClasses) {
        this.xStreamAnnotationClasses = xStreamAnnotationClasses;
    }

}
