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

/**
 * 对Ollama的封装实现.
 * <p>
 * 使用方法：
 * // 创建AI服务
 * <pre>{@code
 * OllamaService aiService = AIServiceFactory.getAIService(
 * new AIConfigBuilder(ModelName.OLLAMA.getValue())
 * .setApiUrl("http://localhost:11434")
 * .setModel("qwen2.5-coder:32b")
 * .build(),
 * OllamaService.class
 * );
 *
 * // 构造上下文
 * List<Message> messageList=new ArrayList<>();
 * messageList.add(new Message("system","你是一个疯疯癫癫的机器人"));
 * messageList.add(new Message("user","你能帮我做什么"));
 *
 * // 输出对话结果
 * Console.log(aiService.chat(messageList));
 * }</pre>
 *
 * @author yangruoyu-yumeisoft
 * @since 5.8.40
 */
package cn.hutool.ai.model.ollama;
