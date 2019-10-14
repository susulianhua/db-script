package org.tinygroup.remoteproperties.listener;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.RemoteConfigReadClient;
import org.tinygroup.remoteconfig.utils.RemoteConfigHandler;
import org.tinygroup.remoteconfig.utils.TinyConfigParamUtil;
import org.tinygroup.remoteconfig.zk.manager.impl.ZKConfigClientImpl;

public class RemoteProSpringApplicationRunListener implements SpringApplicationRunListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteProSpringApplicationRunListener.class);

    public RemoteProSpringApplicationRunListener(SpringApplication application, String[] args) {
    }

    @Override
    public void starting() {
        LOGGER.infoMessage("开始RemoteProSpringApplicationRunListener...");
        TinyConfigParamUtil.setReadClient(new ZKConfigClientImpl());
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        InputStream inputStream = this.getClass().getResourceAsStream(
                "/application.xml");
        try {
            String applicationConfig = StreamUtil.readText(inputStream, "UTF-8", true);
            // 远程配置
            RemoteConfigReadClient client = TinyConfigParamUtil.getReadClient();
            RemoteConfigHandler remoteConfig = new RemoteConfigHandler(
                    applicationConfig, client);
            remoteConfig.start();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {

    }
}
