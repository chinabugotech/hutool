package cn.hutool.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpFormArrayTest {
	@Test
	public void joinsPrimitiveAndBoxedArrayValues() {
		final HttpRequest request = HttpRequest.post("http://localhost/")
				.form("ids", new long[]{12, 34})
				.form("boxed", new Long[]{12L, 34L})
				.form("empty", new int[0]);
		assertEquals("12,34", request.form().get("ids"));
		assertEquals("12,34", request.form().get("boxed"));
		assertEquals("", request.form().get("empty"));
	}
}
