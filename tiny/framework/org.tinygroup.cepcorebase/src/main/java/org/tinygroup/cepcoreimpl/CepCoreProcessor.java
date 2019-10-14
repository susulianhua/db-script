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

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreOperator;
import org.tinygroup.cepcore.EventProcessorChoose;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.TinyConfig;
import org.tinygroup.config.util.TinyConfigConstants;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.UUID;

public class CepCoreProcessor implements ApplicationProcessor {
    private static final String CEP_CONFIG_PATH = "/application/cep-configuration";
    private static final String OPERATOR_TAG = "operator";
    private static final String CHOOSER_TAG = "chooser";
    //	private static final String PARAMS_TAG = "params";
    private static final String OPERATOR_ATTRIBUTE = "name";
    private static final String CHOOSER_ATTRIBUTE = "name";
    private static final String NODE_NAME = "node-name";
    private static Logger logger = LoggerFactory
            .getLogger(CepCoreProcessor.class);

    private CEPCore cepcore;

    private XmlNode appConfig;

    public CEPCore getCepcore() {
        return cepcore;
    }

    public void setCepcore(CEPCore cepcore) {
        this.cepcore = cepcore;
    }

    public String getApplicationNodePath() {
        return CEP_CONFIG_PATH;
    }

    public String getComponentConfigPath() {
        return null;
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        this.appConfig = applicationConfig;
    }

    public XmlNode getComponentConfig() {
        return null;
    }

    public XmlNode getApplicationConfig() {
        return appConfig;
    }

    public int getOrder() {
        return 0;
    }

    public void start() {
        logger.logMessage(LogLevel.INFO, "开始启动CEPCoreProcessor");
        if (appConfig == null) {
            logger.logMessage(LogLevel.INFO, "配置为空，启动CEPCoreProcessor完毕");
            return;
        }
        cepcore.setConfig(appConfig);
        parseChooser();
        parseNode();
        logger.logMessage(LogLevel.INFO, "启动CEPCoreProcessor完毕");
    }


    private void parseNode() {
        if (appConfig.getSubNode(OPERATOR_TAG) == null) {
            return;
        }
        //设置节点名
        String nodeName = appConfig.getAttribute(NODE_NAME);
        if (StringUtil.isBlank(nodeName)) {
            logger.warnMessage("cepcore nodename is not configration,generate a random one");
            nodeName = UUID.randomUUID().toString();
            //throw new CEPConfigException("配置的" + NODE_NAME + "为空");
        }
        //else{
        logger.logMessage(LogLevel.INFO, "NodeName为:{0}", nodeName);
        LoggerFactory.putGlobalMdc("nodeName", nodeName);
        TinyConfig.setValue(TinyConfigConstants.TINY_NODE_NAME, nodeName);
        cepcore.setNodeName(nodeName);
        //}

        //设置Operator
        String operatorName = appConfig.getSubNode(OPERATOR_TAG).getAttribute(
                OPERATOR_ATTRIBUTE);
        if (StringUtil.isBlank(operatorName)) {
            logger.warnMessage("cepcore operator is not configration");
            //throw new CEPConfigException("配置的" + OPERATOR_TAG + "为空");
        } else {
            CEPCoreOperator operator = BeanContainerFactory.getBeanContainer(
                    this.getClass().getClassLoader()).getBean(operatorName);
            operator.setParam(appConfig);
            cepcore.setOperator(operator);
        }
        cepcore.start();

    }

    private void parseChooser() {
        if (appConfig.getSubNode(CHOOSER_TAG) == null) {
            return;
        }
        String chooserName = appConfig.getSubNode(CHOOSER_TAG).getAttribute(
                CHOOSER_ATTRIBUTE);
        if (StringUtil.isBlank(chooserName)) {
            return;
        }
        // 如果chooserName存在
        EventProcessorChoose chooser = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(chooserName);
        if (chooser == null) {
            return;
        }
        chooser.setParam(appConfig);
        cepcore.setEventProcessorChoose(chooser);
    }

    public void init() {
        //do nothing
    }

    public void stop() {
        logger.logMessage(LogLevel.INFO, "开始关闭CEPCoreProcessor");
        cepcore.stop();
        logger.logMessage(LogLevel.INFO, "关闭CEPCoreProcessor完毕");
    }

    public void setApplication(Application application) {
        //do nothing
    }

}
