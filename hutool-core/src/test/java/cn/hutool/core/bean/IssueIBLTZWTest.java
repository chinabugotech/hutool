package cn.hutool.core.bean;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.TypeConverter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * 自定义某个字段的转换
 */
public class IssueIBLTZWTest {
	@Test
	public void copyTest() {
		TestBean bean = new TestBean();
		bean.setName("test");
		bean.setDate(DateUtil.parse("2025-02-17"));

		final TestBean2 testBean2 = new TestBean2();
		BeanUtil.copyProperties(bean, testBean2, createCopyOptions(TestBean2.class));
		Assertions.assertEquals("2025", testBean2.getDate());
	}

	@Data
	static class TestBean {
		private String name;
		private Date date;
	}

	@Data
	static class TestBean2 {
		private String name;
		private String date;
	}

	static CopyOptions createCopyOptions(Class<?> targetClass) {
		CopyOptions copyOptions = CopyOptions.create();
		TypeConverter converter = (TypeConverter) ReflectUtil.getFieldValue(copyOptions, "converter");
		copyOptions
			.setIgnoreError(true) // 忽略类型错误，避免自动转换
			.setConverter(null)
			.setFieldValueEditor((fieldName, value) -> {
				try {
					Field targetField = targetClass.getDeclaredField(fieldName);
					// Date类型的 value instanceof 结果是String
					if (targetField.getType() == String.class && value instanceof Date) {
						return DateUtil.format((Date)value, "yyyy");
					}
					return converter.convert(targetField.getType(), value);
				} catch (NoSuchFieldException e) {
					return value;
				}
			});
		return copyOptions;
	}
}
