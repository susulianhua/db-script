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
package org.tinygroup.jspengine.el.parser;

import org.tinygroup.jspengine.el.lang.EvaluationContext;

import javax.el.ELException;
import java.math.BigDecimal;


/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: tcfujii $
 */
public final class AstFloatingPoint extends SimpleNode {
    private Number number;

    public AstFloatingPoint(int id) {
        super(id);
    }

    public Number getFloatingPoint() {
        if (this.number == null) {
            try {
                this.number = Double.valueOf(this.image);
            } catch (ArithmeticException e0) {
                this.number = new BigDecimal(this.image);
            }
        }
        return this.number;
    }

    public Object getValue(EvaluationContext ctx)
            throws ELException {
        return this.getFloatingPoint();
    }

    public Class getType(EvaluationContext ctx)
            throws ELException {
        return this.getFloatingPoint().getClass();
    }
}
