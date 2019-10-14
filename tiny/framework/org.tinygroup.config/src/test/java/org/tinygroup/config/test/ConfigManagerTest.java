package org.tinygroup.config.test;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.config.ConfigChangeEvent;
import org.tinygroup.config.ConfigChangeEventManager;
import org.tinygroup.config.ConfigManager;

import com.google.common.eventbus.Subscribe;

import junit.framework.TestCase;

public class ConfigManagerTest extends TestCase {

	public void testAddMap() {
		ConfigManager manager = new ConfigManager();
		manager.addLast("local", getLocalMap());
		manager.addLast("remote", getRemoteMap());
		assertEquals("2", manager.getConfig("a"));
		assertEquals("1", manager.getConfig("b"));
		assertEquals("2", manager.getConfig("f"));
	}

	public void testAddandDeleteConfig() {
		final Info info = new Info();
		final Info info2 = new Info();
		final Info info3 = new Info();
		class abc {
			@Subscribe
			public void deal(ConfigChangeEvent event) {
				if (event.getType().equals(ConfigChangeEvent.TYPE_ADD)) {
					info.appendStr(event.getNewValue());
					info.appendKey(event.getKey());
				}else if(event.getType().equals(ConfigChangeEvent.TYPE_DELETE)) {
					info2.appendStr(event.getNewValue());
					info2.appendKey(event.getKey());
				}else if(event.getType().equals(ConfigChangeEvent.TYPE_UPDATE)) {
					info3.appendStr(event.getNewValue());
					info3.appendKey(event.getKey());
				}
			}
		}
		ConfigChangeEventManager.getInstance().register(new abc());
		
		ConfigManager manager = new ConfigManager();
		manager.addLast("local", getLocalMap());
		manager.addLast("remote", getRemoteMap());
		manager.addConfig("a", "3");
		
		assertEquals("2", manager.getConfig("a"));
		try {
			Thread.sleep(1000);//因为是异步监听
		} catch (Exception e) {
		}
		assertEquals(9, info.getKey().length());
		assertEquals(0, info3.getKey().length());
		manager.removeConfig("a");
		assertEquals("2", manager.getConfig("a"));
		try {
			Thread.sleep(2000);//因为是异步监听
		} catch (Exception e) {
		}
		assertEquals(1, info2.getKey().length());
		
	}

	public void testUpdateConfig(){
		final Info info = new Info();
		class abc {
			@Subscribe
			public void deal(ConfigChangeEvent event) {
				if (event.getType().equals(ConfigChangeEvent.TYPE_UPDATE)) {
					info.appendStr(event.getNewValue());
					info.appendKey(event.getKey());
				}
			}
		}
		ConfigChangeEventManager.getInstance().register(new abc());
		ConfigManager manager = new ConfigManager();
		manager.addLast("local", getLocalMap());
		manager.replace("local", getRemoteMap());
		try {
			Thread.sleep(2000);//因为是异步监听
		} catch (Exception e) {
		}
		
		assertEquals(1, info.getKey().length());
		assertEquals("2", manager.getConfig("a"));
		assertEquals("2", manager.getConfig("e"));
	}
	
	public Map<String, String> getLocalMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("a", "1");
		map.put("b", "1");
		map.put("c", "1");
		map.put("d", "1");
		return map;
	}

	public Map<String, String> getRemoteMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("a", "2");
		map.put("e", "2");
		map.put("f", "2");
		map.put("g", "2");
		return map;
	}

	public Map<String, String> getMemoryMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("b", "3");
		map.put("h", "3");
		map.put("i", "3");
		map.put("j", "3");
		return map;
	}

	class Info {
		String s = "";
		String k = "";
		String getStr() {
			return s;
		}
		String getKey() {
			return k;
		}
		synchronized void appendStr(String v) {
			s = s + v;
		}
		synchronized void appendKey(String v) {
			k = k + v;
		}
	}

}
