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
package org.tinygroup.metadata.stddatatype;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.metadata.TestInit;
import org.tinygroup.metadata.util.MetadataUtil;

public class StandardTypeProcessorTest extends TestCase {
    static {
        TestInit.init();
    }

    StandardTypeProcessor standardTypeProcessor;

    protected void setUp() throws Exception {
        super.setUp();
        standardTypeProcessor = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                MetadataUtil.STANDARDTYPEPROCESSOR_BEAN);
    }

    public void testGetTypeString() {
        // assertEquals("varchar2(12)",
        // standardTypeProcessor.getType( "hsvarcharid", "oracle"));
        // assertEquals("varchar2(${length})",
        // standardTypeProcessor.getType( "hsvarchar1id", "oracle"));
    }

}
