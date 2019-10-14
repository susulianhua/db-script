/**
 *
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package org.tinygroup.remoteconfig.config;

import java.io.Serializable;
import java.util.Date;

/**
 * 配置发布节点数据项
 * @author zhangliang08072
 * @version $Id: ConfigPublishItem.java, v 0.1 2017-12-19 下午8:35:51 zhangliang08072 Exp $
 */
public class ConfigPublishItem implements Serializable {

	private static final long serialVersionUID = -5392173080745173640L;
	
	private long version;

	private Date publishTime;
	
	private Boolean publishStatus;

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Boolean getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Boolean publishStatus) {
		this.publishStatus = publishStatus;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	public void addVersion(){
		this.version++;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[publishStatus:").append(publishStatus).append("]");
		sb.append("[publishTime:").append(publishTime).append("]");
		sb.append("[version:").append(version).append("]");
		return sb.toString();
	}
	
}
