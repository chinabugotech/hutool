package cn.hutool.extra.barcode;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.ansi.AnsiColors;
import cn.hutool.core.lang.ansi.AnsiElement;
import cn.hutool.core.lang.ansi.AnsiEncoder;
import cn.hutool.core.lang.ansi.ForeOrBack;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 条形码工具类，基于Zxing
 *
 * @author Libai
 * @since 5.8.38
 */
public class BarCodeUtil {

	/**
	 * SVG矢量图格式
	 */
	public static final String BARCODE_TYPE_SVG = "svg";

	/**
	 * Ascii Art字符画文本
	 */
	public static final String BARCODE_TYPE_TXT = "txt";

	private static final AnsiColors ansiColors = new AnsiColors(AnsiColors.BitDepth.EIGHT);

	/**
	 * 生成 Base64 编码格式的条形码，以 String 形式表示
	 *
	 * <p>
	 * 输出格式为: data:image/[type];base64,[data]
	 * </p>
	 *
	 * @param content 内容
	 * @param width   宽度（单位：像素）
	 * @param height  高度（单位：像素）
	 * @return 图片 Base64 编码字符串
	 */
	public static String generateAsBase64(String content, int width, int height) {
		return generateAsBase64(content, new BarCodeConfig(width, height), ImgUtil.IMAGE_TYPE_PNG);
	}

	/**
	 * 生成 Base64 编码格式的条形码，以 String 形式表示
	 *
	 * <p>
	 * 输出格式为: data:image/[type];base64,[data]
	 * </p>
	 *
	 * @param content   内容
	 * @param config    条形码配置，包括宽度、高度、边距、颜色等
	 * @param imageType 图片类型（扩展名），见{@link ImgUtil}、{@link #BARCODE_TYPE_SVG}、{@link #BARCODE_TYPE_TXT}
	 * @return 图片 Base64 编码字符串
	 */
	public static String generateAsBase64(String content, BarCodeConfig config, String imageType) {
		String result;
		switch (imageType) {
			case BARCODE_TYPE_SVG:
				String svg = generateAsSvg(content, config);
				result = svgToBase64(svg);
				break;
			case BARCODE_TYPE_TXT:
				String txt = generateAsAsciiArt(content, config);
				result = txtToBase64(txt);
				break;
			default:
				final BufferedImage img = generate(content, config);
				result = ImgUtil.toBase64DataUri(img, imageType);
				break;
		}

		return result;
	}

	private static String txtToBase64(String txt) {
		return URLUtil.getDataUri("text/plain", "base64", Base64.encode(txt));
	}

	private static String svgToBase64(String svg) {
		return URLUtil.getDataUri("image/svg+xml", "base64", Base64.encode(svg));
	}

	/**
	 * 生成SVG矢量图格式的条形码
	 *
	 * @param content 内容
	 * @param config  条形码配置，包括宽度、高度、边距、颜色等
	 * @return SVG矢量图（字符串）
	 * @since 5.8.38
	 */
	public static String generateAsSvg(String content, BarCodeConfig config) {
		final BitMatrix bitMatrix = encode(content, config);
		return toSVG(bitMatrix, config);
	}

	/**
	 * BitMatrix转SVG(字符串)
	 *
	 * @param matrix BitMatrix
	 * @param config 条形码配置，包括宽度、高度、边距、颜色等
	 * @return SVG矢量图（字符串）
	 */
	public static String toSVG(BitMatrix matrix, BarCodeConfig config) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();

		StringBuilder result = StrUtil.builder();
		result.append("<svg width=\"").append(width).append("\" height=\"").append(height).append("\" \n");
		if (config.backColor != null) {
			Color back = config.backColor;
			result.append("style=\"background-color:rgba(").append(back.getRed()).append(",").append(back.getGreen()).append(",").append(back.getBlue()).append(",").append(back.getAlpha() / 255.0).append(")\"\n");
		}
		result.append("viewBox=\"0 0 ").append(width).append(" ").append(height).append("\" \n");
		result.append("xmlns=\"http://www.w3.org/2000/svg\" \n");
		result.append("xmlns:xlink=\"http://www.w3.org/1999/xlink\" >\n");

		StringBuilder path = new StringBuilder();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (matrix.get(x, y)) {
					path.append(" M").append(x).append(",").append(y).append("h1v1h-1z");
				}
			}
		}

		result.append("<path d=\"").append(path).append("\" ");
		if (config.foreColor != null) {
			Color fore = config.foreColor;
			result.append("stroke=\"rgba(").append(fore.getRed()).append(",").append(fore.getGreen()).append(",").append(fore.getBlue()).append(",").append(fore.getAlpha() / 255.0).append(")\"");
		}
		result.append(" /> \n");

		// 如果需要显示文本，在SVG中添加文本
		if (config.isShowText()) {
			String text = config.getCustomText() != null ? config.getCustomText() : "";
			int textY = "bottom".equalsIgnoreCase(config.getTextPosition()) ? height + 15 : 15;
			result.append("<text x=\"").append(width / 2).append("\" y=\"").append(textY).append("\" ");
			result.append("text-anchor=\"middle\" font-family=\"Arial\" font-size=\"12\" ");

			Color textColor = config.getTextColor();
			result.append("fill=\"rgba(").append(textColor.getRed()).append(",").append(textColor.getGreen()).append(",").append(textColor.getBlue()).append(",").append(textColor.getAlpha() / 255.0).append(")\">");
			result.append(text).append("</text>\n");
		}

		result.append("</svg>");
		return result.toString();
	}

	/**
	 * 生成ASCII Art字符画形式的条形码
	 *
	 * @param content 内容
	 * @return ASCII Art字符画形式的条形码
	 */
	public static String generateAsAsciiArt(String content) {
		return generateAsAsciiArt(content, 0, 0, 1);
	}

	/**
	 * 生成ASCII Art字符画形式的条形码
	 *
	 * @param content 内容
	 * @param width   宽度（单位：字符）
	 * @param height  高度（单位：字符）
	 * @param margin  边距大小（1~4）
	 * @return ASCII Art字符画形式的条形码
	 */
	public static String generateAsAsciiArt(String content, int width, int height, int margin) {
		final BarCodeConfig config = new BarCodeConfig(width, height).setMargin(margin);
		return generateAsAsciiArt(content, config);
	}

	/**
	 * 生成ASCII Art字符画形式的条形码
	 *
	 * @param content 内容
	 * @param config  条形码配置，仅宽度、高度、边距有效
	 * @return ASCII Art字符画形式的条形码
	 */
	public static String generateAsAsciiArt(String content, BarCodeConfig config) {
		final BitMatrix bitMatrix = encode(content, config);
		return toAsciiArt(bitMatrix, config);
	}

	/**
	 * BitMatrix转ASCII Art字符画形式的条形码
	 *
	 * @param bitMatrix BitMatrix
	 * @param config    条形码配置
	 * @return ASCII Art字符画形式的条形码
	 */
	public static String toAsciiArt(BitMatrix bitMatrix, BarCodeConfig config) {
		final int width = bitMatrix.getWidth();
		final int height = bitMatrix.getHeight();

		final AnsiElement foreground = config.foreColor == null ? null : rgbToAnsi8BitElement(config.foreColor, ForeOrBack.FORE);
		final AnsiElement background = config.backColor == null ? null : rgbToAnsi8BitElement(config.backColor, ForeOrBack.BACK);

		StringBuilder builder = new StringBuilder();
		for (int y = 0; y < height; y++) {
			StringBuilder rowBuilder = new StringBuilder();
			for (int x = 0; x < width; x++) {
				if (bitMatrix.get(x, y)) {
					rowBuilder.append('█');//'\u2588'
				} else {
					rowBuilder.append(' ');//'\u0020'
				}
			}
			builder.append(AnsiEncoder.encode(foreground, background, rowBuilder)).append('\n');
		}

		// 如果需要显示文本
		if (config.isShowText()) {
			String text = config.getCustomText() != null ? config.getCustomText() : "";
			builder.append("\n").append(text).append("\n");
		}

		return builder.toString();
	}

	/**
	 * rgb转AnsiElement
	 *
	 * @param color      颜色值
	 * @param foreOrBack 前景or背景
	 * @return AnsiElement
	 */
	private static AnsiElement rgbToAnsi8BitElement(Color color, ForeOrBack foreOrBack) {
		return ansiColors.findClosest(color).toAnsiElement(foreOrBack);
	}

	/**
	 * 生成条形码图片并写入文件
	 *
	 * @param content    文本内容
	 * @param width      宽度（单位：像素）
	 * @param height     高度（单位：像素）
	 * @param targetFile 目标文件
	 * @return 目标文件
	 */
	public static File generate(String content, int width, int height, File targetFile) {
		return generate(content, new BarCodeConfig(width, height), targetFile);
	}

	/**
	 * 生成条形码图片并写入文件
	 *
	 * @param content    文本内容
	 * @param config     条形码配置，包括宽度、高度、边距、颜色等
	 * @param targetFile 目标文件
	 * @return 目标文件
	 */
	public static File generate(String content, BarCodeConfig config, File targetFile) {
		String extName = FileUtil.extName(targetFile);
		switch (extName) {
			case BARCODE_TYPE_SVG:
				String svg = generateAsSvg(content, config);
				FileUtil.writeString(svg, targetFile, StandardCharsets.UTF_8);
				break;
			case BARCODE_TYPE_TXT:
				String txt = generateAsAsciiArt(content, config);
				FileUtil.writeString(txt, targetFile, StandardCharsets.UTF_8);
				break;
			default:
				final BufferedImage image = generate(content, config);
				ImgUtil.write(image, targetFile);
				break;
		}
		return targetFile;
	}

	/**
	 * 生成条形码到输出流
	 *
	 * @param content   文本内容
	 * @param width     宽度（单位：像素）
	 * @param height    高度（单位：像素）
	 * @param imageType 图片类型（扩展名），见{@link ImgUtil}、{@link #BARCODE_TYPE_SVG}、{@link #BARCODE_TYPE_TXT}
	 * @param out       目标流
	 */
	public static void generate(String content, int width, int height, String imageType, OutputStream out) {
		generate(content, new BarCodeConfig(width, height), imageType, out);
	}

	/**
	 * 生成条形码到输出流
	 *
	 * @param content   文本内容
	 * @param config    条形码配置，包括宽度、高度、边距、颜色等
	 * @param imageType 图片类型（扩展名），见{@link ImgUtil}、{@link #BARCODE_TYPE_SVG}、{@link #BARCODE_TYPE_TXT}
	 * @param out       目标流
	 */
	public static void generate(String content, BarCodeConfig config, String imageType, OutputStream out) {
		switch (imageType) {
			case BARCODE_TYPE_SVG:
				final String svg = generateAsSvg(content, config);
				IoUtil.writeUtf8(out, false, svg);
				break;
			case BARCODE_TYPE_TXT:
				final String txt = generateAsAsciiArt(content, config);
				IoUtil.writeUtf8(out, false, txt);
				break;
			default:
				final BufferedImage image = generate(content, config);
				ImgUtil.write(image, imageType, out);
				break;
		}
	}

	/**
	 * 生成条形码图片
	 *
	 * @param content 文本内容
	 * @param width   宽度（单位：像素）
	 * @param height  高度（单位：像素）
	 * @return 条形码图片
	 */
	public static BufferedImage generate(String content, int width, int height) {
		return generate(content, new BarCodeConfig(width, height));
	}

	/**
	 * 生成条形码图片
	 *
	 * @param content 文本内容
	 * @param format  条形码格式，见{@link BarcodeFormat}
	 * @param width   宽度（单位：像素）
	 * @param height  高度（单位：像素）
	 * @return 条形码图片
	 */
	public static BufferedImage generate(String content, BarcodeFormat format, int width, int height) {
		final BarCodeConfig config = new BarCodeConfig(width, height);
		config.setBarcodeFormat(format);
		return generate(content, config);
	}

	/**
	 * 生成条形码图片
	 *
	 * @param content 文本内容
	 * @param config  条形码配置，包括宽度、高度、边距、颜色等
	 * @return 条形码图片
	 */
	public static BufferedImage generate(String content, BarCodeConfig config) {
		final BitMatrix bitMatrix = encode(content, config);

		// 获取条形码图片
		BufferedImage barcode = toImage(bitMatrix, config.foreColor, config.backColor);

		// 如果不保留静区，则裁剪条形码两端的空白区域
		if (!config.isKeepQuietZone()) {
			barcode = removeQuietZone(barcode, config);
		}

		// 如果不需要显示文本，则直接返回条形码图片
		if (!config.isShowText()) {
			return barcode;
		}

		// 创建带文本的图片
		return addTextToBarcode(barcode, content, config);
	}

	/**
	 * 生成PNG格式的条形码图片，以byte[]形式表示
	 *
	 * @param content 内容
	 * @param width   宽度（单位：像素）
	 * @param height  高度（单位：像素）
	 * @return 图片的byte[]
	 */
	public static byte[] generatePng(String content, int width, int height) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		generate(content, width, height, ImgUtil.IMAGE_TYPE_PNG, out);
		return out.toByteArray();
	}

	/**
	 * 生成PNG格式的条形码图片，以byte[]形式表示
	 *
	 * @param content 内容
	 * @param config  条形码配置，包括宽度、高度、边距、颜色等
	 * @return 图片的byte[]
	 */
	public static byte[] generatePng(String content, BarCodeConfig config) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		generate(content, config, ImgUtil.IMAGE_TYPE_PNG, out);
		return out.toByteArray();
	}

	/**
	 * 将文本内容编码为条形码
	 *
	 * @param content 文本内容
	 * @param width   宽度（单位：像素）
	 * @param height  高度（单位：像素）
	 * @return {@link BitMatrix}
	 */
	public static BitMatrix encode(String content, int width, int height) {
		return encode(content, new BarCodeConfig(width, height));
	}

	/**
	 * 将文本内容编码为条形码
	 *
	 * @param content 文本内容
	 * @param config  条形码配置，包括宽度、高度、边距、颜色等
	 * @return {@link BitMatrix}
	 */
	public static BitMatrix encode(String content, BarCodeConfig config) {
		final MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		BitMatrix bitMatrix;
		try {
			bitMatrix = multiFormatWriter.encode(content, config.getBarcodeFormat(), config.getWidth(), config.getHeight(), config.toHints());
		} catch (WriterException e) {
			throw new BarCodeException(e);
		}

		return bitMatrix;
	}

	/**
	 * BitMatrix转BufferedImage
	 *
	 * @param matrix    BitMatrix
	 * @param foreColor 前景色
	 * @param backColor 背景色(null表示透明背景)
	 * @return BufferedImage
	 */
	public static BufferedImage toImage(BitMatrix matrix, Color foreColor, Color backColor) {
		final int width = matrix.getWidth();
		final int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, null == backColor ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (matrix.get(x, y)) {
					image.setRGB(x, y, foreColor.getRGB());
				} else if (null != backColor) {
					image.setRGB(x, y, backColor.getRGB());
				}
			}
		}
		return image;
	}

	/**
	 * 在条形码图片下方添加文本
	 *
	 * @param barcode 条形码图片
	 * @param content 文本内容
	 * @param config  条形码配置
	 * @return 带文本的条形码图片
	 */
	private static BufferedImage addTextToBarcode(BufferedImage barcode, String content, BarCodeConfig config) {
		String text = config.getCustomText() != null ? config.getCustomText() : content;

		// 创建字体对象
		Font font = config.getTextFont();
		FontMetrics metrics = barcode.getGraphics().getFontMetrics(font);
		int textWidth = metrics.stringWidth(text);
		int textHeight = metrics.getHeight();

		// 计算文本区域的高度
		int textAreaHeight = textHeight + config.getTextMargin() * 2;

		// 创建新的图片，高度增加文本区域的高度
		int newWidth = Math.max(barcode.getWidth(), textWidth + config.getTextMargin() * 2);
		int newHeight = barcode.getHeight() + textAreaHeight;
		// 当背景色为空时使用支持透明的图片类型
		BufferedImage combinedImage = new BufferedImage(newWidth, newHeight,
			config.getBackColor() == null ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = combinedImage.createGraphics();

		// 抗锯齿设置
		enableTextAntialiasing(g2d);

		// 如果有背景色，则填充背景
		if (config.getBackColor() != null) {
			g2d.setColor(config.getBackColor());
			g2d.fillRect(0, 0, newWidth, newHeight);
		}

		// 根据文本位置设置
		if ("bottom".equalsIgnoreCase(config.getTextPosition())) {
			// 底部显示文本
			g2d.drawImage(barcode, (newWidth - barcode.getWidth()) / 2, 0, null);
			g2d.setColor(config.getTextColor());
			g2d.setFont(font);
			g2d.drawString(text, (newWidth - textWidth) / 2, barcode.getHeight() + config.getTextMargin() + metrics.getAscent());
		} else {
			// 顶部显示文本
			g2d.drawImage(barcode, (newWidth - barcode.getWidth()) / 2, textAreaHeight, null);
			g2d.setColor(config.getTextColor());
			g2d.setFont(font);
			g2d.drawString(text, (newWidth - textWidth) / 2, config.getTextMargin() + metrics.getAscent());
		}

		g2d.dispose();
		return combinedImage;
	}

	/**
	 * 启用文本抗锯齿
	 *
	 * @param g2d Graphics2D对象
	 */
	private static void enableTextAntialiasing(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	}

	/**
	 * 移除条形码两端的静区（空白区域），并保持原始宽度
	 *
	 * @param barcode 原始条形码图片
	 * @param config  条形码配置
	 * @return 处理后的条形码图片
	 */
	private static BufferedImage removeQuietZone(BufferedImage barcode, BarCodeConfig config) {
		int width = barcode.getWidth();
		int height = barcode.getHeight();
		Color backgroundColor = config.getBackColor();

		// 判断非条形码部分的像素
		// 背景色为null时，透明像素(alpha为0)被视为背景
		// 背景色不为null时，与背景色相同的像素被视为背景
		int[] background = new int[4];
		if (backgroundColor != null) {
			background[0] = backgroundColor.getRGB();
		}

		// 找到左侧第一个非背景色像素的x坐标
		int leftBound = 0;
		for (int x = 0; x < width; x++) {
			boolean foundNonBackground = false;
			for (int y = 0; y < height; y++) {
				int rgb = barcode.getRGB(x, y);
				if (backgroundColor == null) {
					// 如果背景色为null，透明度为0的像素视为背景
					if ((rgb >>> 24) != 0) {
						leftBound = x;
						foundNonBackground = true;
						break;
					}
				} else if (rgb != background[0]) {
					leftBound = x;
					foundNonBackground = true;
					break;
				}
			}
			if (foundNonBackground) break;
		}

		// 找到右侧最后一个非背景色像素的x坐标
		int rightBound = width - 1;
		for (int x = width - 1; x >= 0; x--) {
			boolean foundNonBackground = false;
			for (int y = 0; y < height; y++) {
				int rgb = barcode.getRGB(x, y);
				if (backgroundColor == null) {
					// 如果背景色为null，透明度为0的像素视为背景
					if ((rgb >>> 24) != 0) {
						rightBound = x;
						foundNonBackground = true;
						break;
					}
				} else if (rgb != background[0]) {
					rightBound = x;
					foundNonBackground = true;
					break;
				}
			}
			if (foundNonBackground) break;
		}

		// 找到顶部第一个非背景色像素的y坐标
		int topBound = 0;
		for (int y = 0; y < height; y++) {
			boolean foundNonBackground = false;
			for (int x = 0; x < width; x++) {
				int rgb = barcode.getRGB(x, y);
				if (backgroundColor == null) {
					// 如果背景色为null，透明度为0的像素视为背景
					if ((rgb >>> 24) != 0) {
						topBound = y;
						foundNonBackground = true;
						break;
					}
				} else if (rgb != background[0]) {
					topBound = y;
					foundNonBackground = true;
					break;
				}
			}
			if (foundNonBackground) break;
		}

		// 找到底部最后一个非背景色像素的y坐标
		int bottomBound = height - 1;
		for (int y = height - 1; y >= 0; y--) {
			boolean foundNonBackground = false;
			for (int x = 0; x < width; x++) {
				int rgb = barcode.getRGB(x, y);
				if (backgroundColor == null) {
					// 如果背景色为null，透明度为0的像素视为背景
					if ((rgb >>> 24) != 0) {
						bottomBound = y;
						foundNonBackground = true;
						break;
					}
				} else if (rgb != background[0]) {
					bottomBound = y;
					foundNonBackground = true;
					break;
				}
			}
			if (foundNonBackground) break;
		}

		// 计算实际条形码的宽度和高度（不包括静区）
		int actualWidth = rightBound - leftBound + 1;
		int actualHeight = bottomBound - topBound + 1;

		// 创建新图片，保持原始宽度，仅裁剪上下多余部分
		BufferedImage result = new BufferedImage(width, actualHeight,
			backgroundColor == null ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = result.createGraphics();

		// 填充背景色（如果有背景色）
		if (backgroundColor != null) {
			g2d.setColor(backgroundColor);
			g2d.fillRect(0, 0, width, actualHeight);
		}

		// 将条形码绘制到整个宽度（拉伸）
		g2d.drawImage(
			barcode.getSubimage(leftBound, topBound, actualWidth, actualHeight),
			0, 0, width, actualHeight, null
		);
		g2d.dispose();

		return result;
	}
}
