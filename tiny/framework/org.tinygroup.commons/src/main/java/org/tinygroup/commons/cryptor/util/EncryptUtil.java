/**
 *
 * Copyright (c) 2014-2018 All Rights Reserved.
 */
package org.tinygroup.commons.cryptor.util;

import org.tinygroup.commons.cryptor.Cryptor;

/**
 * 获取加解密器工具类
 * @author zhangliang08072
 * @version $Id: EncryptUtil.java, v 0.1 2018年1月28日 下午8:12:14 zhangliang08072 Exp $
 */
public class EncryptUtil {
	
	public static final String ENCRYPT_CLASSNAME = "TINY_ENCRYPT_CLASS";
	
	public static final String DEFAULT_ENCRYPT_CLASSNAME = "org.tinygroup.commons.cryptor.DefaultCryptor";
	
	private static EncryptUtil encryptUtil;
	
	private String encryptClassName;
	
	private Cryptor cryptor;

	public String getEncryptClassName() {
		return encryptClassName;
	}

	public void setEncryptClassName(String encryptClassName) {
		this.encryptClassName = encryptClassName;
	}

	public static EncryptUtil  getInstance(){
		if(encryptUtil == null){
			encryptUtil = new EncryptUtil();
		}
		return encryptUtil;
	}
	
	private void setCryptor(){
		if(getEncryptClassName()==null ||getEncryptClassName().trim()==""){
			setEncryptClassName(DEFAULT_ENCRYPT_CLASSNAME);
		}
		try {
			Class c = Class.forName(encryptClassName);
			this.cryptor = (Cryptor)c.newInstance();
		} catch (Exception e) {
            throw new RuntimeException(String.format("创建[%s]加解密类实例失败！", encryptClassName),e);
		} 
	}
	
	public void setCryptor(String encryptClassName){
		setEncryptClassName(encryptClassName);
		setCryptor();
	}
	
	public Cryptor getCryptor(){
		if(this.cryptor == null){
			setCryptor();
		}
		return this.cryptor;
	}

	/**
	 * 
	 */
	private EncryptUtil() {
	}

}
