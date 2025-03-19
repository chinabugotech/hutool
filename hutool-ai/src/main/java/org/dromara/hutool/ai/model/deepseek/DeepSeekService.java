package org.dromara.hutool.ai.model.deepseek;

import org.dromara.hutool.ai.core.AIService;

/**
 * deepSeek支持的扩展接口
 *
 * @author elichow
 * @since 6.0.0
 */
public interface DeepSeekService extends AIService {

	/**
	 * 模型beta功能
	 *
	 * @param prompt 题词
	 * @return AI的回答
	 * @since 6.0.0
	 */
	String beta(String prompt);

	/**
	 * 列出所有模型列表
	 *
	 * @return model列表
	 * @since 6.0.0
	 */
	String models();

	/**
	 * 查询余额
	 *
	 * @return 余额
	 * @since 6.0.0
	 */
	String balance();
}
