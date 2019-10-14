package org.tinygroup.config.test.util;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.config.util.ConfigurationUtil;

import junit.framework.TestCase;

public class ConfigReplaceTest extends TestCase {
//	private static final String ABC = "smyhost";
//	private static final String T_ABC = "{TINY_ENV_" + ABC + "}";
//
//	public void testEnv() {
//		Map<String, String> map = System.getenv();
//		System.out.println(map.get(ABC));
//		String value = ConfigurationUtil.replace(T_ABC, new HashMap<String, String>());
//		System.out.println(value);
//	}
	
	public void testMapReplate(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("a1", "{a2}");
		map.put("a4", "1");
		Map<String,String> map2 = new HashMap<String, String>();
		map2.put("a3", "{a4}");
		map2.put("a2", "5");
		
		ConfigurationUtil.replace(map, map2);
		assertEquals("1", map2.get("a3"));
		assertEquals("5", map.get("a1"));
	}
}
