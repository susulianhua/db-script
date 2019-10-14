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
package org.tinygroup.tinytest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.TestContextBootstrapper;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class TinyJUnit4ClassRunner extends SpringJUnit4ClassRunner {

    private static final Log LOGGER = LogFactory.getLog(TinyJUnit4ClassRunner.class);

    public TinyJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TinyJUnit4ClassRunner constructor called with [" + clazz + "]");
        }

    }

    protected TestContextManager createTestContextManager(Class<?> testClass) {
        TestContextBootstrapper testContextBootstrapper = BootstrapUtils.resolveTestContextBootstrapper(BootstrapUtils.createBootstrapContext(testClass));
        return new TestContextManager(testContextBootstrapper);
    }

}
