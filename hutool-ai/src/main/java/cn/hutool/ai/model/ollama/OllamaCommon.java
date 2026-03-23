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

/**
 * Ollama公共类
 *
 * @author yangruoyu-yumeisoft
 * @since 5.8.40
 */
public class OllamaCommon {

	/**
	 * Ollama模型格式枚举
	 */
	public enum OllamaFormat {
		/**
		 * JSON格式
		 */
		JSON("json"),
		/**
		 * 无格式
		 */
		NONE("");

		private final String format;

		OllamaFormat(String format) {
			this.format = format;
		}

		public String getFormat() {
			return format;
		}
	}

	/**
	 * Ollama选项常量
	 */
	public static class Options {
		/**
		 * 温度参数
		 */
		public static final String TEMPERATURE = "temperature";
		/**
		 * top_p参数
		 */
		public static final String TOP_P = "top_p";
		/**
		 * top_k参数
		 */
		public static final String TOP_K = "top_k";
		/**
		 * 最大token数
		 */
		public static final String NUM_PREDICT = "num_predict";
		/**
		 * 随机种子
		 */
		public static final String SEED = "seed";
	}
}
