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
package org.tinygroup.parsedsql;

import org.junit.Assert;
import org.junit.Test;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.parsedsql.base.DatabaseType;
import org.tinygroup.parsedsql.impl.DefaultSQLParser;

import java.util.HashMap;
import java.util.Map;

public class SqlParserTest {

	@Test
	public void testParserSelect() {
		SQLParser sqlParser = new DefaultSQLParser();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("user_id", "1234555");
		paramMap.put("user_name", "hello");
		Context context = new ContextImpl(paramMap);
		Context runContext = new ContextImpl();
		runContext.setParent(context);
		String sql = sqlParser
				.parse(DatabaseType.MySQL,
						"select * from user where user_id=@user_id and user_name=@user_name",
						runContext).getSqlBuilder().toSQL();
		System.out.println(sql);
		Assert.assertEquals(
				"SELECT * FROM user WHERE user_id = @user_id AND user_name = @user_name",
				sql);

	}

	@Test
	public void testParserSelect2() {
		SQLParser sqlParser = new DefaultSQLParser();
		Map<String, String> paramMap = new HashMap<String, String>();
		// paramMap.put("user_id", "1234555");
		paramMap.put("user_name", "hello");
		Context context = new ContextImpl(paramMap);
		String sql = sqlParser
				.parse(DatabaseType.MySQL,
						"select * from user where user_id=@user_id and user_name=@user_name",
						context).getSqlBuilder().toSQL();
		System.out.println(sql);
		Assert.assertEquals(
				"SELECT * FROM user WHERE  1=1  AND user_name = @user_name",
				sql);

	}

	@Test
	public void testParserUpdate() {
		SQLParser sqlParser = new DefaultSQLParser();
		Map<String, String> paramMap = new HashMap<String, String>();
		// paramMap.put("user_id", "1234555");
		paramMap.put("user_name", "hello");
		paramMap.put("user_detail", "hello1234");
		Context context = new ContextImpl(paramMap);
		String sql = sqlParser
				.parse(DatabaseType.MySQL,
						"update user set user_title=@user_title,user_detail=@user_detail,user_name=@user_name,user_id=@user_id where user_id=@user_id2",
						context).getSqlBuilder().toSQL();
		System.out.println(sql);

	}
	
	
	@Test
	public void testParserUpdateWithNow() {
		SQLParser sqlParser = new DefaultSQLParser();
		Map<String, String> paramMap = new HashMap<String, String>();
		// paramMap.put("user_id", "1234555");
		paramMap.put("user_name", "hello");
		paramMap.put("user_detail", "hello1234");
		Context context = new ContextImpl(paramMap);
		String sql = sqlParser
				.parse(DatabaseType.MySQL,
						"update user set update_time=now(),user_title=@user_title,user_detail=@user_detail,user_name=@user_name,user_id=@user_id  where user_id=@user_id2",
						context).getSqlBuilder().toSQL();
		System.out.println(sql);

	}
	

	@Test
	public void testParserDelete() {
		SQLParser sqlParser = new DefaultSQLParser();
		Map<String, String> paramMap = new HashMap<String, String>();
		// paramMap.put("user_id", "1234555");
		paramMap.put("user_name", "hello");
		Context context = new ContextImpl(paramMap);
		String sql = sqlParser
				.parse(DatabaseType.MySQL,
						"delete from user where user_id=@user_id and user_name=@user_name",
						context).getSqlBuilder().toSQL();
		System.out.println(sql);

	}
}
