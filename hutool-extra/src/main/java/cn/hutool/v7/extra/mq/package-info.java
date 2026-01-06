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

/**
 * MQ(message queue)（消息队列）门面封装<br>
 * 通过定义统一接口，统一消息中间件的调用，实现消息队列的解耦。
 * 组件包括：
 * <ul>
 *     <li>{@link cn.hutool.v7.extra.mq.Producer}: 消息生产者，业务的发起方，负责生产消息</li>
 *     <li>{@link cn.hutool.v7.extra.mq.Consumer}: 消息消费者，业务的处理方</li>
 *     <li>{@link cn.hutool.v7.extra.mq.Message}: 消息体，根据不同通信协议定义的固定格式进行编码的数据包</li>
 * </ul>
 *
 * @author Looly
 * @since 6.0.0
 */
package cn.hutool.v7.extra.mq;
