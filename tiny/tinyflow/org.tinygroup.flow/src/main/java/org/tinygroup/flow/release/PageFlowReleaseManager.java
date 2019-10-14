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
 * ;
 */
/**;
 *
 */
package org.tinygroup.flow.release;

import org.tinygroup.flow.release.config.PageFlowRelease;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 举几个场景
 * 1.只有白名单
 * 		那么在列的才是可用
 * 2.只有黑名单
 * 		那么除了黑名单中的以外，都是可用
 * 3.同时存在
 * 		优先黑名单，白名单作废
 * 4.没有配置黑白名单
 * 		那么没有被过滤
 *
 * 本功能可多文件同时配置，请谨慎管理
 *
 * @author yanwj
 *
 */
public class PageFlowReleaseManager {

    /**
     * 黑名单
     */
    private static Set<String> excludes = new HashSet<String>();

    /**
     * 白名单
     */
    private static Set<String> includes = new HashSet<String>();

    public static void add(PageFlowRelease releaseInfo) {
        if (releaseInfo.getExcludes() != null) {
            for (String item : releaseInfo.getExcludes().getItems()) {
                excludes.add(item);
            }
        }
        if (releaseInfo.getIncludes() != null) {
            for (String item : releaseInfo.getIncludes().getItems()) {
                includes.add(item);
            }
        }
    }

    public static void clear() {
        excludes.clear();
        includes.clear();
    }

    public static void reload(List<PageFlowRelease> list) {
        clear();
        for (PageFlowRelease releaseInfo : list) {
            add(releaseInfo);
        }
    }

    /**
     * true 为接受 ，false 为过滤
     *
     * @param flowId
     * @return
     */
    public static boolean isAccept(String flowId) {
        //没有名单，则一直为可用
        if (excludes.size() == 0 && includes.size() == 0) {
            return true;
        }
        //优先黑名单
        if (excludes.size() > 0) {
            return !excludes.contains(flowId);
        }
        //最后白名单
        return includes.contains(flowId);
    }

}
