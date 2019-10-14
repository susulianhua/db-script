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
package org.tinygroup.indexinstaller.impl;

import org.tinygroup.fulltext.FullText;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 抽象索引数据源基类
 *
 * @param <Config>
 * @author yancheng11334
 */
public abstract class AbstractIndexDataSource {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractIndexDataSource.class);

    private FullText fullText;

    public FullText getFullText() {
        return fullText;
    }

    public void setFullText(FullText fullText) {
        this.fullText = fullText;
    }

}
