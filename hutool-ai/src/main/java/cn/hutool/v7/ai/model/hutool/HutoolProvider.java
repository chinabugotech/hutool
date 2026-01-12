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

package cn.hutool.v7.ai.model.hutool;

import cn.hutool.v7.ai.core.AIConfig;
import cn.hutool.v7.ai.core.AIServiceProvider;

/**r
 * 创建Hutool服务实现类
 *
 * @author elichow
 * @since 7.0.0
 */
public class HutoolProvider implements AIServiceProvider {

	@Override
	public String getServiceName() {
		return "hutool";
	}

	@Override
	public HutoolService create(final AIConfig config) {
		return new HutoolServiceImpl(config);
	}
}
