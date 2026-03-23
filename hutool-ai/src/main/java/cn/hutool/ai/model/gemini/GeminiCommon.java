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

package cn.hutool.ai.model.gemini;

/**
 * gemini公共类
 *
 * @author elichow
 * @since 5.8.43
 */
public class GeminiCommon {

	//要生成的图片数量
	public enum GeminiImageCount {

		ONE(1),
		TWO(2),
		THREE(3),
		FOUR(4);

		private final int count;

		GeminiImageCount(int count) {
			this.count = count;
		}

		public int getCount() {
			return count;
		}
	}

	//生成的图片大小 (imageSize) - 仅限 Standard 和 Ultra
	public enum GeminiImageSize {

		SIZE_1K("1K"),
		SIZE_2K("2K");

		private final String value;

		GeminiImageSize(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	//宽高比
	public enum GeminiAspectRatio {

		SQUARE("1:1"),
		PORTRAIT_3_4("3:4"),
		LANDSCAPE_4_3("4:3"),
		PORTRAIT_9_16("9:16"),
		LANDSCAPE_16_9("16:9");

		private final String ratio;

		GeminiAspectRatio(String ratio) {
			this.ratio = ratio;
		}

		public String getRatio() {
			return ratio;
		}

	}

	//人物生成权限
	public enum GeminiPersonGeneration {

		DONT_ALLOW("dont_allow"),
		ALLOW_ADULT("allow_adult"),
		ALLOW_ALL("allow_all");

		private final String value;

		GeminiPersonGeneration(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	//生成的视频的时长
	public enum GeminiDurationSeconds {

		FOUR(4),
		SIX(6),
		EIGHT(8);

		private final Integer value;

		GeminiDurationSeconds(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}

	//语音音色
	public enum GeminiVoice {

		AOEDE("Aoede"),
		CHARON("Charon"),
		KORE("Kore"),
		FENRIR("Fenrir"),
		PUCK("Puck");

		private final String value;

		GeminiVoice(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

}
