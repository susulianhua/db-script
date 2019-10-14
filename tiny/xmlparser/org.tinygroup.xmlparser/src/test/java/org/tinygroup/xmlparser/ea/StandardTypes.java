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
package org.tinygroup.xmlparser.ea;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * 标准数据类型列表
 *
 * @author luoguo
 */
@XStreamAlias("standard-types")
public class StandardTypes {
    @XStreamAlias("package-name")
    @XStreamAsAttribute
    private String packageName;

    @XStreamImplicit
    private List<StandardType> standardTypeList;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<StandardType> getStandardTypeList() {
        return standardTypeList;
    }

    public void setStandardTypeList(List<StandardType> standardTypeList) {
        this.standardTypeList = standardTypeList;
    }

}
