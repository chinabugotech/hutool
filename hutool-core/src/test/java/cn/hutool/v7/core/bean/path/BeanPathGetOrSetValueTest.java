/*
 * Copyright (c) 2013-2026 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.core.bean.path;

import cn.hutool.v7.core.bean.path.node.NameNode;
import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.lang.test.bean.ExamInfoDict;
import cn.hutool.v7.core.lang.test.bean.UserInfoDict;
import cn.hutool.v7.core.map.Dict;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanPathGetOrSetValueTest {
	Map<String, Object> tempMap;

	@BeforeEach
	public void init() {
		// ------------------------------------------------- 考试信息列表
		final ExamInfoDict examInfoDict = new ExamInfoDict();
		examInfoDict.setId(1);
		examInfoDict.setExamType(0);
		examInfoDict.setAnswerIs(1);

		final ExamInfoDict examInfoDict1 = new ExamInfoDict();
		examInfoDict1.setId(2);
		examInfoDict1.setExamType(0);
		examInfoDict1.setAnswerIs(0);

		final ExamInfoDict examInfoDict2 = new ExamInfoDict();
		examInfoDict2.setId(3);
		examInfoDict2.setExamType(1);
		examInfoDict2.setAnswerIs(0);

		final List<ExamInfoDict> examInfoDicts = new ArrayList<>();
		examInfoDicts.add(examInfoDict);
		examInfoDicts.add(examInfoDict1);
		examInfoDicts.add(examInfoDict2);

		// ------------------------------------------------- 用户信息
		final UserInfoDict userInfoDict = new UserInfoDict();
		userInfoDict.setId(1);
		userInfoDict.setPhotoPath("yx.mm.com");
		userInfoDict.setRealName("张三");
		userInfoDict.setExamInfoDict(examInfoDicts);

		tempMap = new HashMap<>();
		tempMap.put("userInfo", userInfoDict);
		tempMap.put("flag", 1);
	}

	@Test
	public void getValueTest() {
		final BeanPath<Object> pattern = BeanPath.of("$.userInfo.examInfoDict[0].id");
		final Object result = pattern.getValue(tempMap);
		Assertions.assertEquals(1, result);
	}

	@Test
	public void setValueTest() {
		final BeanPath<Object> pattern = BeanPath.of("userInfo.examInfoDict[0].id");
		pattern.setValue(tempMap, 2);
		final Object result = pattern.getValue(tempMap);
		Assertions.assertEquals(2, result);
	}

	@Test
	public void getMapTest () {
		final BeanPath<Object> pattern = BeanPath.of("userInfo[id, photoPath]");
		@SuppressWarnings("unchecked")
		final Map<String, Object> result = (Map<String, Object>)pattern.getValue(tempMap);
		Assertions.assertEquals(1, result.get("id"));
		Assertions.assertEquals("yx.mm.com", result.get("photoPath"));
	}

	@Test
	public void getKeyWithDotTest () {
		final Map<String, Object> dataMap = new HashMap<>(16);
		dataMap.put("aa", "value0");
		dataMap.put("aa.bb.cc", "value111111");//     key   是类名 格式 带 ' . '

		final BeanPath<Object> pattern = BeanPath.of("'aa.bb.cc'");
		Assertions.assertEquals("value111111", pattern.getValue(dataMap));
	}

	@Test
	public void issue2362Test() {
		final Map<String, Object> map = new HashMap<>();

		BeanPath<Object> beanPath = BeanPath.of("list[0].name");
		beanPath.setValue(map, "张三");
		Assertions.assertEquals("{list=[{name=张三}]}", map.toString());

		map.clear();
		beanPath = BeanPath.of("list[1].name");
		beanPath.setValue(map, "张三");
		Assertions.assertEquals("{list=[null, {name=张三}]}", map.toString());

		map.clear();
		beanPath = BeanPath.of("list[0].1.name");
		beanPath.setValue(map, "张三");
		Assertions.assertEquals("{list=[[null, {name=张三}]]}", map.toString());
	}

	@Test
	public void putTest() {
		final Map<String, Object> map = new HashMap<>();

		final BeanPath<Object> beanPath = BeanPath.of("list[1].name");
		beanPath.setValue(map, "张三");
		Assertions.assertEquals("{list=[null, {name=张三}]}", map.toString());
	}

	@Test
	public void putByPathTest() {
		final Dict dict = new Dict();
		BeanPath.of("aa.bb").setValue(dict, "BB");
		Assertions.assertEquals("{aa={bb=BB}}", dict.toString());
	}

	@Test
	public void appendArrayTest(){
		// issue#3008@Github
		final MyUser myUser = new MyUser();
		BeanPath.of("hobby[0]").setValue(myUser, "LOL");
		BeanPath.of("hobby[1]").setValue(myUser, "KFC");
		BeanPath.of("hobby[2]").setValue(myUser, "COFFE");

		Assertions.assertEquals("[LOL, KFC, COFFE]", ArrayUtil.toString(myUser.getHobby()));
	}

	@Test
	public void appendArrayTest2(){
		// issue#3008@Github
		final MyUser2 myUser = new MyUser2();
		BeanPath.of("myUser.hobby[0]").setValue(myUser, "LOL");
		BeanPath.of("myUser.hobby[1]").setValue(myUser, "KFC");
		BeanPath.of("myUser.hobby[2]").setValue(myUser, "COFFE");

		Assertions.assertEquals("[LOL, KFC, COFFE]", ArrayUtil.toString(myUser.getMyUser().getHobby()));
	}

	@Data
	static class MyUser {
		private String[] hobby;
	}

	@Data
	static class MyUser2 {
		private MyUser myUser;
	}

	@Test
	void returnsExpandedRootAndReconnectsExpandedIntermediateArray() {
		final Object[] empty = {};
		final BeanPath<Object> path = BeanPath.of("[0].detail.name");
		final Object[] expanded = (Object[]) path.setValue(empty, "value");
		Assertions.assertNotSame(empty, expanded);
		Assertions.assertEquals("value", path.getValue(expanded));
		final Map<String, Object> root = new HashMap<>();
		root.put("items", empty);
		final BeanPath<Object> nested = BeanPath.of("items[0].detail.name");
		Assertions.assertSame(root, nested.setValue(root, "nested"));
		Assertions.assertEquals("nested", nested.getValue(root));
	}


	@Test
	void propagatesCopyOnWriteFactoryResultsToTheRoot() {
		final NodeBeanFactory<Map<String, Object>> factory = new NodeBeanFactory<Map<String, Object>>() {
			@Override
			public Map<String, Object> create(Map<String, Object> parent, BeanPath<Map<String, Object>> path) {
				return new HashMap<>();
			}

			@Override
			public Object getValue(Map<String, Object> bean, BeanPath<Map<String, Object>> path) {
				return bean.get(((NameNode) path.getNode()).getName());
			}

			@Override
			public Map<String, Object> setValue(Map<String, Object> bean, Object value, BeanPath<Map<String, Object>> path) {
				final Map<String, Object> updated = new HashMap<>(bean);
				updated.put(((NameNode) path.getNode()).getName(), value);
				return updated;
			}
		};
		final Map<String, Object> original = new HashMap<>();
		final BeanPath<Map<String, Object>> path = BeanPath.of("a.b.c", factory);
		@SuppressWarnings("unchecked")
		final Map<String, Object> updated = (Map<String, Object>) path.setValue(original, 42);
		Assertions.assertTrue(original.isEmpty());
		Assertions.assertEquals(42, path.getValue(updated));
	}
}
