/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.swing.qrcode;

import cn.hutool.v7.core.codec.Encoder;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.ObjUtil;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码（条形码等）编码器，用于将文本内容转换为二维码
 *
 * @author Looly
 * @since 6.0.0
 */
public class QrEncoder implements Encoder<CharSequence, BitMatrix> {

	/**
	 * 创建QrEncoder
	 *
	 * @param config {@link QrConfig}
	 * @return QrEncoder
	 */
	public static QrEncoder of(final QrConfig config) {
		return new QrEncoder(config);
	}

	private final QrConfig config;

	/**
	 * 构造
	 *
	 * @param config {@link QrConfig}
	 */
	public QrEncoder(final QrConfig config) {
		this.config = ObjUtil.defaultIfNull(config, QrConfig::of);
	}

	@Override
	public BitMatrix encode(final CharSequence content) {
		final MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

		final BitMatrix bitMatrix;
		try {
			bitMatrix = multiFormatWriter.encode(
					StrUtil.toString(content),
					config.format, config.width, config.height,
					config.toHints());
		} catch (final WriterException e) {
			throw new QrCodeException(e);
		}

		return bitMatrix;
	}
}
