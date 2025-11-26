package cn.hutool.v7.poi.word;

import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.awt.Color;
import java.awt.Font;

/**
 * 字体样式
 *
 * @param font  字体信息
 * @param color 字体颜色
 * @author looly
 * @since 7.0.0
 */
public record FontStyle(Font font, Color color) {

	/**
	 * 构造
	 *
	 * @param name  字体名称
	 * @param style 字体样式，见{@link Font#PLAIN}, {@link Font#BOLD}, {@link Font#ITALIC}
	 * @param size  字体大小
	 */
	@SuppressWarnings("MagicConstant")
	public FontStyle(final String name, final int style, final int size) {
		this(new Font(name, style, size), null);
	}

	/**
	 * 构造
	 *
	 * @param name  字体名称
	 * @param style 字体样式，见{@link Font#PLAIN}, {@link Font#BOLD}, {@link Font#ITALIC}
	 * @param size  字体大小
	 * @param color 字体颜色
	 */
	@SuppressWarnings("MagicConstant")
	public FontStyle(final String name, final int style, final int size, final Color color) {
		this(new Font(name, style, size), color);
	}

	/**
	 * 填充字体样式到段落
	 *
	 * @param run 段落对象
	 */
	public void fill(final XWPFRun run) {
		run.setFontFamily(font.getFamily());
		run.setFontSize(font.getSize());
		run.setBold(font.isBold());
		run.setItalic(font.isItalic());
		if (null != color) {
			run.setColor(String.format("%06X", color.getRGB() & 0xFFFFFF));
		}
	}
}
