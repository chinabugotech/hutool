package cn.hutool.ai.model.grok;

/**
 * grok公共类
 *
 * @author elichow
 * @since 5.8.37
 */
public class GrokCommon {

	//grok视觉参数
	public enum GrokVision {

		AUTO("auto"),
		LOW("low"),
		HIGH("high");

		private final String detail;

		GrokVision(String detail) {
			this.detail = detail;
		}

		public String getDetail() {
			return detail;
		}
	}
}
