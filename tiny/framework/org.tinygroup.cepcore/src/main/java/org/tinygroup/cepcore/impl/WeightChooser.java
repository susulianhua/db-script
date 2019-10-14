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
package org.tinygroup.cepcore.impl;

import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.EventProcessorChoose;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.ArrayList;
import java.util.List;

public class WeightChooser implements EventProcessorChoose {

    public EventProcessor choose(List<EventProcessor> processors) {
        int totalWeight = 0;
        List<EventProcessor> backup = new ArrayList<EventProcessor>();
        for (EventProcessor eventProcessor : processors) {
            int wight = eventProcessor.getWeight();
            if (wight == 0) {
                wight = DEFAULT_WEIGHT;
            } else if (wight < 0) { //<0就是備份服務器，衹有全是備份，才會使用備份
                backup.add(eventProcessor);
                continue;
            }
            totalWeight += wight;
        }
        if (totalWeight == 0) {
            return chooseBackUp(backup);
        }

        int random = (int) (Math.random() * totalWeight);
        for (EventProcessor eventProcessor : processors) {
            int wight = eventProcessor.getWeight();
            if (wight == 0) {
                wight = DEFAULT_WEIGHT;
            } else if (wight < 0) {
                continue;
            }
            random -= wight;
            if (random < 0) { //[0,eventprocessor.getweight)
                return eventProcessor;
            }
        }
        //不会到达
        return processors.get(0);
    }

    private EventProcessor chooseBackUp(List<EventProcessor> backup) {
        int totalWeight = 0;
        for (EventProcessor p : backup) {
            totalWeight += p.getWeight();
        }
        int random = (int) (Math.random() * totalWeight);
        for (EventProcessor eventProcessor : backup) {
            int wight = eventProcessor.getWeight();
            random -= wight;
            if (random > 0) { //[0,eventprocessor.getweight)
                return eventProcessor;
            }
        }
        return backup.get(0);
    }

    public void setParam(XmlNode param) {
        //do nothing
    }

}
