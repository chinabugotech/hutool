package org.dromara.hutool.ai.model.doubao;

/**
 * doubao公共类
 *
 * @author elichow
 * @since 6.0.0
 */
public class DoubaoCommon {

	//doubao上下文缓存参数
	public enum DoubaoContext {

		SESSION("session"),
		COMMON_PREFIX("common_prefix");

		private final String mode;

		DoubaoContext(String mode) {
			this.mode = mode;
		}

		public String getMode() {
			return mode;
		}
	}

	//豆包视觉参数
	public enum DoubaoVision {

		AUTO("auto"),
		LOW("low"),
		HIGH("high");

		private final String detail;

		DoubaoVision(String detail) {
			this.detail = detail;
		}

		public String getDetail() {
			return detail;
		}
	}

	//doubao视频生成参数
	public enum DoubaoVideo {

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

		DoubaoVideo(String type, Object value) {
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
