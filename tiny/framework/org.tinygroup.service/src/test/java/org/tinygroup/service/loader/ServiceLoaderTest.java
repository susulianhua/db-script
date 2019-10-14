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
package org.tinygroup.service.loader;

import junit.framework.TestCase;
import org.tinygroup.service.exception.ServiceLoadException;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.impl.ServiceRegistryImpl;

public class ServiceLoaderTest extends TestCase {
    ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
    ServiceLoader serviceLoader = new ServiceLoader1Impl();

    protected void setUp() throws Exception {
        super.setUp();
        serviceRegistry.clear();
    }

    public void testLoadService() throws ServiceLoadException {
        serviceLoader.loadService(serviceRegistry, this.getClass().getClassLoader());
        assertEquals(5, serviceRegistry.size());
    }
}
