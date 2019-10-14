package org.tinygroup.servicewrapper.exception;

import org.tinygroup.context.Context;
import org.tinygroup.exception.BaseRuntimeException;

import java.util.Locale;

public class ServiceWrapperException extends BaseRuntimeException {

    public ServiceWrapperException(String errorCode, Object... params) {
        super(errorCode, params);
    }

    public ServiceWrapperException(String errorCode, String defaultErrorMsg, Locale locale, Object... params) {
        super(errorCode, defaultErrorMsg, locale, params);
    }

    public ServiceWrapperException(String errorCode, Throwable throwable, Object... params) {
        super(errorCode, throwable, params);
    }

    public ServiceWrapperException(String errorCode, String defaultErrorMsg, Throwable throwable, Object... params) {
        super(errorCode, defaultErrorMsg, throwable, params);
    }

    public ServiceWrapperException(String errorCode, String defaultErrorMsg, Locale locale, Throwable throwable, Object... params) {
        super(errorCode, defaultErrorMsg, locale, throwable, params);
    }

    public ServiceWrapperException(String errorCode, Context context, Locale locale) {
        super(errorCode, context, locale);
    }

    public ServiceWrapperException(String errorCode, String defaultErrorMsg, Context context, Locale locale) {
        super(errorCode, defaultErrorMsg, context, locale);
    }

    public ServiceWrapperException(String errorCode, Context context) {
        super(errorCode, context);
    }

    public ServiceWrapperException() {
        super();
    }

    public ServiceWrapperException(Throwable cause) {
        super(cause);
    }
}
