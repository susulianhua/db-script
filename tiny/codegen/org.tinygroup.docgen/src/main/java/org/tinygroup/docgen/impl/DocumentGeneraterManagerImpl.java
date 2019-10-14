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
package org.tinygroup.docgen.impl;

import org.tinygroup.docgen.DocumentGenerater;
import org.tinygroup.docgen.DocumentGeneraterManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能说明: 文档生成器的多实例管理对象
 * <p>
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-7-25 <br>
 * <br>
 */
public class DocumentGeneraterManagerImpl implements DocumentGeneraterManager {
    private Map<String, DocumentGenerater> documentGeneraterMap = new HashMap<String, DocumentGenerater>();

    public void putDocumentGenerater(String type, DocumentGenerater generate) {
        documentGeneraterMap.put(type, generate);
    }

    public DocumentGenerater getFileGenerater(String type) {
        return documentGeneraterMap.get(type);
    }

}
