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
package org.tinygroup.fileresolver.impl;

import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @author Administrator
 */
public class MergePropertiesFileProcessor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MergePropertiesFileProcessor.class);

    public void start() {
        Map<String, String> proMap = ConfigurationUtil.getConfigurationManager().getConfiguration();
        Map<String, String> tempMap = new HashMap<String, String>();
        for (Iterator<String> iterator = proMap.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String value = proMap.get(key);
            value = ConfigurationUtil.replace(value, proMap);
            tempMap.put(key, value);
        }
        ConfigurationUtil.getConfigurationManager().getConfiguration().clear();
        ConfigurationUtil.getConfigurationManager().getConfiguration().putAll(tempMap);
        ConfigurationUtil.getConfigurationManager().replace();

    }

}
