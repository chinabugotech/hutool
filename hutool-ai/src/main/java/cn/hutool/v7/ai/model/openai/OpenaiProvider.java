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

package cn.hutool.v7.ai.model.openai;

import cn.hutool.v7.ai.core.AIConfig;
import cn.hutool.v7.ai.core.AIServiceProvider;

/**
 * 创建Openai服务实现类
 *
 * @author elichow
 * @since 6.0.0
 */
public class OpenaiProvider implements AIServiceProvider {

	@Override
	public String getServiceName() {
		return "openai";
	}

	@Override
	public OpenaiService create(final AIConfig config) {
		return new OpenaiServiceImpl(config);
	}
}
