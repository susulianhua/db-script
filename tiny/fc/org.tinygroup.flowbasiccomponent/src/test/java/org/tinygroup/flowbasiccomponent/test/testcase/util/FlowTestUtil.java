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
package org.tinygroup.flowbasiccomponent.test.testcase.util;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.context.Context;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.tinytestutil.AbstractTestUtil;

public class FlowTestUtil extends AbstractTestUtil {
    static FlowExecutor flowExecutor;
    static FlowExecutor pageFlowExecutor;
    static FlowTestUtil flowTestUtil = new FlowTestUtil();

    private static void init() {
        init(null, true);
    }

    public static void execute(String flowId, Context context) {
        check();
        flowExecutor.execute(flowId, context);
    }

    public static void executePage(String flowId, Context context) {
        check();
        pageFlowExecutor.execute(flowId, context);
    }

    private static void check() {
        init();
        flowExecutor = BeanContainerFactory.getBeanContainer(
                FlowTestUtil.class.getClassLoader()).getBean("flowExecutor");
        pageFlowExecutor = BeanContainerFactory.getBeanContainer(
                FlowTestUtil.class.getClassLoader())
                .getBean("pageFlowExecutor");
    }

}
