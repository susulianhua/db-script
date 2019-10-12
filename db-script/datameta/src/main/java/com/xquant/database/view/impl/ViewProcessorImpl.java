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
package com.xquant.database.view.impl;

import com.xquant.database.ProcessorManager;
import com.xquant.database.config.view.RefViewIds;
import com.xquant.database.config.view.View;
import com.xquant.database.config.view.Views;
import com.xquant.database.exception.DatabaseRuntimeException;
import com.xquant.database.view.ViewProcessor;
import com.xquant.database.view.ViewSqlProcessor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static com.xquant.database.exception.DatabaseErrorCode.VIEW__ADD_ALREADY_ERROR;

@Component("viewProcessor")
public class ViewProcessorImpl implements ViewProcessor {
    private static ViewProcessor viewProcessor = new ViewProcessorImpl();
    private Map<String, View> viewMap = new HashMap<String, View>();
    private Map<String, View> viewIdMap = new HashMap<String, View>();
    private ProcessorManager processorManager;
    private Map<String, Long> viewModifiedTimeMap = new HashMap<String, Long>();
    // 视图依赖关系
    private Map<String, List<String>> dependencyMap = new HashMap<String, List<String>>();

    public static ViewProcessor getViewProcessor() {
        return viewProcessor;
    }

    public ProcessorManager getProcessorManager() {
        return processorManager;
    }

    public void setProcessorManager(ProcessorManager processorManager) {
        this.processorManager = processorManager;
    }

    public void registerModifiedTime(Views views, long lastModify) {
        for (View view : views.getViewTableList()) {
            viewModifiedTimeMap.put(view.getId(), lastModify);
        }
    }

    public long getLastModifiedTime(String viewId) {
        return viewModifiedTimeMap.get(viewId);
    }

    public void addViews(Views views) {
        for (View view : views.getViewTableList()) {
            if (viewIdMap.containsKey(view.getId())) {
                //重复视图异常
                throw new DatabaseRuntimeException(VIEW__ADD_ALREADY_ERROR, view.getName(), view.getId());
            }
            viewMap.put(view.getName(), view);
            viewIdMap.put(view.getId(), view);
        }
    }

    public void removeViews(Views views) {
        for (View view : views.getViewTableList()) {
            viewMap.remove(view.getName());
            viewIdMap.remove(view.getId());
        }

    }

    public View getView(String name) {
        if (!viewMap.containsKey(name)) {
            throw new RuntimeException(String.format("视图[name:%s]不存在,", name));
        }
        return viewMap.get(name);
    }

    public String getCreateSql(String name, String language) {
        View view = getView(name);
        return getCreateSql(view, language);
    }

    public String getCreateSql(View view, String language) {
        ViewSqlProcessor sqlProcessor = (ViewSqlProcessor) processorManager
                .getProcessor(language, "view");
        return sqlProcessor.getCreateSql(view);
    }

    public String getUpdateSql(List<String> createViewSqls, View view, Connection connection, String language) throws SQLException {
        ViewSqlProcessor sqlProcessor = (ViewSqlProcessor) processorManager
                .getProcessor(language, "view");
        String createViewSql = sqlProcessor.getUpdateSql(view);
        if (!StringUtils.isBlank(createViewSql) && checkViewExists(view, connection, language)) {
            createViewSqls.add(sqlProcessor.getDropSql(view));
        }
        return createViewSql;
    }


    public List<String> getCreateSql(String language) {
        List<String> list = new ArrayList<String>();
        List<View> views = getViews();
        for (View view : views) {
            list.add(getCreateSql(view, language));
        }
        return list;
    }

    public String getDropSql(String name, String language) {
        View view = getView(name);
        return getDropSql(view, language);
    }

    public String getDropSql(View view, String language) {
        ViewSqlProcessor sqlProcessor = (ViewSqlProcessor) processorManager
                .getProcessor(language, "view");
        return sqlProcessor.getDropSql(view);
    }

    public List<String> getDropSql(String language) {
        List<String> list = new ArrayList<String>();
        for (View view : viewMap.values()) {
            list.add(getDropSql(view, language));
        }
        return list;
    }

    public List<View> getViews() {
        List<View> list = new ArrayList<View>();
        list.addAll(viewMap.values());
        Collections.sort(list, new ViewSort(dependencyMap));
        return list;
    }

    public View getViewById(String id) {
        if (!viewIdMap.containsKey(id)) {
            throw new RuntimeException(String.format("视图[id:%s]不存在,", id));
        }
        return viewIdMap.get(id);
    }

    public void dependencyInit() {

        for (View view : viewIdMap.values()) {
            List<String> dependencies = dependencyMap.get(view.getId());
            if (dependencies == null) {
                dependencies = new ArrayList<String>();
                dependencyMap.put(view.getId(), dependencies);
            }
            RefViewIds refViews = view.getRefViewIds();
            if (refViews == null || refViews.getRefViewIdList() == null
                    || refViews.getRefViewIdList().size() == 0) {
                continue;
            }
            for (String refViewId : refViews.getRefViewIdList()) {
                View dependView = viewIdMap.get(refViewId);
                if (dependView == null) {
                    throw new RuntimeException(String.format(
                            "视图[id:%s]依赖的视图[id:%s]不存在,", view.getId(),
                            refViewId));
                }
                if (!dependencies.contains(dependView.getId())) {
                    dependencies.add(dependView.getId());
                }

            }
        }
    }

    public boolean checkViewExists(View view, Connection conn, String language) throws SQLException {
        ViewSqlProcessor sqlProcessor = (ViewSqlProcessor) processorManager.getProcessor(language, "view");
        return sqlProcessor.checkViewExists(view, conn);
    }

}
