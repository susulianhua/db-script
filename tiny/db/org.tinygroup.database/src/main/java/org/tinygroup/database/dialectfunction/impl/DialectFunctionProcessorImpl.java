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
package org.tinygroup.database.dialectfunction.impl;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.database.config.dialectfunction.Dialect;
import org.tinygroup.database.config.dialectfunction.DialectFunction;
import org.tinygroup.database.config.dialectfunction.DialectFunctions;
import org.tinygroup.database.dialectfunction.DialectFunctionProcessor;
import org.tinygroup.database.dialectfunction.DialectReplaceFormater;
import org.tinygroup.format.Formater;
import org.tinygroup.format.PatternDefine;
import org.tinygroup.format.impl.DefaultPatternDefine;
import org.tinygroup.format.impl.FormaterImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明: 方言函数配置处理接口的实现
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-20 <br>
 * <br>
 */
public class DialectFunctionProcessorImpl implements DialectFunctionProcessor {

    private static DialectFunctionProcessor dialectFunctionProcessor = new DialectFunctionProcessorImpl();
    private Map<String, DialectFunction> dialectMap = new HashMap<String, DialectFunction>();
    /**
     * key:数据库名称 value:该数据库对应的所有方言配置信息
     */
    private Map<String, List<Dialect>> database2Dialects = new HashMap<String, List<Dialect>>();
    private Formater formatter;

    public static DialectFunctionProcessor getDialectFunctionProcessor() {
        return dialectFunctionProcessor;
    }

    public void addDialectFunctions(DialectFunctions functions) {
        List<DialectFunction> dialectFunctions = functions.getFunctions();
        for (DialectFunction dialectFunction : dialectFunctions) {
            List<Dialect> dialects = dialectFunction.getDialects();
            for (Dialect dialect : dialects) {
                List<Dialect> dialectList = database2Dialects.get(dialect.getName());
                if (dialectList == null) {
                    dialectList = new ArrayList<Dialect>();
                    database2Dialects.put(dialect.getName(), dialectList);
                }
                dialectList.add(dialect);
            }
            dialectMap.put(dialectFunction.getName(), dialectFunction);
        }
    }

    public void removeDialectFunctions(DialectFunctions functions) {
        List<DialectFunction> dialectFunctions = functions.getFunctions();
        for (DialectFunction dialectFunction : dialectFunctions) {
            List<Dialect> dialects = dialectFunction.getDialects();
            for (Dialect dialect : dialects) {
                List<Dialect> dialectList = database2Dialects.get(dialect.getName());
                if (!CollectionUtil.isEmpty(dialectList)) {
                    dialectList.remove(dialect);
                }
            }
            dialectMap.remove(dialectFunction.getName());
        }
    }

    public DialectFunction getDialectFunction(String functionName) {
        return dialectMap.get(functionName);
    }

    @Deprecated
    public String getFuntionSql(String originalSql, String databaseType) {
        return getFunctionSql(originalSql, databaseType);
    }

    public String getFunctionSql(String originalSql, String databaseType) {
        if (formatter == null) {
            initFormater();
        }
        Context context = new ContextImpl();
        context.put(DATABASE_TYPE, databaseType);
        String beforeFormater = originalSql;
        String afterFormater = formatter.format(context, beforeFormater);
        while (!beforeFormater.equals(afterFormater)) {
            beforeFormater = afterFormater;
            afterFormater = formatter.format(context, beforeFormater);
        }
        return afterFormater;
    }

    public Dialect getDialectWithDatabaseType(String functionName,
                                              String databaseType) {
        DialectFunction function = getDialectFunction(functionName);
        List<Dialect> dialects = function.getDialects();
        if (dialects != null) {
            for (Dialect dialect : dialects) {
                if (dialect.getName().equalsIgnoreCase(databaseType)) {
                    return dialect;
                }
            }
        }
        return null;
    }

    private void initFormater() {
        formatter = new FormaterImpl();
        PatternDefine define = new DefaultPatternDefine();
        define.setPrefixPatternString("#{");
        define.setPostfixPatternString("}");
        define.setPatternString("([#][{]([^#{}]*)[}])");
        formatter.setPatternHandle(define);
        DialectReplaceFormater replace = new DialectReplaceFormater(this);
        formatter.addFormatProvider("", replace);
    }

}
