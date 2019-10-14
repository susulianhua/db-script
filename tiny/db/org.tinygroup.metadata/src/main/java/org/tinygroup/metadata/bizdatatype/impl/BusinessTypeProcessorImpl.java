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
package org.tinygroup.metadata.bizdatatype.impl;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.metadata.bizdatatype.BusinessTypeProcessor;
import org.tinygroup.metadata.config.bizdatatype.BusinessType;
import org.tinygroup.metadata.config.bizdatatype.BusinessTypes;
import org.tinygroup.metadata.exception.MetadataRuntimeException;
import org.tinygroup.metadata.stddatatype.StandardTypeProcessor;
import org.tinygroup.metadata.stddatatype.impl.StandardTypeProcessorImpl;
import org.tinygroup.metadata.util.ConfigUtil;
import org.tinygroup.metadata.util.MetadataUtil;

import java.util.HashMap;
import java.util.Map;

import static org.tinygroup.metadata.exception.MetadataErrorCode.*;

public class BusinessTypeProcessorImpl implements BusinessTypeProcessor {
    // packagename/name/businessType
    //private static Map<String, Map<String, BusinessType>> businessTypeMap = new HashMap<String, Map<String, BusinessType>>();
    private static Logger LOGGER = LoggerFactory.getLogger(BusinessTypeProcessorImpl.class);
    private static BusinessTypeProcessor businessTypeProcessor = new BusinessTypeProcessorImpl();

    private Map<String, BusinessType> businessTypeMap = new HashMap<String, BusinessType>();
    private StandardTypeProcessor standardTypeProcessor;

    public static BusinessTypeProcessor getBusinessTypeProcessor() {
        businessTypeProcessor.setStandardTypeProcessor(StandardTypeProcessorImpl.getStandardTypeProcessor());
        return businessTypeProcessor;
    }

    public StandardTypeProcessor getStandardTypeProcessor() {
        return standardTypeProcessor;
    }

    public void setStandardTypeProcessor(
            StandardTypeProcessor standardDataTypeProcessor) {
        this.standardTypeProcessor = standardDataTypeProcessor;
    }


    public String getType(String id, String language) {
        BusinessType type = getBusinessTypes(id);
        String stdType = standardTypeProcessor.getType(type.getTypeId(), language);
        String result = MetadataUtil.formatType(stdType,
                type.getPlaceholderValueList());
        if (result != null) {
            return result;
        }
        throw new MetadataRuntimeException(BIZTYPE_LANGUAGE_TYPE_NOT_EXISTS_ERROR, id, language);
    }

    public void addBusinessTypes(BusinessTypes businessTypes) {
        if (businessTypes != null && businessTypes.getBusinessTypeList() != null) {
            for (BusinessType type : businessTypes.getBusinessTypeList()) {
                if (businessTypeMap.containsKey(type.getId())) {
                    if (ConfigUtil.isCheckStrict()) {
                        //重复id
                        throw new MetadataRuntimeException(BIZTYPE_ADD_ALREADY_ERROR, type.getName(), type.getId());
                    } else {
                        LOGGER.error(new MetadataRuntimeException(BIZTYPE_ADD_ALREADY_ERROR, type.getName(), type.getId()));
                    }
                }
                businessTypeMap.put(type.getId(), type);
            }
        }
    }

    public BusinessType getBusinessTypes(String id) {
        if (businessTypeMap.containsKey(id)) {
            return businessTypeMap.get(id);
        }
        throw new MetadataRuntimeException(BIZTYPE_NOT_EXISTS_ERROR, id);
    }

    public void removeBusinessTypes(BusinessTypes businessTypes) {
        if (businessTypes != null && businessTypes.getBusinessTypeList() != null) {
            for (BusinessType type : businessTypes.getBusinessTypeList()) {
                businessTypeMap.remove(type.getId());
            }
        }
    }

    public Map<String, BusinessType> getBusinessTypeMap() {
        return businessTypeMap;
    }
}
