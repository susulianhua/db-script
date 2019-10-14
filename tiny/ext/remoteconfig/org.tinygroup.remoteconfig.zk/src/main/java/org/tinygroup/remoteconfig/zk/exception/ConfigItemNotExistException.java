/**
 *
 * Copyright (c) 2014-2018 All Rights Reserved.
 */
package org.tinygroup.remoteconfig.zk.exception;

import java.util.Locale;

import org.tinygroup.context.Context;
import org.tinygroup.exception.BaseRuntimeException;

/**
 * 
 * @author zhangliang08072
 * @version $Id: ConfigItemNotExistException.java, v 0.1 2018年1月19日 下午2:34:08 zhangliang08072 Exp $
 */
public class ConfigItemNotExistException extends BaseRuntimeException {
	private static final long serialVersionUID = -4267250288827679683L;

	/**
	 * 
	 */
	public ConfigItemNotExistException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ConfigItemNotExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 * @param params
	 */
	public ConfigItemNotExistException(String errorCode, Object... params) {
		super(errorCode, params);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 * @param context
	 */
	public ConfigItemNotExistException(String errorCode, Context context) {
		super(errorCode, context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 * @param throwable
	 * @param params
	 */
	public ConfigItemNotExistException(String errorCode, Throwable throwable, Object... params) {
		super(errorCode, throwable, params);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 * @param context
	 * @param locale
	 */
	public ConfigItemNotExistException(String errorCode, Context context, Locale locale) {
		super(errorCode, context, locale);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 * @param defaultErrorMsg
	 * @param locale
	 * @param params
	 */
	public ConfigItemNotExistException(String errorCode, String defaultErrorMsg, Locale locale, Object... params) {
		super(errorCode, defaultErrorMsg, locale, params);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 * @param defaultErrorMsg
	 * @param throwable
	 * @param params
	 */
	public ConfigItemNotExistException(String errorCode, String defaultErrorMsg, Throwable throwable,
			Object... params) {
		super(errorCode, defaultErrorMsg, throwable, params);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 * @param defaultErrorMsg
	 * @param context
	 * @param locale
	 */
	public ConfigItemNotExistException(String errorCode, String defaultErrorMsg, Context context, Locale locale) {
		super(errorCode, defaultErrorMsg, context, locale);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorCode
	 * @param defaultErrorMsg
	 * @param locale
	 * @param throwable
	 * @param params
	 */
	public ConfigItemNotExistException(String errorCode, String defaultErrorMsg, Locale locale, Throwable throwable,
			Object... params) {
		super(errorCode, defaultErrorMsg, locale, throwable, params);
		// TODO Auto-generated constructor stub
	}

}
