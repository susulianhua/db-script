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
package org.tinygroup.elmvel2;

import org.mvel2.MVEL;
import org.tinygroup.context.Context;
import org.tinygroup.context.Context2Map;
import org.tinygroup.el.EL;
import org.tinygroup.el.ElImportContainer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Mvel2Impl implements EL {
    private Map<String, Serializable> cacheExpression = new HashMap<String, Serializable>();

    @SuppressWarnings("unchecked")
    public Object execute(String expression, Context context) {
        Serializable s = null;
        if (cacheExpression.containsKey(expression)) {
            s = cacheExpression.get(expression);
        } else if (ElImportContainer.getImports().isEmpty()) {
            s = MVEL.compileExpression(expression);
        } else {
            s = MVEL.compileExpression(expression,
                    ElImportContainer.getImports());
        }
        Object result = MVEL.executeExpression(s, new Context2Map(context));
        if (s != null) {
            cacheExpression.put(expression, s);
        }
        return result;
    }

}
