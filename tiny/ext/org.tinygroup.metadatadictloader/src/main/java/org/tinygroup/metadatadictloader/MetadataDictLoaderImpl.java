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
package org.tinygroup.metadatadictloader;

import org.tinygroup.dict.Dict;
import org.tinygroup.dict.DictGroup;
import org.tinygroup.dict.DictItem;
import org.tinygroup.dict.DictManager;
import org.tinygroup.dict.impl.AbstractDictLoader;
import org.tinygroup.metadata.dict.DictProcessor;

import java.util.Map;

/**
 * Created by wangwy11342 on 2017/5/19.
 */
public class MetadataDictLoaderImpl extends AbstractDictLoader {
    private DictProcessor dictProcessor;

    public void setDictProcessor(DictProcessor dictProcessor) {
        this.dictProcessor = dictProcessor;
    }

    public void load(DictManager dictManager) {
        Map<String, org.tinygroup.metadata.config.dict.Dict> dictConfigMap = dictProcessor.getDictMap();
        for (String dictKey : dictConfigMap.keySet()) {
            org.tinygroup.metadata.config.dict.Dict configDict = dictConfigMap.get(dictKey);
            Dict dict = new Dict();
            dict.setName(configDict.getName());
            dict.setDescription(configDict.getDescription());
            DictGroup dictGroup = new DictGroup();
            for (org.tinygroup.metadata.config.dict.DictItem configDictItem : configDict.getDictItemsList()) {
                DictItem dictItem = new DictItem();
                dictItem.setText(configDictItem.getText());
                dictItem.setValue(configDictItem.getValue());
                dictGroup.addDictItem(dictItem);
            }
            dict.addDictGroup(dictGroup);
            putDict(dictKey, dict, dictManager);

        }
    }
}
