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

package cn.hutool.ai.model.openai;

/**
 * openai公共类
 *
 * @author elichow
 * @since 5.8.38
 */
public class OpenaiCommon {

	//openai推理参数
	public enum OpenaiReasoning {

		LOW("low"),
		MEDIUM("medium"),
		HIGH("high");

		private final String effort;

		OpenaiReasoning(String effort) {
			this.effort = effort;
		}

		public String getEffort() {
			return effort;
		}
	}

	//openai视觉参数
	public enum OpenaiVision {

		AUTO("auto"),
		LOW("low"),
		HIGH("high");

		private final String detail;

		OpenaiVision(String detail) {
			this.detail = detail;
		}

		public String getDetail() {
			return detail;
		}
	}

	//openai音频参数
	public enum OpenaiSpeech {

		ALLOY("alloy"),
		ASH("ash"),
		CORAL("coral"),
		ECHO("echo"),
		FABLE("fable"),
		ONYX("onyx"),
		NOVA("nova"),
		SAGE("sage"),
		SHIMMER("shimmer");

		private final String voice;

		OpenaiSpeech(String voice) {
			this.voice = voice;
		}

		public String getVoice() {
			return voice;
		}
	}
}
