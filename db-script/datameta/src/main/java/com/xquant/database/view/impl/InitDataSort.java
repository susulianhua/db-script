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

import com.xquant.database.config.initdata.InitData;
import com.xquant.database.config.table.Table;
import com.xquant.database.table.TableSort;
import com.xquant.database.util.DataBaseUtil;

import java.util.Comparator;

public class InitDataSort implements Comparator<InitData> {

    public int compare(InitData initdata1, InitData initdata2) {
        Table t1 = DataBaseUtil.getTableById(initdata1.getTableId(), this.getClass().getClassLoader());
        Table t2 = DataBaseUtil.getTableById(initdata2.getTableId(), this.getClass().getClassLoader());
        return new TableSort().compare(t1, t2);
    }

}
