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
package org.tinygroup.metadata.dict;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.metadata.config.dict.Dict;
import org.tinygroup.metadata.config.dict.DictItem;
import org.tinygroup.metadata.config.dict.Dicts;
import org.tinygroup.xstream.XStreamFactory;

import java.util.ArrayList;
import java.util.List;

public class DictDataTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        XStream stream = XStreamFactory.getXStream();
        stream.autodetectAnnotations(true);
        stream.processAnnotations(Dicts.class);

        Dicts dicts = new Dicts();
        List<Dict> dictList = new ArrayList<Dict>();
        dicts.setDictList(dictList);
        Dict dict = new Dict();
        dictList.add(dict);
        dict.setId("simple");
        dict.setName("simple");
        List<DictItem> dictItemList = new ArrayList<DictItem>();
        dict.setDictItemsList(dictItemList);
        DictItem dictItem = new DictItem();
        dictItem.setText("man");
        dictItem.setValue("1");
        dictItem.setOrder("2");
        dictItemList.add(dictItem);

        DictItem dictItem2 = new DictItem();
        dictItem2.setText("woman");
        dictItem2.setValue("2");
        dictItem2.setOrder("1");
        dictItemList.add(dictItem2);

        System.out.println(stream.toXML(dicts));
    }

}
