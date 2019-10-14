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
package org.tinygroup.jspengine.runtime;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

/**
 * Concrete implementation of {@link javax.el.ELContext}.
 * ELContext's constructor is protected to control creation of ELContext
 * objects through their appropriate factory methods.  This version of
 * ELContext forces construction through JspApplicationContextImpl.
 *
 * @author Mark Roth
 * @author Kin-man Chung
 */
public class ELContextImpl
        extends ELContext {
    private FunctionMapper functionMapper;
    private VariableMapper variableMapper;
    private ELResolver resolver;

    /**
     * Constructs a new ELContext associated with the given ELResolver.
     */
    public ELContextImpl(ELResolver resolver) {
        this.resolver = resolver;
    }

    public ELResolver getELResolver() {
        return resolver;
    }

    public FunctionMapper getFunctionMapper() {
        return functionMapper;
    }

    public void setFunctionMapper(FunctionMapper fnMapper) {
        functionMapper = fnMapper;
    }

    public VariableMapper getVariableMapper() {
        return variableMapper;
    }

    public void setVariableMapper(VariableMapper varMapper) {
        variableMapper = varMapper;
    }
}
