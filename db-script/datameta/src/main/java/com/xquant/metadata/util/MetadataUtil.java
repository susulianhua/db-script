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
package com.xquant.metadata.util;


import com.xquant.metadata.bizdatatype.BusinessTypeProcessor;
import com.xquant.metadata.bizdatatype.impl.BusinessTypeProcessorImpl;
import com.xquant.metadata.config.PlaceholderValue;
import com.xquant.metadata.config.bizdatatype.BusinessType;
import com.xquant.metadata.config.stddatatype.DialectType;
import com.xquant.metadata.config.stddatatype.StandardType;
import com.xquant.metadata.config.stdfield.StandardField;
import com.xquant.metadata.defaultvalue.DefaultValueProcessor;
import com.xquant.metadata.defaultvalue.impl.DefaultValueProcessorImpl;
import com.xquant.metadata.exception.MetadataRuntimeException;
import com.xquant.metadata.stddatatype.StandardTypeProcessor;
import com.xquant.metadata.stddatatype.impl.StandardTypeProcessorImpl;
import com.xquant.metadata.stdfield.StandardFieldProcessor;
import com.xquant.metadata.stdfield.impl.StandardFieldProcessorImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

import static com.xquant.metadata.exception.MetadataErrorCode.BIZTYPE_LANGUAGE_TYPE_NOT_EXISTS_ERROR;

public final class MetadataUtil {
    public static final String METADATA_XSTREAM = "metadata";
    public static final String STDFIELDPROCESSOR_BEAN = "standardFieldProcessor";
    public static final String STANDARDTYPEPROCESSOR_BEAN = "standardTypeProcessor";
    public static final String BUSINESSTYPEPROCESSOR_BEAN = "businessTypeProcessor";
    public static final String CONSTANTPROCESSOR_BEAN = "constantProcessor";
    public static final String ERRORMESSAGEPROCESSOR_BEAN = "errorMessageProcessor";
    public static final String DEFAULTVALUEPROCESSOR_BEAN = "defaultValueProcessor";

    private MetadataUtil() {
    }

    public static String passNull(String string) {
        if (string == null) {
            return "";
        }
        return string;
    }

    public static String formatType(String type,
                                    List<PlaceholderValue> placeholderValueList) {
        String result = type;
        if (placeholderValueList != null) {
            for (PlaceholderValue placeholderValue : placeholderValueList) {
                if (!StringUtils.isEmpty(placeholderValue.getName())
                        && !StringUtils.isEmpty(placeholderValue.getValue())) {
                    result = result.replaceAll(
                            "[$][{]" + placeholderValue.getName() + "[}]",
                            placeholderValue.getValue());
                }

            }
        }
        return result;
    }

    public static StandardField getStandardField(String fieldId,
                                                 ClassLoader loader) {
        StandardFieldProcessor standardFieldProcessor = getStandardFieldProcessor(loader);
        return standardFieldProcessor.getStandardField(fieldId);
    }

    public static String getBaseStandardType(BusinessType businessType, String language,
                                             ClassLoader loader) {
        BusinessTypeProcessor businessTypeProcessor = getBusinessTypeProcessor(loader);
        return businessTypeProcessor.getType(businessType.getId(), language);
    }

    public static String getStandardFieldType(BusinessType businessType, String language,
                                              ClassLoader loader) {
        String stdType = getBaseStandardType(businessType, language, loader);
        String result = formatType(stdType,
                businessType.getPlaceholderValueList());
        if (result != null) {
            return result;
        }
        throw new MetadataRuntimeException(BIZTYPE_LANGUAGE_TYPE_NOT_EXISTS_ERROR, businessType.getId(), language);
    }


    public static StandardFieldProcessor getStandardFieldProcessor(
            ClassLoader loader) {
        return StandardFieldProcessorImpl.getStandardFieldProcessor();
    }

    public static DialectType getDialectType(String stdFieldId, String type,
                                             ClassLoader loader) {
        StandardType standardType = getStandardType(stdFieldId, loader);
        List<DialectType> dialectTypes = standardType.getDialectTypeList();
        if (!CollectionUtils.isEmpty(dialectTypes)) {
            for (DialectType dialectType : dialectTypes) {
                if (dialectType.getLanguage().equals(type)) {
                    return dialectType;
                }
            }
        }
        return null;
    }

    public static String getPlaceholderValue(String stdFieldId,
                                             String holderName, ClassLoader loader) {
        return getPlaceholderValue(stdFieldId, holderName, null, loader);
    }

    public static String getPlaceholderValue(String stdFieldId,
                                             String holderName, String defaultValue, ClassLoader loader) {
        BusinessType businessType = getBusinessType(stdFieldId, loader);
        List<PlaceholderValue> values = businessType.getPlaceholderValueList();
        if (!CollectionUtils.isEmpty(values)) {
            String[] names = holderName.split(",");
            for (String name : names) {
                for (PlaceholderValue value : values) {
                    if (value.getName().equals(name)) {
                        return value.getValue();
                    }
                }
            }
        }
        return defaultValue;
    }

    public static StandardType getStandardType(String fieldId,
                                               ClassLoader loader) {
        StandardField field = getStandardField(fieldId, loader);
        BusinessTypeProcessor businessTypeProcessor = getBusinessTypeProcessor(loader);
        BusinessType businessType = businessTypeProcessor
                .getBusinessTypes(field.getTypeId());
        StandardTypeProcessor standardTypeProcessor = getStandardTypeProcessor(loader);
        return standardTypeProcessor.getStandardType(businessType.getTypeId());
    }

    public static BusinessTypeProcessor getBusinessTypeProcessor(
            ClassLoader loader) {
        return BusinessTypeProcessorImpl.getBusinessTypeProcessor();
    }

    public static StandardTypeProcessor getStandardTypeProcessor(
            ClassLoader loader) {

        return StandardTypeProcessorImpl.getStandardTypeProcessor();

    }

    public static DefaultValueProcessor getDefaultValueProcessor(
            ClassLoader loader) {

        return DefaultValueProcessorImpl.getDefaultValueProcessor();

    }

    public static String getDefaultValue(String defaultId, String type,
                                         ClassLoader loader) {
        DefaultValueProcessor defaultValueProcessor = getDefaultValueProcessor(loader);
        return defaultValueProcessor.getValue(defaultId, type);
    }

    public static BusinessType getBusinessType(String fieldId,
                                               ClassLoader loader) {
        StandardField field = getStandardField(fieldId, loader);
        BusinessTypeProcessor businessTypeProcessor = getBusinessTypeProcessor(loader);
        BusinessType businessType = businessTypeProcessor
                .getBusinessTypes(field.getTypeId());
        return businessType;
    }

    public static Map<String, BusinessType> getAllBusinessTypes(ClassLoader loader) {
        BusinessTypeProcessor businessTypeProcessor = getBusinessTypeProcessor(loader);
        Map<String, BusinessType> businessTypeMap = businessTypeProcessor.getBusinessTypeMap();
        return businessTypeMap;
    }

    public static StandardType getStandardType(StandardField standardField,
                                               ClassLoader loader) {
        BusinessTypeProcessor businessTypeProcessor = getBusinessTypeProcessor(loader);
        BusinessType businessType = businessTypeProcessor
                .getBusinessTypes(standardField.getTypeId());
        return getStandardType(businessType, loader);
    }

    public static StandardType getStandardType(BusinessType businessType,
                                               ClassLoader loader) {
        StandardTypeProcessor standardTypeProcessor = getStandardTypeProcessor(loader);
        return standardTypeProcessor.getStandardType(businessType.getTypeId());
    }

    public static String getDataTpOfStdFiled(StandardField standardField, String language, ClassLoader loader) {
        BusinessType businessType = MetadataUtil.getBusinessType(standardField.getId(), loader);
        return MetadataUtil.getStandardFieldType(businessType, language,
                loader);
    }
}
