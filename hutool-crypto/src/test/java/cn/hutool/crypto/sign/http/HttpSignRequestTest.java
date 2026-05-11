package cn.hutool.crypto.sign.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTP签名请求模型测试。
 *
 * @author mumu
 */
public class HttpSignRequestTest {

	@Test
	public void headerCaseInsensitiveReplaceTest() {
		final HttpSignRequest request = HttpSignRequest.create()
			.setHeader("X-Test", "1")
			.setHeader("x-test", "2");

		Assertions.assertEquals("2", request.getHeader("X-Test"));
		Assertions.assertEquals(1, request.getHeaders().size());
		Assertions.assertThrows(UnsupportedOperationException.class, () -> request.getHeaderValues("X-Test").add("3"));
		Assertions.assertThrows(UnsupportedOperationException.class, () -> request.getHeaders().get("x-test").add("3"));
	}

	@Test
	public void queryIterableAndArrayTest() {
		final Map<String, Object> queryParams = new LinkedHashMap<>();
		queryParams.put("list", Arrays.asList("1", "2"));
		queryParams.put("array", new String[]{"a", "b"});
		queryParams.put("empty", null);

		final HttpSignRequest request = HttpSignRequest.create().setQueryParams(queryParams);

		Assertions.assertEquals(5, request.getQueryParams().size());
		Assertions.assertEquals("", request.getQueryParams().get(4).getValue());
	}

	@Test
	public void bodyBytesDefensiveCopyTest() {
		final byte[] body = new byte[]{1, 2, 3};
		final HttpSignRequest request = HttpSignRequest.create().setBodyBytes(body);
		body[0] = 9;

		Assertions.assertEquals(1, request.getBodyBytes()[0]);

		final byte[] readBody = request.getBodyBytes();
		readBody[1] = 9;
		Assertions.assertEquals(2, request.getBodyBytes()[1]);

		final HttpSignRequest copy = request.copy();
		final byte[] copyBody = copy.getBodyBytes();
		copyBody[2] = 9;
		Assertions.assertEquals(3, copy.getBodyBytes()[2]);
	}
}
