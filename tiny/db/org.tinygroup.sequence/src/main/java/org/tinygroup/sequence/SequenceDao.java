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
package org.tinygroup.sequence;

import org.tinygroup.sequence.exception.SequenceException;

/**
 * 序列dao接口
 *
 * @author renhui
 */
public interface SequenceDao {
    /**
     * 取得下一个可用的序列区间
     *
     * @param name 序列名称
     * @return 返回下一个可用的序列区间
     * @throws SequenceException
     */
    SequenceRange nextRange(String name) throws SequenceException;

}