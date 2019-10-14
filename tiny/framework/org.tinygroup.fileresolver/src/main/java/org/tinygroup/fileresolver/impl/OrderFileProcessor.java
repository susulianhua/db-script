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
package org.tinygroup.fileresolver.impl;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.order.processor.OrderProcessor;
import org.tinygroup.vfs.FileObject;

/**
 * 功能说明:对象顺序文件的扫描器
 * <p>
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-5-17 <br>
 * <br>
 */
public class OrderFileProcessor extends AbstractFileProcessor {
    private static final String ORDER_FILE_NAME = ".order.xml";
    private OrderProcessor<?> orderProcessor;

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(ORDER_FILE_NAME);
    }

    public OrderProcessor<?> getOrderProcessor() {
        return orderProcessor;
    }

    public void setOrderProcessor(OrderProcessor<?> orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    public void process() {

        LOGGER.logMessage(LogLevel.INFO, "处理对象顺序文件开始");
//		OrderProcessor<?> orderProcessor = SpringBeanContainer
//				.getBean(OrderProcessor.ORDER_NAME);
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "加载对象顺序文件：[{}]",
                    fileObject.getAbsolutePath());
            orderProcessor.loadOrderFile(fileObject);
            LOGGER.logMessage(LogLevel.INFO, "加载对象顺序文件：[{}]完毕",
                    fileObject.getAbsolutePath());
        }
        LOGGER.logMessage(LogLevel.INFO, "处理对象顺序文件结束");
    }

}
