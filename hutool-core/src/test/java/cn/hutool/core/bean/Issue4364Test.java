package cn.hutool.core.bean;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 见 https://github.com/chinabugotech/hutool/issues/4364
 * 土耳其语Locale下，忽略大小写的Getter/Setter匹配失效（大写I转为无点的ı）
 *
 * <p>说明：方法名使用{@code isActIve}（含大写I，可通过方法名预过滤），
 * 用于单独验证{@link BeanDesc}匹配层的Locale问题。
 * 完整的{@code ISActive}场景还需配合ReflectUtil层的修复（PR #4360）。</p>
 */
public class Issue4364Test {

	private static Locale originalLocale;

	@BeforeAll
	public static void initTurkishLocale() {
		originalLocale = Locale.getDefault();
		Locale.setDefault(new Locale("tr", "TR"));
	}

	@AfterAll
	public static void restoreLocale() {
		Locale.setDefault(originalLocale);
	}

	@Test
	public void isPrefixedGetterSetterFoundUnderTurkishLocaleTest() {
		final BeanDesc beanDesc = new BeanDesc(SampleBean.class);
		final PropDesc activeProp = beanDesc.getProp("active");
		assertNotNull(activeProp);
		assertNotNull(activeProp.getGetter(), "isActIve getter should be found under Turkish locale");
		assertNotNull(activeProp.getSetter(), "setActIve setter should be found under Turkish locale");
		assertEquals(Boolean.TRUE, activeProp.getValue(new SampleBean()));
	}

	public static class SampleBean {
		public boolean active;

		public boolean isActIve() {
			return true;
		}

		public void setActIve(boolean active) {
			this.active = active;
		}
	}
}
