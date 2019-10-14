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
package org.tinygroup.metadatadictloader;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.dict.Dict;
import org.tinygroup.dict.DictManager;
import org.tinygroup.tinyrunner.Runner;

import java.util.ArrayList;

public class MetadataDictLoaderTest extends TestCase {

    public void testDictManager() {
        Runner.init(null, new ArrayList<String>());
        DictManager dictManager = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean("dictManager");
        Dict dict = dictManager.getDict("sexname", new ContextImpl());
        assertEquals(dict.getName(), "sexname");
    }

}
