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
package org.tinygroup.tinystarter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.support.ApplicationObjectSupport;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.springutil.SpringBootBeanContainer;

/**
 * 启动与关闭tiny框架的spring生命周期接口扩展
 * @author ballackhui
 *
 */
public class TinyLifecycle extends ApplicationObjectSupport implements
        SmartLifecycle, InitializingBean {

    private final Object lifecycleMonitor = new Object();
    private volatile boolean running;
    private boolean autoStartup = true;
    private String applicationXml = "application.xml";

    private Starter starter;

    @Override
    public void start() {
        synchronized (lifecycleMonitor) {
            starter = Starter.getInstance();
            starter.start(applicationXml);
            running = true;
        }
    }

    @Override
    public void stop() {
        synchronized (lifecycleMonitor) {
            starter.stop();
            running = false;
        }
    }

    @Override
    public boolean isRunning() {
        synchronized (lifecycleMonitor) {
            return running;
        }
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isAutoStartup() {
        return autoStartup;
    }

    public void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    @Override
    public void stop(Runnable callback) {
        synchronized (lifecycleMonitor) {
            stop();
            callback.run();
        }
    }

    public void setApplicationXml(String applicationXml) {
        this.applicationXml = applicationXml;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BeanContainerFactory.initBeanContainer(SpringBootBeanContainer.class
                .getName());
        SpringBootBeanContainer beanContainer = (SpringBootBeanContainer) BeanContainerFactory
                .getBeanContainer(getClass().getClassLoader());
        beanContainer.setApplicationContext(getApplicationContext());
    }

}
