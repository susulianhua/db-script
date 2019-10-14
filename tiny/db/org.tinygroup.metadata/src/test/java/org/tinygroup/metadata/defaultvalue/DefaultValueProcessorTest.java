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
package org.tinygroup.metadata.defaultvalue;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.metadata.TestInit;
import org.tinygroup.metadata.config.defaultvalue.DefaultValue;
import org.tinygroup.metadata.util.MetadataUtil;

import java.util.Map;

/**
 * Created by wangwy11342 on 2016/7/16.
 */
public class DefaultValueProcessorTest extends TestCase {
    static {
        TestInit.init();
    }

    DefaultValueProcessor defaultValueProcessor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        defaultValueProcessor = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                MetadataUtil.DEFAULTVALUEPROCESSOR_BEAN);
    }

    public void testGetDefault() {
        Map<String, DefaultValue> defaultValueMap = defaultValueProcessor.getDefaultValueMap();
        System.out.println(defaultValueMap);
    }
}
