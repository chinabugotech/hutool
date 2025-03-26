package org.dromara.hutool.ai.model.grok;

/**
 * grok公共类
 *
 * @author elichow
 * @since 6.0.0
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
