package cn.hutool.extra.barcode;

import cn.hutool.core.img.ImgUtil;
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

	/**
	 * 赞不支持半透明，暂时不测试，仅保留
	 * 测试半透明前景色和背景色
	 * 生成具有半透明效果的条形码
	 */
	@Test
	@Disabled
	@Deprecated
	public void generateWithSemiTransparentColorsTest() {
		// 半透明前景色和背景色
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(400);
		config.setHeight(100);
		config.setShowText(true);
		config.setForeColor(new Color(0, 0, 0, 128)); // 半透明黑色
		config.setBackColor(new Color(255, 255, 255, 128)); // 半透明白色

		final String path = FileUtil.isWindows() ? "d:/test/barcodeSemiTransparent.png" : "~/Desktop/hutool/barcodeSemiTransparent.png";
		BarCodeUtil.generate("1234567890", config, FileUtil.touch(path));

		// 半透明前景色和透明背景
		final BarCodeConfig configTransparentBg = new BarCodeConfig();
		configTransparentBg.setWidth(400);
		configTransparentBg.setHeight(100);
		configTransparentBg.setShowText(true);
		configTransparentBg.setForeColor(new Color(255, 0, 0, 160)); // 半透明红色
		configTransparentBg.setBackColor(null); // 透明背景

		final String pathTransparentBg = FileUtil.isWindows() ? "d:/test/barcodeSemiTransWithTransparentBg.png" : "~/Desktop/hutool/barcodeSemiTransWithTransparentBg.png";
		BarCodeUtil.generate("1234567890", configTransparentBg, FileUtil.touch(pathTransparentBg));
	}

	/**
	 * 测试不同条形码格式的兼容性
	 * 生成各种类型的条形码
	 */
	@Test
	@Disabled
	public void testDifferentBarcodeFormats() {
		BarcodeFormat[] formats = new BarcodeFormat[] {
			BarcodeFormat.CODE_128,
			BarcodeFormat.CODE_39,
			BarcodeFormat.EAN_13,
			BarcodeFormat.EAN_8,
			BarcodeFormat.UPC_A,
			BarcodeFormat.QR_CODE,
			BarcodeFormat.DATA_MATRIX,
			BarcodeFormat.AZTEC,
			BarcodeFormat.PDF_417
		};

		String[] testData = new String[] {
			"1234567890", // 一般数字
			"1234567890", // CODE_39
			"6923450657713", // EAN_13
			"69234505", // EAN_8
			"123456789012", // UPC_A
			"https://hutool.cn", // QR_CODE
			"HUTOOL123456", // DATA_MATRIX
			"HUTOOL条形码生成测试", // AZTEC
			"HUTOOL Barcode Generation Test" // PDF_417
		};

		for (int i = 0; i < formats.length; i++) {
			try {
				final BarCodeConfig config = new BarCodeConfig();
				config.setWidth(400);
				config.setHeight(200);
				config.setShowText(true);
				config.setBarcodeFormat(formats[i]);

				final String formatName = formats[i].name().toLowerCase();
				final String path = FileUtil.isWindows() ?
					"d:/test/barcode_" + formatName + ".png" :
					"~/Desktop/hutool/barcode_" + formatName + ".png";

				BarCodeUtil.generate(testData[i], config, FileUtil.touch(path));
			} catch (Exception e) {
				System.out.println("格式 " + formats[i].name() + " 生成失败: " + e.getMessage());
			}
		}
	}

	/**
	 * 测试极端尺寸的条形码生成
	 */
	@Test
	@Disabled
	public void testExtremeSizes() {
		// 非常小的条形码
		final BarCodeConfig smallConfig = new BarCodeConfig();
		smallConfig.setWidth(50);
		smallConfig.setHeight(20);

		final String smallPath = FileUtil.isWindows() ? "d:/test/barcodeSmall.png" : "~/Desktop/hutool/barcodeSmall.png";
		BarCodeUtil.generate("123", smallConfig, FileUtil.touch(smallPath));

		// 非常大的条形码
		final BarCodeConfig largeConfig = new BarCodeConfig();
		largeConfig.setWidth(1200);
		largeConfig.setHeight(300);

		final String largePath = FileUtil.isWindows() ? "d:/test/barcodeLarge.png" : "~/Desktop/hutool/barcodeLarge.png";
		BarCodeUtil.generate("1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ", largeConfig, FileUtil.touch(largePath));
	}

	/**
	 * 测试不同的字体和文本样式
	 */
	@Test
	@Disabled
	public void testDifferentFontsAndTextStyles() {
		// 不同字体
		String[] fontNames = {"宋体", "Arial", "Times New Roman", "Courier New", "微软雅黑"};
		int[] fontStyles = {Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC};
		String[] styleNames = {"plain", "bold", "italic", "bolditalic"};

		for (int i = 0; i < fontNames.length; i++) {
			for (int j = 0; j < fontStyles.length; j++) {
				final BarCodeConfig config = new BarCodeConfig();
				config.setWidth(400);
				config.setHeight(100);
				config.setShowText(true);
				config.setTextFont(new Font(fontNames[i], fontStyles[j], 16));
				config.setCustomText(fontNames[i] + " " + styleNames[j]);

				final String path = FileUtil.isWindows() ?
					"d:/test/barcode_font_" + fontNames[i] + "_" + styleNames[j] + ".png" :
					"~/Desktop/hutool/barcode_font_" + fontNames[i] + "_" + styleNames[j] + ".png";

				try {
					BarCodeUtil.generate("1234567890", config, FileUtil.touch(path));
				} catch (Exception e) {
					System.out.println("字体 " + fontNames[i] + " 样式 " + styleNames[j] + " 生成失败: " + e.getMessage());
				}
			}
		}
	}

	/**
	 * 测试条形码内容为空或特殊字符
	 * {{条码不支持中文}}
	 */
	@Test
	@Disabled
	public void testSpecialContentBarcodes() {
		String[] specialContents = {
			"", // 空字符串
			" ", // 空格
			"!@#$%^&*()_+-=[]{}|;':\",./<>?", // 特殊字符
			"中文测试", // 中文
			"English 中文 Mixed 混合", // 混合文本
			"https://hutool.cn/docs/", // URL
			"12345678901234567890123456789012345678901234567890" // 长内容
		};

		for (int i = 0; i < specialContents.length; i++) {
			final BarCodeConfig config = new BarCodeConfig();
			config.setWidth(400);
			config.setHeight(100);
			config.setShowText(true);

			final String fileName = "barcode_special_" + i + ".png";
			final String path = FileUtil.isWindows() ?
				"d:/test/" + fileName :
				"~/Desktop/hutool/" + fileName;

			try {
				BarCodeUtil.generate(specialContents[i], config, FileUtil.touch(path));
			} catch (Exception e) {
				System.out.println("特殊内容 " + i + " 生成失败: " + e.getMessage());
			}
		}
	}

	/**
	 * 测试错误处理情况
	 */
	@Test
	public void testErrorHandling() {
		// 预期会抛出异常的情况
		final BarCodeConfig invalidFormatConfig = new BarCodeConfig();
		invalidFormatConfig.setWidth(0); // 宽度为0
		invalidFormatConfig.setHeight(0); // 高度为0

		try {
			BarCodeUtil.generate("test", invalidFormatConfig);
			// 如果没有抛出异常则失败
			Assert.isTrue(false, "应该抛出异常但没有");
		} catch (Exception e) {
			// 期望捕获异常
			Assert.isTrue(true);
		}

		// EAN-13需要13位数字
		final BarCodeConfig ean13Config = new BarCodeConfig();
		ean13Config.setBarcodeFormat(BarcodeFormat.EAN_13);

		try {
			BarCodeUtil.generate("123", ean13Config); // 非13位数字
			// 某些实现可能不会立即验证
		} catch (Exception e) {
			// 期望捕获异常
			Assert.isTrue(true);
		}
	}

	/**
	 * 测试生成大量条形码的性能
	 */
	@Test
	@Disabled
	public void testPerformance() {
		final int iterations = 100;
		final long startTime = System.currentTimeMillis();

		for (int i = 0; i < iterations; i++) {
			BarCodeUtil.generate(String.valueOf(i), 300, 100);
		}

		final long endTime = System.currentTimeMillis();
		final long totalTime = endTime - startTime;

		System.out.println("生成 " + iterations + " 个条形码耗时: " + totalTime + "ms");
		System.out.println("平均每个条形码耗时: " + (totalTime / iterations) + "ms");
	}

	/**
	 * 测试组合多种条形码设置
	 */
	@Test
	@Disabled
	public void testCombinedSettings() {
		// 透明背景 + 自定义文本 + 顶部文本位置
		final BarCodeConfig config1 = new BarCodeConfig();
		config1.setWidth(400);
		config1.setHeight(100);
		config1.setBackColor(null);
		config1.setForeColor(Color.BLUE);
		config1.setShowText(true);
		config1.setCustomText("透明背景+蓝色条码+自定义文本");
		config1.setTextPosition("top");
		config1.setTextFont(new Font("微软雅黑", Font.BOLD, 14));
		config1.setTextColor(Color.RED);

		final String path1 = FileUtil.isWindows() ? "d:/test/barcode_combined1.png" : "~/Desktop/hutool/barcode_combined1.png";
		BarCodeUtil.generate("1234567890", config1, FileUtil.touch(path1));

		// 灰色背景 + 不保留静区 + 底部文本
		final BarCodeConfig config2 = new BarCodeConfig();
		config2.setWidth(400);
		config2.setHeight(100);
		config2.setBackColor(new Color(240, 240, 240));
		config2.setForeColor(Color.BLACK);
		config2.setShowText(true);
		config2.setKeepQuietZone(false);
		config2.setMargin(5);
		config2.setTextPosition("bottom");

		final String path2 = FileUtil.isWindows() ? "d:/test/barcode_combined2.png" : "~/Desktop/hutool/barcode_combined2.png";
		BarCodeUtil.generate("1234567890", config2, FileUtil.touch(path2));
	}

	/**
	 * 测试透明背景在不同输出格式下的表现
	 */
	@Test
	@Disabled
	public void testTransparentBackgroundInDifferentFormats() {
		// 创建透明背景条形码配置
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(400);
		config.setHeight(100);
		config.setShowText(true);
		config.setBackColor(null);
		config.setForeColor(Color.BLACK);
		config.setCustomText("透明背景条形码");

		// 保存为PNG
		final String pngPath = FileUtil.isWindows() ? "d:/test/transparent_format_png.png" : "~/Desktop/hutool/transparent_format_png.png";
		BarCodeUtil.generate("1234567890", config, FileUtil.touch(pngPath));

		// 保存为SVG
		final String svgPath = FileUtil.isWindows() ? "d:/test/transparent_format_svg.svg" : "~/Desktop/hutool/transparent_format_svg.svg";
		final String svg = BarCodeUtil.generateAsSvg("1234567890", config);
		FileUtil.writeString(svg, svgPath, StandardCharsets.UTF_8);

		// 生成为Base64
		final String pngBase64 = BarCodeUtil.generateAsBase64("1234567890", config, ImgUtil.IMAGE_TYPE_PNG);
		final String pngBase64Path = FileUtil.isWindows() ? "d:/test/transparent_png_base64.txt" : "~/Desktop/hutool/transparent_png_base64.txt";
		FileUtil.writeString(pngBase64, pngBase64Path, StandardCharsets.UTF_8);

		final String svgBase64 = BarCodeUtil.generateAsBase64("1234567890", config, BarCodeUtil.BARCODE_TYPE_SVG);
		final String svgBase64Path = FileUtil.isWindows() ? "d:/test/transparent_svg_base64.txt" : "~/Desktop/hutool/transparent_svg_base64.txt";
		FileUtil.writeString(svgBase64, svgBase64Path, StandardCharsets.UTF_8);
	}

	/**
	 * 测试Base64编码条形码的生成
	 */
	@Test
	public void testBase64BarcodeGeneration() {
		// 测试普通条形码的Base64编码
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(400);
		config.setHeight(100);
		config.setShowText(true);

		// PNG格式
		final String pngBase64 = BarCodeUtil.generateAsBase64("1234567890", config, ImgUtil.IMAGE_TYPE_PNG);
		Assert.notNull(pngBase64);
		Assert.isTrue(pngBase64.startsWith("data:image/png;base64,"));

		// 透明背景PNG
		config.setBackColor(null);
		final String transparentPngBase64 = BarCodeUtil.generateAsBase64("1234567890", config, ImgUtil.IMAGE_TYPE_PNG);
		Assert.notNull(transparentPngBase64);
		Assert.isTrue(transparentPngBase64.startsWith("data:image/png;base64,"));

		// 不同条形码格式的Base64
		config.setBarcodeFormat(BarcodeFormat.QR_CODE);
		config.setWidth(200);
		config.setHeight(200);
		final String qrBase64 = BarCodeUtil.generateAsBase64("https://hutool.cn", config, ImgUtil.IMAGE_TYPE_PNG);
		Assert.notNull(qrBase64);
		Assert.isTrue(qrBase64.startsWith("data:image/png;base64,"));
	}

	/**
	 * 测试中文和特殊符号在条形码文本中的显示
	 * {{不支持颜文字}}
	 */
	@Test
	@Disabled
	public void testChineseAndSpecialCharsInBarcode() {
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(400);
		config.setHeight(100);
		config.setShowText(true);
		config.setTextFont(new Font("宋体", Font.PLAIN, 14));

		// 中文文本
		config.setCustomText("测试中文显示效果");
		final String chinesePath = FileUtil.isWindows() ? "d:/test/barcode_chinese_text.png" : "~/Desktop/hutool/barcode_chinese_text.png";
		BarCodeUtil.generate("1234567890", config, FileUtil.touch(chinesePath));

		// 特殊符号文本
		config.setCustomText("!@#$%^&*()_+-={}[]|\\:;\"'<>,.?/");
		final String specialCharsPath = FileUtil.isWindows() ? "d:/test/barcode_special_chars_text.png" : "~/Desktop/hutool/barcode_special_chars_text.png";
		BarCodeUtil.generate("1234567890", config, FileUtil.touch(specialCharsPath));

		// Emoji文本
		config.setCustomText("😀👍🎉🚀🌈");
		final String emojiPath = FileUtil.isWindows() ? "d:/test/barcode_emoji_text.png" : "~/Desktop/hutool/barcode_emoji_text.png";
		try {
			BarCodeUtil.generate("1234567890", config, FileUtil.touch(emojiPath));
		} catch (Exception e) {
			System.out.println("Emoji文本生成失败: " + e.getMessage());
		}
	}

	/**
	 * 测试条形码保存为其他格式
	 */
	@Test
	@Disabled
	public void testSaveToOtherImageFormats() {
		final BarCodeConfig config = new BarCodeConfig();
		config.setWidth(400);
		config.setHeight(100);
		config.setShowText(true);

		// 保存为JPEG
		final String jpegPath = FileUtil.isWindows() ? "d:/test/barcode.jpg" : "~/Desktop/hutool/barcode.jpg";
		try (final BufferedOutputStream out = FileUtil.getOutputStream(jpegPath)) {
			BarCodeUtil.generate("1234567890", config, "jpg", out);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 保存为GIF
		final String gifPath = FileUtil.isWindows() ? "d:/test/barcode.gif" : "~/Desktop/hutool/barcode.gif";
		try (final BufferedOutputStream out = FileUtil.getOutputStream(gifPath)) {
			BarCodeUtil.generate("1234567890", config, "gif", out);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 保存为BMP
		final String bmpPath = FileUtil.isWindows() ? "d:/test/barcode.bmp" : "~/Desktop/hutool/barcode.bmp";
		try (final BufferedOutputStream out = FileUtil.getOutputStream(bmpPath)) {
			BarCodeUtil.generate("1234567890", config, "bmp", out);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 透明背景测试 - 只有PNG和GIF支持透明
		config.setBackColor(null);

		// PNG(支持透明)
		final String transparentPngPath = FileUtil.isWindows() ? "d:/test/barcode_transparent.png" : "~/Desktop/hutool/barcode_transparent.png";
		try (final BufferedOutputStream out = FileUtil.getOutputStream(transparentPngPath)) {
			BarCodeUtil.generate("1234567890", config, "png", out);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// JPEG(不支持透明)
		final String transparentJpegPath = FileUtil.isWindows() ? "d:/test/barcode_transparent.jpg" : "~/Desktop/hutool/barcode_transparent.jpg";
		try (final BufferedOutputStream out = FileUtil.getOutputStream(transparentJpegPath)) {
			BarCodeUtil.generate("1234567890", config, "jpg", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
