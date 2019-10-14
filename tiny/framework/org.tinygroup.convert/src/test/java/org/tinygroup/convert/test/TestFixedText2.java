/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * <p>
 *       http://www.gnu.org/licenses/gpl.html
 * <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.convert.test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.convert.objecttxt.fixwidth.TextToObject;

import junit.framework.TestCase;

public class TestFixedText2 extends TestCase {
	public void test() throws Exception {
		// FileUtil.readFileContent("fixwidth.txt", "GBK");
		String value = FileUtil
				.readFileContent(new File(getClass().getClassLoader().getResource("fixwidth.txt").getFile()), "GBK");
		System.out.println(value);
		Map<String, String> titleMap = new HashMap<String, String>();
		titleMap.put("name", "name");
		titleMap.put("address", "address");
		titleMap.put("length", "length");
		TextToObject<User> textToObject = new TextToObject<User>(User.class, titleMap, "\n");
		List<User> users = textToObject.convert(value);
		assertEquals(4, users.size());
		assertEquals("name12345\"", users.get(0).getName());;

	}
}
