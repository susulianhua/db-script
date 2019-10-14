/**
 * Copyright (c) 2012-2016, www.tinygroup.org (luo_guo@icloud.com).
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
package org.tinygroup.flowcomponent.test.testcase;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.vfs.VFS;

import java.io.File;
import java.util.ArrayList;

public abstract class AbstractFlowComponent extends TestCase {

    protected FlowExecutor flowExecutor;

    void init() {
        Runner.init("application.xml", new ArrayList<String>());
    }

    public void deleteFile(String filePath) {
        File file = new File(VFS.resolveFile(filePath).getAbsolutePath());
        if (file.exists()) {
            file.delete();
        }
    }

    protected void setUp() throws Exception {
        init();
        flowExecutor = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                FlowExecutor.FLOW_BEAN);
    }

}
