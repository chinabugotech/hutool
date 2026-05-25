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

package cn.hutool.ai.model.hutool;

/**
 * hutool公共类
 *
 * @author elichow
 * @since 5.8.39
 */
public class HutoolCommon {

	//hutool视觉参数
	public enum HutoolVision {

		AUTO("auto"),
		LOW("low"),
		HIGH("high");

		private final String detail;

		HutoolVision(String detail) {
			this.detail = detail;
		}

		public String getDetail() {
			return detail;
		}
	}

	//hutool音频参数
	public enum HutoolSpeech {

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

		HutoolSpeech(String voice) {
			this.voice = voice;
		}

		public String getVoice() {
			return voice;
		}
	}

	//hutool视频生成参数
	public enum HutoolVideo {

		//宽高比例
		RATIO_16_9("--rt", "16:9"),//[1280, 720]
		RATIO_4_3("--rt", "4:3"),//[960, 720]
		RATIO_1_1("--rt", "1:1"),//[720, 720]
		RATIO_3_4("--rt", "3:4"),//[720, 960]
		RATIO_9_16("--rt", "9:16"),//[720, 1280]
		RATIO_21_9("--rt", "21:9"),//[1280, 544]

		//生成视频时长
		DURATION_5("--dur", 5),//文生视频，图生视频
		DURATION_10("--dur", 10),//文生视频

		//帧率，即一秒时间内视频画面数量
		FPS_5("--fps", 24),

		//视频分辨率
		RESOLUTION_5("--rs", "720p"),

		//生成视频是否包含水印
		WATERMARK_TRUE("--wm", true),
		WATERMARK_FALSE("--wm", false);

		private final String type;
		private final Object value;

		HutoolVideo(String type, Object value) {
			this.type = type;
			this.value = value;
		}

		public String getType() {
			return type;
		}

		public Object getValue() {
			if (value instanceof String) {
				return (String) value;
			} else if (value instanceof Integer) {
				return (Integer) value;
			} else if (value instanceof Boolean) {
				return (Boolean) value;
			}
			return value;
		}

	}

}
