package cn.hutool.ai.model.deepseek;

import cn.hutool.ai.core.AIService;

/**
 * deepSeek支持的扩展接口
 *
 * @author elichow
 * @since 5.8.37
 */
public interface DeepSeekService extends AIService {

	/**
	 * 模型beta功能
	 *
	 * @param prompt 题词
	 * @return AI的回答
	 * @since 5.8.37
	 */
	String beta(String prompt);

	/**
	 * 列出所有模型列表
	 *
	 * @return model列表
	 * @since 5.8.37
	 */
	String models();

	/**
	 * 查询余额
	 *
	 * @return 余额
	 * @since 5.8.37
	 */
	String balance();
}
