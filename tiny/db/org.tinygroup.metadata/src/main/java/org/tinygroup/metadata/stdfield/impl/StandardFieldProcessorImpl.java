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
package org.tinygroup.metadata.stdfield.impl;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.metadata.bizdatatype.BusinessTypeProcessor;
import org.tinygroup.metadata.bizdatatype.impl.BusinessTypeProcessorImpl;
import org.tinygroup.metadata.config.stdfield.NickName;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.config.stdfield.StandardFields;
import org.tinygroup.metadata.exception.MetadataRuntimeException;
import org.tinygroup.metadata.stdfield.StandardFieldProcessor;
import org.tinygroup.metadata.util.ConfigUtil;

import java.util.HashMap;
import java.util.Map;

import static org.tinygroup.metadata.exception.MetadataErrorCode.*;


public class StandardFieldProcessorImpl implements StandardFieldProcessor {
    // private static Map<String, Map<String, StandardField>> standardFieldMap =
    // new HashMap<String, Map<String, StandardField>>();
    private static Logger LOGGER = LoggerFactory.getLogger(StandardFieldProcessorImpl.class);
    private static StandardFieldProcessor standardFieldProcessor = new StandardFieldProcessorImpl();
    BusinessTypeProcessor businessTypeProcessor;
    private Map<String, StandardField> standardFieldMap = new HashMap<String, StandardField>();

    public static StandardFieldProcessor getStandardFieldProcessor() {
        standardFieldProcessor.setBusinessTypeProcessor(BusinessTypeProcessorImpl.getBusinessTypeProcessor());
        return standardFieldProcessor;
    }

    public BusinessTypeProcessor getBusinessTypeProcessor() {
        return businessTypeProcessor;
    }

    public void setBusinessTypeProcessor(
            BusinessTypeProcessor businessTypeProcessor) {
        this.businessTypeProcessor = businessTypeProcessor;
    }

    public StandardField getStandardField(String id) {
        if (standardFieldMap.containsKey(id)) {
            return standardFieldMap.get(id);
        }
        throw new MetadataRuntimeException(STDFIELD_NOT_EXISTS_ERROR, id);
    }

    public void addStandardFields(StandardFields standardFields) {
        if (standardFields != null
                && standardFields.getStandardFieldList() != null) {
            for (StandardField field : standardFields.getStandardFieldList()) {
                if (standardFieldMap.containsKey(field.getId())) {
                    if (ConfigUtil.isCheckStrict()) {
                        //重复id
                        throw new MetadataRuntimeException(STDFIELD_ADD_ALREADY_ERROR, field.getId());
                    } else {
                        LOGGER.error(new MetadataRuntimeException(STDFIELD_ADD_ALREADY_ERROR, field.getId()));
                        continue;
                    }
                }
                standardFieldMap.put(field.getId(), field);
                if (field.getNickNames() != null) {
                    for (NickName name : field.getNickNames()) {
                        StandardField newStandardField = new StandardField();
                        newStandardField.setId(name.getId());
                        newStandardField.setName(name.getName());
                        newStandardField.setTitle(name.getTitle());
                        newStandardField.setDefaultValue(field.getDefaultValue());
                        newStandardField.setDescription(field.getDescription());
                        newStandardField.setTypeId(field.getTypeId());

                        standardFieldMap.put(name.getId(), newStandardField);
                    }
                }
            }
        }
    }

    public void removeStandardFields(StandardFields standardFields) {
        if (standardFields != null
                && standardFields.getStandardFieldList() != null) {
            for (StandardField field : standardFields.getStandardFieldList()) {
                standardFieldMap.remove(field.getId());
                if (field.getNickNames() != null) {
                    for (NickName name : field.getNickNames()) {
                        standardFieldMap.remove(name.getId());
                    }
                }
            }
        }
    }

    public String getType(String id, String language) {
        StandardField standardField = getStandardField(id);
        String type = businessTypeProcessor.getType(standardField.getTypeId(),
                language);
        if (type != null) {
            return type;
        }
        throw new MetadataRuntimeException(STDFIELD_LANGUAGE_TYPE_NOT_EXISTS_ERROR, id, language);
    }


}
