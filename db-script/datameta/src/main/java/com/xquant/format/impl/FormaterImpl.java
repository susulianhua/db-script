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
package com.xquant.format.impl;

import com.xquant.common.context.Context;
import com.xquant.format.FormatProvider;
import com.xquant.format.Formater;
import com.xquant.format.PatternDefine;
import com.xquant.format.exception.FormatException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Formater的默认实现
 *
 * @author luoguo
 */
public class FormaterImpl implements Formater {

    private Map<String, FormatProvider> formatProviders = new HashMap<String, FormatProvider>();
    private PatternDefine patternDefine = new DefaultPatternDefine();

    /**
     * 构造函数 使用默认的配置加载器
     */
    public FormaterImpl() {
    }

    /**
     * 格式化找到的内容，其余内容不变，如果找不到内容，则原样保留
     *
     * @throws FormatException
     */
    public String format(Context context, String source) throws FormatException {
        if (StringUtils.isBlank(source)) {
            return StringUtils.EMPTY;
        }
        Matcher matcher = patternDefine.getPattern().matcher(source);
        StringBuilder buf = new StringBuilder();
        int curpos = 0;
        while (matcher.find()) {
            String replaceStr = patternDefine.getPureMatchText(matcher.group());
            buf.append(source.substring(curpos, matcher.start()));
            curpos = matcher.end();
            String str = formatSingle(context, replaceStr);
            if (str != null) {
                buf.append(str);
            }
            continue;
        }
        buf.append(source.substring(curpos));
        return buf.toString();
    }

    /**
     * 格式化字符串
     *
     * @param string String
     * @return String
     * @throws FormatException
     * @throws Exception
     */
    private String formatSingle(Context context, String string)
            throws FormatException {
        String s[] = string.split(patternDefine.getSplitChar() + "");
        if (s.length >= 2) {
            FormatProvider o = formatProviders.get(s[0]);
            if (o != null) {
                return o.format(context, s[1]);
            }
        } else {
            FormatProvider o = formatProviders.get("");
            if (o != null) {
                return o.format(context, string);
            }
        }
        return patternDefine.getFullMatchText(string);
    }

    public void setFormatProviders(Map<String, FormatProvider> formatProviders) {
        this.formatProviders = formatProviders;
    }

    public void setPatternHandle(PatternDefine patternHandle) {
        this.patternDefine = patternHandle;

    }

    public void addFormatProvider(String prefix, FormatProvider formatProvider) {
        if (formatProviders == null) {
            formatProviders = new HashMap<String, FormatProvider>();
        }
        formatProviders.put(prefix, formatProvider);
    }

}
