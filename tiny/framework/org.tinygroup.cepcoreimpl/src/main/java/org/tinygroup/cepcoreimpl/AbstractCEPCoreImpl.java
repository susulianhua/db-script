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
package org.tinygroup.cepcoreimpl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.*;
import org.tinygroup.cepcore.aop.CEPCoreAopManager;
import org.tinygroup.cepcore.impl.WeightChooser;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public abstract class AbstractCEPCoreImpl implements CEPCore {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCEPCoreImpl.class);
    private static final String ASYN_TAG = "asyn-thread-pool-config";
    private static final String FILTERS_TAG = "filters";
    private static final String FILTER_TAG = "filter";
    private static final String FILTER__CLASS_ATTRIBUTE = "class";
    private static final String ASYN_POOL_ATTRIBUTE = "bean";
    // 服务版本，每次注册注销都会使其+1;
    private static int serviceVersion = 0;
    private static ExecutorService executor = null;
    private CEPCoreOperator operator;
    private String nodeName;
    private EventProcessorChoose eventProcessorChoose;
    private CEPCoreAopManager aopMananger;

    public CEPCoreAopManager getAopMananger() {
        return aopMananger;
    }

    public void setAopMananger(CEPCoreAopManager aopMananger) {
        this.aopMananger = aopMananger;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public CEPCoreOperator getOperator() {
        return operator;
    }

    public void setOperator(CEPCoreOperator operator) {
        this.operator = operator;
        this.operator.setCEPCore(this);
    }

    public void startCEPCore(CEPCore cep) {
        operator.startCEPCore(cep);
    }

    public void stopCEPCore(CEPCore cep) {
        operator.stopCEPCore(cep);
    }

    public void start() {
        if (operator != null) {
            operator.startCEPCore(this);
        }
    }

    public void stop() {
        if (operator != null) {
            operator.stopCEPCore(this);
        }
        try {
            getExecutorService().shutdown();
        } catch (Exception e) {
            LOGGER.errorMessage("关闭CEPCore异步线程池时发生异常", e);
        }

    }

    public void setConfig(XmlNode config) {
        if (config == null) {
            return;
        }
        parseAsynPool(config);
        parseFileters(config);
    }

    private void parseFileters(XmlNode appConfig) {
        XmlNode filtersXmlNode = appConfig.getSubNode(FILTERS_TAG);
        if (filtersXmlNode == null) {
            return;
        }
        List<XmlNode> filterXmlNodes = filtersXmlNode.getSubNodes(FILTER_TAG);
        if (CollectionUtil.isEmpty(filterXmlNodes)) {
            return;
        }
        List<CEPCoreProcessFilter> processFilters = new ArrayList<CEPCoreProcessFilter>();
        for (XmlNode filterXmlNode : filterXmlNodes) {
            String className = filterXmlNode.getAttribute(FILTER__CLASS_ATTRIBUTE);
            if (StringUtil.isBlank(className)) {
                continue;
            }
            try {
                LOGGER.logMessage(LogLevel.INFO, "instance config CEPCoreProcessFilter class : {}", className);
                CEPCoreProcessFilter processFilter = (CEPCoreProcessFilter) Class.forName(className).newInstance();
                processFilters.add(processFilter);
            } catch (Exception e) {
                throw new RuntimeException("instance config CEPCoreProcessFilter class " + className + " exception", e);
            }
        }
        setFilters(processFilters);
    }

    abstract void setFilters(List<CEPCoreProcessFilter> processFilters);

    private void parseAsynPool(XmlNode appConfig) {
        String configBean = ThreadPoolConfig.DEFAULT_THREADPOOL;
        if (appConfig == null) {
            LOGGER.logMessage(LogLevel.WARN, "未配置异步服务线程池config bean,使用默认配置bean:{}",
                    ThreadPoolConfig.DEFAULT_THREADPOOL);
        } else if (appConfig.getSubNode(ASYN_TAG) == null) {
            LOGGER.logMessage(LogLevel.WARN, "未配置异步服务线程池节点：{}", ASYN_TAG);
        } else {
            configBean = appConfig.getSubNode(ASYN_TAG).getAttribute(ASYN_POOL_ATTRIBUTE);
        }
        if (StringUtil.isBlank(configBean)) {
            configBean = ThreadPoolConfig.DEFAULT_THREADPOOL;
            LOGGER.logMessage(LogLevel.WARN, "未配置异步服务线程池config bean,使用默认配置bean:{}",
                    ThreadPoolConfig.DEFAULT_THREADPOOL);
        }
        initThreadPool(configBean);
    }

    protected synchronized void initThreadPool(String configBean) {

        if (executor != null) {
            return;
        }
        ThreadPoolConfig poolConfig = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader())
                .getBean(configBean);
        executor = ThreadPoolFactory.getThreadPoolExecutor(poolConfig);
    }

    protected ExecutorService getExecutorService() {
        if (executor == null) {
            LOGGER.logMessage(LogLevel.WARN, "未配置异步服务线程池config bean,使用默认配置bean:{}",
                    ThreadPoolConfig.DEFAULT_THREADPOOL);
            initThreadPool(ThreadPoolConfig.DEFAULT_THREADPOOL);

        }
        return executor;
    }

    protected void changeVersion(EventProcessor eventProcessor) {
        if (eventProcessor.getType() == EventProcessor.TYPE_LOCAL) {
            LOGGER.logMessage(LogLevel.INFO, "本地EventProcessor变动,对CEPCORE服务版本进行变更");
            serviceVersion++;// 如果发生了本地EventProcessor变动，则改变版本
        }
    }

    public int getServiceInfosVersion() {
        return serviceVersion;
    }

    protected EventProcessorChoose getEventProcessorChoose() {
        if (eventProcessorChoose == null) {
            eventProcessorChoose = new WeightChooser();
        }
        return eventProcessorChoose;
    }

    public void setEventProcessorChoose(EventProcessorChoose eventProcessorChoose) {
        this.eventProcessorChoose = eventProcessorChoose;
    }

}
