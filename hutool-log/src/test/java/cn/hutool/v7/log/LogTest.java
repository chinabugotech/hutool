/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.log;

import cn.hutool.v7.log.level.Level;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志门面单元测试
 * @author Looly
 *
 */
public class LogTest {

	@Test
	public void logTest(){
		final Log log = Log.get();

		// 自动选择日志实现
		log.debug("This is {} log", Level.DEBUG);
		log.info("This is {} log", Level.INFO);
		log.warn("This is {} log", Level.WARN);
	}

	/**
	 * 兼容slf4j日志消息格式测试，即第二个参数是异常对象时正常输出异常信息
	 */
	@Test
	@Disabled
	public void logWithExceptionTest() {
		final Log log = Log.get();
		final Exception e = new Exception("test Exception");
		log.error("我是错误消息", e);
	}

	@Test
	public void logNullTest(){
		final Log log = Log.get();
		log.debug(null);
		log.info(null);
		log.warn(null);
	}

	@Test
	void getLogByClassTest() {
		Log.get(LogTest.class);
	}

	@Test
	public void parameterizedMessageEdgeCasesTest() {
		final Log log = Log.get();

		// 测试不同数量的参数
		log.info("No parameters");
		log.info("One: {}", "param1");
		log.info("Two: {} and {}", "param1", "param2");
		log.info("Three: {}, {}, {}", "param1", "param2", "param3");
		log.info("Four: {}, {}, {}, {}", "param1", "param2", "param3", "param4");

		// 测试参数不足的情况
		log.info("Missing param: {} and {}", "only_one");

		// 测试参数过多的情况
		log.info("Extra param: {}", "param1", "extra_param");
	}

	@Test
	public void i18nMessageTest() {
		final Log log = Log.get();
		// 国际化消息测试
		log.info("中文消息测试");
		log.info("Message with unicode: {}", "特殊字符©®™✓✗★☆");
		log.info("多语言混排: 中文, English, 日本語, 한글");
		log.info("Emoji测试: 😀🚀🌏");
	}

	@Test
	public void complexObjectTest() {
		final Log log = Log.get();
		// 复杂对象参数测试
		final List<String> list = Arrays.asList("item1", "item2");
		final Map<String, Object> map = new HashMap<>();
		map.put("key", "value");

		log.info("List: {}", list);
		log.info("Map: {}", map);
		log.info("Null object: {}", (Object)null);
	}
}
