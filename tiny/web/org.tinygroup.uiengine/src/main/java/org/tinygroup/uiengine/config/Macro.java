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
package org.tinygroup.uiengine.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

@XStreamAlias("macro")
public class Macro {
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String icon;
    @XStreamAsAttribute
    private String title;
    @XStreamAsAttribute
    private boolean hasBody;// 是否可以包含内容
    @XStreamAlias("sub-macros")
    private List<SubMacro> submacros;// 可以包含的子节点

    @XStreamAlias("short-description")
    private String shortDescription;
    @XStreamAlias("long-description")
    private String longDescription;
    @XStreamAlias("macro-parameter")
    private List<MacroParameter> macroParameterList;

    public List<SubMacro> getSubmacros() {
        return submacros;
    }

    public void setSubmacros(List<SubMacro> submacros) {
        this.submacros = submacros;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHasBody() {
        return hasBody;
    }

    public void setHasBody(boolean hasBody) {
        this.hasBody = hasBody;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<MacroParameter> getMacroParameterList() {
        return macroParameterList;
    }

    public void setMacroParameterList(List<MacroParameter> macroParameterList) {
        this.macroParameterList = macroParameterList;
    }

}
