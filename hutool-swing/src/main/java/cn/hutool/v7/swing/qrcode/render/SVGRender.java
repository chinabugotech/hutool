/*
 * Copyright (c) 2013-2026 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.swing.qrcode.render;

import com.google.zxing.common.BitMatrix;
import cn.hutool.v7.core.io.IORuntimeException;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.swing.img.ImgUtil;
import cn.hutool.v7.swing.img.color.ColorUtil;
import cn.hutool.v7.swing.qrcode.QrConfig;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * SVG渲染器
 *
 * @author Looly
 * @since 6.0.0
 */
public class SVGRender implements BitMatrixRender {

	private final QrConfig qrConfig;

	/**
	 * 构造
	 *
	 * @param qrConfig 二维码配置
	 */
	public SVGRender(final QrConfig qrConfig) {
		this.qrConfig = qrConfig;
	}

	@Override
	public void render(final BitMatrix matrix, final OutputStream out) {
		render(matrix, new OutputStreamWriter(out, qrConfig.getCharset()));
	}

	/**
	 * 渲染SVG
	 *
	 * @param matrix 二维码
	 * @param writer 输出
	 */
	public void render(final BitMatrix matrix, final Appendable writer) {
		final Image logoImg = qrConfig.getImg();
		final Integer foreColor = qrConfig.getForeColor();
		final Integer backColor = qrConfig.getBackColor();
		final int ratio = qrConfig.getRatio();

		final int qrWidth = matrix.getWidth();
		int qrHeight = matrix.getHeight();
		final int moduleHeight = (qrHeight == 1) ? qrWidth / 2 : 1;

		qrHeight *= moduleHeight;
		String logoBase64 = "";
		int logoWidth = 0;
		int logoHeight = 0;
		int logoX = 0;
		int logoY = 0;
		if (logoImg != null) {
			logoBase64 = ImgUtil.toBase64DataUri(logoImg, "png");
			// 按照最短的边做比例缩放
			if (qrWidth < qrHeight) {
				logoWidth = qrWidth / ratio;
				logoHeight = logoImg.getHeight(null) * logoWidth / logoImg.getWidth(null);
			} else {
				logoHeight = qrHeight / ratio;
				logoWidth = logoImg.getWidth(null) * logoHeight / logoImg.getHeight(null);
			}
			logoX = (qrWidth - logoWidth) / 2;
			logoY = (qrHeight - logoHeight) / 2;

		}

		try {
			writer.append("<svg width=\"").append(String.valueOf(qrWidth)).append("\" height=\"").append(String.valueOf(qrHeight)).append("\" \n");
			if (backColor != null) {
				final Color back = new Color(backColor, true);
				writer.append("style=\"background-color:").append(ColorUtil.toCssRgba(back)).append("\"\n");
			}
			writer.append("viewBox=\"0 0 ").append(String.valueOf(qrWidth)).append(" ").append(String.valueOf(qrHeight)).append("\" \n");
			writer.append("xmlns=\"http://www.w3.org/2000/svg\" \n");
			writer.append("xmlns:xlink=\"http://www.w3.org/1999/xlink\" >\n");
			writer.append("<path d=\"");

			// 数据
			for (int y = 0; y < qrHeight; y++) {
				for (int x = 0; x < qrWidth; x++) {
					if (matrix.get(x, y)) {
						writer.append(" M").append(String.valueOf(x)).append(",").append(String.valueOf(y)).append("h1v").append(String.valueOf(moduleHeight)).append("h-1z");
					}
				}
			}

			writer.append("\" ");
			if (foreColor != null) {
				final Color fore = new Color(foreColor, true);
				writer.append("stroke=\"").append(ColorUtil.toCssRgba(fore)).append("\"");
			}
			writer.append(" /> \n");
			if (StrUtil.isNotBlank(logoBase64)) {
				writer.append("<image xlink:href=\"").append(logoBase64)
					.append("\" height=\"").append(String.valueOf(logoHeight)).append("\" width=\"").append(String.valueOf(logoWidth))
					.append("\" y=\"").append(String.valueOf(logoY)).append("\" x=\"").append(String.valueOf(logoX)).append("\" />\n");
			}
			writer.append("</svg>");
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
