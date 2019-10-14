package org.tinygroup.cepcoreimpl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreProcessDealer;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.util.CEPCoreUtil;
import org.tinygroup.cepcoreimpl.util.LocalThreadContextUtil;
import org.tinygroup.event.Event;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class CEPCoreDealer implements CEPCoreProcessDealer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CEPCoreDealer.class);
    private static ExecutorService executor = null;

    //TODO:异步线程池初始化
    protected synchronized void initThreadPool(String configBean) {
        if (executor != null) {
            return;
        }
        ThreadPoolConfig poolConfig = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader())
                .getBean(configBean);
        executor = ThreadPoolFactory.getThreadPoolExecutor(poolConfig);
    }

    protected ExecutorService getExecutorService() {
        if (executor == null) {
            LOGGER.logMessage(LogLevel.WARN, "未配置异步服务线程池config bean,使用默认配置bean:{}",
                    ThreadPoolConfig.DEFAULT_THREADPOOL);
            initThreadPool(ThreadPoolConfig.DEFAULT_THREADPOOL);

        }
        return executor;
    }

    private void dealOnEventProcessor(EventProcessor eventProcessor, Event event) {
        if (CEPCoreUtil.isLocal(eventProcessor)) {
            dealLocal(eventProcessor, event);
        } else {
            dealRemote(eventProcessor, event);
        }
    }

    private void dealRemote(EventProcessor eventProcessor, Event event) {
        LOGGER.logMessage(LogLevel.DEBUG, "请求指定的执行处理器为:{0}", eventProcessor.getId());
        eventProcessor.process(event);
    }

    private void dealLocal(EventProcessor eventProcessor, Event event) {
        LOGGER.logMessage(LogLevel.DEBUG, "请求指定的执行处理器为:{0}", eventProcessor.getId());
        if (event.getMode() == Event.EVENT_MODE_ASYNCHRONOUS) {
            LOGGER.logMessage(LogLevel.INFO, "请求{}为异步请求", event.getEventId());
            Event e = Event.copy(event);
            // q调整为线程池
            SynchronousDeal thread = new SynchronousDeal(eventProcessor, e);
            // thread.start();
            if (!getExecutorService().isShutdown()) {
                // 将线程上下文放入请求上下文
                LocalThreadContextUtil.synBeforeCall(e);
                getExecutorService().execute(thread);
                // 虽然不是after，主要是为了移除日志线程变量
                LocalThreadContextUtil.synAfterCall(e);
                LOGGER.logMessage(LogLevel.INFO, "已开启异步请求{}执行线程", event.getEventId());
            } else {
                LOGGER.logMessage(LogLevel.ERROR, "异步请求{}线程池已关闭，直接返回", event.getEventId());
                return;
            }
        } else {
            eventProcessor.process(event);
        }

    }

    @Override
    public void process(Event event, CEPCore core, EventProcessor processor) {
        dealOnEventProcessor(processor, event);
    }

    class SynchronousDeal implements Runnable {
        Event e;
        EventProcessor eventProcessor;

        public SynchronousDeal(EventProcessor eventProcessor, Event e) {
            this.e = e;
            this.eventProcessor = eventProcessor;
        }

        public void run() {
            // 从请求上下文中解析线程上下文
            // ThreadContextUtil.parseCurrentThreadContext(e.getServiceRequest().getContext());
            LocalThreadContextUtil.synBeforeProcess(e);
            try {
                eventProcessor.process(e);
            } finally {
                LocalThreadContextUtil.synAfterProcess();
            }

        }
    }

}
