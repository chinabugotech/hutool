package cn.hutool.core.bean;

import cn.hutool.core.lang.test.bean.ExamInfoDict;
import cn.hutool.core.lang.test.bean.UserInfoDict;
import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

	/**
	 * 测试key中包含冒号（使用转义）
	 */
	@Test
	public void keyWithColonTest() {
		final Map<String, Object> map = new HashMap<>();
		map.put("a:b", "value1");
		map.put("time:12:30", "value2");

		// 使用 \: 转义冒号
		BeanPath beanPath = BeanPath.create("a\\:b");
		assertEquals("value1", beanPath.get(map));

		beanPath = BeanPath.create("time\\:12\\:30");
		assertEquals("value2", beanPath.get(map));
	}

	/**
	 * 测试key中包含逗号（使用转义）
	 */
	@Test
	public void keyWithCommaTest() {
		final Map<String, Object> map = new HashMap<>();
		map.put("a,b", "value1");
		map.put("x,y,z", "value2");

		// 使用 \, 转义逗号
		BeanPath beanPath = BeanPath.create("a\\,b");
		assertEquals("value1", beanPath.get(map));

		beanPath = BeanPath.create("x\\,y\\,z");
		assertEquals("value2", beanPath.get(map));
	}

	/**
	 * 测试key中包含转义单引号
	 */
	@Test
	public void keyWithEscapedQuoteTest() {
		final Map<String, Object> map = new HashMap<>();
		map.put("it's", "value1");
		map.put("test'key", "value2");

		// 使用 \' 转义单引号
		BeanPath beanPath = BeanPath.create("['it\\'s']");
		assertEquals("value1", beanPath.get(map));

		beanPath = BeanPath.create("['test\\'key']");
		assertEquals("value2", beanPath.get(map));
	}

	/**
	 * 测试嵌套使用包含特殊字符的key
	 */
	@Test
	public void nestedSpecialKeyTest() {
		final Map<String, Object> map = new HashMap<>();
		final Map<String, Object> inner = new HashMap<>();
		inner.put("key:1", "innerValue");
		map.put("outer,key", inner);

		// 嵌套访问包含特殊字符的key（使用转义）
		BeanPath beanPath = BeanPath.create("outer\\,key.key\\:1");
		assertEquals("innerValue", beanPath.get(map));
	}

	/**
	 * 测试切片表达式仍然正常工作
	 */
	@Test
	public void sliceExpressionTest() {
		final List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
		list.add("e");

		final Map<String, Object> map = new HashMap<>();
		map.put("items", list);

		// 切片表达式仍然有效
		BeanPath beanPath = BeanPath.create("items[1:3]");
		Object result = beanPath.get(map);
		assertEquals("[b, c]", result.toString());
	}

	/**
	 * 测试多选索引表达式仍然正常工作
	 */
	@Test
	public void multiIndexExpressionTest() {
		final List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");

		final Map<String, Object> map = new HashMap<>();
		map.put("items", list);

		// 多选索引表达式仍然有效
		BeanPath beanPath = BeanPath.create("items[0,2]");
		Object result = beanPath.get(map);
		assertEquals("[a, c]", result.toString());
	}

	/**
	 * 测试set方法支持包含冒号的key
	 */
	@Test
	public void setKeyWithColonTest() {
		final Map<String, Object> map = new HashMap<>();

		// 使用转义冒号的key进行set
		BeanPath beanPath = BeanPath.create("a\\:b");
		beanPath.set(map, "value1");
		assertEquals("value1", map.get("a:b"));

		// 再次验证get
		assertEquals("value1", beanPath.get(map));
	}

	/**
	 * 测试set方法支持包含逗号的key
	 */
	@Test
	public void setKeyWithCommaTest() {
		final Map<String, Object> map = new HashMap<>();

		// 使用转义逗号的key进行set
		BeanPath beanPath = BeanPath.create("x\\,y\\,z");
		beanPath.set(map, "value2");
		assertEquals("value2", map.get("x,y,z"));

		// 再次验证get
		assertEquals("value2", beanPath.get(map));
	}

	/**
	 * 测试set方法支持包含单引号的key
	 */
	@Test
	public void setKeyWithQuoteTest() {
		final Map<String, Object> map = new HashMap<>();

		// 使用转义单引号的key进行set
		BeanPath beanPath = BeanPath.create("it\\'s");
		beanPath.set(map, "value3");
		assertEquals("value3", map.get("it's"));

		// 再次验证get
		assertEquals("value3", beanPath.get(map));
	}

	/**
	 * 测试set方法支持嵌套特殊字符key
	 */
	@Test
	public void setNestedSpecialKeyTest() {
		final Map<String, Object> map = new HashMap<>();

		// 嵌套设置包含特殊字符的key
		BeanPath beanPath = BeanPath.create("outer\\,key.inner\\:key");
		beanPath.set(map, "nestedValue");

		// 验证结构
		@SuppressWarnings("unchecked")
		Map<String, Object> outer = (Map<String, Object>) map.get("outer,key");
		assertEquals("nestedValue", outer.get("inner:key"));

		// 再次验证get
		assertEquals("nestedValue", beanPath.get(map));
	}

	@Data
	static class MyUser {
		private String[] hobby;
	}
}
