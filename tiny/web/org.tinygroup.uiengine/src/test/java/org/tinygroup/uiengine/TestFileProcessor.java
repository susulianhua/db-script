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
package org.tinygroup.uiengine;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.uiengine.config.UIComponent;
import org.tinygroup.uiengine.manager.UIComponentManager;

public class TestFileProcessor extends TestCase {
    private static UIComponentManager manager;

    @Override
    protected void setUp() throws Exception {
        AbstractTestUtil.init(null, true);
        manager = BeanContainerFactory.getBeanContainer(
                TestFileProcessor.class.getClassLoader()).getBean(
                UIComponentManager.UIComponentManager_BEAN);
    }


    public void testGet() {
        UIComponent component = manager.getUiComponents().iterator().next();
        assertEquals("x.css".equals(component.getCssResource()), true);
    }
}
