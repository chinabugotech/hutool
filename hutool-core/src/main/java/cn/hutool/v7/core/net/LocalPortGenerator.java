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

package cn.hutool.v7.core.net;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本地端口生成器（LocalPortGenerator）。
 * 用于从指定起点开始递增探测一个当前“可用”的本地端口。探测通过短暂绑定
 * {@link java.net.ServerSocket}（以及可选 UDP DatagramSocket）完成，但不会真正占用端口。
 * <p>注意：</p>
 * <ul>
 *   <li>该方法执行的是端口“探测”，非“分配”，返回端口不保证实际使用时仍然可用。</li>
 *   <li>存在 TOCTOU（检测到使用之间）竞态，多线程下可能返回同一端口。</li>
 *   <li>UDP 探测可能导致误判（TCP 可用但 UDP 被占用）。</li>
 *   <li>不适合作为生产级端口分配策略，推荐使用 {@code new ServerSocket(0)}。</li>
 * </ul>
 *
 * <p>TODO 未来版本计划：</p>
 * <ul>
 *   <li>提供真正可靠的端口获取实现（绑定即占用，避免竞态）。</li>
 *   <li>优化探测策略，减少不必要的 UDP 检测。</li>
 *   <li>提供更安全的随机端口生成 API。</li>
 * </ul>
 * @author looly
 * @since 4.0.3
 */
public class LocalPortGenerator implements Serializable{
	@Serial
	private static final long serialVersionUID = 1L;

	/** 备选的本地端口 */
	private final AtomicInteger alternativePort;

	/**
	 * 构造
	 *
	 * @param beginPort 起始端口号
	 */
	public LocalPortGenerator(final int beginPort) {
		alternativePort = new AtomicInteger(beginPort);
	}

	/**
	 * 生成一个本地端口，用于远程端口映射
	 *
	 * @return 未被使用的本地端口
	 */
	public int generate() {
		int validPort = alternativePort.get();
		// 获取可用端口
		while (!NetUtil.isUsableLocalPort(validPort)) {
			validPort = alternativePort.incrementAndGet();
		}
		return validPort;
	}
}
