package cn.hutool.captcha;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class GifCaptchaUtilTest {

	private GifCaptcha captcha;

	@BeforeEach
	public void setUp() {
		// 初始化 GifCaptcha 类的实例
		captcha = new GifCaptcha(200, 100, 4, 10);  // width, height, codeCount, interfereCount
	}

	// 使用反射调用私有方法
	private Object invokePrivateMethod(String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Exception {
		Method method = GifCaptcha.class.getDeclaredMethod(methodName, parameterTypes);
		method.setAccessible(true);  // 允许访问私有方法
		return method.invoke(captcha, parameters);
	}

	// 测试 setQuality() 方法
	@Test
	public void testSetQuality() throws Exception {
		captcha.setQuality(20);
		// 通过反射获取 quality 字段的值并进行断言
		assertEquals(20, getPrivateField("quality"), "Quality 应该设置为 20");

		captcha.setQuality(0);  // 设置无效值，应该被设置为 1
		assertEquals(1, getPrivateField("quality"), "Quality 应该设置为 1，如果小于 1");
	}

	// 测试 setRepeat() 方法
	@Test
	public void testSetRepeat() throws Exception {
		captcha.setRepeat(5);
		// 通过反射获取 repeat 字段的值并进行断言
		assertEquals(5, getPrivateField("repeat"), "Repeat 应该设置为 5");

		captcha.setRepeat(-1);  // 设置无效值，应该保持为 0
		assertEquals(0, getPrivateField("repeat"), "Repeat 应该设置为 0，如果设置了负值");
	}

	// 测试 setColorRange() 方法
	@Test
	public void testSetColorRange() throws Exception {
		captcha.setMinColor(100).setMaxColor(200);
		// 通过反射获取 minColor 和 maxColor 字段的值并进行断言
		assertEquals(100, getPrivateField("minColor"), "Min color 应该设置为 100");
		assertEquals(200, getPrivateField("maxColor"), "Max color 应该设置为 200");
	}

	// 测试生成验证码图像的方法 createCode()
	@Test
	public void testCreateCode() throws Exception {
		captcha.createCode();
		byte[] imageBytes = captcha.getImageBytes();

		// 检查生成的图片字节是否不为 null 或空
		assertNotNull(imageBytes, "生成的图片字节不应该为 null");
		assertTrue(imageBytes.length > 0, "生成的图片字节不应该为空");

		// 可选：你也可以通过解码图片字节，检查它是否是有效的 GIF 格式
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(imageBytes);

		// 解码图片检查它是否为有效的 GIF（假设你有库可以解码 GIF）
		// ImageIO.read(new ByteArrayInputStream(imageBytes)); // 可以取消注释来检查它是否是有效的 GIF
	}

	// 测试 graphicsImage() 方法
	@Test
	public void testGraphicsImage() throws Exception {
		char[] chars = new char[]{'A', 'B', 'C', 'D'};
		Color[] colors = new Color[]{
			Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW
		};

		// 使用反射调用 private 方法 graphicsImage
		Object result = invokePrivateMethod("graphicsImage", new Class[]{char[].class, Color[].class, char[].class, int.class}, new Object[]{chars, colors, chars, 0});

		assertNotNull(result, "生成的图片不应该为 null");
		assertInstanceOf(BufferedImage.class, result, "返回的结果应该是 BufferedImage 类型");
	}

	// 测试 getRandomColor() 方法
	@Test
	public void testRandomColor() throws Exception {
		// 使用反射调用 private 方法 getRandomColor
		Object result = invokePrivateMethod("getRandomColor", new Class[]{int.class, int.class}, new Object[]{0, 255});

		assertNotNull(result, "生成的颜色不应该为 null");
		assertInstanceOf(Color.class, result, "返回的结果应该是 Color 类型");

		Color color = (Color) result;
		assertTrue(color.getRed() >= 0 && color.getRed() <= 255, "颜色的红色分量应该在 0 到 255 之间");
		assertTrue(color.getGreen() >= 0 && color.getGreen() <= 255, "颜色的绿色分量应该在 0 到 255 之间");
		assertTrue(color.getBlue() >= 0 && color.getBlue() <= 255, "颜色的蓝色分量应该在 0 到 255 之间");
	}

	// 辅助方法：通过反射获取私有字段的值
	private Object getPrivateField(String fieldName) throws NoSuchFieldException, IllegalAccessException {
		Field field = GifCaptcha.class.getDeclaredField(fieldName);
		field.setAccessible(true);  // 允许访问私有字段
		return field.get(captcha);
	}
}
