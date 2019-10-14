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
package org.tinygroup.logger;

import junit.framework.TestCase;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.i18n.I18nMessageFactory;

import java.util.Locale;
import java.util.Properties;

import static org.tinygroup.logger.LogLevel.INFO;

/**
 * @author yanwj06282
 */
public class LoggerInfoTest extends TestCase {

    static Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    protected void setUp() throws Exception {
        super.setUp();
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream(
                "/i18n/info_zh_CN.properties"));
        I18nMessageFactory.addResource(Locale.SIMPLIFIED_CHINESE, properties);
        properties = new Properties();
        properties.load(this.getClass().getResourceAsStream(
                "/i18n/info_en_US.properties"));
        I18nMessageFactory.addResource(Locale.ENGLISH, properties);
        properties = new Properties();
        properties.load(this.getClass().getResourceAsStream(
                "/i18n/info_zh_TW.properties"));
        I18nMessageFactory.addResource(Locale.TRADITIONAL_CHINESE, properties);
    }

    public void testInfo() {
        Exception exception = new RuntimeException("测试异常");
        Context context = new ContextImpl();
        context.put("name", "dada");
        context.put("name1", "fafa");
        logger.log(INFO, "name1");
        logger.log(INFO, "loginInfo", "dada");
        logger.log(INFO, Locale.ENGLISH, "loginInfo", "dada");
        logger.log(INFO, "loginInfo", exception, "dada");
        logger.log(INFO, Locale.ENGLISH, "loginInfo", exception, "dada");
        logger.logMessage(INFO, "测试日志");
        logger.logMessage(INFO, "测试【{0}】日志", "da");
        logger.info("loginInfoContext", context);
        logger.info("loginInfo", "dada");
        logger.info(Locale.ENGLISH, "loginInfoContext1", context);
        logger.info(Locale.CHINESE, "loginInfo", "dada");
        logger.info("loginInfoContext", exception, context);
        logger.info("loginInfo", exception, "dada");
        logger.info(Locale.ENGLISH, "loginInfoContext", exception, context);
        logger.info(Locale.CHINESE, "loginInfo", exception, "dada");
        logger.infoMessage("测试日志【${name}】，【${name1}】", context);
        logger.infoMessage("测试日志【{0}】，【{1}】", "dada", "fafa");
        logger.infoMessage("测试日志【${name}】，【${name1}】", exception, context);
        logger.infoMessage("测试日志【{0}】，【{1}】", exception, "dada", "fafa");
    }

}
