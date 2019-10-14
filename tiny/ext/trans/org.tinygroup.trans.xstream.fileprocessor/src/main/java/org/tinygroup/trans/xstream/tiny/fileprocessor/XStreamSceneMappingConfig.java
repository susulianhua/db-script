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
package org.tinygroup.trans.xstream.tiny.fileprocessor;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.trans.xstream.base.XStreamSceneMapping;
import org.tinygroup.trans.xstream.base.XStreamSceneMappingManager;
import org.tinygroup.trans.xstream.base.XStreamSceneMappings;

import java.util.List;

public abstract class XStreamSceneMappingConfig extends AbstractFileProcessor {
    protected void loadInfo(XStreamSceneMappings converts) {
        List<XStreamSceneMapping> sceneMappings = converts.getSceneMappings();
        for (XStreamSceneMapping mapping : sceneMappings) {
            XStreamSceneMappingManager.putSceneName(mapping.getScene(),
                    mapping.getXstreamPackageName());
        }
    }

    protected void removeInfo(XStreamSceneMappings converts) {
        List<XStreamSceneMapping> sceneMappings = converts.getSceneMappings();
        for (XStreamSceneMapping mapping : sceneMappings) {
            XStreamSceneMappingManager.removeSceneName(mapping.getScene());
        }
    }
}
