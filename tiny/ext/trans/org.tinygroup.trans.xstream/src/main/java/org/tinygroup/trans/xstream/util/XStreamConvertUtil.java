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
package org.tinygroup.trans.xstream.util;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.trans.xstream.XStreamConvertService;
import org.tinygroup.trans.xstream.XStreamTransFactory;

public class XStreamConvertUtil {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(XStreamConvertUtil.class);
    static XStreamTransFactory factory;

    static {
        if (factory == null) {
            factory = new XStreamTransFactory();
        }
        try {
            Class<?> clazz = Class
                    .forName("org.tinygroup.trans.xstream.base.XStreamConvertImpl");
            XStreamConvertUtil
                    .setXStreamConvertService((XStreamConvertService) clazz
                            .newInstance());
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.WARN,
                    "设置XStreamConvertService失败，失败原因：{0}", e);
            throw new RuntimeException("设置XStreamConvertService失败", e);
        }
    }

    public static void setXStreamConvertService(
            XStreamConvertService xStreamConvertService) {
        factory.setxStreamConvertService(xStreamConvertService);
    }

    public static Object convert(Object obj, String scene) {
        XStream xStream = factory.getXStreamByScene(scene);
        if (obj instanceof String) {
            return xml2Object(xStream, (String) obj);
        } else {
            return object2Xml(xStream, obj);
        }
    }

    public static Object xml2Object(XStream xStream, String xml) {
        return xStream.fromXML(xml);
    }

    public static String object2Xml(XStream xStream, Object obj) {
        return xStream.toXML(obj);
    }

}
