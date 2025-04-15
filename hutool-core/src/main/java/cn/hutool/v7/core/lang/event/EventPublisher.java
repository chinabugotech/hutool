/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.core.lang.event;

/**
 * 事件发布者接口，用于发布事件
 *
 * @author Looly
 */
public interface EventPublisher {

	/**
	 * 注册订阅者，订阅者将接收到所有发布者发布的事件
	 *
	 * @param subscriber 订阅者
	 * @return this
	 */
	EventPublisher register(Subscriber subscriber);

	/**
	 * 发布事件，事件发布者将事件发布给多个订阅者，可以自定义发布策略，如：
	 * <ul>
	 *     <li>所有订阅者都接收此消息（多订阅）</li>
	 *     <li>订阅者按照顺序或权重接收此消息，接收后其它订阅者不再接收。（单订阅）</li>
	 *     <li>按照自定义规则选择要接收消息的订阅者，如根据消息或实践类型（选择性多订阅）</li>
	 * </ul>
	 *
	 * @param event 事件对象
	 */
	void publish(Event event);
}
