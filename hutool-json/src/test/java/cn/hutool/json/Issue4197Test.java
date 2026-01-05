package cn.hutool.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class Issue4197Test {
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static
	class TestDTO {

		private BigDecimal h;
	}

	@Test
	void toBeanTest() {
		final TestDTO bean = JSONUtil.toBean("{\"h\":\"123，456，789\"}", TestDTO.class);
		Assertions.assertEquals(new BigDecimal("123456789"), bean.getH());
	}
}
