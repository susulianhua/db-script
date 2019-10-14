package org.tinygroup.weblayer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.tinygroup.weblayer.listener.ServletContextHolder;

/**
 * 把ServletContext实例绑定到ServletContextHolder中
 * @author ballackhui
 *
 */
public class ServletContextBindingListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContextHolder.setServletContext(sce.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContextHolder.clear();
	}

}
