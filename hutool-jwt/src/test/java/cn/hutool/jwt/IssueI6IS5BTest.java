package cn.hutool.jwt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueI6IS5BTest {
	@Test
	public void payloadToBeanTest() {
		final LocalDateTime iat = LocalDateTimeUtil.of(DateUtil.parse("2023-03-03"));
		final JwtToken jwtToken = new JwtToken();
		jwtToken.setIat(iat);
		final String token = JWTUtil.createToken(JSONUtil.parseObj(jwtToken), "123".getBytes(StandardCharsets.UTF_8));
		assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2Nzc3NzI4MDB9.W88PB2ovAqCXV4QdbeKbdFW-P057xOTXEosD8hbOa9U", token);
		final JSONObject payloads = JWTUtil.parseToken(token).getPayloads();
		assertEquals("{\"iat\":1677772800}", payloads.toString());
		final JwtToken o = payloads.toBean(JwtToken.class);
		assertEquals(iat, o.getIat());
	}

	@Data
	static class JwtToken {
		private LocalDateTime iat;
	}

	@Test
	public void payloadToBeanTest2() {
		final Date iat = DateUtil.parse("2023-03-03");
		final JwtToken2 jwtToken = new JwtToken2();
		jwtToken.setIat(iat);
		final String token = JWTUtil.createToken(JSONUtil.parseObj(jwtToken), "123".getBytes(StandardCharsets.UTF_8));
		assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2Nzc3NzI4MDB9.W88PB2ovAqCXV4QdbeKbdFW-P057xOTXEosD8hbOa9U", token);
		final JSONObject payloads = JWTUtil.parseToken(token).getPayloads();
		assertEquals("{\"iat\":1677772800}", payloads.toString());
		final JwtToken2 o = payloads.toBean(JwtToken2.class);
		assertEquals(iat, o.getIat());
	}

	@Data
	static class JwtToken2 {
		private Date iat;
	}
}
