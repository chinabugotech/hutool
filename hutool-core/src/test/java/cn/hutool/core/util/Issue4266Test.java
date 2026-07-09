package cn.hutool.core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Predicate;

public class Issue4266Test {
	@Getter
	@AllArgsConstructor
	public enum TestEnum  {

		ONE(1, "1"),
		DEFAULT(0, "默认");

		private final Integer value;

		private final String name;

		public static TestEnum valueOf(Integer value) {
			return EnumUtil.getBy(TestEnum.class, (Predicate<? super TestEnum>) item -> item.getValue().equals(value), TestEnum.DEFAULT);
		}

	}
}
