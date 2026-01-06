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

package cn.hutool.v7.ai.model.doubao;

/**
 * doubao公共类
 *
 * @author elichow
 * @since 6.0.0
 */
public class DoubaoCommon {

	/**
	 * doubao上下文缓存参数
	 */
	public enum DoubaoContext {

		/**
		 * session
		 */
		SESSION("session"),
		/**
		 * common_prefix
		 */
		COMMON_PREFIX("common_prefix");

		private final String mode;

		DoubaoContext(final String mode) {
			this.mode = mode;
		}

		/**
		 * 获取参数
		 *
		 * @return 参数
		 */
		public String getMode() {
			return mode;
		}
	}

	/**
	 * doubao视觉参数
	 */
	public enum DoubaoVision {

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

		DoubaoVision(final String detail) {
			this.detail = detail;
		}

		/**
		 * 获取参数
		 *
		 * @return 参数
		 */
		public String getDetail() {
			return detail;
		}
	}

	/**
	 * doubao视频生成参数
	 */
	public enum DoubaoVideo {

		//宽高比例
		/**
		 * 视频比例16:9，适用于横向宽屏显示，常用作标准视频比例
		 */
		RATIO_16_9("--rt", "16:9"),//[1280, 720]

		/**
		 * 视频比例4:3，传统电视屏幕比例，适用于标准清晰度的视频内容
		 */
		RATIO_4_3("--rt", "4:3"),//[960, 720]

		/**
		 * 视频比例1:1，正方形画面，适用于社交媒体平台上的短视频内容
		 */
		RATIO_1_1("--rt", "1:1"),//[720, 720]

		/**
		 * 视频比例3:4，竖向视频比例，适用于手机端的视频播放场景
		 */
		RATIO_3_4("--rt", "3:4"),//[720, 960]

		/**
		 * 视频比例9:16，常见的竖屏视频比例，广泛用于短视频应用
		 */
		RATIO_9_16("--rt", "9:16"),//[720, 1280]

		/**
		 * 视频比例21:9，超宽屏幕比例，提供更广阔的视野，适合电影和游戏体验
		 */
		RATIO_21_9("--rt", "21:9"),//[1280, 544]

		//生成视频时长
		/**
		 * 文生视频，图生视频
		 */
		DURATION_5("--dur", 5),
		/**
		 * 文生视频
		 */
		DURATION_10("--dur", 10),

		/**
		 * 帧率，即一秒时间内视频画面数量
		 */
		FPS_5("--fps", 24),

		/**
		 * 视频分辨率
		 */
		RESOLUTION_5("--rs", "720p"),

		/**
		 * 生成视频包含水印
		 */
		WATERMARK_TRUE("--wm", true),
		/**
		 * 生成视频不包含水印
		 */
		WATERMARK_FALSE("--wm", false);

		private final String type;
		private final Object value;

		/**
		 * 构造
		 *
		 * @param type  参数类型
		 * @param value 参数值
		 */
		DoubaoVideo(final String type, final Object value) {
			this.type = type;
			this.value = value;
		}

		/**
		 * 获取参数类型
		 *
		 * @return 参数类型
		 */
		public String getType() {
			return type;
		}

		/**
		 * 获取参数值
		 *
		 * @return 值
		 */
		public Object getValue() {
			if (value instanceof String) {
				return value;
			} else if (value instanceof Integer) {
				return value;
			} else if (value instanceof Boolean) {
				return value;
			}
			return value;
		}

	}
}
