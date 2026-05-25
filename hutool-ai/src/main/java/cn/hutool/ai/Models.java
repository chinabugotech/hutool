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

package cn.hutool.ai;

/**
 * 各模型厂商包含的model（指具体的模型）
 *
 * @author elichow
 * @since 5.8.38
 */
public class Models {


	// Hutool的模型
	public enum Hutool {
		HUTOOL("hutool");

		private final String model;

		Hutool(String model) {
			this.model = model;
		}

		public String getModel() {
			return model;
		}
	}

	// DeepSeek的模型
	public enum DeepSeek {
		DEEPSEEK_CHAT("deepseek-chat"),
		DEEPSEEK_REASONER("deepseek-reasoner");

		private final String model;

		DeepSeek(String model) {
			this.model = model;
		}

		public String getModel() {
			return model;
		}
	}

	// Openai的模型
	public enum Openai {
		GPT_4_5_PREVIEW("gpt-4.5-preview"),
		GPT_4O("gpt-4o"),
		CHATGPT_4O_LATEST("chatgpt-4o-latest"),
		GPT_4O_MINI("gpt-4o-mini"),
		O1("o1"),
		O1_MINI("o1-mini"),
		O1_PREVIEW("o1-preview"),
		O3_MINI("o3-mini"),
		GPT_4O_REALTIME_PREVIEW("gpt-4o-realtime-preview"),
		GPT_4O_MINI_REALTIME_PREVIEW("gpt-4o-mini-realtime-preview"),
		GPT_4O_AUDIO_PREVIEW("gpt-4o-audio-preview"),
		GPT_4O_MINI_AUDIO_PREVIEW("gpt-4o-mini-audio-preview"),
		GPT_4_TURBO("gpt-4-turbo"),
		GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview"),
		GPT_4("gpt-4"),
		GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125"),
		GPT_3_5_TURBO("gpt-3.5-turbo"),
		GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106"),
		GPT_3_5_TURBO_INSTRUCT("gpt-3.5-turbo-instruct"),
		DALL_E_3("dall-e-3"),
		DALL_E_2("dall-e-2"),
		TTS_1("tts-1"),
		TTS_1_HD("tts-1-hd"),
		WHISPER_1("whisper-1"),
		TEXT_EMBEDDING_3_LARGE("text-embedding-3-large"),
		TEXT_EMBEDDING_3_SMALL("text-embedding-3-small"),
		TEXT_EMBEDDING_ADA_002("text-embedding-ada-002"),
		OMNI_MODERATION_LATEST("omni-moderation-latest"),
		OMNI_MODERATION_2024_09_26("omni-moderation-2024-09-26"),
		TEXT_MODERATION_LATEST("text-moderation-latest"),
		TEXT_MODERATION_STABLE("text-moderation-stable"),
		TEXT_MODERATION_007("text-moderation-007"),
		BABBAGE_002("babbage-002"),
		DAVINCI_002("davinci-002");

		private final String model;

		Openai(String model) {
			this.model = model;
		}

		public String getModel() {
			return model;
		}
	}

	// Doubao的模型
	public enum Doubao {
		DOUBAO_1_5_PRO_32K("doubao-1.5-pro-32k-250115"),
		DOUBAO_1_5_PRO_256K("doubao-1.5-pro-256k-250115"),
		DOUBAO_1_5_LITE_32K("doubao-1.5-lite-32k-250115"),
		DEEPSEEK_R1("deepseek-r1-250120"),
		DEEPSEEK_R1_DISTILL_QWEN_32B("deepseek-r1-distill-qwen-32b-250120"),
		DEEPSEEK_R1_DISTILL_QWEN_7B("deepseek-r1-distill-qwen-7b-250120"),
		DEEPSEEK_V3("deepseek-v3-241226"),
		DOUBAO_PRO_4K_240515("doubao-pro-4k-240515"),
		DOUBAO_PRO_4K_CHARACTER_240728("doubao-pro-4k-character-240728"),
		DOUBAO_PRO_4K_FUNCTIONCALL_240615("doubao-pro-4k-functioncall-240615"),
		DOUBAO_PRO_4K_BROWSING_240524("doubao-pro-4k-browsing-240524"),
		DOUBAO_PRO_32K_241215("doubao-pro-32k-241215"),
		DOUBAO_PRO_32K_FUNCTIONCALL_241028("doubao-pro-32k-functioncall-241028"),
		DOUBAO_PRO_32K_BROWSING_241115("doubao-pro-32k-browsing-241115"),
		DOUBAO_PRO_32K_CHARACTER_241215("doubao-pro-32k-character-241215"),
		DOUBAO_PRO_128K_240628("doubao-pro-128k-240628"),
		DOUBAO_PRO_256K_240828("doubao-pro-256k-240828"),
		DOUBAO_LITE_4K_240328("doubao-lite-4k-240328"),
		DOUBAO_LITE_4K_PRETRAIN_CHARACTER_240516("doubao-lite-4k-pretrain-character-240516"),
		DOUBAO_LITE_32K_240828("doubao-lite-32k-240828"),
		DOUBAO_LITE_32K_CHARACTER_241015("doubao-lite-32k-character-241015"),
		DOUBAO_LITE_128K_240828("240828"),
		MOONSHOT_V1_8K("moonshot-v1-8k"),
		MOONSHOT_V1_32K("moonshot-v1-32k"),
		MOONSHOT_V1_128K("moonshot-v1-128k"),
		CHATGLM3_130B_FC("chatglm3-130b-fc-v1.0"),
		CHATGLM3_130_FIN("chatglm3-130-fin-v1.0-update"),
		MISTRAL_7B("mistral-7b-instruct-v0.2"),
		DOUBAO_1_5_VISION_PRO_32K("doubao-1.5-vision-pro-32k-250115"),
		DOUBAO_VISION_PRO_32K("doubao-vision-pro-32k-241008"),
		DOUBAO_VISION_LITE_32K("doubao-vision-lite-32k-241015"),
		DOUBAO_EMBEDDING_LARGE("doubao-embedding-large-text-240915"),
		DOUBAO_EMBEDDING_TEXT_240715("doubao-embedding-text-240715"),
		DOUBAO_EMBEDDING_VISION("doubao-embedding-vision-241215"),
		DOUBAO_SEEDREAM_3_0_T2I("doubao-seedream-3-0-t2i-250415"),
		Doubao_Seedance_1_0_lite_t2v("doubao-seedance-1-0-lite-t2v-250428"),
		Doubao_Seedance_1_0_lite_i2v("doubao-seedance-1-0-lite-i2v-250428"),
		Wan2_1_14B_t2v("wan2-1-14b-t2v-250225"),
		Wan2_1_14B_i2v("wan2-1-14b-i2v-250225");

		private final String model;

		Doubao(String model) {
			this.model = model;
		}

		public String getModel() {
			return model;
		}
	}

	// Grok的模型
	public enum Grok {
		GROK_3_BETA_LATEST("grok-3-beta"),
		GROK_3_BETA("grok-3-beta"),
		GROK_3("grok-3-beta"),
		GROK_3_MINI_FAST_LATEST("grok-3-mini-fast-beta"),
		GROK_3_MINI_FAST_BETA("grok-3-mini-fast-beta"),
		GROK_3_MINI_FAST("grok-3-mini-fast-beta"),
		GROK_3_FAST_LATEST("grok-3-fast-beta"),
		GROK_3_FAST_BETA("grok-3-fast-beta"),
		GROK_3_FAST("grok-3-fast-beta"),
		GROK_3_MINI_LATEST("grok-3-mini-beta"),
		GROK_3_MINI_BETA("grok-3-mini-beta"),
		GROK_3_MINI("grok-3-mini-beta"),
		GROK_2_IMAGE_LATEST("grok-2-image-1212"),
		GROK_2_IMAGE("grok-2-image-1212"),
		GROK_2_IMAGE_1212("grok-2-image-1212"),
		grok_2_latest("grok-2-1212"),
		GROK_2("grok-2-1212"),
		GROK_2_1212("grok-2-1212"),
		GROK_2_VISION_1212("grok-2-vision-1212"),
		GROK_BETA("grok-beta"),
		GROK_VISION_BETA("grok-vision-beta");

		private final String model;

		Grok(String model) {
			this.model = model;
		}

		public String getModel() {
			return model;
		}
	}

	// Ollama的模型
	public enum Ollama {
		QWEN3_32B("qwen3:32b");

		private final String model;

		Ollama(String model) {
			this.model = model;
		}

		public String getModel() {
			return model;
		}
	}

}
