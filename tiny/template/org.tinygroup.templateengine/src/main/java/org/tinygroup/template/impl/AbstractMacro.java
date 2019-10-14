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
package org.tinygroup.template.impl;

import org.tinygroup.context.Context;
import org.tinygroup.template.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象宏
 * Created by luoguo on 2014/6/6.
 */
public abstract class AbstractMacro implements Macro {
    private Macro bodyContentMacro;
    private String name;
    private List<String> parameterNames = new ArrayList<String>();
    private List<EvaluateExpression> parameterDefaultValues = new ArrayList<EvaluateExpression>();
    private TemplateEngine templateEngine;
    private String macroPath;
    private long lastModifiedTime;
    private String absolutePath;

    public AbstractMacro(String name) {
        this.name = name;
    }

    public AbstractMacro(String name, Macro bodyContentMacro) {
        this.name = name;
        this.bodyContentMacro = bodyContentMacro;
    }

    public AbstractMacro(String name, List<String> parameterNames, List<EvaluateExpression> parameterDefaultValues) {
        this(name);
        this.parameterNames = parameterNames;
        this.parameterDefaultValues = parameterDefaultValues;
    }

    protected Macro getMacro(TemplateContext $context) {
        Macro $macro;
        $macro = getBodyContentMacro();
        if ($macro == null) {
            $macro = (Macro) $context.getItemMap().get("bodyContent");
        }
        if ($macro == null) {
            Context context = $context;
            while (context.getParent() != null) {
                if (context.get("bodyContent") != null && context.getItemMap().size() > 0 && !context.getItemMap().containsKey("isCalled")) {
                    $macro = (Macro) context.getItemMap().get("bodyContent");
                    return $macro;
                }
                context = context.getParent();
            }
        }
        return $macro;
    }

    public Macro getBodyContentMacro() {
        return bodyContentMacro;
    }

    protected void addParameter(String parameterName, EvaluateExpression defaultValue) {
        parameterNames.add(parameterName);
        parameterDefaultValues.add(defaultValue);
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public List<EvaluateExpression> getParameterDefaultValues() {
        return parameterDefaultValues;
    }

    public void setParameterDefaultValues(List<EvaluateExpression> parameterDefaultValues) {
        this.parameterDefaultValues = parameterDefaultValues;
    }

    protected void init(String name) {
        this.name = name;
    }

    public void render(Template template, TemplateContext pageContext, TemplateContext context, OutputStream outputStream) throws TemplateException {
        try {
            for (int i = 0; i < parameterNames.size(); i++) {
                Object value = context.get(parameterNames.get(i));
                //如果没有传值且有默认值
                if (value == null && parameterDefaultValues.get(i) != null) {
                    context.put(parameterNames.get(i), parameterDefaultValues.get(i).evaluate(context));
                }
            }
            renderMacro(template, pageContext, context, outputStream);
        } catch (IOException e) {
            throw new TemplateException(e);
        }
    }

    protected abstract void renderMacro(Template template, TemplateContext pageContext, TemplateContext context, OutputStream outputStream) throws IOException, TemplateException;

    public String getName() {
        return name;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public void setParameterNames(List<String> parameterNames) {
        this.parameterNames = parameterNames;
    }

    public String getParameterName(int index) {
        if (index < parameterNames.size()) {
            return parameterNames.get(index);
        }
        return null;
    }

    public String getMacroPath() {
        return macroPath;
    }

    public void setMacroPath(String macroPath) {
        this.macroPath = macroPath;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long time) {
        this.lastModifiedTime = time;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String path) {
        this.absolutePath = path;
    }
}
