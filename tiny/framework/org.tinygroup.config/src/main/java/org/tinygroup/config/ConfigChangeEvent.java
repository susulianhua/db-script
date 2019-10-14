package org.tinygroup.config;

public class ConfigChangeEvent {
	public final static String TYPE_ADD = "add";
	public final static String TYPE_DELETE = "delete";
	public final static String TYPE_UPDATE = "update";
	
	String type;
	String key;
	String newValue;
	String oldvalue;
	
	
	public ConfigChangeEvent(String type, String key, String newValue, String oldvalue) {
		super();
		this.type = type;
		this.key = key;
		this.newValue = newValue;
		this.oldvalue = oldvalue;
	}
	public String getType() {
		return type;
	}
	public String getKey() {
		return key;
	}
	public String getNewValue() {
		return newValue;
	}
	public String getOldvalue() {
		return oldvalue;
	}
	
	
}
