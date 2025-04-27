package cn.hutool.extra.barcode;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 条形码配置类
 *
 * @author Libai
 * @since 5.8.38
 */
public class BarCodeConfig {

	/**
	 * 条形码宽度，单位：像素
	 */
	protected int width;

	/**
	 * 条形码高度，单位：像素
	 */
	protected int height;

	/**
	 * 图片格式，默认为png
	 */
	protected String format = ImgUtil.IMAGE_TYPE_PNG;

	/**
	 * 字符编码，默认为UTF-8
	 */
	protected String charset = "UTF-8";

	/**
	 * 条形码边距，默认为0
	 */
	protected int margin = 0;

	/**
	 * 条形码类型，默认为CODE_128
	 */
	protected BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

	/**
	 * 条形码前景色（条形码颜色），默认为黑色
	 */
	protected Color foreColor = Color.BLACK;

	/**
	 * 条形码背景色，默认为白色
	 */
	protected Color backColor = Color.WHITE;

	/**
	 * 是否显示条形码原文文本，默认为false
	 */
	protected boolean showText = false;

	/**
	 * 自定义文本，默认为null（使用条形码原文）
	 */
	protected String customText = null;

	/**
	 * 文本字体，默认为Arial，12号字体
	 */
	protected Font textFont = new Font("宋体", Font.PLAIN, 12);

	/**
	 * 文本颜色，默认为黑色
	 */
	protected Color textColor = Color.BLACK;

	/**
	 * 文本位置，默认为底部，值为：bottom（底部）、top（顶部）
	 */
	protected String textPosition = "bottom";

	/**
	 * 文本边距，单位像素，默认为5
	 */
	protected int textMargin = 5;

	/**
	 * 是否保留静区（条形码两端的空白区域），默认为true
	 * 注意：不保留静区可能影响某些扫描设备的识别率
	 */
	protected boolean keepQuietZone = true;

	/**
	 * 构造，默认宽度300、高度100
	 */
	public BarCodeConfig() {
		this(300, 100);
	}

	/**
	 * 构造
	 *
	 * @param width  宽度
	 * @param height 高度
	 */
	public BarCodeConfig(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * 构造
	 *
	 * @param width    宽度
	 * @param height   高度
	 * @param showText 是否显示文本
	 */
	public BarCodeConfig(int width, int height, boolean showText) {
		this.width = width;
		this.height = height;
		this.showText = showText;
	}

	/**
	 * 创建BarcodeConfig
	 *
	 * @return BarcodeConfig
	 */
	public static BarCodeConfig create() {
		return new BarCodeConfig();
	}

	/**
	 * 创建带显示文本的BarcodeConfig
	 *
	 * @return 带显示文本的BarcodeConfig
	 */
	public static BarCodeConfig createWithText() {
		final BarCodeConfig config = new BarCodeConfig();
		config.setShowText(true);
		return config;
	}

	/**
	 * 创建带自定义文本的BarcodeConfig
	 *
	 * @param customText 自定义文本
	 * @return 带自定义文本的BarcodeConfig
	 */
	public static BarCodeConfig createWithCustomText(String customText) {
		final BarCodeConfig config = new BarCodeConfig();
		config.setShowText(true);
		config.setCustomText(customText);
		return config;
	}

	/**
	 * 获取条形码编码提示
	 *
	 * @return 条形码编码提示
	 */
	public Map<EncodeHintType, Object> toHints() {
		final Map<EncodeHintType, Object> hints = new HashMap<>(8);
		hints.put(EncodeHintType.CHARACTER_SET, charset);
		hints.put(EncodeHintType.MARGIN, margin);
		return hints;
	}

	/**
	 * 获取宽度
	 *
	 * @return 宽度
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 设置宽度
	 *
	 * @param width 宽度
	 * @return this
	 */
	public BarCodeConfig setWidth(int width) {
		this.width = width;
		return this;
	}

	/**
	 * 获取高度
	 *
	 * @return 高度
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 设置高度
	 *
	 * @param height 高度
	 * @return this
	 */
	public BarCodeConfig setHeight(int height) {
		this.height = height;
		return this;
	}

	/**
	 * 获取图片格式
	 *
	 * @return 图片格式
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 设置图片格式
	 *
	 * @param format 图片格式
	 * @return this
	 */
	public BarCodeConfig setFormat(String format) {
		this.format = format;
		return this;
	}

	/**
	 * 获取字符编码
	 *
	 * @return 字符编码
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * 设置字符编码
	 *
	 * @param charset 字符编码
	 * @return this
	 */
	public BarCodeConfig setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 获取边距
	 *
	 * @return 边距
	 */
	public int getMargin() {
		return margin;
	}

	/**
	 * 设置边距
	 *
	 * @param margin 边距
	 * @return this
	 */
	public BarCodeConfig setMargin(int margin) {
		this.margin = margin;
		return this;
	}

	/**
	 * 获取条形码格式
	 *
	 * @return 条形码格式
	 */
	public BarcodeFormat getBarcodeFormat() {
		return barcodeFormat;
	}

	/**
	 * 设置条形码格式
	 *
	 * @param barcodeFormat 条形码格式
	 * @return this
	 */
	public BarCodeConfig setBarcodeFormat(BarcodeFormat barcodeFormat) {
		this.barcodeFormat = barcodeFormat;
		return this;
	}

	/**
	 * 获取前景色
	 *
	 * @return 前景色
	 */
	public Color getForeColor() {
		return foreColor;
	}

	/**
	 * 设置前景色
	 *
	 * @param foreColor 前景色
	 * @return this
	 */
	public BarCodeConfig setForeColor(Color foreColor) {
		this.foreColor = foreColor;
		return this;
	}

	/**
	 * 获取背景色
	 *
	 * @return 背景色
	 */
	public Color getBackColor() {
		return backColor;
	}

	/**
	 * 设置背景色
	 *
	 * @param backColor 背景色
	 * @return this
	 */
	public BarCodeConfig setBackColor(Color backColor) {
		this.backColor = backColor;
		return this;
	}

	/**
	 * 是否显示文本
	 *
	 * @return 是否显示文本
	 */
	public boolean isShowText() {
		return showText;
	}

	/**
	 * 设置是否显示文本
	 *
	 * @param showText 是否显示文本
	 * @return this
	 */
	public BarCodeConfig setShowText(boolean showText) {
		this.showText = showText;
		return this;
	}

	/**
	 * 获取自定义文本
	 *
	 * @return 自定义文本
	 */
	public String getCustomText() {
		return customText;
	}

	/**
	 * 设置自定义文本
	 *
	 * @param customText 自定义文本
	 * @return this
	 */
	public BarCodeConfig setCustomText(String customText) {
		this.customText = customText;
		return this;
	}

	/**
	 * 获取文本字体
	 *
	 * @return 文本字体
	 */
	public Font getTextFont() {
		return textFont;
	}

	/**
	 * 设置文本字体
	 *
	 * @param textFont 文本字体
	 * @return this
	 */
	public BarCodeConfig setTextFont(Font textFont) {
		this.textFont = ObjectUtil.defaultIfNull(textFont, this.textFont);
		return this;
	}

	/**
	 * 获取文本颜色
	 *
	 * @return 文本颜色
	 */
	public Color getTextColor() {
		return textColor;
	}

	/**
	 * 设置文本颜色
	 *
	 * @param textColor 文本颜色
	 * @return this
	 */
	public BarCodeConfig setTextColor(Color textColor) {
		this.textColor = ObjectUtil.defaultIfNull(textColor, this.textColor);
		return this;
	}

	/**
	 * 获取文本位置
	 *
	 * @return 文本位置
	 */
	public String getTextPosition() {
		return textPosition;
	}

	/**
	 * 设置文本位置
	 *
	 * @param textPosition 文本位置，可选值：bottom、top
	 * @return this
	 */
	public BarCodeConfig setTextPosition(String textPosition) {
		this.textPosition = textPosition;
		return this;
	}

	/**
	 * 获取文本边距
	 *
	 * @return 文本边距
	 */
	public int getTextMargin() {
		return textMargin;
	}

	/**
	 * 设置文本边距
	 *
	 * @param textMargin 文本边距
	 * @return this
	 */
	public BarCodeConfig setTextMargin(int textMargin) {
		this.textMargin = textMargin;
		return this;
	}

	/**
	 * The quiet zone is the area surrounding the barcode (typically white)
	 *
	 * @return 是否保留静区
	 */
	public boolean isKeepQuietZone() {
		return keepQuietZone;
	}

	/**
	 * 设置是否保留静区
	 *
	 * @param keepQuietZone 是否保留静区
	 * @return this
	 */
	public BarCodeConfig setKeepQuietZone(boolean keepQuietZone) {
		this.keepQuietZone = keepQuietZone;
		return this;
	}
}
