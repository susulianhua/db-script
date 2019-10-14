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
package org.tinygroup.database;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.database.config.tablespace.TableSpace;
import org.tinygroup.database.config.tablespace.TableSpaces;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.xstream.XStreamFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwy11342 on 2016/8/1.
 */
public class TestTablespace {
    public static void main(String[] args) {
        XStream stream = XStreamFactory.getXStream(DataBaseUtil.DATABASE_XSTREAM);
        stream.processAnnotations(TableSpaces.class);

        TableSpaces tableSpaces = new TableSpaces();
        List<TableSpace> tableArrayList = new ArrayList<TableSpace>();
        tableSpaces.setTableSpaces(tableArrayList);
        TableSpace tableSpace = new TableSpace();
        tableSpace.setName("user1");
        tableSpace.setDescription("user1空间描述");
        tableSpace.setTitle("user1空间");
        tableSpace.setId("user1_tb_space");
        tableArrayList.add(tableSpace);

        tableSpace = new TableSpace();
        tableSpace.setName("user2");
        tableSpace.setDescription("user2空间描述");
        tableSpace.setTitle("user2空间");
        tableSpace.setId("user2_tb_space");
        tableArrayList.add(tableSpace);

        System.out.println(stream.toXML(tableSpaces));
    }
}
