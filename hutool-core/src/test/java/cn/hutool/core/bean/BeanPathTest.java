package cn.hutool.core.bean;

import cn.hutool.core.lang.test.bean.ExamInfoDict;
import cn.hutool.core.lang.test.bean.UserInfoDict;
import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link BeanPath} 单元测试
 *
 * @author looly
 */
public class BeanPathTest {

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
	public void beanPathTest1() {
		final BeanPath pattern = new BeanPath("userInfo.examInfoDict[0].id");
		assertEquals("userInfo", pattern.patternParts.get(0));
		assertEquals("examInfoDict", pattern.patternParts.get(1));
		assertEquals("0", pattern.patternParts.get(2));
		assertEquals("id", pattern.patternParts.get(3));

	}

	@Test
	public void beanPathTest2() {
		final BeanPath pattern = new BeanPath("[userInfo][examInfoDict][0][id]");
		assertEquals("userInfo", pattern.patternParts.get(0));
		assertEquals("examInfoDict", pattern.patternParts.get(1));
		assertEquals("0", pattern.patternParts.get(2));
		assertEquals("id", pattern.patternParts.get(3));
	}

	@Test
	public void beanPathTest3() {
		final BeanPath pattern = new BeanPath("['userInfo']['examInfoDict'][0]['id']");
		assertEquals("userInfo", pattern.patternParts.get(0));
		assertEquals("examInfoDict", pattern.patternParts.get(1));
		assertEquals("0", pattern.patternParts.get(2));
		assertEquals("id", pattern.patternParts.get(3));
	}

	@Test
	public void getTest() {
		final BeanPath pattern = BeanPath.create("userInfo.examInfoDict[0].id");
		final Object result = pattern.get(tempMap);
		assertEquals(1, result);
	}

	@Test
	public void setTest() {
		final BeanPath pattern = BeanPath.create("userInfo.examInfoDict[0].id");
		pattern.set(tempMap, 2);
		final Object result = pattern.get(tempMap);
		assertEquals(2, result);
	}

	@Test
	public void getMapTest() {
		final BeanPath pattern = BeanPath.create("userInfo[id, photoPath]");
		@SuppressWarnings("unchecked") final Map<String, Object> result = (Map<String, Object>) pattern.get(tempMap);
		assertEquals(1, result.get("id"));
		assertEquals("yx.mm.com", result.get("photoPath"));
	}

	@Test
	public void issue2362Test() {
		final Map<String, Object> map = new HashMap<>();

		BeanPath beanPath = BeanPath.create("list[0].name");
		beanPath.set(map, "张三");
		assertEquals("{list=[{name=张三}]}", map.toString());

		map.clear();
		beanPath = BeanPath.create("list[1].name");
		beanPath.set(map, "张三");
		assertEquals("{list=[null, {name=张三}]}", map.toString());

		map.clear();
		beanPath = BeanPath.create("list[0].1.name");
		beanPath.set(map, "张三");
		assertEquals("{list=[[null, {name=张三}]]}", map.toString());
	}

	@Test
	public void appendArrayTest(){
		// issue#3008@Github
		final MyUser myUser = new MyUser();
		BeanPath.create("hobby[0]").set(myUser, "LOL");
		BeanPath.create("hobby[1]").set(myUser, "KFC");
		BeanPath.create("hobby[2]").set(myUser, "COFFE");

		assertEquals("[LOL, KFC, COFFE]", ArrayUtil.toString(myUser.getHobby()));
	}

	@Test
	public void wildcardTest() {
		// 测试通配符 * 语法
		final BeanPath pattern = BeanPath.create("userInfo.examInfoDict[*].id");
		final Object result = pattern.get(tempMap);
		
		// 应该返回一个包含所有 examInfoDict 元素的 id 的列表
		assertEquals("[1, 2, 3]", result.toString());
	}

	@Data
	static class MyUser {
		private String[] hobby;
	}

	@Test
	void createsMissingPathWithoutRestartingAtRoot() {
		final int[] reads = {0};
		final Map<String, Object> root = new HashMap<String, Object>() {
			@Override
			public Object get(Object key) {
				reads[0]++;
				return super.get(key);
			}

			@Override
			public Object put(String key, Object value) {
				// 模拟容器在写入时替换为自己的 Map 实例。
				return super.put(key, value instanceof Map ? new LinkedHashMap<>() : value);
			}
		};
		final BeanPath path = BeanPath.create("a.b.c[2].d.e.f");
		path.set(root, "value");
		assertEquals(2, reads[0]);
		assertEquals("value", path.get(root));
	}

	@Test
	void createsIntermediateNodeWhenNestedArrayGrows() {
		final Map<String, Object> root = new HashMap<>();
		root.put("items", new Object[0]);
		final BeanPath path = BeanPath.create("items[0].detail.name");
		path.set(root, "value");
		assertEquals("value", path.get(root));
		assertEquals(1, ((Object[]) root.get("items")).length);
	}


	@Test
	void preservesSignedUnicodeAndOverflowingPathParts() {
		final Map<String, Object> root = new HashMap<>();
		BeanPath.create("signed[+1].value").set(root, "s");
		BeanPath.create("unicode[１].value").set(root, "u");
		BeanPath.create("large[2147483648].value").set(root, "m");
		assertEquals("s", BeanPath.create("signed[1].value").get(root));
		assertEquals("u", BeanPath.create("unicode[1].value").get(root));
		assertEquals("m", BeanPath.create("large['2147483648'].value").get(root));
	}

	@Test
	void preservesRootAliasAndProjectionWriteSemantics() {
		final Map<String, Object> root = new HashMap<>();
		BeanPath.create("outer[0].hashMap[0].value").set(root, 7);
		assertEquals(7, BeanPath.create("outer[0].hashMap[0].value").get(root));
		assertFalse(root.containsKey("value"));

		final List<Object> rows = new ArrayList<>();
		root.put("rows", rows);
		assertThrows(IllegalArgumentException.class, () -> BeanPath.create("rows.name[2].value").set(root, "x"));
		assertEquals(0, rows.size());
	}
}
