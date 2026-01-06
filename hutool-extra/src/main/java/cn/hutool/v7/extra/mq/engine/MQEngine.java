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

package cn.hutool.v7.extra.mq.engine;

import cn.hutool.v7.extra.mq.Consumer;
import cn.hutool.v7.extra.mq.MQConfig;
import cn.hutool.v7.extra.mq.Producer;

/**
 * 消息队列引擎接口
 *
 * @author Looly
 * @since 6.0.0
 */
public interface MQEngine {

	/**
	 * 初始化配置
	 *
	 * @param config 配置
	 * @return this
	 */
	MQEngine init(MQConfig config);

	/**
	 * 获取消息生产者
	 *
	 * @return 消息生产者
	 */
	Producer getProducer();

	/**
	 * 获取消息消费者
	 *
	 * @return 消息消费者
	 */
	Consumer getConsumer();
}
