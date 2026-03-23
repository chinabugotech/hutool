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

package cn.hutool.ai.model.ollama;

import cn.hutool.ai.AIServiceFactory;
import cn.hutool.ai.ModelName;
import cn.hutool.ai.core.AIConfigBuilder;
import cn.hutool.ai.core.Message;
import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
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
	String javaEngineerPrompt="# 角色  \n" +
		"你是一位精通Spring Boot 3.0的资深Java全栈工程师，具备以下核心能力：  \n" +
		"- 精通Spring Boot 3.0新特性与最佳实践  \n" +
		"- 熟练整合Hutool工具包、Redis数据访问、Feign远程调用、FreeMarker模板引擎  \n" +
		"- 能输出符合工程规范的代码结构和配置文件  \n" +
		"- 注重代码可读性与注释规范  \n" +
		"\n" +
		"# 任务  \n" +
		"请完成以下编程任务（按优先级排序）：  \n" +
		"1. **核心要求**  \n" +
		"   - 使用Spring Boot 3.0构建项目  \n" +
		"   - 必须包含以下依赖：  \n" +
		"     - `cn.hutool:hutool-all`（最新版）  \n" +
		"     - `org.springframework.boot:spring-boot-starter-data-redis`  \n" +
		"     - `org.springframework.cloud:spring-cloud-starter-openfeign`  \n" +
		"     - `org.springframework.boot:spring-boot-starter-freemarker`  \n" +
		"2. **约束条件**  \n" +
		"   - 代码需符合Java 17语法规范  \n" +
		"   - 每个类必须包含Javadoc风格的类注释  \n" +
		"   - 关键方法需添加`@Api`/`@ApiOperation`注解（若涉及接口）  \n" +
		"   - Redis操作需使用`RedisTemplate`实现  \n" +
		"3. **实现流程**  \n" +
		"   ```  \n" +
		"   1. 生成pom.xml依赖配置  \n" +
		"   2. 创建基础配置类（如RedisConfig）  \n" +
		"   3. 编写Feign客户端接口  \n" +
		"   4. 实现FreeMarker模板渲染服务  \n" +
		"   5. 提供完整Controller示例  \n" +
		"   ```  \n" +
		"\n" +
		"# 输出要求  \n" +
		"请以严格Markdown格式输出，每个模块独立代码块：  \n" +
		"```markdown  \n" +
		"## 1. 项目依赖配置（pom.xml片段）  \n" +
		"```xml  \n" +
		"<dependency>...</dependency>  \n" +
		"```  \n" +
		"\n" +
		"## 2. Redis配置类  \n" +
		"```java  \n" +
		"@Configuration  \n" +
		"public class RedisConfig { ... }  \n" +
		"```  \n" +
		"\n" +
		"## 3. Feign客户端示例  \n" +
		"```java  \n" +
		"@FeignClient(name = \"...\")  \n" +
		"public interface ... { ... }  \n" +
		"```  \n" +
		"\n" +
		"## 4. FreeMarker模板服务  \n" +
		"```java  \n" +
		"@Service  \n" +
		"public class TemplateService { ... }  \n" +
		"```  \n" +
		"\n" +
		"## 5. 控制器示例  \n" +
		"```java  \n" +
		"@RestController  \n" +
		"@RequestMapping(\"/example\")  \n" +
		"public class ExampleController { ... }  \n" +
		"```  \n" +
		"```  \n" +
		"\n" +
		"# 示例片段（供格式参考）  \n" +
		"```java  \n" +
		"/** \n" +
		" * 示例Feign客户端 \n" +
		" * @since 1.0.0 \n" +
		" */  \n" +
		"@FeignClient(name = \"demo-service\", url = \"${demo.service.url}\")  \n" +
		"public interface DemoClient {  \n" +
		"\n" +
		"    @GetMapping(\"/data/{id}\")  \n" +
		"    @ApiOperation(\"获取示例数据\")  \n" +
		"    ResponseEntity<String> getData(@PathVariable(\"id\") Long id);  \n" +
		"}  \n" +
		"```  \n" +
		"\n" +
		"请按此规范输出完整代码结构，确保自动化程序可直接解析生成项目文件。";

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
		AtomicBoolean isDone = new AtomicBoolean(false);
		AtomicReference<String> errorMessage = new AtomicReference<>();
		ollamaService.chat("写一个疯狂星期四广告词", data -> {
			// 输出到控制台
			JSON streamData = JSONUtil.parse(data);
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
		List<Message> messageList=new ArrayList<>();
		messageList.add(new Message("system",javaEngineerPrompt));
		messageList.add(new Message("user","帮我写一个Java通过Post方式发送JSON给HTTP接口，请求头带有token"));
		String result = ollamaService.chat(messageList);
		assertNotNull(result);
	}

	@Test
	@Disabled
	void testStreamWithHistory(){
		List<Message> messageList=new ArrayList<>();
		messageList.add(new Message("system",javaEngineerPrompt));
		messageList.add(new Message("user","帮我写一个Java通过Post方式发送JSON给HTTP接口，请求头带有token"));
		AtomicBoolean isDone = new AtomicBoolean(false);
		AtomicReference<String> errorMessage = new AtomicReference<>();
		ollamaService.chat(messageList, data -> {
			// 输出到控制台
			JSON streamData = JSONUtil.parse(data);
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
		String models = ollamaService.listModels();
		JSONArray modelList = JSONUtil.parse(models).getByPath("models", JSONArray.class);
	}

	/**
	 * 让Ollama拉取模型
	 */
	@Test
	@Disabled
	void testPullModel(){
		String result = ollamaService.pullModel("qwen2.5:0.5b");
		List<String> lines = StrUtil.splitTrim(result, "\n");
		for (String line : lines) {
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
