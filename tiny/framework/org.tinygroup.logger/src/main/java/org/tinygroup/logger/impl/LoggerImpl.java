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
package org.tinygroup.logger.impl;

import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;
import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.context.Context;
import org.tinygroup.i18n.I18nMessage;
import org.tinygroup.i18n.I18nMessageFactory;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luoguo
 */
public class LoggerImpl implements Logger {
    /**
     * 日志缓存默认最大记录条数及最大字节数.
     */
    private static final int DEFAULT_MAX_BUFFER_RECORDS = 80000;
    private static Pattern pattern = Pattern.compile("[{](.)*?[}]");
    // MDC适配器
    protected MDCAdapter mdc = MDC.getMDCAdapter();
    private I18nMessage i18nMessage = I18nMessageFactory.getI18nMessages();
    private org.slf4j.Logger logger;
    private boolean supportTransaction = false;
    private ThreadLocal<LogBuffer> threadLocal = new ThreadLocal<LogBuffer>();
    private Map<String, String> mdcMap = new HashMap<String, String>();
    /**
     * The max buffer records.
     */
    private int maxBufferRecords = DEFAULT_MAX_BUFFER_RECORDS;

    /**
     * The buffer records.
     */
    private int bufferRecords = 0;

    public LoggerImpl(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tinygroup.logger.ILogger#isSupportTransaction()
     */
    public boolean isSupportTransaction() {
        return supportTransaction;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tinygroup.logger.ILogger#setSupportTransaction(boolean)
     */
    public void setSupportTransaction(boolean supportBusiness) {
        this.supportTransaction = supportBusiness;
    }

    public void removeLogBuffer() {
        threadLocal.set(null);
    }

    public synchronized LogBuffer getLogBuffer() {
        if (supportTransaction) {
            LogBuffer logBuffer = threadLocal.get();
            if (logBuffer == null) {
                logBuffer = new LogBuffer();
                threadLocal.set(logBuffer);
            }
            return logBuffer;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tinygroup.logger.ILogger#startTransaction()
     */
    public void startTransaction() {
        LogBuffer logBuffer = getLogBuffer();
        if (logBuffer != null) {
            logBuffer.increaseTransactionDepth();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tinygroup.logger.ILogger#endTransaction()
     */
    public void endTransaction() {
        LogBuffer logBuffer = getLogBuffer();
        if (logBuffer != null) {
            logBuffer.decreaseTransactionDepth();
            if (logBuffer.getTimes() == 0) {
                flushLog(logBuffer);
                removeLogBuffer();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tinygroup.logger.ILogger#flushTransaction()
     */
    public void flushTransaction() {
        LogBuffer logBuffer = getLogBuffer();
        if (logBuffer != null) {
            flushLog(logBuffer);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tinygroup.logger.ILogger#resetTransaction()
     */
    public void resetTransaction() {
        LogBuffer logBuffer = getLogBuffer();
        if (logBuffer != null) {
            logBuffer.reset();
            maxBufferRecords = 0;
        }
    }

    private void flushLog(LogBuffer logBuffer) {
        for (Message message : logBuffer.getLogMessages()) {
            if (message.getThrowable() != null) {
                pLogMessage(message.getLevel(), message.getMessage(),
                        message.getThrowable());
            } else {
                pLogMessage(message.getLevel(), message.getMessage());
            }
        }
        logBuffer.getLogMessages().clear();
        bufferRecords = 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tinygroup.logger.ILogger#getLogger()
     */
    public org.slf4j.Logger getLogger() {
        return logger;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.tinygroup.logger.ILogger#isEnabled(org.tinygroup.logger.LogLevel)
     */
    public boolean isEnabled(LogLevel logLevel) {
        switch (logLevel) {
            case DEBUG:
                return logger.isDebugEnabled();
            case INFO:
                return logger.isInfoEnabled();
            case WARN:
                return logger.isWarnEnabled();
            case TRACE:
                return logger.isTraceEnabled();
            case ERROR:
                return logger.isErrorEnabled();
        }
        return true;
    }

    /**
     * 直接记录日志
     *
     * @param logLevel
     * @param message
     */
    private void pLogMessage(LogLevel logLevel, String message) {
        putMdcVariable();// 在输出日志之前先放入局部线程变量中的mdc值
        switch (logLevel) {
            case DEBUG:
                logger.debug(message);
                break;
            case INFO:
                logger.info(message);
                break;
            case WARN:
                logger.warn(message);
                break;
            case TRACE:
                logger.trace(message);
                break;
            case ERROR:
                logger.error(message);
                break;
        }
        clearMdcVariable();
    }

    private void putMdcVariable() {
        MDC.clear();
        putMdcVariable(mdcMap);
        putMdcVariable(LoggerFactory.getGlobalMdcMap());
        putMdcVariable(LoggerFactory.getThreadVariableMap());
    }

    private void clearMdcVariable() {
        MDC.clear();
    }

    private void putMdcVariable(Map<String, String> mdcMap) {
        if (!CollectionUtil.isEmpty(mdcMap)) {
            for (Entry<String, String> entry : mdcMap.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    mdc.put(entry.getKey(), value);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.tinygroup.logger.ILogger#logMessage(org.tinygroup.logger.LogLevel,
     * java.lang.String)
     */
    public void logMessage(LogLevel logLevel, final String message) {
        logMessage(logLevel, new FormatCallback() {
            @Override
            public String format() {
                return message;
            }
        });
    }

    private void logMessage(LogLevel logLevel, FormatCallback callback) {
        if (!isEnabled(logLevel)) {
            return;
        }
        LogLevel threadLevel = LoggerFactory.getThreadLogLevel();
        if (threadLevel == null || threadLevel != null
                && logLevel.toString().equals(threadLevel.toString())) {
            exportLog(logLevel, callback.format());
        }
    }

    private void logMessageWithThrowable(LogLevel logLevel, Throwable t,
                                         FormatCallback callback) {
        if (!isEnabled(logLevel)) {
            return;
        }
        LogLevel threadLevel = LoggerFactory.getThreadLogLevel();
        if (threadLevel == null || threadLevel != null
                && logLevel.toString().equals(threadLevel.toString())) {
            exportLog(logLevel, callback.format(), t);
        }
    }

    private void exportLog(LogLevel logLevel, String message) {
        LogBuffer logBuffer = getLogBuffer();
        if (logBuffer != null && logBuffer.getTimes() > 0) {
            logBuffer.getLogMessages().add(
                    new Message(logLevel, message, System.currentTimeMillis()));
            checkBufferSize(logBuffer);
        } else {
            pLogMessage(logLevel, message);
        }
    }

    public void logMessage(LogLevel logLevel, final String message, Throwable t) {
        logMessageWithThrowable(logLevel, t, new FormatCallback() {

            @Override
            public String format() {
                return message;
            }
        });
    }

    private void exportLog(LogLevel logLevel, String message, Throwable t) {
        LogBuffer logBuffer = getLogBuffer();
        if (logBuffer != null && logBuffer.getTimes() > 0) {
            logBuffer.getLogMessages().add(
                    new Message(logLevel, message, System.currentTimeMillis(),
                            t));
            checkBufferSize(logBuffer);
        } else {
            pLogMessage(logLevel, message, t);
        }
    }

    private void pLogMessage(LogLevel logLevel, String message, Throwable t) {
        putMdcVariable();// 在输出日志之前先放入局部线程变量中的mdc值
        switch (logLevel) {
            case DEBUG:
                logger.debug(message, t);
                break;
            case INFO:
                logger.info(message, t);
                break;
            case WARN:
                logger.warn(message, t);
                break;
            case TRACE:
                logger.trace(message, t);
                break;
            case ERROR:
                logger.error(message, t);
                break;
        }
        clearMdcVariable();
    }

    /**
     * 检测日志缓存列表，如果超过了最大限制条数或最大限制记录数，<br>
     * 则强制执行刷新操作。<br>
     * .
     *
     * @param logBuffer
     */
    private void checkBufferSize(LogBuffer logBuffer) {
        bufferRecords++;
        if (bufferRecords >= maxBufferRecords) {
            flushLog(logBuffer);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tinygroup.logger.ILogger#log(org.tinygroup.logger.LogLevel,
     * java.util.Locale, java.lang.String, java.lang.Object)
     */
    public void log(LogLevel logLevel, final Locale locale, final String code,
                    final Object... args) {
        logMessage(logLevel, new FormatCallback() {
            @Override
            public String format() {
                String message = i18nMessage.getMessage(code, locale, null,
                        args);
                return judgeNullMessage(code, message);
            }

        });
    }

    private String judgeNullMessage(final String code, String message) {
        String exportMessage = message;
        if (message == null) {
            warnMessage("message code:{} 对应的国际化信息未定义,将原样输出code信息", code);
            exportMessage = code;
        }
        return exportMessage;
    }

    public void log(LogLevel logLevel, final String code) {
        logMessage(logLevel, new FormatCallback() {
            @Override
            public String format() {
                String message = i18nMessage.getMessage(code);
                return judgeNullMessage(code, message);
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tinygroup.logger.ILogger#log(org.tinygroup.logger.LogLevel,
     * java.lang.String, java.lang.Object)
     */
    public void log(LogLevel logLevel, final String code, final Object... args) {
        logMessage(logLevel, new FormatCallback() {
            @Override
            public String format() {
                String message = i18nMessage.getMessage(code, LocaleUtil
                        .getContext().getLocale(), null, args);
                return judgeNullMessage(code, message);
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tinygroup.logger.ILogger#log(org.tinygroup.logger.LogLevel,
     * java.util.Locale, java.lang.String, org.tinygroup.context.Context)
     */
    public void log(LogLevel logLevel, final Locale locale, final String code,
                    final Context context) {
        logMessage(logLevel, new FormatCallback() {
            @Override
            public String format() {
                String message = i18nMessage.getMessage(code, null, context,
                        locale);
                return judgeNullMessage(code, message);
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see org.tinygroup.logger.ILogger#log(org.tinygroup.logger.LogLevel,
     * java.lang.String, org.tinygroup.context.Context)
     */
    public void log(LogLevel logLevel, final String code, final Context context) {
        logMessage(logLevel, new FormatCallback() {
            @Override
            public String format() {
                String message = i18nMessage.getMessage(code, null, context,
                        LocaleUtil.getContext().getLocale());
                return judgeNullMessage(code, message);
            }
        });
    }

    public void logMessage(LogLevel logLevel, final String message,
                           final Object... args) {
        logMessage(logLevel, new FormatCallback() {
            @Override
            public String format() {
                return LoggerImpl.this.format(message, args);
            }
        });
    }

    public void logMessage(LogLevel logLevel, final String message,
                           Throwable t, final Object... args) {
        logMessageWithThrowable(logLevel, t, new FormatCallback() {
            @Override
            public String format() {
                return LoggerImpl.this.format(message, args);
            }
        });
    }

    private String format(String message, Object... args) {
        if (message == null) {
            return "";
        }
        Matcher matcher = pattern.matcher(message);
        StringBuilder stringBuffer = new StringBuilder();
        int start = 0;
        int count = 0;
        while (matcher.find(start)) {
            count++;
            stringBuffer.append(message.substring(start, matcher.start()));
            start = matcher.end();
            if (count <= args.length) {
                stringBuffer.append(args[count - 1]);
            }
        }
        if (args.length != count) {
            warnMessage(String.format("占位符个数[%s]和参数个数[%s]不匹配.", count,
                    args.length));
        }
        stringBuffer.append(message.substring(start, message.length()));
        return stringBuffer.toString();
    }

    public void logMessage(LogLevel logLevel, final String message,
                           final Context context) {
        logMessage(logLevel, new FormatCallback() {
            @Override
            public String format() {
                return i18nMessage.format(message, context);
            }
        });
    }

    public void logMessage(LogLevel logLevel, final String message,
                           Throwable t, final Context context) {
        logMessageWithThrowable(logLevel, t, new FormatCallback() {

            @Override
            public String format() {
                return i18nMessage.format(message, context);
            }
        });
    }

    public void error(String code, Object... args) {
        log(LogLevel.ERROR, code, args);
    }

    public void error(final String code, Throwable throwable,
                      final Object... args) {
        log(LogLevel.ERROR, code, throwable, args);
    }

    public void error(String code, Throwable throwable, Context context) {
        log(LogLevel.ERROR, code, throwable, context);
    }

    public void errorMessage(String message, Object... args) {
        logMessage(LogLevel.ERROR, message, args);
    }

    public void errorMessage(final String message, Throwable throwable,
                             final Object... args) {

        logMessage(LogLevel.ERROR, message, throwable, args);
    }

    public void errorMessage(final String message, Throwable throwable,
                             final Context context) {
        logMessage(LogLevel.ERROR, message, throwable, context);
    }

    public void error(final Throwable throwable) {
        log(LogLevel.ERROR, throwable);
    }

    private void log(LogLevel logLevel, final Throwable throwable) {
        logMessageWithThrowable(logLevel, throwable, new FormatCallback() {
            @Override
            public String format() {
                return throwable.getMessage();
            }
        });
    }

    public void putToMDC(String key, Object value) {
        mdcMap.put(key, value.toString());
    }

    public void removeFromMDC(String key) {
        mdc.remove(key);
    }

    public int getMaxBufferRecords() {
        return maxBufferRecords;
    }

    public void setMaxBufferRecords(int maxBufferRecords) {
        this.maxBufferRecords = maxBufferRecords;
    }

    public void infoMessage(String message, Object... args) {
        logMessage(LogLevel.INFO, message, args);
    }

    public void infoMessage(String message, Throwable t, Object... args) {
        logMessage(LogLevel.INFO, message, t, args);
    }

    public void infoMessage(String message, Context context) {
        logMessage(LogLevel.INFO, message, context);
    }

    public void infoMessage(String message, Throwable t, Context context) {
        logMessage(LogLevel.INFO, message, t, context);
    }

    public void debugMessage(String message, Object... args) {
        logMessage(LogLevel.DEBUG, message, args);
    }

    public void debugMessage(String message, Throwable t, Object... args) {
        logMessage(LogLevel.DEBUG, message, t, args);
    }

    public void debugMessage(String message, Context context) {
        logMessage(LogLevel.DEBUG, message, context);
    }

    public void debugMessage(String message, Throwable t, Context context) {
        logMessage(LogLevel.DEBUG, message, t, context);
    }

    public void warnMessage(String message, Object... args) {
        logMessage(LogLevel.WARN, message, args);
    }

    public void warnMessage(String message, Throwable t, Object... args) {
        logMessage(LogLevel.WARN, message, t, args);
    }

    public void warnMessage(String message, Context context) {
        logMessage(LogLevel.WARN, message, context);
    }

    public void warnMessage(String message, Throwable t, Context context) {
        logMessage(LogLevel.WARN, message, t, context);
    }

    public void traceMessage(String message, Object... args) {
        logMessage(LogLevel.TRACE, message, args);
    }

    public void traceMessage(String message, Throwable t, Object... args) {
        logMessage(LogLevel.TRACE, message, t, args);
    }

    public void traceMessage(String message, Context context) {
        logMessage(LogLevel.TRACE, message, context);
    }

    public void traceMessage(String message, Throwable t, Context context) {
        logMessage(LogLevel.TRACE, message, t, context);
    }

    public void log(LogLevel logLevel, final Locale locale, final String code,
                    Throwable t, final Object... args) {
        logMessageWithThrowable(logLevel, t, new FormatCallback() {
            @Override
            public String format() {
                String message = i18nMessage.getMessage(code, locale, null,
                        args);
                return judgeNullMessage(code, message);
            }
        });
    }

    public void log(LogLevel logLevel, final String code, Throwable t,
                    final Object... args) {
        logMessageWithThrowable(logLevel, t, new FormatCallback() {
            @Override
            public String format() {
                String message = i18nMessage.getMessage(code, null, args);
                return judgeNullMessage(code, message);
            }
        });
    }

    public void log(LogLevel logLevel, final Locale locale, final String code,
                    Throwable t, final Context context) {
        logMessageWithThrowable(logLevel, t, new FormatCallback() {
            @Override
            public String format() {
                String message = i18nMessage.getMessage(code, null, context,
                        locale);
                return judgeNullMessage(code, message);
            }
        });
    }

    public void log(LogLevel logLevel, final String code, Throwable t,
                    final Context context) {
        logMessageWithThrowable(logLevel, t, new FormatCallback() {
            @Override
            public String format() {
                String message = i18nMessage.getMessage(code, context,
                        LocaleUtil.getContext().getLocale());
                return judgeNullMessage(code, message);
            }
        });
    }

    public void info(Locale locale, String code, Object... args) {
        log(LogLevel.INFO, locale, code, args);
    }

    public void info(Locale locale, String code, Throwable t, Object... args) {
        log(LogLevel.INFO, locale, code, t, args);
    }

    public void info(String code, Object... args) {
        log(LogLevel.INFO, code, args);
    }

    public void info(String code, Throwable t, Object... args) {
        log(LogLevel.INFO, code, t, args);
    }

    public void info(Locale locale, String code, Context context) {
        log(LogLevel.INFO, locale, code, context);
    }

    public void info(Locale locale, String code, Throwable t, Context context) {
        log(LogLevel.INFO, locale, code, t, context);
    }

    public void info(String code, Context context) {
        log(LogLevel.INFO, code, context);
    }

    public void info(String code, Throwable t, Context context) {
        log(LogLevel.INFO, code, t, context);
    }

    public void debug(Locale locale, String code, Object... args) {
        log(LogLevel.DEBUG, locale, code, args);
    }

    public void debug(Locale locale, String code, Throwable t, Object... args) {
        log(LogLevel.DEBUG, locale, code, t, args);
    }

    public void debug(String code, Object... args) {
        log(LogLevel.DEBUG, code, args);
    }

    public void debug(String code, Throwable t, Object... args) {
        log(LogLevel.DEBUG, code, t, args);
    }

    public void debug(Locale locale, String code, Context context) {
        log(LogLevel.DEBUG, locale, code, context);
    }

    public void debug(Locale locale, String code, Throwable t, Context context) {
        log(LogLevel.DEBUG, locale, code, t, context);
    }

    public void debug(String code, Context context) {
        log(LogLevel.DEBUG, code, context);
    }

    public void debug(String code, Throwable t, Context context) {
        log(LogLevel.DEBUG, code, t, context);
    }

    public void warn(Locale locale, String code, Object... args) {
        log(LogLevel.WARN, locale, code, args);
    }

    public void warn(Locale locale, String code, Throwable t, Object... args) {
        log(LogLevel.WARN, locale, code, t, args);
    }

    public void warn(String code, Object... args) {
        log(LogLevel.WARN, code, args);
    }

    public void warn(String code, Throwable t, Object... args) {
        log(LogLevel.WARN, code, t, args);
    }

    public void warn(Locale locale, String code, Context context) {
        log(LogLevel.WARN, locale, code, context);
    }

    public void warn(Locale locale, String code, Throwable t, Context context) {
        log(LogLevel.WARN, locale, code, t, context);
    }

    public void warn(String code, Context context) {
        log(LogLevel.WARN, code, context);
    }

    public void warn(String code, Throwable t, Context context) {
        log(LogLevel.WARN, code, t, context);
    }

    public void trace(Locale locale, String code, Object... args) {
        log(LogLevel.TRACE, locale, code, args);
    }

    public void trace(Locale locale, String code, Throwable t, Object... args) {
        log(LogLevel.TRACE, locale, code, t, args);
    }

    public void trace(String code, Object... args) {
        log(LogLevel.TRACE, code, args);
    }

    public void trace(String code, Throwable t, Object... args) {
        log(LogLevel.TRACE, code, t, args);
    }

    public void trace(Locale locale, String code, Context context) {
        log(LogLevel.TRACE, locale, code, context);
    }

    public void trace(Locale locale, String code, Throwable t, Context context) {
        log(LogLevel.TRACE, locale, code, t, context);
    }

    public void trace(String code, Context context) {
        log(LogLevel.TRACE, code, context);
    }

    public void trace(String code, Throwable t, Context context) {
        log(LogLevel.TRACE, code, t, context);
    }

    public void error(Locale locale, String code, Object... args) {
        log(LogLevel.ERROR, locale, code, args);
    }

    public void error(Locale locale, String code, Throwable throwable,
                      Object... args) {
        log(LogLevel.ERROR, locale, code, throwable, args);
    }

    public void error(Locale locale, String code, Throwable throwable,
                      Context context) {
        log(LogLevel.ERROR, locale, code, throwable, context);
    }

    public void errorMessage(String message, Context context) {
        logMessage(LogLevel.ERROR, message, context);
    }

    public void error(String code, Context context) {
        log(LogLevel.ERROR, code, context);
    }

    public void error(Locale locale, String code, Context context) {
        log(LogLevel.ERROR, locale, code, context);
    }

    public void error(String code) {
        log(LogLevel.ERROR, code);
    }

    public void error(String code, Throwable throwable) {
        log(LogLevel.ERROR, code, throwable);
    }

    interface FormatCallback {
        String format();
    }
}
