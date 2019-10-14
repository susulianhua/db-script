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
/**
 *
 */
package org.tinygroup.fileresolver.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.tinygroup.commons.cryptor.util.EncryptUtil;
import org.tinygroup.config.ConfigManagerFactory;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.config.util.IpUtil;
import org.tinygroup.config.util.TinyConfigConstants;
import org.tinygroup.fileresolver.util.RandomUtil;
import org.tinygroup.ini.IniOperator;
import org.tinygroup.ini.Section;
import org.tinygroup.ini.ValuePair;
import org.tinygroup.ini.impl.IniOperatorDefault;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

/**
 * @author Administrator
 */
public class LocalPropertiesFileProcessor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(LocalPropertiesFileProcessor.class);

    private static final String APPLICATION_PROPERTIES_PROPERTY = "/application/application-properties/property";

    private static final String APPLICATION_PROPERTIES_FILE = "/application/application-properties/file";

    String applicationConfig;
    
	public LocalPropertiesFileProcessor(String applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    private void loadApplicationProperties(XmlNode applicationConfig,Map<String,String> localConfigMap) {
        PathFilter<XmlNode> filter = new PathFilter<XmlNode>(applicationConfig);
        List<XmlNode> propertyList = filter
                .findNodeList(APPLICATION_PROPERTIES_PROPERTY);
        for (XmlNode property : propertyList) {
            String name = property.getAttribute("name");
            String value = property.getAttribute("value");
            localConfigMap.put(name, value);
        }
    }

    public void start() {
        LOGGER.logMessage(LogLevel.INFO, "开始读取本地Application变量配置信息");
        XmlNode xmlNode = new XmlStringParser().parse(applicationConfig).getRoot();
        Map<String,String> localConfigMap = new HashMap<String,String>();
        //先加载application property节点
        loadApplicationProperties(xmlNode,localConfigMap);
        EncryptUtil.getInstance().setCryptor(localConfigMap.get(EncryptUtil.ENCRYPT_CLASSNAME));
        //后加载外部配置文件信息
        loadApplicationPropertyFiles(xmlNode,localConfigMap);
        ConfigManagerFactory.getManager().addFirst(ConfigManagerFactory.TYPE_LOCAL, localConfigMap);
        //如果没有设置应用的节点名称，随机生成一个
        if(ConfigManagerFactory.getManager().getConfig(TinyConfigConstants.TINY_APP_NAME) == null){
        	String nodeName = IpUtil.getHostName()+"_"+RandomUtil.get8digitUUID();
        	ConfigManagerFactory.getManager().addConfig(TinyConfigConstants.TINY_APP_NAME, nodeName);
        	LOGGER.logMessage(LogLevel.INFO, "未设置应用的节点名称[tiny_app_name]。系统自动生成节点名称[{}]",nodeName);
        }
        LOGGER.logMessage(LogLevel.INFO, "读取本地Application变量配置信息完成");
    }

    private void loadApplicationPropertyFiles(XmlNode applicationConfig,Map<String,String> localConfigMap) {
        PathFilter<XmlNode> filter = new PathFilter<XmlNode>(applicationConfig);
        List<XmlNode> propertyList = filter
                .findNodeList(APPLICATION_PROPERTIES_FILE);
        for (XmlNode property : propertyList) {
            String path = property.getAttribute("path");
            String encript = property.getAttribute("encrypt");
            boolean encriptFlag = "true".equalsIgnoreCase(encript)?true:false;
            if (path.endsWith(".ini")) {
                loadApplicationPropertyIniFile(path,localConfigMap,encriptFlag);
            } else if (path.endsWith(".properties")) {
                loadApplicationPropertyPropertiesFile(path,localConfigMap,encriptFlag);
            }

        }
    }

    private void loadApplicationPropertyPropertiesFile(String path,Map<String,String> localConfigMap,boolean encript) {
        Properties p = new Properties();
        InputStream in = ConfigurationUtil.class.getResourceAsStream(path);
        try {
            p.load(in);
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("读取配置文件:" + path + "时出错", e);
        }
        if (p.size() <= 0) {
            return;
        }
        for (Object key : p.keySet()) {
            String value = p.getProperty(key.toString());
            if(encript){
            	value = decryptValue(value,path,key.toString());
            }
            localConfigMap.put(key.toString(), value);
        }

    }

    private void loadApplicationPropertyIniFile(String path,Map<String,String> localConfigMap,boolean encript) {
        IniOperator operator = new IniOperatorDefault();
        try {
        	URL url = ConfigurationUtil.class.getClassLoader().getResource(path);
            File file ;
            if( url !=null){
            	file = new File(url.toURI());
            	operator.read(new FileInputStream(file), "UTF-8");
            }else{
            	operator.read( ConfigurationUtil.class.getResourceAsStream(path),"UTF-8");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("读取配置文件:" + path + "时出错", e);
        }
        List<Section> sectionList = operator.getSections().getSectionList();
        for (Section section : sectionList) {
            List<ValuePair> valuePairs = section.getValuePairList();
            for (ValuePair valuePair : valuePairs) {
                String key = valuePair.getKey();
                String value = valuePair.getValue();
                if(encript){
                	value = decryptValue(value,path,key);
                }
                localConfigMap.put(key, value);
            }
        }
    }
    
    private String decryptValue(String value,String path,String key){
    	try {
			return EncryptUtil.getInstance().getCryptor().decrypt(value);
		} catch (Exception e) {
			throw new RuntimeException(String.format("配置文件[%s]的配置项[%s]解密失败", path,key), e);
		}
    }

}
