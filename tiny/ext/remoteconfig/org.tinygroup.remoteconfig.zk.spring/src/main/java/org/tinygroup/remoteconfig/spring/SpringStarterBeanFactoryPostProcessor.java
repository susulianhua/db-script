/**
 *
 * Copyright (c) 2014-2018 All Rights Reserved.
 */
package org.tinygroup.remoteconfig.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.tinygroup.remoteconfig.utils.TinyConfigParamUtil;
import org.tinygroup.remoteconfig.zk.manager.impl.ZKConfigClientImpl;

/**
 * 
 * @author zhangliang08072
 * @version $Id: SpringStarterBeanFactoryPostProcessor.java, v 0.1 2018-1-5 下午4:44:09 zhangliang08072 Exp $
 */
public class SpringStarterBeanFactoryPostProcessor implements
		BeanFactoryPostProcessor {

	/** 
	 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if(TinyConfigParamUtil.getReadClient()==null){
			TinyConfigParamUtil.setReadClient(new ZKConfigClientImpl());
		}
	}

}
