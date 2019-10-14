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
package org.tinygroup.metadata.dict;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.metadata.TestInit;
import org.tinygroup.metadata.config.dict.Dict;
import org.tinygroup.metadata.exception.MetadataRuntimeException;

public class DictProcessorTest extends TestCase {
    static {
        TestInit.init();
    }

    DictProcessor dictProcessor;

    protected void setUp() throws Exception {
        super.setUp();
        dictProcessor = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                "dictProcessor");
    }

    public void testDictProcessor() {
        Dict dict = dictProcessor.getDict("sexname");

        assertEquals(dict.getId(), "sex");
        assertEquals(dict.getDictItemsList().size(), 2);
    }

    public void testDictException() {
        try {
            dictProcessor.getDict("sex2");
            fail();
        } catch (MetadataRuntimeException metadataRuntimeException) {
            assertEquals(metadataRuntimeException.getErrorCode().toString(), "0TE120061052");
        }
    }

}
