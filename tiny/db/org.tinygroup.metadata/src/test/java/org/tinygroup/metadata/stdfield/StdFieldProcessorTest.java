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
package org.tinygroup.metadata.stdfield;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.metadata.TestInit;
import org.tinygroup.metadata.util.MetadataUtil;

public class StdFieldProcessorTest extends TestCase {

    static {
        TestInit.init();
    }

    StandardFieldProcessor standardFieldProcessor;

    protected void setUp() throws Exception {
        super.setUp();
        standardFieldProcessor = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                MetadataUtil.STDFIELDPROCESSOR_BEAN);
    }

    public void testGetTypeStringStringString() {
        // assertEquals("varchar2(12)",
        // standardFieldProcessor.getType( "aaid", "oracle"));
        // assertEquals("varchar2(112)",
        // standardFieldProcessor.getType( "bbid", "oracle"));
        // assertEquals("varchar2(12)",
        // standardFieldProcessor.getType( "aa1id", "oracle"));
        // assertEquals("varchar2(112)",
        // standardFieldProcessor.getType( "bb1id", "oracle"));
    }

}
