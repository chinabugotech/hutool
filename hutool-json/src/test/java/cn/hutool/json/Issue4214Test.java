package cn.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue4214Test {

	@Test
	void toBeanTest(){
		LicenseInfo licenseInfo = new LicenseInfo();
		licenseInfo.setAuthTypeEnum(AuthTypeEnum.OFFICIAL);

		String jsonStr = JSONUtil.toJsonStr(licenseInfo);
		assertEquals("{\"authTypeEnum\":\"OFFICIAL\"}", jsonStr);

		// 这里反序列化会报错
		LicenseInfo bean = JSONUtil.toBean(jsonStr, LicenseInfo.class);
		assertEquals(AuthTypeEnum.OFFICIAL, bean.getAuthTypeEnum());
	}

	@Data
	static class LicenseInfo{
		private AuthTypeEnum authTypeEnum;
	}

	enum AuthTypeEnum{
		OFFICIAL,
		SELF_BUILD
	}
}
