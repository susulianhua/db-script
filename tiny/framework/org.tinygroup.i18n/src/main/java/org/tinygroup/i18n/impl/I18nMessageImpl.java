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
package org.tinygroup.i18n.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.context.Context;
import org.tinygroup.format.Formater;
import org.tinygroup.i18n.I18nMessage;
import org.tinygroup.i18n.I18nMessageFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class I18nMessageImpl implements I18nMessage {
    private static final String CONTEXT_LOCALE_KEY = "contextLocale";
    private static final Log LOGGER = LogFactory.getLog(I18nMessageImpl.class
            .getName());
    private static Pattern pattern = Pattern.compile("[{](.)*?[}]");
    private Formater formater;
    private String localeKey = CONTEXT_LOCALE_KEY;

    public String getLocaleKey() {
        return localeKey;
    }

    public void setLocaleKey(String localeKey) {
        this.localeKey = localeKey;
    }

    public Formater getFormater() {
        return formater;
    }

    public void setFormater(Formater formatter) {
        this.formater = formatter;
    }

    private String format(String message, Object... args) {
        Matcher matcher = pattern.matcher(message);
        StringBuilder stringBuffer = new StringBuilder();
        int start = 0;
        int count = 0;
        while (matcher.find(start)) {
            stringBuffer.append(message.substring(start, matcher.start()));
            stringBuffer.append(args[count++]);
            start = matcher.end();
            if (count == args.length) {
                break;
            }
        }
        if (args.length != count) {
            LOGGER.warn(String.format("占位符[%s]和参数[%s]长度不匹配：", args.length,
                    count));
        }
        stringBuffer.append(message.substring(start, message.length()));
        return stringBuffer.toString();
    }

    public String getMessage(String code, Object... args) {
        return getMessage(code, LocaleUtil.getContext().getLocale(), args);
    }

    public String getMessage(String code, Locale locale, Object... args) {
        String pattern = I18nMessageFactory.getMessage(locale, code);
        if (pattern == null) {
            return null;
        }
        String formatStr = null;
        try {
            //使用 java.text.MessageFormat 用以支持参数顺序及其他的强大功能。
            formatStr = MessageFormat.format(pattern, args);
        } catch (IllegalArgumentException e) {
            //之前型如 : This {} is {} sample. 不是java.text.MessageFormat合法规则，故使用旧方法处理。
            return format(pattern, args);
        }
        if (formatStr != null && formatStr.equalsIgnoreCase(pattern)) {//如果MessageFormat.format没有变化,继续使用原来的format
            return format(pattern, args);
        }
        return formatStr;
    }

    public String getMessage(String code) {
        return I18nMessageFactory.getMessage(code);
    }

    public String getMessage(String code, Locale locale) {
        return I18nMessageFactory.getMessage(locale, code);
    }

    public String getMessage(String code, Context context) {
        String string = I18nMessageFactory.getMessage(LocaleUtil.getContext()
                .getLocale(), code);
        return formater.format(context, string);
    }

    public String format(String message, Context context) {
        return formater.format(context, message);
    }

    public String getMessage(String code, Context context, Locale locale) {
        String string = I18nMessageFactory.getMessage(locale, code);
        return formater.format(context, string);

    }

    public Locale getContextLocale(Context context) {
        Locale locale = context.get(localeKey);
        if (locale == null) {
            locale = LocaleUtil.getContext().getLocale();
        }
        return locale;
    }

    public String getMessage(String code, String defaultMessage, Object... args) {
        String message = getMessage(code, args);
        return getRealMessage(defaultMessage, message);
    }

    public String getMessage(String code, Locale locale, String defaultMessage,
                             Object... args) {
        String message = getMessage(code, locale, args);
        return getRealMessage(defaultMessage, message);
    }

    public String getMessage(String code, String defaultMessage) {
        String message = getMessage(code);
        return getRealMessage(defaultMessage, message);
    }

    public String getMessage(String code, Locale locale, String defaultMessage) {
        String message = getMessage(code, locale);
        return getRealMessage(defaultMessage, message);
    }

    public String getMessage(String code, String defaultMessage, Context context) {
        String message = getMessage(code, context);
        return getRealMessage(defaultMessage, message);
    }

    public String getMessage(String code, String defaultMessage,
                             Context context, Locale locale) {
        String message = getMessage(code, context, locale);
        return getRealMessage(defaultMessage, message);
    }

    private String getRealMessage(String defaultMessage, String message) {
        if (message == null) {
            return defaultMessage;
        }
        return message;
    }

}
