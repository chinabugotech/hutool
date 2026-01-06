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

package cn.hutool.v7.extra.spring;

import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.core.reflect.TypeReference;
import jakarta.annotation.Resource;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SpringUtil.class, SpringUtilTest.Demo2.class})
// @ActiveProfiles("dev") // SpringUtil.getActiveProfile()效果与下面方式一致
@TestPropertySource(properties = {"spring.profiles.active=dev"})
//@Import(spring.cn.hutool.v7.extra.SpringUtil.class)
public class SpringUtilTest {

	/**
	 * 注册验证bean
	 */
	@Test
	public void registerBeanTest() {
		final Demo2 registerBean = new Demo2();
		registerBean.setId(123);
		registerBean.setName("222");
		SpringUtil.registerBean("registerBean", registerBean);

		final Demo2 registerBean2 = SpringUtil.getBean("registerBean");
		Assertions.assertEquals(123, registerBean2.getId());
		Assertions.assertEquals("222", registerBean2.getName());
	}

	/**
	 * 测试注销bean
	 */
	@Test
	public void unregisterBeanTest() {
		registerTestAutoWired();
		Assertions.assertNotNull(SpringUtil.getBean("testAutoWired"));
		SpringUtil.unregisterBean("testAutoWired1");
		try {
			SpringUtil.getBean("testAutoWired");
		} catch (final NoSuchBeanDefinitionException e) {
			Assertions.assertEquals(NoSuchBeanDefinitionException.class, e.getClass());
		}
	}

	/**
	 * 测试自动注入
	 */
	private void registerTestAutoWired() {
		TestAutoWired testAutoWired = new TestAutoWired();
		final TestBean testBean = new TestBean();
		testBean.setId("123");
		SpringUtil.registerBean("testBean", testBean);
		SpringUtil.registerBean("testAutoWired", testAutoWired);

		testAutoWired = SpringUtil.getBean("testAutoWired");
		Assertions.assertNotNull(testAutoWired);
		Assertions.assertNotNull(testAutoWired.getAutowiredBean());
		Assertions.assertNotNull(testAutoWired.getResourceBean());
		Assertions.assertEquals("123", testAutoWired.getAutowiredBean().getId());

	}

	@Test
	public void getBeanTest(){
		final Demo2 testDemo = SpringUtil.getBean("testDemo");
		Assertions.assertEquals(12345, testDemo.getId());
		Assertions.assertEquals("test", testDemo.getName());
	}

	@Test
	public void getBeanWithTypeReferenceTest() {
		final Map<String, Object> mapBean = SpringUtil.getBean(new TypeReference<>() {
		});
		Assertions.assertNotNull(mapBean);
		Assertions.assertEquals("value1", mapBean.get("key1"));
		Assertions.assertEquals("value2", mapBean.get("key2"));
	}

	@Test
	public void getActiveProfileTest() {
		final String activeProfile = SpringUtil.getActiveProfile();
		final String defaultProfile = SpringUtil.getProperty("spring.profiles.default");
		final String activeProfile2 = SpringUtil.getProperty("spring.profiles.active");
		assert "dev".equals(activeProfile);
		assert null == defaultProfile;
		assert "dev".equals(activeProfile2);
	}

	@Test
	void getEnvironmentTest() {
		ConfigurableEnvironment bean = SpringUtil.getBean(ConfigurableEnvironment.class);
		Assertions.assertNotNull(bean);
		bean = SpringUtil.getBean(ConfigurableEnvironment.class, new Object[0]);
		Assertions.assertNotNull(bean);
		bean = SpringUtil.getBean(ConfigurableEnvironment.class, (Object[])null);
		Assertions.assertNotNull(bean);
	}

	@Data
	public static class Demo2{
		private long id;
		private String name;

		@Bean(name="testDemo")
		public Demo2 generateDemo() {
			final Demo2 demo = new Demo2();
			demo.setId(12345);
			demo.setName("test");
			return demo;
		}

		@Bean(name="mapDemo")
		public Map<String, Object> generateMap() {
			final HashMap<String, Object> map = MapUtil.newHashMap();
			map.put("key1", "value1");
			map.put("key2", "value2");
			return map;
		}
	}

	@Data
	public static class TestAutoWired {

		@Autowired
		// @Resource
		private TestBean autowiredBean;

		 @Resource
		private TestBean resourceBean;
	}

	@Data
	public static class TestBean {
		private String id;
	}
}
