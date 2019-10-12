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
package com.xquant.metadata.stddatatype.impl;

import com.xquant.metadata.config.stddatatype.DialectType;
import com.xquant.metadata.config.stddatatype.StandardType;
import com.xquant.metadata.config.stddatatype.StandardTypes;
import com.xquant.metadata.exception.MetadataRuntimeException;
import com.xquant.metadata.stddatatype.StandardTypeProcessor;
import com.xquant.metadata.util.ConfigUtil;
import com.xquant.metadata.util.MetadataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.xquant.metadata.exception.MetadataErrorCode.STDTYPE_ADD_ALREADY_ERROR;
import static com.xquant.metadata.exception.MetadataErrorCode.STDTYPE_NOT_EXISTS_ERROR;

/**
 * @author luoguo
 */
public class StandardTypeProcessorImpl implements StandardTypeProcessor {
    private static StandardTypeProcessor standardTypeProcessor = new StandardTypeProcessorImpl();
    private static Logger LOGGER = LoggerFactory.getLogger(StandardTypeProcessorImpl.class);
    private Map<String, StandardType> standardMap = new HashMap<String, StandardType>();

    public static StandardTypeProcessor getStandardTypeProcessor() {
        return standardTypeProcessor;
    }

    public String getType(String id, String language) {
        StandardType standardType = standardMap.get(id);
        if (standardType == null) {
            throw new RuntimeException(String.format(
                    "不存在, 标准类型ID:[%s],语言:[%s]对应的标准数据类型", id, language));
        }
        return getType(standardType, language);
    }

    private String getType(StandardType standardType, String language) {
        if (standardType.getDialectTypeList() != null) {
            for (DialectType dialectType : standardType.getDialectTypeList()) {
                if (dialectType.getLanguage().equals(language)) {
                    return MetadataUtil.formatType(dialectType.getType(),
                            dialectType.getPlaceholderValueList());
                }
            }
        }
        throw new RuntimeException(String.format(
                "不存在, 标准类型ID:[%s],语言:[%s]对应的类型 ", standardType.getId(), language));
    }

    public void addStandardTypes(StandardTypes standardTypes) {
        if (standardTypes != null
                && standardTypes.getStandardTypeList() != null) {
            for (StandardType standardType : standardTypes
                    .getStandardTypeList()) {
                if (standardMap.containsKey(standardType.getId())) {
                    if (ConfigUtil.isCheckStrict()) {
                        //重复id
                        throw new MetadataRuntimeException(STDTYPE_ADD_ALREADY_ERROR, standardType.getName(), standardType.getId());
                    } else {
                        LOGGER.error((new MetadataRuntimeException(STDTYPE_ADD_ALREADY_ERROR, standardType.getName(), standardType.getId())).toString());
                        continue;
                    }
                }
                standardMap.put(standardType.getId(), standardType);
            }
        }
    }

    public void removeStandardTypes(StandardTypes standardTypes) {
        if (standardTypes != null
                && standardTypes.getStandardTypeList() != null) {
            for (StandardType standardType : standardTypes
                    .getStandardTypeList()) {
                standardMap.remove(standardType.getId());
            }
        }
    }

    public StandardType getStandardType(String id) {
        if (standardMap.containsKey(id))
            return standardMap.get(id);
        throw new MetadataRuntimeException(STDTYPE_NOT_EXISTS_ERROR, id);
    }

}
