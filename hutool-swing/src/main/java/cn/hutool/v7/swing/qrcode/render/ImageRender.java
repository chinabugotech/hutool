/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import cn.hutool.v7.swing.img.Img;
import cn.hutool.v7.swing.img.ImgUtil;
import cn.hutool.v7.swing.qrcode.QrConfig;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * 二维码图片渲染器
 *
 * @author Looly
 * @since 6.0.0
 */
public class ImageRender implements BitMatrixRender {

	private final QrConfig config;
	private final String imageType;

	/**
	 * 构造
	 *
	 * @param config    二维码配置
	 * @param imageType 图片类型
	 */
	public ImageRender(final QrConfig config, final String imageType) {
		this.config = config;
		this.imageType = imageType;
	}

	@Override
	public void render(final BitMatrix matrix, final OutputStream out) {
		BufferedImage img = null;
		try {
			img = render(matrix);
			ImgUtil.write(img, imageType, out);
		} finally {
			ImgUtil.flush(img);
		}
	}

	/**
	 * 渲染
	 *
	 * @param matrix 二维码矩阵
	 * @return 图片
	 */
	public BufferedImage render(final BitMatrix matrix) {
		final BufferedImage image = getBufferedImage(matrix);

		final Image logo = config.getImg();
		if (null != logo && BarcodeFormat.QR_CODE == config.getFormat()) {
			pressLogo(image, logo);
		}
		return image;
	}

	/**
	 * 获取图片
	 *
	 * @param matrix 二维码矩阵
	 * @return 图片
	 */
	private BufferedImage getBufferedImage(final BitMatrix matrix) {
		final BufferedImage image = new BufferedImage(
			matrix.getWidth(),
			matrix.getHeight(),
			null == config.getBackColor() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);

		final int width = matrix.getWidth();
		final int height = matrix.getHeight();
		final Integer foreColor = config.getForeColor();
		final Integer backColor = config.getBackColor();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (matrix.get(x, y)) {
					image.setRGB(x, y, foreColor);
				} else if (null != backColor) {
					image.setRGB(x, y, backColor);
				}
			}
		}
		return image;
	}

	/**
	 * 贴图
	 *
	 * @param image   二维码图片
	 * @param logoImg logo图片
	 */
	private void pressLogo(final BufferedImage image, final Image logoImg) {
		// 只有二维码可以贴图
		final int qrWidth = image.getWidth();
		final int qrHeight = image.getHeight();
		final int imgWidth;
		final int imgHeight;
		// 按照最短的边做比例缩放
		if (qrWidth < qrHeight) {
			imgWidth = qrWidth / config.getRatio();
			imgHeight = logoImg.getHeight(null) * imgWidth / logoImg.getWidth(null);
		} else {
			imgHeight = qrHeight / config.getRatio();
			imgWidth = logoImg.getWidth(null) * imgHeight / logoImg.getHeight(null);
		}

		// 原图片上直接绘制水印
		Img.from(image).pressImage(//
			Img.from(logoImg).round(config.getImgRound()).getImg(), // 圆角
			new Rectangle(imgWidth, imgHeight), // 位置
			1//不透明
		);
	}
}
