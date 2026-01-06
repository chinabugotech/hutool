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

package cn.hutool.v7.ai.model.grok;

/**
 * grok公共类
 *
 * @author elichow
 * @since 6.0.0
 */
public class GrokCommon {

	/**
	 * grok视觉参数
	 */
	public enum GrokVision {

		/**
		 * 自动
		 */
		AUTO("auto"),
		/**
		 * 低
		 */
		LOW("low"),
		/**
		 * 高
		 */
		HIGH("high");

		private final String detail;

		GrokVision(final String detail) {
			this.detail = detail;
		}

		/**
		 * 获取详情
		 *
		 * @return 详情
		 */
		public String getDetail() {
			return detail;
		}
	}
}
