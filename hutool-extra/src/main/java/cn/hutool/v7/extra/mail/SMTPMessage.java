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

package cn.hutool.v7.extra.mail;

import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.io.IORuntimeException;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.ObjUtil;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.activation.FileTypeMap;
import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.util.ByteArrayDataSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

/**
 * SMTP消息封装
 *
 * @author looly
 * @since 7.0.0
 */
public class SMTPMessage extends MimeMessage {

	/**
	 * 创建SMTP消息
	 *
	 * @param mailAccount      邮件账户
	 * @param useGlobalSession 是否使用全局Session
	 * @param debugOutput      输出调试信息
	 * @return this
	 */
	public static SMTPMessage of(final MailAccount mailAccount, final boolean useGlobalSession, final PrintStream debugOutput) {
		final Session session = MailUtil.getSession(mailAccount, useGlobalSession);
		if (null != debugOutput) {
			session.setDebugOut(debugOutput);
		}
		return new SMTPMessage(mailAccount, session);
	}

	/**
	 * 邮件账户
	 */
	private final MailAccount mailAccount;
	/**
	 * 正文、附件和图片的混合部分
	 */
	private final Multipart multipart;

	/**
	 * 构造
	 *
	 * @param mailAccount 邮件账户
	 * @param session     Session
	 */
	public SMTPMessage(final MailAccount mailAccount, final Session session) {
		super(session);
		this.mailAccount = mailAccount;
		multipart = new MimeMultipart();
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 发件人
		final String from = this.mailAccount.getFrom();
		try {
			if (StrUtil.isEmpty(from)) {
				// 用户未提供发送方，则从Session中自动获取
				super.setFrom();
			} else {
				super.setFrom(InternalMailUtil.parseFirstAddress(from, this.mailAccount.getCharset()));
			}
			// 默认发送时间
			super.setSentDate(DateUtil.now());
		} catch (final MessagingException e) {
			throw new MailException(e);
		}

	}

	/**
	 * 设置标题
	 *
	 * @param title 标题
	 * @return this
	 */
	public SMTPMessage setTitle(final String title) {
		try {
			super.setSubject(title, ObjUtil.apply(mailAccount.getCharset(), Object::toString));
		} catch (final MessagingException e) {
			throw new MailException(e);
		}
		return this;
	}

	// region ----- setRecipients

	/**
	 * 设置收件人
	 *
	 * @param tos 收件人列表
	 * @return this
	 */
	public SMTPMessage setTos(final String... tos) {
		return this.setRecipients(Message.RecipientType.TO, tos);
	}

	/**
	 * 设置抄送
	 *
	 * @param ccs 抄送列表
	 * @return this
	 */
	public SMTPMessage setCcs(final String... ccs) {
		return this.setRecipients(Message.RecipientType.CC, ccs);
	}

	/**
	 * 设置密送
	 *
	 * @param bccs 密送列表
	 * @return this
	 */
	public SMTPMessage setBccs(final String... bccs) {
		return this.setRecipients(Message.RecipientType.BCC, bccs);
	}


	/**
	 * 设置收件人，支持收件人、密送、抄送
	 *
	 * @param type      收件人类型
	 * @param addresses 收件人列表
	 * @return this
	 */
	public SMTPMessage setRecipients(final Message.RecipientType type, final String... addresses) {
		try {
			super.setRecipients(type, InternalMailUtil.parseAddressFromStrs(addresses, this.mailAccount.getCharset()));
		} catch (final MessagingException e) {
			throw new MailException(e);
		}
		return this;
	}
	// endregion

	/**
	 * 设置回复地址
	 *
	 * @param reply 回复地址列表
	 * @return this
	 */
	public SMTPMessage setReply(final String... reply) {
		try {
			super.setReplyTo(InternalMailUtil.parseAddressFromStrs(reply, this.mailAccount.getCharset()));
		} catch (final MessagingException e) {
			throw new MailException(e);
		}
		return this;
	}

	/**
	 * 设置邮件内容
	 *
	 * @param content 内容
	 * @param isHtml  是否为HTML
	 * @return this
	 */
	public SMTPMessage setContent(final String content, final boolean isHtml) {
		try {
			super.setContent(buildContent(content, this.mailAccount.getCharset(), isHtml));
		} catch (final MessagingException e) {
			throw new MailException(e);
		}
		return this;
	}

	// region ----- addAttachments

	/**
	 * 增加图片，图片的键对应到邮件模板中的占位字符串，图片类型默认为"image/jpeg"
	 *
	 * @param cid         图片与占位符，占位符格式为cid:${cid}
	 * @param imageStream 图片文件
	 * @return this
	 */
	public SMTPMessage addImage(final String cid, final InputStream imageStream) {
		return addImage(cid, imageStream, null);
	}

	/**
	 * 增加图片，图片的键对应到邮件模板中的占位字符串
	 *
	 * @param cid       图片与占位符，占位符格式为cid:${cid}
	 * @param imageFile 图片文件
	 * @return this
	 */
	public SMTPMessage addImage(final String cid, final File imageFile) {
		InputStream in = null;
		try {
			in = FileUtil.getInputStream(imageFile);
			return addImage(cid, in, FileTypeMap.getDefaultFileTypeMap().getContentType(imageFile));
		} finally {
			IoUtil.closeQuietly(in);
		}
	}

	/**
	 * 增加图片，图片的键对应到邮件模板中的占位字符串
	 *
	 * @param cid         图片与占位符，占位符格式为cid:${cid}
	 * @param imageStream 图片流，不关闭
	 * @param contentType 图片类型，null赋值默认的"image/jpeg"
	 * @return this
	 */
	public SMTPMessage addImage(final String cid, final InputStream imageStream, final String contentType) {
		final ByteArrayDataSource imgSource;
		try {
			imgSource = new ByteArrayDataSource(imageStream, ObjUtil.defaultIfNull(contentType, "image/jpeg"));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		imgSource.setName(cid);
		return addAttachments(imgSource);
	}

	/**
	 * 设置文件类型附件，文件可以是图片文件，此时自动设置cid（正文中引用图片），默认cid为文件名
	 *
	 * @param files 附件文件列表
	 * @return this
	 */
	public SMTPMessage addFiles(final File... files) {
		if (ArrayUtil.isEmpty(files)) {
			return this;
		}

		final DataSource[] attachments = new DataSource[files.length];
		for (int i = 0; i < files.length; i++) {
			attachments[i] = new FileDataSource(files[i]);
		}
		return addAttachments(attachments);
	}

	/**
	 * 增加附件或图片，附件使用{@link DataSource} 形式表示，可以使用{@link FileDataSource}包装文件表示文件附件
	 *
	 * @param attachments 附件列表
	 * @return this
	 * @since 4.0.9
	 */
	public SMTPMessage addAttachments(final DataSource... attachments) {
		if (ArrayUtil.isNotEmpty(attachments)) {
			final Charset charset = this.mailAccount.getCharset();
			for (final DataSource attachment : attachments) {
				addBodyPart(buildBodyPart(attachment, charset));
			}
		}
		return this;
	}
	// endregion

	/**
	 * 添加邮件信息主体
	 *
	 * @param bodyPart 邮件信息主体
	 * @return this
	 */
	public SMTPMessage addBodyPart(final BodyPart bodyPart) {
		try {
			this.multipart.addBodyPart(bodyPart);
		} catch (final MessagingException e) {
			throw new MailException(e);
		}
		return this;
	}

	/**
	 * 添加邮件信息主体
	 *
	 * @param bodyPart 邮件信息主体
	 * @param index    插入位置，0表示插入到开头
	 * @return this
	 */
	public SMTPMessage addBodyPart(final BodyPart bodyPart, final int index) {
		try {
			this.multipart.addBodyPart(bodyPart, index);
		} catch (final MessagingException e) {
			throw new MailException(e);
		}
		return this;
	}

	/**
	 * 发送
	 *
	 * @return message-id
	 * @throws MailException 邮件发送异常
	 */
	public String send() throws MailException {
		try {
			return doSend();
		} catch (final MessagingException e) {
			if (e instanceof SendFailedException) {
				// 当地址无效时，显示更加详细的无效地址信息
				final Address[] invalidAddresses = ((SendFailedException) e).getInvalidAddresses();
				final String msg = StrUtil.format("Invalid Addresses: {}", ArrayUtil.toString(invalidAddresses));
				throw new MailException(msg, e);
			}
			throw new MailException(e);
		}
	}

	/**
	 * 构建邮件信息主体
	 *
	 * @param content 内容, {@code null}则使用{@link StrUtil#EMPTY}替换
	 * @param charset 编码，{@code null}则使用{@link MimeUtility#getDefaultJavaCharset()}
	 * @param isHtml  是否为HTML
	 * @return 邮件信息主体
	 * @throws MessagingException 消息异常
	 */
	private Multipart buildContent(final String content, final Charset charset, final boolean isHtml) throws MessagingException {
		final String charsetStr = null != charset ? charset.name() : MimeUtility.getDefaultJavaCharset();
		// 正文
		final MimeBodyPart body = new MimeBodyPart();
		// 内容如果是null会抛异常, 使用空字符串代替
		body.setContent(StrUtil.emptyIfNull(content), StrUtil.format("text/{}; charset={}", isHtml ? "html" : "plain", charsetStr));
		addBodyPart(body, 0);
		return this.multipart;
	}

	/**
	 * 执行发送
	 *
	 * @return message-id
	 * @throws MessagingException 发送异常
	 */
	private String doSend() throws MessagingException {
		Transport.send(this);
		return getMessageID();
	}

	/**
	 * 构建邮件信息主体
	 *
	 * @param attachment 附件
	 * @param charset    编码
	 * @return 邮件信息主体
	 */
	private MimeBodyPart buildBodyPart(final DataSource attachment, final Charset charset) {
		final MimeBodyPart bodyPart = new MimeBodyPart();

		try {
			bodyPart.setDataHandler(new DataHandler(attachment));
			String nameEncoded = attachment.getName();
			if (this.mailAccount.isEncodefilename()) {
				nameEncoded = InternalMailUtil.encodeText(nameEncoded, charset);
			}
			// 普通附件文件名
			bodyPart.setFileName(nameEncoded);
			if (StrUtil.startWith(attachment.getContentType(), "image/")) {
				// 图片附件，用于正文中引用图片
				bodyPart.setContentID(nameEncoded);
				bodyPart.setDisposition(MimeBodyPart.INLINE);
			}
		} catch (final MessagingException e) {
			throw new MailException(e);
		}
		return bodyPart;
	}
}
