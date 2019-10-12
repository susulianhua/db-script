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
package com.xquant.metadata.dict.impl;

import com.xquant.metadata.config.dict.Dict;
import com.xquant.metadata.config.dict.Dicts;
import com.xquant.metadata.dict.DictProcessor;
import com.xquant.metadata.exception.MetadataRuntimeException;
import com.xquant.metadata.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.xquant.metadata.exception.MetadataErrorCode.DICT_ADD_ALREADY_ERROR;
import static com.xquant.metadata.exception.MetadataErrorCode.DICT_NOT_EXISTS_ERROR;


/**
 * Created by wangwy11342 on 2017/5/19.
 */
public class DictProcessorImpl implements DictProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DictProcessorImpl.class);

    private static DictProcessor dictProcessor;

    private Map<String, Dict> dictMap = new HashMap<String, Dict>();

    public static DictProcessor getDictProcessor() {
        if (dictProcessor == null) {
            dictProcessor = new DictProcessorImpl();
        }
        return dictProcessor;
    }

    @Override
    public void removeDicts(Dicts dicts) {
        if (dicts != null && dicts.getDictList() != null) {
            for (Dict dict : dicts.getDictList()) {
                dictMap.remove(dict.getId());
            }
        }
    }

    @Override
    public void addDicts(Dicts dicts) {
        if (dicts != null
                && dicts.getDictList() != null) {
            for (Dict dict : dicts
                    .getDictList()) {
                if (dictMap.containsKey(dict.getName())) {
                    if (ConfigUtil.isCheckStrict()) {
                        //重复id
                        throw new MetadataRuntimeException(DICT_ADD_ALREADY_ERROR, dict.getName(), dict.getId());
                    } else {
                        LOGGER.error((new MetadataRuntimeException(DICT_ADD_ALREADY_ERROR, dict.getName(), dict.getId())).toString());
                    }
                }
                dictMap.put(dict.getName(), dict);
            }
        }
    }

    @Override
    public Dict getDict(String dictId) {
        Dict dict = dictMap.get(dictId);
        if (dict == null) {
            throw new MetadataRuntimeException(DICT_NOT_EXISTS_ERROR, dictId);
        }
        return dict;

    }

    @Override
    public Map<String, Dict> getDictMap() {
        return dictMap;
    }

}
