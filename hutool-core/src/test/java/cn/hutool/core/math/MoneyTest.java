package cn.hutool.core.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Currency;

public class MoneyTest {

	@Test
	public void yuanToCentTest() {
		final Money money = new Money("1234.56");
		assertEquals(123456, money.getCent());

		assertEquals(123456, MathUtil.yuanToCent(1234.56));
	}

	@Test
	public void centToYuanTest() {
		final Money money = new Money(1234, 56);
		assertEquals(1234.56D, money.getAmount().doubleValue(), 0);

		assertEquals(1234.56D, MathUtil.centToYuan(123456), 0);
	}

	@Test
	public void currencyScalingTest() {
		Money jpyMoney = new Money(0, Currency.getInstance("JPY"));
		jpyMoney.setAmount(BigDecimal.ONE);
		assertEquals(1, jpyMoney.getCent());
	}

}
