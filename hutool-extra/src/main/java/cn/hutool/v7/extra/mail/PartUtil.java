/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.extra.mail;

import cn.hutool.v7.core.text.StrUtil;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeUtility;

import java.nio.charset.Charset;

/**
 * {@link jakarta.mail.Part}相关工具
 *
 * @author looly
 * @since 7.0.0
 */
public class PartUtil {

	/**
	 * 构建邮件信息主体
	 *
	 * @param content 内容, {@code null}则使用{@link StrUtil#EMPTY}替换
	 * @param charset 编码，{@code null}则使用{@link MimeUtility#getDefaultJavaCharset()}
	 * @param isHtml  是否为HTML
	 * @return 邮件信息主体
	 * @throws MailException 邮件异常
	 */
	public static MimeBodyPart buildContent(final String content, final Charset charset, final boolean isHtml) throws MailException {
		final String charsetStr = null != charset ? charset.name() : MimeUtility.getDefaultJavaCharset();
		// 正文
		final MimeBodyPart body = new MimeBodyPart();
		// 内容如果是null会抛异常, 使用空字符串代替
		try {
			body.setContent(StrUtil.emptyIfNull(content), StrUtil.format("text/{}; charset={}", isHtml ? "html" : "plain", charsetStr));
		} catch (final MessagingException e) {
			throw new MailException(e);
		}
		return body;
	}

	/**
	 * 构建邮件附件主体，自动识别是否为图片附件，如果是图片附件，可作为邮件正文的一部分直接显示
	 *
	 * @param attachment       附件
	 * @param charset          编码
	 * @param isEncodeFilename 对于文件名是否使用指定编码编码
	 * @return 邮件信息主体
	 */
	public static MimeBodyPart buildAttachment(final DataSource attachment, final Charset charset, final boolean isEncodeFilename) {
		final MimeBodyPart bodyPart = new MimeBodyPart();

		try {
			bodyPart.setDataHandler(new DataHandler(attachment));

			String nameEncoded = attachment.getName();
			if (isEncodeFilename) {
				nameEncoded = InternalMailUtil.encodeText(nameEncoded, charset);
			}
			// 普通附件文件名
			bodyPart.setFileName(nameEncoded);
			if (StrUtil.startWith(attachment.getContentType(), "image/")) {
				// 图片附件，用于正文中引用图片
				bodyPart.setContentID(nameEncoded);
				// 内联：作为邮件正文的一部分直接显示
				bodyPart.setDisposition(MimeBodyPart.INLINE);
			}
		} catch (final MessagingException e) {
			throw new MailException(e);
		}
		return bodyPart;
	}
}
