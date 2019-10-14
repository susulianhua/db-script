/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 */
package org.tinygroup.remoteconfig;

import org.junit.Assert;
import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.i18n.I18nMessageFactory;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.ConfigValue;
import org.tinygroup.remoteconfig.zk.client.BaseManager;
import org.tinygroup.remoteconfig.zk.client.ZKManager;
import org.tinygroup.remoteconfig.zk.config.RemoteEnvironment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * @author Administrator
 */
public class ClientTest {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        BaseManager.setConfig(RemoteEnvironment.load());
        ZKManager.start();
        init();
        try{
        	Assert.assertNull(ZKManager.get("O2O", null));
        }catch (BaseRuntimeException e) {
        	e.printStackTrace();
        }
        
        try {
            ZKManager.getAll(new ConfigPath());
            //单条查询
            ZKManager.set("O2O", new ConfigValue("123"), null);
            Assert.assertTrue(ZKManager.exists("O2O", null));
            ConfigValue value = ZKManager.get("O2O", null);
            Assert.assertNotNull(value);
            System.out.println("====>"+value);
            ZKManager.delete("O2O", null);
        } catch (Exception e) {
        	e.printStackTrace();
            Assert.fail();
        }
    }

    private static void init() throws IOException {
        loadI18n("zh", "CN");
        loadI18n("en", "US");
        loadI18n("zh", "TW");
    }

    private static void loadI18n(String language, String country) throws IOException {
        Locale locale = new Locale(language, country);
        Properties properties = new Properties();
        InputStream inputStream = ClientTest.class.getResourceAsStream(
                "/i18n/info_" + locale.getLanguage() + "_"
                        + locale.getCountry() + ".properties");
//		BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        properties.load(inputStream);
        I18nMessageFactory.addResource(locale, properties);
    }
}
