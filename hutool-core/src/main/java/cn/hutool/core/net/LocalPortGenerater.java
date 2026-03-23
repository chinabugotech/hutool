package cn.hutool.core.net;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本地端口生成器（LocalPortGenerator）。
 * <p>
 * 当前类名中“Generater”为拼写错误（正确应为 Generator），为保持兼容性暂未更改。
 *   该问题将在后续大版本中以重命名方式修复，并保留旧类名的弃用（@Deprecated）兼容层。
 * <p>
 *
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
 * <p>未来版本计划：</p>
 * <ul>
 *   <li>修复类名拼写问题：将“Generater”更名为 “Generator”。</li>
 *   <li>提供真正可靠的端口获取实现（绑定即占用，避免竞态）。</li>
 *   <li>优化探测策略，减少不必要的 UDP 检测。</li>
 *   <li>提供更安全的随机端口生成 API。</li>
 * </ul>
 * @author looly
 * @since 4.0.3
 */


public class LocalPortGenerater  implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 备选的本地端口 */
	private final AtomicInteger alternativePort;

	/**
	 * 构造
	 *
	 * @param beginPort 起始端口号
	 */
	public LocalPortGenerater(int beginPort) {
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
		while (false == NetUtil.isUsableLocalPort(validPort)) {
			validPort = alternativePort.incrementAndGet();
		}
		return validPort;
	}
}
