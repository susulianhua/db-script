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
package org.tinygroup.webservice.manager;

import org.tinygroup.webservice.config.ContextParams;
import org.tinygroup.webservice.config.PastPattern;
import org.tinygroup.webservice.config.SkipPattern;

import java.util.List;

/**
 * ws监听器参数过滤器
 *
 * @author renhui
 */
public interface ContextParamManager {

    String CONTEXT_PARAM_MANAGER_BEAN_NAME = "contextParamManager";

    String XSTEAM_PACKAGE_NAME = "contextParam";

    public void addContextParams(ContextParams contextParams);


    public void removeContextParams(ContextParams contextParams);


    public List<ContextParams> getAllContextParams();

    public List<SkipPattern> getAllSkipPattens();

    public List<PastPattern> getAllPastPatterns();

}
