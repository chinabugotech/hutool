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

package cn.hutool.ai.model.grok;

import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.AIServiceProvider;

/**r
 * 创建Grok服务实现类
 *
 * @author elichow
 * @since 5.8.38
 */
public class GrokProvider implements AIServiceProvider {

	@Override
	public String getServiceName() {
		return "grok";
	}

	@Override
	public GrokService create(final AIConfig config) {
		return new GrokServiceImpl(config);
	}
}
