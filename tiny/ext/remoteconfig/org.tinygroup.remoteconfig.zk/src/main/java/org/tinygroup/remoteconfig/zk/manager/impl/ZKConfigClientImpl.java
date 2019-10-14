/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * <p>
 *       http://www.gnu.org/licenses/gpl.html
 * <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.remoteconfig.zk.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.tinygroup.commons.cryptor.util.EncryptUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.commons.tools.VariableUtil;
import org.tinygroup.config.ConfigManagerFactory;
import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.IRemoteConfigConstant;
import org.tinygroup.remoteconfig.RemoteConfigPublishWatch;
import org.tinygroup.remoteconfig.RemoteConfigReadClient;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.ConfigPublishItem;
import org.tinygroup.remoteconfig.config.ConfigValue;
import org.tinygroup.remoteconfig.config.Environment;
import org.tinygroup.remoteconfig.config.Module;
import org.tinygroup.remoteconfig.manager.EnvironmentManager;
import org.tinygroup.remoteconfig.placeholder.PlaceHolderHandlerFactory;
import org.tinygroup.remoteconfig.utils.TinyConfigParamUtil;
import org.tinygroup.remoteconfig.zk.client.SessionConnectionListener;
import org.tinygroup.remoteconfig.zk.client.ZKManager;
import org.tinygroup.remoteconfig.zk.client.ZKPublishManager;
import org.tinygroup.remoteconfig.zk.config.RemoteConfig;
import org.tinygroup.remoteconfig.zk.config.RemoteEnvironment;
import org.tinygroup.remoteconfig.zk.utils.PathHelper;
import org.tinygroup.remoteconfig.zk.utils.SerializeUtil;


public class ZKConfigClientImpl implements RemoteConfigReadClient,RemoteConfigPublishWatch {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ZKConfigClientImpl.class);

    ConfigPath configPath;
    boolean newEnvAble = false;

    private EnvironmentManager environmentManager;

    public EnvironmentManager getEnvironmentManager() {
        if (environmentManager == null) {
            environmentManager = new EnvironmentManagerImpl();
        }
        return environmentManager;
    }

    public void setEnvironmentManager(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    public boolean exists(String key) throws BaseRuntimeException {
        String path = PathHelper.createPath(configPath);
        LOGGER.logMessage(LogLevel.INFO, "远程配置，判断节点是否存在[key={0} ,path={1}]", key, path);
        try {
            boolean status = ZKManager.exists(key, configPath);
            LOGGER.logMessage(LogLevel.INFO, status ? "存在":"不存在");
            return status;
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，判断节点是否存在[key={0} ,path={1}]", e, key, path);
        }
        return false;
    }

    public ConfigValue get(String key) throws BaseRuntimeException {
        String path = PathHelper.createPath(configPath);
        LOGGER.logMessage(LogLevel.INFO, "远程配置，获取节点[key={0} ,path={1}]", key, path);
        try {
        	ConfigValue value =  ZKManager.get(key, configPath);
        	LOGGER.logMessage(LogLevel.INFO, "远程配置，节点信息[key={0} ,title={1} ,value={2} ,desc={3}]", key, value.getTitle() ,value.getValue() ,value.getDesc());
        	return value;
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，获取节点失败[key={0} ,path={1}]", e, key, path);
        }
        return null;
    }


    public Map<String, ConfigValue> getAll() throws BaseRuntimeException {
        Map<String, ConfigValue> itemMap = new HashMap<String, ConfigValue>();
        Map<String, ConfigValue> defaultItemMap = new HashMap<String, ConfigValue>();
        String path = PathHelper.createPath(configPath);
        LOGGER.logMessage(LogLevel.INFO, "远程配置，批量获取节点[path={0}]", path);
        Environment defaultEnvironment = getEnvironmentManager().get(IRemoteConfigConstant.DEFAULT_ENV_NAME, configPath.getVersionName(), configPath.getProductName());
        Environment environment = getEnvironmentManager().get(configPath.getEnvironmentName(), configPath.getVersionName(), configPath.getProductName());
        if (environment == null && newEnvAble) {
        	Environment env = new Environment();
        	env.setEnvironment(configPath.getEnvironmentName());
        	env.setName(configPath.getEnvironmentName());
        	getEnvironmentManager().add(env, configPath.getVersionName(), configPath.getProductName());
        	environment = getEnvironmentManager().get(configPath.getEnvironmentName(), configPath.getVersionName(), configPath.getProductName());
		}
        if (environment != null && defaultEnvironment != null) {
            //取默认环境
            getConfig(defaultEnvironment, defaultItemMap);

            //取指定模块
            getConfig(environment, itemMap);
            for (String key : itemMap.keySet()) {
                if (defaultItemMap.get(key) != null) {
                    defaultItemMap.put(key, itemMap.get(key));
                }
            }
        }
        if (LOGGER.isEnabled(LogLevel.INFO)) {
			for (String key : defaultItemMap.keySet()) {
				ConfigValue value = defaultItemMap.get(key);
				LOGGER.logMessage(LogLevel.INFO, "节点信息[key={0} ,title={1} ,value={2} ,desc={3}]", key, value.getTitle() ,value.getValue() ,value.getDesc());
			}
		}
        return defaultItemMap;
    }

    public void start() {
        //配置中心客户端如果获取配置失败，则获取环境变量
        RemoteConfig config = RemoteEnvironment.getConfig();
        if (config == null) {
            config = RemoteEnvironment.load();
        }
        placeholderHandle(config);
        fillConfig(config);
        if (isError(config)) {
           throw new RuntimeException("配置信息不合法，远程配置拉取失败...");
        }
        LOGGER.logMessage(LogLevel.INFO, "============解析处理配置信息================");
        LOGGER.logMessage(LogLevel.INFO, config.toString());
        LOGGER.logMessage(LogLevel.INFO, "====================================");
        
        setConfigPath(getConfigPath(config));
        setNewEnvAble(config.isNewEnvAble());
        ZKManager.setConfig(config);
        ZKManager.start();
        ZKPublishManager.setConfigPath(getConfigPath(config));
    }

    public void stop() {
    	ZKPublishManager.stopWatch();
        ZKManager.stop();
    }
    
    /**
     * 重新获取远程配置
     * @param m
     */
    public void retrieveRemoteConfig(Map<String, String> m){
    	Map<String, ConfigValue> newConfigMap = getAll();
    	for (String key : newConfigMap.keySet()) {
            if (newConfigMap.get(key) == null || newConfigMap.get(key).getValue() == null) {
                continue;
            }
            String value = newConfigMap.get(key).getValue();
            LOGGER.debugMessage("[{0} = {1}]", key, value);
            Boolean encrypt = newConfigMap.get(key).getEncrypt();
            if(encrypt){
            	try {
					value = EncryptUtil.getInstance().getCryptor().decrypt(value);
				} catch (Exception e) {
					throw new RuntimeException(String.format("解密远程配置项[%s]的值，失败！", key),e);
				}
            }
            m.put(key, value);
        }
    }

    /**
     * 刷新项目的配置
     */
    private void refreshAppConfig(){
    	LOGGER.infoMessage("=====>重新获取远程配置信息并刷新，开始");
    	Map<String, String> newConfigMap = new HashMap<String, String>();
    	retrieveRemoteConfig(newConfigMap);
    	ConfigManagerFactory.getManager().replace(ConfigManagerFactory.TYPE_REMOTE, newConfigMap);
    	LOGGER.infoMessage("=====>重新获取远程配置信息并刷新，结束");
    }
    
    public void registerClientWatchPublish(final String clientId){
    	//监听处理
        NodeCacheListener listener = new NodeCacheListener(){
			@Override
			public void nodeChanged() throws Exception {
				NodeCache nodeCache = ZKPublishManager.getNodeCache();
				if(nodeCache!=null){
				    if(nodeCache.getCurrentData()!=null){
				    	ConfigPublishItem cfgItem = SerializeUtil.unserialize(nodeCache.getCurrentData().getData());
				    	LOGGER.logMessage(LogLevel.INFO, "========>触发监听事件{}", cfgItem);
				    	// 发布版本比上次获取版本大
				    	if(cfgItem.getVersion()>TinyConfigParamUtil.getVersion()){
				    		LOGGER.logMessage(LogLevel.INFO, "远程配置中心有新的发布，客户端[{}]开始重新获取远程配置项！", clientId);
				    		refreshAppConfig();
				    		TinyConfigParamUtil.setLastUpdateConfigTime(cfgItem.getPublishTime());
				    		TinyConfigParamUtil.setVersion(cfgItem.getVersion());
				    	}
				    }
				}
				
			}
        };
        
        ConfigPublishItem item = new ConfigPublishItem();
        item.setPublishStatus(true);
        item.setPublishTime(TinyConfigParamUtil.getLastUpdateConfigTime());
        item.setVersion(TinyConfigParamUtil.getVersion());
    	try {
    		LOGGER.logMessage(LogLevel.INFO, "发起监听远程配置发布操作开始。");
			ZKPublishManager.set(clientId, item, ZKPublishManager.getConfigPath());
			ZKPublishManager.startWatch(clientId, listener);//新增监听
			ZKPublishManager.addSessionTimeoutHandler(new SessionConnectionListener());//增加session重连的处理
			LOGGER.logMessage(LogLevel.INFO, "发起监听远程配置发布操作完成。");
		} catch (Exception e) {
			LOGGER.logMessage(LogLevel.ERROR, "发起监听远程配置发布操作失败！", e);
		}
    }

    /**
     * 对象配置项中存在的占位符，进行替换
     * 
     * @param config
     */
    private void placeholderHandle(RemoteConfig config){
    	config.setEnv(PlaceHolderHandlerFactory.handle(config.getEnv()));
    }
    
    /**
     * 如果本地配置项读取不到，则去环境变量获取
     * 
     * @param config
     */
    private void fillConfig(RemoteConfig config){
    	config.setUrls(parseKey(config.getUrls()));
    	config.setApp(parseKey(config.getApp()));
    	config.setVersion(parseKey(config.getVersion()));
    	config.setEnv(parseKey(config.getEnv()));
    	config.setUsername(parseKey(config.getUsername()));
    	config.setPassword(parseKey(config.getPassword()));
    }
    
    
    private String parseKey(String key){
    	Pattern pattern = Pattern.compile("(\\{[^\\}]*\\})");
        Matcher matcher = pattern.matcher(key);
        int curpos = 0;
        StringBuilder buf = new StringBuilder();
        while (matcher.find()) {
            buf.append(key.substring(curpos, matcher.start()));
            curpos = matcher.end();
            String var = key.substring(matcher.start(), curpos);
            String placeholder = StringUtil.substring(var, 1, var.length() - 1);
            buf.append(getSystemConfig(placeholder));
            continue;
        }
        buf.append(key.substring(curpos));
    	
    	return buf.toString();
    }
    
    private String getSystemConfig(String key){
    	 if (StringUtils.isNotBlank(key)) {
    		 //如果是环境
    		 if (StringUtils.startsWith(key, IRemoteConfigConstant.SYSTEM_VAR_PREFIX)) {
    			 String tempEnvKey = StringUtils.substringAfter(key, IRemoteConfigConstant.SYSTEM_VAR_PREFIX);
    			 return VariableUtil.getEnvVariable(tempEnvKey);
			}
         }
    	 return VariableUtil.getEnvVariable(key);
    }
    
    private boolean isError(RemoteConfig config) {
        if (config == null || StringUtils.isBlank(config.getUrls()) || StringUtils.isBlank(config.getApp())
                || StringUtils.isBlank(config.getEnv()) || StringUtils.isBlank(config.getVersion())) {
            return true;
        }
        return false;
    }

    private ConfigPath getConfigPath(RemoteConfig config) {
        ConfigPath configPath = new ConfigPath();
        if (config != null) {
            configPath.setProductName(config.getApp());
            configPath.setVersionName(config.getVersion());
            configPath.setEnvironmentName(config.getEnv());
        }
        return configPath;
    }

    public void setConfigPath(ConfigPath configPath) {
        this.configPath = configPath;
    }

    public void setNewEnvAble(boolean newEnvAble) {
		this.newEnvAble = newEnvAble;
	}
    
    private void getConfig(Environment environment, Map<String, ConfigValue> itemMap) {
        List<Module> modules = environment.getModules();
        for (Module subModule : modules) {
            ConfigPath tempConfigPath = new ConfigPath();
            tempConfigPath.setProductName(configPath.getProductName());
            tempConfigPath.setVersionName(configPath.getVersionName());
            tempConfigPath.setEnvironmentName(environment.getName());
            recursionModule(subModule, tempConfigPath, itemMap);
        }
    }

    /**
     * 递归遍历模块
     *
     * @param parentModule
     * @param configPath
     * @param itemMap
     */
    private void recursionModule(Module parentModule, ConfigPath configPath, Map<String, ConfigValue> itemMap) {
        if (configPath == null) {
            return;
        }
        configPath.setModulePath(PathHelper.getConfigPath(configPath.getModulePath(), parentModule.getName()));
        Map<String, ConfigValue> subItemMap = null;
        try {
            subItemMap = ZKManager.getAll(configPath);
        } catch (Exception e) {
            return;
        }
        itemMap.putAll(subItemMap);
        for (String key : subItemMap.keySet()) {
            itemMap.put(key, subItemMap.get(key));
        }
        for (Module subModule : parentModule.getSubModules()) {
            recursionModule(subModule, configPath, itemMap);
        }
    }

}
