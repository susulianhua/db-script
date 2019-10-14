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
package org.tinygroup.flowweblayer.tinyprocessor;

import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.AbstractTinyProcessor;
import org.tinygroup.weblayer.WebContext;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 页面流flow-processor
 *
 * @author renhui
 */
public class PageFlowTinyProcessor extends AbstractTinyProcessor {

    private final static String NODE_ID = "tiny_flow_id";
    private Logger logger = LoggerFactory
            .getLogger(PageFlowTinyProcessor.class);
    private FlowExecutor executor;

    public FlowExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(FlowExecutor executor) {
        this.executor = executor;
    }

    public void reallyProcess(String urlString, WebContext context)
            throws ServletException, IOException {
        logger.logMessage(LogLevel.INFO, "{}开始处理", urlString);
        String serviceId = getRequestId(urlString);
        String nodeId = context.get(NODE_ID);
        if (nodeId == null || "".equals(nodeId)) {
            executor.execute(serviceId, context);
        } else {
            executor.execute(serviceId, nodeId, context);
        }

        logger.logMessage(LogLevel.INFO, "{}处理结束", urlString);

    }

    public String getRequestId(String urlString) {
        int lastDot = urlString.lastIndexOf(".");
        int lastSlash = urlString.lastIndexOf("/");
        return urlString.substring(lastSlash + 1, lastDot);
    }

    @Override
    protected void customInit() throws ServletException {

    }
}
