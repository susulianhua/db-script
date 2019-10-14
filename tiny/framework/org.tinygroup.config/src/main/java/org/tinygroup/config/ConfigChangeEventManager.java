package org.tinygroup.config;

import java.util.concurrent.Executors;

import com.google.common.eventbus.AsyncEventBus;

public class ConfigChangeEventManager {

	private static final int DEFAULT_THREAD_NUM = Runtime.getRuntime().availableProcessors();
	private AsyncEventBus bus = new AsyncEventBus(Executors.newFixedThreadPool(DEFAULT_THREAD_NUM));
	private static ConfigChangeEventManager instance;
	static {
		instance = new ConfigChangeEventManager();
	}

	public static ConfigChangeEventManager getInstance() {
		return instance;
	}

	private ConfigChangeEventManager() {

	}

	public void post(ConfigChangeEvent event) {
		bus.post(event);

	}

	public void register(Object listenter) {
		bus.register(listenter);
	}
}
