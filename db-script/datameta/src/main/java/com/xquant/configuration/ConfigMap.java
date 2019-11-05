package com.xquant.configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 能够触发事件的方法有ConfigMap(Map<String, String> targetMap),put,putAll和remove方法。clear方法不会触发事件。
 * 
 * @author zhangliang08072
 * @version $Id: ConfigMap.java, v 0.1 2017-12-28 下午4:19:31 zhangliang08072 Exp $
 */
public class ConfigMap implements Map<String, String> ,Cloneable{
	Map<String, String> map = new HashMap<String, String>();
	
	public ConfigMap(Map<String, String> targetMap){
		putAll(targetMap);
	}
	public ConfigMap(){
		
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsKey(value);
	}

	public String get(Object key) {
		return map.get(key);
	}

	public String put(String key, String value) {
		if (map.containsKey(key)) {
			ConfigChangeEventManager.getInstance()
					.post(new ConfigChangeEvent(ConfigChangeEvent.TYPE_UPDATE, key, value, map.get(key)));
		} else {
			ConfigChangeEventManager.getInstance()
					.post(new ConfigChangeEvent(ConfigChangeEvent.TYPE_ADD, key, value, value));
		}
		return map.put(key, value);
	}

	private String getStr(Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof String) {
			return (String) o;
		} else {
			return o.toString();
		}
	}

	public String remove(Object key) {
		String keyStr = getStr(key);
		if (map.containsKey(keyStr)) {
			ConfigChangeEventManager.getInstance()
					.post(new ConfigChangeEvent(ConfigChangeEvent.TYPE_DELETE, keyStr, map.get(key), map.get(key)));
		}
		return map.remove(key);
	}

	public void putAll(Map<? extends String, ? extends String> m) {
		for (String k : m.keySet()) {
			put(k, m.get(k));
		}
	}

	public void clear() {
		map.clear();
	}

	public Set<String> keySet() {

		return map.keySet();
	}

	public Collection<String> values() {
		return map.values();
	}

	public Set<Entry<String, String>> entrySet() {
		return map.entrySet();
	}
	
	//clone不触发事件
	public ConfigMap clone(){
		ConfigMap newConfigMap = new ConfigMap();
		newConfigMap.map.putAll(this.map);
		return newConfigMap;
	}
}
