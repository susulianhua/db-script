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
package org.tinygroup.lucene472;

import org.tinygroup.fulltext.IndexOperator;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.lucene472.builder.StaticLuceneBuilder;

/**
 * 基于Lucene的操作接口
 *
 * @author yancheng11334
 */
@SuppressWarnings("rawtypes")
public class LuceneIndexOperator extends BaseLuceneOperator implements IndexOperator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(LuceneIndexOperator.class);

    public LuceneIndexOperator() {
        super();
        luceneBuilder = new StaticLuceneBuilder();
    }

}
