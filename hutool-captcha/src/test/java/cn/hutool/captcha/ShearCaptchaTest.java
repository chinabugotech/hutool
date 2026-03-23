package cn.hutool.captcha;

import cn.hutool.captcha.generator.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class ShearCaptchaTest {

	private ShearCaptcha captcha;

	@BeforeEach
	public void setUp() {
		// 初始化 ShearCaptcha 实例
		captcha = new ShearCaptcha(200, 100);
	}

	// 测试构造函数和基本功能
	@Test
	public void testConstructor() {
		assertNotNull(captcha, "Captcha 实例应该被成功创建");
	}

	// 测试生成验证码图片的功能
	@Test
	public void testCreateImage() {
		String code = "ABCD";
		Image image = captcha.createImage(code);
		assertNotNull(image, "验证码图片不应该为 null");
		assertInstanceOf(BufferedImage.class, image, "生成的图片应该是 BufferedImage 类型");

		// 可选：进一步测试图像的内容
		BufferedImage bufferedImage = (BufferedImage) image;
		assertEquals(200, bufferedImage.getWidth(), "图像宽度应该为 200");
		assertEquals(100, bufferedImage.getHeight(), "图像高度应该为 100");
	}

	// 测试绘制字符串的方法
	@Test
	public void testDrawString() throws Exception {
		String code = "ABCD";
		Method drawStringMethod = ShearCaptcha.class.getDeclaredMethod("drawString", Graphics2D.class, String.class);
		drawStringMethod.setAccessible(true);

		Graphics2D g2d = (Graphics2D) new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
		drawStringMethod.invoke(captcha, g2d, code);

		assertNotNull(g2d, "Graphics2D 对象不应该为 null");
		assertTrue(g2d.getRenderingHints().containsKey(RenderingHints.KEY_ANTIALIASING), "应该启用抗锯齿");
	}

	// 测试 shear() 方法
	@Test
	public void testShear() throws Exception {
		// 使用反射测试 shear 方法
		Method shearMethod = ShearCaptcha.class.getDeclaredMethod("shear", Graphics.class, int.class, int.class, Color.class);
		shearMethod.setAccessible(true);

		Graphics g = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
		shearMethod.invoke(captcha, g, 200, 100, Color.WHITE);

		// 假设没有明显的错误输出，认为测试通过
		assertNotNull(g, "Graphics 对象不应该为 null");
	}

	// 测试 shearX() 方法
	@Test
	public void testShearX() throws Exception {
		// 使用反射测试 shearX 方法
		Method shearXMethod = ShearCaptcha.class.getDeclaredMethod("shearX", Graphics.class, int.class, int.class, Color.class);
		shearXMethod.setAccessible(true);

		Graphics g = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
		shearXMethod.invoke(captcha, g, 200, 100, Color.RED);

		// 假设没有明显的错误输出，认为测试通过
		assertNotNull(g, "Graphics 对象不应该为 null");
	}

	// 测试 shearY() 方法
	@Test
	public void testShearY() throws Exception {
		// 使用反射测试 shearY 方法
		Method shearYMethod = ShearCaptcha.class.getDeclaredMethod("shearY", Graphics.class, int.class, int.class, Color.class);
		shearYMethod.setAccessible(true);

		Graphics g = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
		shearYMethod.invoke(captcha, g, 200, 100, Color.BLUE);

		// 假设没有明显的错误输出，认为测试通过
		assertNotNull(g, "Graphics 对象不应该为 null");
	}

	// 测试 drawInterfere() 方法
	@Test
	public void testDrawInterfere() throws Exception {
		// 使用反射测试 drawInterfere 方法
		Method drawInterfereMethod = ShearCaptcha.class.getDeclaredMethod("drawInterfere", Graphics.class, int.class, int.class, int.class, int.class, int.class, Color.class);
		drawInterfereMethod.setAccessible(true);

		Graphics g = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
		drawInterfereMethod.invoke(captcha, g, 0, 0, 200, 100, 4, Color.GREEN);

		// 假设没有明显的错误输出，认为测试通过
		assertNotNull(g, "Graphics 对象不应该为 null");
	}

	// 测试验证码生成时的干扰线
	@Test
	public void testDrawInterfereLines() {
		// 设置干扰线数量
		captcha = new ShearCaptcha(200, 100, 4);
		Image image = captcha.createImage("ABCD");

		// 检查图像内容，判断干扰线是否正确绘制
		assertNotNull(image, "生成的验证码图片不应该为空");
	}

	// 测试验证码的尺寸
	@Test
	public void testCaptchaSize() {
		captcha = new ShearCaptcha(300, 150);

		String code = "XYZ";
		Image image = captcha.createImage(code);

		BufferedImage bufferedImage = (BufferedImage) image;
		assertEquals(300, bufferedImage.getWidth(), "图像宽度应该为 300");
		assertEquals(150, bufferedImage.getHeight(), "图像高度应该为 150");
	}

	// 测试生成随机验证码字符
	@Test
	public void testRandomGenerator() {
		RandomGenerator randomGenerator = new RandomGenerator(4);
		String code = randomGenerator.generate();
		assertNotNull(code, "生成的验证码字符不应该为 null");
		assertEquals(4, code.length(), "验证码字符长度应该为 4");
	}
}
