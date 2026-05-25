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

package cn.hutool.ai.core;

/**
 * 用于加载AI服务,每一个通过SPI创建的AI服务都要实现此接口
 *
 * @author elichow
 * @since 5.8.38
 */
public interface AIServiceProvider {

	/**
	 * 获取AI服务名称
	 *
	 * @return AI服务名称
	 * @since 5.8.38
	 */
	String getServiceName();

	/**
	 * 创建AI服务实例
	 *
	 * @param config AIConfig配置
	 * @param <T>    AIService实现类
	 * @return AI服务实例
	 * @since 5.8.38
	 */
	<T extends AIService> T create(final AIConfig config);
}
