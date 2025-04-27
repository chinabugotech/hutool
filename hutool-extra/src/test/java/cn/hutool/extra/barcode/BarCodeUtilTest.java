package cn.hutool.extra.barcode;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import com.google.zxing.BarcodeFormat;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 条形码工具类单元测试
 *
 * @author Libai
 * @since 5.8.38
 */
public class BarCodeUtilTest {

	/**
	 * 测试生成基本条形码图片
	 * 使用默认的CODE_128格式生成简单条形码
	 */
	@Test
	public void generateTest() {
		final BufferedImage image = BarCodeUtil.generate("1234567890", 300, 100);
		Assert.notNull(image);
	}

	/**
	 * 测试使用自定义配置生成条形码
	 * 包括自定义宽高、边距、颜色、字体等
	 */
	@Test
	@Disabled
	public void generateWithCustomConfigTest() {
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(400);
		config.setHeight(100);
		config.setMargin(10);
		config.setForeColor(Color.BLUE);
		config.setBackColor(Color.LIGHT_GRAY);
		config.setShowText(true);
		config.setTextFont(new Font("Arial", Font.BOLD, 20));
		config.setTextColor(Color.RED);

		final String path = FileUtil.isWindows() ? "d:/test/barcodeCustom.png" : "~/Desktop/hutool/barcodeCustom.png";
		BarCodeUtil.generate("1234567890", config, FileUtil.touch(path));
	}

	/**
	 * 测试不同条形码格式的生成
	 * 演示CODE_39和EAN_13两种不同格式的条形码生成
	 */
	@Test
	@Disabled
	public void generateFormatTest() {
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(400);
		config.setHeight(100);
		config.setShowText(true);
		config.setBarcodeFormat(BarcodeFormat.CODE_39);

		final String path = FileUtil.isWindows() ? "d:/test/barcode39.png" : "~/Desktop/hutool/barcode39.png";
		BarCodeUtil.generate("1234567890", config, FileUtil.touch(path));

		final BarCodeConfig configEan = new BarCodeConfig();
		configEan.setWidth(400);
		configEan.setHeight(100);
		configEan.setShowText(true);
		configEan.setBarcodeFormat(BarcodeFormat.EAN_13);

		final String pathEan = FileUtil.isWindows() ? "d:/test/barcodeEan13.png" : "~/Desktop/hutool/barcodeEan13.png";
		BarCodeUtil.generate("6923450657713", configEan, FileUtil.touch(pathEan));
	}

	/**
	 * 测试生成Base64编码的条形码
	 * 生成条形码并转换为Base64字符串
	 */
	@Test
	public void generateAsBase64Test() {
		final String base64 = BarCodeUtil.generateAsBase64("1234567890", 400, 100);
		Assert.notNull(base64);
	}

	/**
	 * 测试不同文本位置的条形码生成
	 * 分别在条形码底部和顶部显示文本
	 */
	@Test
	@Disabled
	public void generateWithTextPositionTest() {
		// 底部文本
		final BarCodeConfig configBottom = new BarCodeConfig();
		configBottom.setWidth(400);
		configBottom.setHeight(100);
		configBottom.setShowText(true);
		configBottom.setTextPosition("bottom");

		final String pathBottom = FileUtil.isWindows() ? "d:/test/barcodeBottom.png" : "~/Desktop/hutool/barcodeBottom.png";
		BarCodeUtil.generate("1234567890", configBottom, FileUtil.touch(pathBottom));

		// 顶部文本
		final BarCodeConfig configTop = new BarCodeConfig();
		configTop.setWidth(400);
		configTop.setHeight(100);
		configTop.setShowText(true);
		configTop.setTextPosition("top");

		final String pathTop = FileUtil.isWindows() ? "d:/test/barcodeTop.png" : "~/Desktop/hutool/barcodeTop.png";
		BarCodeUtil.generate("1234567890", configTop, FileUtil.touch(pathTop));
	}

	/**
	 * 测试条形码静区（两端空白区域）的显示与裁剪
	 * 分别生成保留静区和不保留静区的条形码进行对比
	 */
	@Test
	@Disabled
	public void generateWithNoQuietZoneTest() {
		// 保留静区
		final BarCodeConfig configWithZone = new BarCodeConfig();
		configWithZone.setWidth(400);
		configWithZone.setHeight(100);
		configWithZone.setShowText(true);
		configWithZone.setKeepQuietZone(true);

		final String pathWithZone = FileUtil.isWindows() ? "d:/test/barcodeWithZone.png" : "~/Desktop/hutool/barcodeWithZone.png";
		BarCodeUtil.generate("1234567890", configWithZone, FileUtil.touch(pathWithZone));

		// 不保留静区
		final BarCodeConfig configNoZone = new BarCodeConfig();
		configNoZone.setWidth(400);
		configNoZone.setHeight(100);
		configNoZone.setShowText(true);
		configNoZone.setKeepQuietZone(false);

		final String pathNoZone = FileUtil.isWindows() ? "d:/test/barcodeNoZone.png" : "~/Desktop/hutool/barcodeNoZone.png";
		BarCodeUtil.generate("1234567890", configNoZone, FileUtil.touch(pathNoZone));
	}

	/**
	 * 测试条形码显示自定义文本
	 * 在条形码下方显示自定义文本而非条形码内容
	 */
	@Test
	@Disabled
	public void generateWithCustomTextTest() {
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(400);
		config.setHeight(100);
		config.setShowText(true);
		config.setCustomText("自定义文本");
		config.setTextFont(new Font("宋体", Font.BOLD, 16));

		final String path = FileUtil.isWindows() ? "d:/test/barcodeCustomText.png" : "~/Desktop/hutool/barcodeCustomText.png";
		BarCodeUtil.generate("1234567890", config, FileUtil.touch(path));
	}

	/**
	 * 测试条形码生成到输出流
	 * 将条形码直接写入到输出流，适用于网页等无文件场景
	 */
	@Test
	@Disabled
	public void generateToStreamTest() {
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(400);
		config.setHeight(100);
		config.setShowText(true);

		final String filepath = FileUtil.isWindows() ? "d:/test/barcode_stream.png" : "~/Desktop/hutool/barcode_stream.png";
		try (final BufferedOutputStream outputStream = FileUtil.getOutputStream(filepath)) {
			BarCodeUtil.generate("1234567890", config, "png", outputStream);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试生成条形码为PNG字节数组
	 * 条形码数据直接以字节数组形式返回，适用于内存处理
	 */
	@Test
	public void generatePngBytesTest() {
		final byte[] bytes = BarCodeUtil.generatePng("1234567890", 300, 100);
		Assert.notNull(bytes);
		Assert.isTrue(bytes.length > 0);
	}

	/**
	 * 测试将PNG字节数组保存到文件
	 * 演示如何将字节数组形式的条形码保存为文件
	 */
	@Test
	@Disabled
	public void generatePngBytesToFileTest() {
		final byte[] bytes = BarCodeUtil.generatePng("1234567890", 300, 100);

		final String path = FileUtil.isWindows() ? "d:/test/barcodePngBytes.png" : "~/Desktop/hutool/barcodePngBytes.png";
		FileUtil.writeBytes(bytes, path);
	}

	/**
	 * 测试生成SVG格式的条形码
	 * 生成矢量图形式的条形码，支持无损缩放
	 */
	@Test
	public void generateAsSvgTest() {
		final String svg = BarCodeUtil.generateAsSvg("1234567890", new BarCodeConfig(400, 100));
		Assert.notNull(svg);
		Assert.isTrue(svg.contains("<svg"));
	}

	/**
	 * 测试将SVG条形码保存到文件
	 * 演示如何将SVG格式的条形码保存为文件
	 */
	@Test
	@Disabled
	public void generateAsSvgToFileTest() {
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(400);
		config.setHeight(100);
		config.setShowText(true);
		config.setForeColor(Color.BLUE);
		config.setBackColor(Color.LIGHT_GRAY);

		final String svg = BarCodeUtil.generateAsSvg("1234567890", config);
		final String path = FileUtil.isWindows() ? "d:/test/barcode.svg" : "~/Desktop/hutool/barcode.svg";
		FileUtil.writeString(svg, path, StandardCharsets.UTF_8);
	}

	/**
	 * 测试生成ASCII Art字符画形式的条形码
	 * 生成由ASCII字符组成的条形码，适用于控制台或纯文本环境
	 */
	@Test
	public void generateAsAsciiArtTest() {
		final String asciiArt = BarCodeUtil.generateAsAsciiArt("1234567890");
		Assert.notNull(asciiArt);
	}

	/**
	 * 测试将ASCII Art条形码保存到文件
	 * 演示如何将ASCII字符画形式的条形码保存为文本文件
	 */
	@Test
	@Disabled
	public void generateAsAsciiArtToFileTest() {
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(80);
		config.setHeight(10);
		config.setShowText(true);
		config.setForeColor(Color.BLACK);
		config.setBackColor(Color.WHITE);

		final String asciiArt = BarCodeUtil.generateAsAsciiArt("1234567890", config);
		System.out.println(asciiArt);
		final String path = FileUtil.isWindows() ? "d:/test/barcode.txt" : "~/Desktop/hutool/barcode.txt";
		FileUtil.writeString(asciiArt, path, StandardCharsets.UTF_8);
	}

	/**
	 * 测试生成SVG格式条形码的Base64编码
	 * 生成SVG条形码并转换为Base64编码字符串
	 */
	@Test
	public void generateAsBase64SvgTest() {
		final String base64 = BarCodeUtil.generateAsBase64("1234567890", new BarCodeConfig(400, 100), BarCodeUtil.BARCODE_TYPE_SVG);
		Assert.notNull(base64);
		Assert.isTrue(base64.startsWith("data:image/svg+xml;base64,"));
	}

	/**
	 * 测试生成ASCII Art条形码的Base64编码
	 * 生成ASCII Art条形码并转换为Base64编码字符串
	 */
	@Test
	public void generateAsBase64TxtTest() {
		final String base64 = BarCodeUtil.generateAsBase64("1234567890", new BarCodeConfig(400, 100), BarCodeUtil.BARCODE_TYPE_TXT);
		Assert.notNull(base64);
		Assert.isTrue(base64.startsWith("data:text/plain;base64,"));
	}

	/**
	 * 测试比较不同格式的条形码
	 * 对比生成PNG、SVG、TXT三种不同格式的条形码
	 */
	@Test
	@Disabled
	public void compareFormatsTest() {
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(400);
		config.setHeight(100);
		config.setShowText(true);
		config.setForeColor(Color.BLACK);
		config.setBackColor(Color.WHITE);

		final String content = "1234567890";

		// PNG
		final String pngPath = FileUtil.isWindows() ? "d:/test/barcode_compare.png" : "~/Desktop/hutool/barcode_compare.png";
		BarCodeUtil.generate(content, config, FileUtil.touch(pngPath));

		// SVG
		final String svgPath = FileUtil.isWindows() ? "d:/test/barcode_compare.svg" : "~/Desktop/hutool/barcode_compare.svg";
		BarCodeUtil.generate(content, config, FileUtil.touch(svgPath));

		// TXT
		final String txtPath = FileUtil.isWindows() ? "d:/test/barcode_compare.txt" : "~/Desktop/hutool/barcode_compare.txt";
		BarCodeUtil.generate(content, config, FileUtil.touch(txtPath));
	}

	/**
	 * 测试透明背景条形码生成
	 * 生成背景透明的条形码，验证backColor为null的处理
	 */
	@Test
	@Disabled
	public void generateWithTransparentBackgroundTest() {
		// 创建透明背景条形码配置
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(400);
		config.setHeight(100);
		config.setShowText(true);
		config.setBackColor(null); // 设置背景色为null，生成透明背景
		config.setForeColor(Color.BLACK);

		final String path = FileUtil.isWindows() ? "d:/test/barcodeTransparent.png" : "~/Desktop/hutool/barcodeTransparent.png";
		BarCodeUtil.generate("1234567890", config, FileUtil.touch(path));

		// 测试不保留静区的透明背景条形码
		final BarCodeConfig configNoZone = new BarCodeConfig();
		configNoZone.setWidth(400);
		configNoZone.setHeight(100);
		configNoZone.setShowText(true);
		configNoZone.setBackColor(null);
		configNoZone.setKeepQuietZone(false);

		final String pathNoZone = FileUtil.isWindows() ? "d:/test/barcodeTransparentNoZone.png" : "~/Desktop/hutool/barcodeTransparentNoZone.png";
		BarCodeUtil.generate("1234567890", configNoZone, FileUtil.touch(pathNoZone));

		// 测试透明背景SVG条形码
		final BarCodeConfig svgConfig = new BarCodeConfig();
		svgConfig.setWidth(400);
		svgConfig.setHeight(100);
		svgConfig.setShowText(true);
		svgConfig.setBackColor(null);

		final String svg = BarCodeUtil.generateAsSvg("1234567890", svgConfig);
		final String svgPath = FileUtil.isWindows() ? "d:/test/barcodeTransparent.svg" : "~/Desktop/hutool/barcodeTransparent.svg";
		FileUtil.writeString(svg, svgPath, StandardCharsets.UTF_8);
	}
}
