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
package org.tinygroup.tinysqldsl;

import org.tinygroup.tinysqldsl.base.Column;
import org.tinygroup.tinysqldsl.base.Table;

/**
 * Created by luoguo on 2015/3/11.
 */
public class ScoreTable extends Table {
    public static final ScoreTable TSCORE = new ScoreTable();
    public final Column ID = new Column(this, "id");
    public final Column NAME = new Column(this, "name");
    public final Column SCORE = new Column(this, "score");
    public final Column COURSE = new Column(this, "course");

    private ScoreTable() {
        super("score");
    }
}
