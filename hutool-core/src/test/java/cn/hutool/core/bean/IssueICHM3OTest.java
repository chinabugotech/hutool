package cn.hutool.core.bean;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IssueICHM3OTest {
	@Test
	public void testMapToBean() {
		Map<Object,Object> map = MapUtil.builder()
			.put("doctor_name", "李医生")
			.put("doctor_id_card_value", "12345")
			.put("gender", "男")
			.build();
		TestClass doctor = BeanUtil.toBean(map, TestClass.class);
		assertTrue(StrUtil.equals(doctor.name, "李医生"), "姓名不一致");
		assertTrue(StrUtil.equals(doctor.idCardValue, "12345"), "证件号不一致");


		Map<String,Object> mapData = BeanUtil.beanToMap(doctor, true, false);
		assertTrue(StrUtil.equals(mapData.get("doctor_name").toString(), "李医生"), "姓名不一致");
		assertTrue(StrUtil.equals(mapData.get("doctor_id_card_value").toString(), "12345"), "证件号不一致");
	}

	@Setter
	@Getter
	public static class TestClass {
		@Alias("doctor_name")
		private String name;
		@Alias("doctor_id_card_value")
		private String idCardValue;
		@Alias("doctor_name")
		private String gender;
	}
}
