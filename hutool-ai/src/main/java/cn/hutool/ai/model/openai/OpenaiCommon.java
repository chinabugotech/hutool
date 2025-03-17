package cn.hutool.ai.model.openai;

/**
 * openai公共类
 *
 * @author elichow
 * @since 5.8.37
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
