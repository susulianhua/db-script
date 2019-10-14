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
package org.tinygroup.tinydb;

import java.io.Serializable;

/**
 * 分页对象
 *
 * @author renhui
 */
public class Pager implements Serializable {

    private static final int DEFAULT_LIMIT = 10;
    private Bean[] currentBeans;//当前页的记录
    private int totalCount;//总记录数
    private int currentPage;//当前页
    private int limit;//每页记录数
    private int start;//开始记录数
    private int totalPages;//总页数

    public Pager(int totalCount, int start, Bean[] beans) {
        this(totalCount, start, DEFAULT_LIMIT, beans);
    }

    public Pager(int totalCount, int start, int limit, Bean[] beans) {
        this.currentBeans = beans;
        init(totalCount, start, limit);
    }

    private void init(int totalCount, int start, int limit) {
        //设置基本参数
        this.totalCount = totalCount;
        if (limit == 0) {
            limit = DEFAULT_LIMIT;
        }
        this.limit = limit;
        this.start = start;
        this.totalPages = totalCount % limit == 0 ? totalCount / limit : totalCount / limit + 1;
        this.currentPage = start % limit == 0 ? start / limit : start / limit + 1;
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }
    }

    public Bean[] getCurrentBeans() {
        return currentBeans;
    }

    public void setCurrentBeans(Bean[] currentBeans) {
        this.currentBeans = currentBeans;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
