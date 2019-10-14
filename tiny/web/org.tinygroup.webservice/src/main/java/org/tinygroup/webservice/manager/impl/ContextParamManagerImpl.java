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
package org.tinygroup.webservice.manager.impl;

import org.tinygroup.webservice.config.ContextParams;
import org.tinygroup.webservice.config.PastPattern;
import org.tinygroup.webservice.config.SkipPattern;
import org.tinygroup.webservice.manager.ContextParamManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数管理器实现类
 *
 * @author renhui
 */
public class ContextParamManagerImpl implements ContextParamManager {

    private List<ContextParams> contextParamList = new ArrayList<ContextParams>();

    public void addContextParams(ContextParams contextParams) {
        contextParamList.add(contextParams);
    }

    public void removeContextParams(ContextParams contextParams) {
        contextParamList.remove(contextParams);
    }

    public List<ContextParams> getAllContextParams() {
        return contextParamList;
    }

    public List<SkipPattern> getAllSkipPattens() {
        List<SkipPattern> skipPatterns = new ArrayList<SkipPattern>();
        for (ContextParams contextParams : contextParamList) {
            skipPatterns.addAll(contextParams.getSkipPatterns());
        }
        return skipPatterns;
    }

    public List<PastPattern> getAllPastPatterns() {
        List<PastPattern> pastPatterns = new ArrayList<PastPattern>();
        for (ContextParams contextParams : contextParamList) {
            pastPatterns.addAll(contextParams.getPastPatterns());
        }
        return pastPatterns;
    }

}
