package cn.hutool.ai.core;

/**
 * 用于加载AI服务,每一个通过SPI创建的AI服务都要实现此接口
 *
 * @author elichow
 * @since 5.8.37
 */
public interface AIServiceProvider {

	/**
	 * 获取AI服务名称
	 *
	 * @return AI服务名称
	 * @since 5.8.37
	 */
	String getServiceName();

	/**
	 * 创建AI服务实例
	 *
	 * @param config AIConfig配置
	 * @param <T>    AIService类型
	 * @return AI服务实例
	 * @since 5.8.37
	 */
	<T extends AIService> T create(AIConfig config);
}
