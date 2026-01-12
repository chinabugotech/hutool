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
import cn.hutool.v7.core.lang.ansi.AnsiElement;
import cn.hutool.v7.core.lang.ansi.AnsiEncoder;
import cn.hutool.v7.swing.img.color.ColorUtil;
import cn.hutool.v7.swing.qrcode.QrConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * ASCII Art渲染
 *
 * @author Looly
 * @since 7.0.0
 */
public class AsciiArtRender implements BitMatrixRender {

	private final QrConfig config;

	/**
	 * 构造
	 *
	 * @param config 二维码配置
	 */
	public AsciiArtRender(final QrConfig config) {
		this.config = config;
	}

	@Override
	public void render(final BitMatrix matrix, final OutputStream out) {
		render(matrix, new OutputStreamWriter(out, config.getCharset()));
	}

	/**
	 * 渲染SVG
	 *
	 * @param matrix 二维码
	 * @param writer 输出
	 */
	public void render(final BitMatrix matrix, final Appendable writer) {
		final int width = matrix.getWidth();
		final int height = matrix.getHeight();

		final Integer foreColor = config.getForeColor();
		final AnsiElement foreground = foreColor == null ? null : ColorUtil.toAnsiColor(foreColor, true, false);
		final Integer backColor = config.getBackColor();
		final AnsiElement background = backColor == null ? null : ColorUtil.toAnsiColor(backColor, true, true);

		try {
			for (int i = 0; i <= height; i += 2) {
				final StringBuilder rowBuilder = new StringBuilder();
				for (int j = 0; j < width; j++) {
					final boolean tp = matrix.get(i, j);
					final boolean bt = i + 1 >= height || matrix.get(i + 1, j);
					if (tp && bt) {
						rowBuilder.append(' ');//'\u0020'
					} else if (tp) {
						rowBuilder.append('▄');//'\u2584'
					} else if (bt) {
						rowBuilder.append('▀');//'\u2580'
					} else {
						rowBuilder.append('█');//'\u2588'
					}
				}
				writer.append(AnsiEncoder.encode(foreground, background, rowBuilder)).append('\n');
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
