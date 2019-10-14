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
package org.tinygroup.custombeandefine.identifier.impl;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.custombeandefine.identifier.ConventionComponentIdentifier;

import java.util.Arrays;
import java.util.List;

/**
 * @author renhui
 */
public abstract class AbstractConventionIdentifier implements ConventionComponentIdentifier {
    private PathMatcher pathMatcher = new AntPathMatcher();
    private List<String> pkgPatterns = null;

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public void setPkgPatterns(List<String> pkgPatterns) {
        this.pkgPatterns = pkgPatterns;
    }

    public List<String> getPackagePatterns() {
        if (CollectionUtil.isEmpty(pkgPatterns)) {
            String[] patterns = new String[]{"**." + this.getHandlerType()};
            pkgPatterns = Arrays.asList(patterns);
        }
        return pkgPatterns;
    }

    protected String getHandlerClassNamePattern(String pkgPattern) {
        return pkgPattern + ".*" + StringUtils.capitalize(getHandlerType());
    }

    public boolean isComponent(String className) {
        List<String> pkgPatterns = this.getPackagePatterns();

        for (String pkgPattern : pkgPatterns) {
            boolean flag = pathMatcher
                    .match(this.getHandlerClassNamePattern(pkgPattern), className);
            if (flag) {
                return true;
            }
        }
        return false;
    }

    protected abstract String getHandlerType();

}
