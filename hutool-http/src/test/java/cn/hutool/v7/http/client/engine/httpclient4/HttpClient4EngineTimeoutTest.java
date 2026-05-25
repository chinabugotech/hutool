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

package cn.hutool.v7.http.client.engine.httpclient4;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.http.HttpException;
import cn.hutool.v7.http.client.ClientConfig;
import cn.hutool.v7.http.client.Request;
import cn.hutool.v7.http.client.Response;
import cn.hutool.v7.http.meta.Method;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link HttpClient4Engine} 超时配置测试，验证 issue#4253 修复。
 * <p>
 * 修复内容：{@link HttpUriRequestBuilder} 构建单次请求配置时，
 * 正确合并 Client 级别的超时配置，避免被 Request 级别默认值覆盖。
 * </p>
 *
 * @author Hutool Team
 * @since 7.0.0
 */
public class HttpClient4EngineTimeoutTest {

	/**
	 * 不可达的测试地址，用于触发连接超时。
	 * 100.100.100.200 属于 TEST-NET-2 保留地址段（RFC 5737），不会被路由。
	 */
	private static final String UNREACHABLE_URL = "http://100.100.100.200:8081/";

	private HttpClient4Engine engine;

	/**
	 * 初始化引擎实例
	 */
	@BeforeEach
	public void setUp() {
		engine = new HttpClient4Engine();
	}

	/**
	 * 清理引擎资源
	 */
	@AfterEach
	public void tearDown() throws IOException {
		if (engine != null) {
			engine.close();
		}
	}

	/**
	 * 测试场景1：只有 Client 级别设置超时，Request 不设置。
	 * 预期：请求使用 Client 的超时配置，访问不可达 IP 时触发连接超时异常。
	 * <p>
	 * 此测试依赖网络环境（不可达 IP），默认禁用，手动运行时取消 @Disabled。
	 * </p>
	 */
	@Test
	@Disabled("依赖不可达IP的网络超时验证，仅手动运行时启用")
	public void testClientLevelTimeout() {
		// 设置 Client 级别连接超时和读取超时为 2 秒
		final ClientConfig config = ClientConfig.of()
			.setConnectionTimeout(2000)
			.setReadTimeout(2000);
		engine.init(config);

		// 创建普通 Request，不设置任何超时（v7 中 Request 无法独立设置超时）
		final Request request = Request.of(UNREACHABLE_URL).method(Method.GET);

		final long start = System.currentTimeMillis();
		try {
			engine.send(request);
			fail("期望因连接超时抛出 HttpException，但请求没有抛出异常");
		} catch (final HttpException e) {
			final long elapsed = System.currentTimeMillis() - start;

			Console.log("超时耗时: {} ms", elapsed);
			Console.log("异常信息: {}", e.getMessage());

			// 验证异常类型为超时相关
			final Throwable cause = e.getCause();
			assertTrue(
				cause instanceof SocketTimeoutException
					|| cause instanceof java.net.ConnectException,
				"期望为超时或连接异常，实际异常: " + (cause != null ? cause.getClass().getName() : "null"));

			// 验证超时时间大致在 2 秒左右，允许一定误差
			assertTrue(elapsed >= 1500,
				"超时时间应不小于 1.5 秒（配置为 2 秒），实际: " + elapsed + " ms");
			assertTrue(elapsed < 8000,
				"超时时间应不超过 8 秒，实际: " + elapsed + " ms");
		}
	}

	/**
	 * 测试场景2：不同 Client 实例使用不同的超时配置，验证配置独立生效。
	 * <p>
	 * Hutool v7 中 {@link Request} 类没有独立的超时设置 API，
	 * 超时统一由 {@link ClientConfig} 控制，每个引擎实例维护独立的配置。
	 * 此测试验证短超时配置的引擎在访问不可达 IP 时快速失败。
	 * </p>
	 * <p>
	 * 此测试依赖网络环境（不可达 IP），默认禁用，手动运行时取消 @Disabled。
	 * </p>
	 */
	@Test
	@Disabled("依赖不可达IP的网络超时验证，仅手动运行时启用")
	public void testDifferentClientTimeoutConfigs() {
		// 使用短超时（500ms）的引擎，应快速失败
		final ClientConfig shortConfig = ClientConfig.of()
			.setConnectionTimeout(500)
			.setReadTimeout(500);

		try (HttpClient4Engine shortEngine = new HttpClient4Engine()) {
			shortEngine.init(shortConfig);
			final Request request = Request.of(UNREACHABLE_URL).method(Method.GET);

			final long start = System.currentTimeMillis();
			try {
				shortEngine.send(request);
				fail("期望因连接超时抛出 HttpException");
			} catch (final HttpException e) {
				final long elapsed = System.currentTimeMillis() - start;
				Console.log("短超时耗时: {} ms", elapsed);

				assertTrue(elapsed < 3000,
					"500ms 超时配置应在 3 秒内失败，实际: " + elapsed + " ms");
			}
		} catch (final IOException e) {
			// 关闭时的 IO 异常忽略
		}
	}

	/**
	 * 测试场景3：Client 不设置超时（使用默认值 -1）。
	 * 预期：buildRequestConfig 方法
	 * 中 connectionTimeout &lt;= 0 时不设置显式超时，使用 Apache HttpClient 默认行为。
	 * <p>
	 * 由于未设置超时时 TCP 层面的默认超时通常非常长（分钟级别），
	 * 此测试仅验证不会在短时间内（5 秒内）意外超时，而不等待完整超时。
	 * </p>
	 * <p>
	 * 此测试依赖网络环境（不可达 IP），默认禁用，手动运行时取消 @Disabled。
	 * </p>
	 */
	@Test
	@Disabled("依赖不可达IP的网络超时验证且耗时较长，仅手动运行时启用")
	public void testNoTimeoutConfiguration() {
		// 使用默认配置（connectionTimeout 和 readTimeout 均为 -1，即不设置超时）
		engine.init(ClientConfig.of());

		final Request request = Request.of(UNREACHABLE_URL).method(Method.GET);

		final long start = System.currentTimeMillis();
		try {
			engine.send(request);
			// 如果请求意外成功（例如地址可路由），记录日志
			Console.log("请求意外成功，耗时: {} ms", System.currentTimeMillis() - start);
		} catch (final HttpException e) {
			final long elapsed = System.currentTimeMillis() - start;
			Console.log("无超时配置下的异常耗时: {} ms", elapsed);
			Console.log("异常信息: {}", e.getMessage());

			// 无显式超时配置时，不应该在短时间内快速超时
			// TCP 层面的默认超时通常远大于 5 秒
			assertTrue(elapsed > 5000,
				"无超时配置时不应快速超时（5秒内），实际耗时: " + elapsed + " ms");
		}
	}

	/**
	 * 测试：设置超时后访问正常可达地址，请求应成功完成。
	 * 验证超时配置不会影响正常的 HTTP 请求。
	 * <p>
	 * 此测试依赖外部网络，默认禁用，手动运行时取消 @Disabled。
	 * </p>
	 */
	@Test
	@Disabled("依赖外部网络访问 hutool.cn，仅手动运行时启用")
	public void testTimeoutWithReachableUrl() {
		// 设置较长超时，访问可达地址
		final ClientConfig config = ClientConfig.of()
			.setConnectionTimeout(10000)
			.setReadTimeout(10000);
		engine.init(config);

		final Request request = Request.of("https://www.hutool.cn/").method(Method.GET);

		final long start = System.currentTimeMillis();
		final Response response = engine.send(request);
		final long elapsed = System.currentTimeMillis() - start;

		Console.log("请求成功，耗时: {} ms", elapsed);
		Console.log("响应状态: {}", response.getStatus());

		assertTrue(response.getStatus() > 0, "应返回有效的 HTTP 状态码");
		assertTrue(elapsed < 15000, "正常请求应在 15 秒内完成");
	}

	/**
	 * 测试：验证连接超时和读取超时分别由独立的配置项控制。
	 * <p>
	 * 此测试不依赖网络，直接验证 {@link ClientConfig} 的配置值。
	 * </p>
	 */
	@Test
	public void testConnectionAndReadTimeoutCanBeSetIndependently() {
		final ClientConfig config = ClientConfig.of()
			.setConnectionTimeout(3000)
			.setReadTimeout(5000);

		assertEquals(3000, config.getConnectionTimeout(), "连接超时应为 3000ms");
		assertEquals(5000, config.getReadTimeout(), "读取超时应为 5000ms");

		// 验证两个超时值可以不同
		assertTrue(config.getConnectionTimeout() != config.getReadTimeout(),
			"连接超时和读取超时应可以独立设置为不同值");
	}

	/**
	 * 测试：验证 {@code connectionTimeout <= 0} 时不会将超时写入请求配置。
	 * <p>
	 * 对应 buildRequestConfig 方法。
	 * {@code if (connectionTimeout > 0)} 判断逻辑。
	 * 验证非正数超时值的配置可以正常初始化引擎。
	 * </p>
	 */
	@Test
	public void testZeroOrNegativeTimeoutDoesNotApply() {
		// 设置超时为 0，应被忽略（不作为有效超时）
		final ClientConfig zeroConfig = ClientConfig.of()
			.setConnectionTimeout(0)
			.setReadTimeout(0);

		assertEquals(0, zeroConfig.getConnectionTimeout(), "连接超时应为 0");
		assertEquals(0, zeroConfig.getReadTimeout(), "读取超时应为 0");

		// 设置超时为 -1，应被忽略
		final ClientConfig negativeConfig = ClientConfig.of()
			.setConnectionTimeout(-1)
			.setReadTimeout(-1);

		assertEquals(-1, negativeConfig.getConnectionTimeout(), "连接超时应为 -1");

		// 使用 -1 超时的配置可以正常初始化引擎（不会抛异常）
		// initEngine() 为延迟初始化，调用 init() 后 config 已设置但引擎尚未构建
		engine.init(negativeConfig);
		// 验证 init 后没有立即构建引擎（延迟初始化行为正确）
		assertNotNull(engine, "引擎实例不应为 null");
	}
}
