package com.xquant.configuration;

import com.xquant.configuration.util.ValueUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 有序存放多个Map
 * 
 * @author chenjiao
 *
 */
public class ConfigManager extends ConfigCurdManager {

	public ConfigManager() {
		super();
	}

	public ConfigManager(boolean isConfigMap) {
		super(isConfigMap);
	}

	public Map<String, String> getConfigMap() {
		return merageMap;
	}

	public <T> T getConfig(Class<T> type, String key, T defaultValue) {
		String value = getConfig(key);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		return (T) ValueUtil.getValue(value, type.getName());
	}

	/**
	 * 取值时,从Map序列最后往前取
	 * 
	 * @param key
	 * @return
	 */
	public String getConfig(String key) {
		return getConfigMap().get(key);
	}

	/**
	 * 存入k-v对，如果在local/remote中有相同的key,会覆盖
	 * 
	 * @param key
	 * @param value
	 */
	public void addConfig(String key, String value) {
		synchronized (this) {
			head.getMap().put(key, value);
			if (!exist(key)) {
				merageMap.put(key, value);
			}
		}
	}
	private boolean exist(String key) {
		MapItem item = head.next;
		while (item != null) {
			if (item.containsKey(key)) {
				return true;
			}
			item = item.next;
		}
		return false;
	}
	/**
	 * 删除k-v对，如果在local/remote中有相同的key,执行此操作后，在下次local/remote刷新前，key将不会存在
	 * 
	 * @param key
	 */
	public void removeConfig(String key) {
		synchronized (this) {
			head.getMap().remove(key);
			if (!exist(key)) {
				merageMap.remove(key);
			}
		}
		
	}

	public void clear(){
		for(Entry<String, MapItem> entry : configs.entrySet()){
			entry.getValue().clear();
		}
		configs.clear();
		configs = null;
		merageMap.clear();
		merageMap = null;
		tail.clear();
		tail = null;
		head.clear();
		head = null;
	}
}
