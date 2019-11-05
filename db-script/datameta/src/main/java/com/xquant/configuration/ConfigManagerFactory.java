package com.xquant.configuration;

public class ConfigManagerFactory {
	
	public final static String TYPE_LOCAL =  "local";
	public final static String TYPE_REMOTE =  "remote";
	public final static String TYPE_ENVVAR =  "envvar";
	public final static String TYPE_COMMANDLINE =  "commandline";
	public final static ConfigManager manager = new ConfigManager(false);
	
	public static ConfigManager getManager(){
		return manager;
	}
	
}
