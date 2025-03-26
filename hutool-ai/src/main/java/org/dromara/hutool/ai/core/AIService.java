package org.dromara.hutool.ai.core;

import java.util.List;

/**
 * 模型公共的API功能，特有的功能在model.xx.XXService下定义
 *
 * @author elichow
 * @since 6.0.0
 */
public interface AIService {

	/**
	 * 对话
	 *
	 * @param prompt user题词
	 * @return AI回答
	 * @since 6.0.0
	 */
	String chat(String prompt);

	/**
	 * 对话
	 *
	 * @param messages 由目前为止的对话组成的消息列表，可以设置role，content。详细参考官方文档
	 * @return AI回答
	 * @since 6.0.0
	 */
	String chat(List<Message> messages);

}
