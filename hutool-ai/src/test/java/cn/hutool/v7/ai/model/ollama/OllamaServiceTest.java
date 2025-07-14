/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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
package cn.hutool.v7.ai.model.ollama;

import cn.hutool.v7.ai.AIServiceFactory;
import cn.hutool.v7.ai.ModelName;
import cn.hutool.v7.ai.core.AIConfigBuilder;
import cn.hutool.v7.ai.core.Message;
import cn.hutool.v7.core.text.split.SplitUtil;
import cn.hutool.v7.core.thread.ThreadUtil;
import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONArray;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * OllamaService
 *
 * @author yangruoyu-yumeisoft
 * @since 5.8.40
 */
class OllamaServiceTest {
	// 创建service
	OllamaService ollamaService = AIServiceFactory.getAIService(
		new AIConfigBuilder(ModelName.OLLAMA.getValue())
			// 这里填写Ollama服务的地址
			.setApiUrl("http://127.0.0.1:11434")
			// 这里填写使用的模型
			.setModel("qwen2.5-coder:32b")
			.build(),
		OllamaService.class
	);

	// 假设有一个Java工程师的Agent提示词
	String javaEngineerPrompt= """
		# 角色
		你是一位精通Spring Boot 3.0的资深Java全栈工程师，具备以下核心能力：
		- 精通Spring Boot 3.0新特性与最佳实践
		- 熟练整合Hutool工具包、Redis数据访问、Feign远程调用、FreeMarker模板引擎
		- 能输出符合工程规范的代码结构和配置文件
		- 注重代码可读性与注释规范

		# 任务
		请完成以下编程任务（按优先级排序）：
		1. **核心要求**
		   - 使用Spring Boot 3.0构建项目
		   - 必须包含以下依赖：
		     - `cn.hutool:hutool-all`（最新版）
		     - `org.springframework.boot:spring-boot-starter-data-redis`
		     - `org.springframework.cloud:spring-cloud-starter-openfeign`
		     - `org.springframework.boot:spring-boot-starter-freemarker`
		2. **约束条件**
		   - 代码需符合Java 17语法规范
		   - 每个类必须包含Javadoc风格的类注释
		   - 关键方法需添加`@Api`/`@ApiOperation`注解（若涉及接口）
		   - Redis操作需使用`RedisTemplate`实现
		3. **实现流程**
		   ```
		   1. 生成pom.xml依赖配置
		   2. 创建基础配置类（如RedisConfig）
		   3. 编写Feign客户端接口
		   4. 实现FreeMarker模板渲染服务
		   5. 提供完整Controller示例
		   ```

		# 输出要求
		请以严格Markdown格式输出，每个模块独立代码块：
		```markdown
		## 1. 项目依赖配置（pom.xml片段）
		```xml
		<dependency>...</dependency>
		```

		## 2. Redis配置类
		```java
		@Configuration
		public class RedisConfig { ... }
		```

		## 3. Feign客户端示例
		```java
		@FeignClient(name = "...")
		public interface ... { ... }
		```

		## 4. FreeMarker模板服务
		```java
		@Service
		public class TemplateService { ... }
		```

		## 5. 控制器示例
		```java
		@RestController
		@RequestMapping("/example")
		public class ExampleController { ... }
		```
		```

		# 示例片段（供格式参考）
		```java
		/**
		 * 示例Feign客户端
		 * @since 1.0.0
		 */
		@FeignClient(name = "demo-service", url = "${demo.service.url}")
		public interface DemoClient {

		    @GetMapping("/data/{id}")
		    @ApiOperation("获取示例数据")
		    ResponseEntity<String> getData(@PathVariable("id") Long id);
		}
		```

		请按此规范输出完整代码结构，确保自动化程序可直接解析生成项目文件。""";

	/**
	 * 同步方式调用
	 */
	@Test
	@Disabled
	void testSimple() {
		final String answer = ollamaService.chat("写一个疯狂星期四广告词");
		assertNotNull(answer);
	}

	/**
	 * 按流方式输出
	 */
	@Test
	@Disabled
	void testStream() {
		final AtomicBoolean isDone = new AtomicBoolean(false);
		final AtomicReference<String> errorMessage = new AtomicReference<>();
		ollamaService.chat("写一个疯狂星期四广告词", data -> {
			// 输出到控制台
			final JSON streamData = JSONUtil.parse(data);
			if (streamData.getByPath("error") != null) {
				isDone.set(true);
				errorMessage.set(streamData.getByPath("error").toString());
				return;
			}

			if ("true".equals(streamData.getByPath("done").toString())) {
				isDone.set(true);
			}
		});
		// 轮询检查结束标志
		while (!isDone.get()) {
			ThreadUtil.sleep(100);
		}
		if (errorMessage.get() != null) {
			throw new RuntimeException(errorMessage.get());
		}
	}

	/**
	 * 带历史上下文的同步方式调用
	 */
	@Test
	@Disabled
	void testSimpleWithHistory(){
		final List<Message> messageList=new ArrayList<>();
		messageList.add(new Message("system",javaEngineerPrompt));
		messageList.add(new Message("user","帮我写一个Java通过Post方式发送JSON给HTTP接口，请求头带有token"));
		final String result = ollamaService.chat(messageList);
		assertNotNull(result);
	}

	@Test
	@Disabled
	void testStreamWithHistory(){
		final List<Message> messageList=new ArrayList<>();
		messageList.add(new Message("system",javaEngineerPrompt));
		messageList.add(new Message("user","帮我写一个Java通过Post方式发送JSON给HTTP接口，请求头带有token"));
		final AtomicBoolean isDone = new AtomicBoolean(false);
		final AtomicReference<String> errorMessage = new AtomicReference<>();
		ollamaService.chat(messageList, data -> {
			// 输出到控制台
			final JSON streamData = JSONUtil.parse(data);
			if (streamData.getByPath("error") != null) {
				isDone.set(true);
				errorMessage.set(streamData.getByPath("error").toString());
				return;
			}

			if ("true".equals(streamData.getByPath("done").toString())) {
				isDone.set(true);
			}
		});
		// 轮询检查结束标志
		while (!isDone.get()) {
			ThreadUtil.sleep(100);
		}
		if (errorMessage.get() != null) {
			throw new RuntimeException(errorMessage.get());
		}
	}

	/**
	 * 列出所有已经拉取到服务器上的模型
	 */
	@Test
	@Disabled
	void testListModels(){
		final String models = ollamaService.listModels();
		final JSONArray modelList = JSONUtil.parse(models).getByPath("models", JSONArray.class);
	}

	/**
	 * 让Ollama拉取模型
	 */
	@Test
	@Disabled
	void testPullModel(){
		final String result = ollamaService.pullModel("qwen2.5:0.5b");
		final List<String> lines = SplitUtil.splitTrim(result, "\n");
		for (final String line : lines) {
			if(line.contains("error")){
				throw new RuntimeException(JSONUtil.parse(line).getByPath("error").toString());
			}
		}
	}

	/**
	 * 让Ollama删除已经存在的模型
	 */
	@Test
	@Disabled
	void testDeleteModel(){
		// 不会返回任何信息
		ollamaService.deleteModel("qwen2.5:0.5b");
	}
}

