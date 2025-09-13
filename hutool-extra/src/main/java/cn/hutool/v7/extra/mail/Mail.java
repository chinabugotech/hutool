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

package cn.hutool.v7.extra.mail;

import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.io.IORuntimeException;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.lang.builder.Builder;
import cn.hutool.v7.core.util.ObjUtil;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.activation.FileTypeMap;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 邮件发送客户端
 *
 * @author Looly
 * @since 3.2.0
 */
public class Mail implements Builder<MimeMessage> {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 邮箱帐户信息以及一些客户端配置信息
	 */
	private final MailAccount mailAccount;
	/**
	 * 收件人列表
	 */
	private String[] tos;
	/**
	 * 抄送人列表（carbon copy）
	 */
	private String[] ccs;
	/**
	 * 密送人列表（blind carbon copy）
	 */
	private String[] bccs;
	/**
	 * 回复地址(reply-to)
	 */
	private String[] reply;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 是否为HTML
	 */
	private boolean isHtml;
	/**
	 * 附件或图片
	 */
	private DataSource[] attachments;
	/**
	 * 是否使用全局会话，默认为false
	 */
	private boolean useGlobalSession = false;

	/**
	 * debug输出位置，可以自定义debug日志
	 */
	private PrintStream debugOutput;

	/**
	 * 创建邮件客户端
	 *
	 * @param mailAccount 邮件帐号
	 * @return Mail
	 */
	public static Mail of(final MailAccount mailAccount) {
		return new Mail(mailAccount);
	}

	/**
	 * 创建邮件客户端，使用全局邮件帐户
	 *
	 * @return Mail
	 */
	public static Mail of() {
		return new Mail();
	}

	// --------------------------------------------------------------- Constructor start

	/**
	 * 构造，使用全局邮件帐户
	 */
	public Mail() {
		this(GlobalMailAccount.INSTANCE.getAccount());
	}

	/**
	 * 构造
	 *
	 * @param mailAccount 邮件帐户，如果为null使用默认配置文件的全局邮件配置
	 */
	public Mail(MailAccount mailAccount) {
		mailAccount = (null != mailAccount) ? mailAccount : GlobalMailAccount.INSTANCE.getAccount();
		this.mailAccount = mailAccount.defaultIfEmpty();
	}
	// --------------------------------------------------------------- Constructor end

	// --------------------------------------------------------------- Getters and Setters start

	/**
	 * 设置收件人
	 *
	 * @param tos 收件人列表
	 * @return this
	 * @see #setTos(String...)
	 */
	public Mail to(final String... tos) {
		return setTos(tos);
	}

	/**
	 * 设置多个收件人
	 *
	 * @param tos 收件人列表
	 * @return this
	 */
	public Mail setTos(final String... tos) {
		this.tos = tos;
		return this;
	}

	/**
	 * 设置多个抄送人（carbon copy）
	 *
	 * @param ccs 抄送人列表
	 * @return this
	 * @since 4.0.3
	 */
	public Mail setCcs(final String... ccs) {
		this.ccs = ccs;
		return this;
	}

	/**
	 * 设置多个密送人（blind carbon copy）
	 *
	 * @param bccs 密送人列表
	 * @return this
	 * @since 4.0.3
	 */
	public Mail setBccs(final String... bccs) {
		this.bccs = bccs;
		return this;
	}

	/**
	 * 设置多个回复地址(reply-to)
	 *
	 * @param reply 回复地址(reply-to)列表
	 * @return this
	 * @since 4.6.0
	 */
	public Mail setReply(final String... reply) {
		this.reply = reply;
		return this;
	}

	/**
	 * 设置标题
	 *
	 * @param title 标题
	 * @return this
	 */
	public Mail setTitle(final String title) {
		this.title = title;
		return this;
	}

	/**
	 * 设置正文<br>
	 * 正文可以是普通文本也可以是HTML（默认普通文本），可以通过调用{@link #setHtml(boolean)} 设置是否为HTML
	 *
	 * @param content 正文
	 * @return this
	 */
	public Mail setContent(final String content) {
		this.content = content;
		return this;
	}

	/**
	 * 设置是否是HTML
	 *
	 * @param isHtml 是否为HTML
	 * @return this
	 */
	public Mail setHtml(final boolean isHtml) {
		this.isHtml = isHtml;
		return this;
	}

	/**
	 * 设置正文
	 *
	 * @param content 正文内容
	 * @param isHtml  是否为HTML
	 * @return this
	 */
	public Mail setContent(final String content, final boolean isHtml) {
		setContent(content);
		return setHtml(isHtml);
	}

	/**
	 * 设置文件类型附件，文件可以是图片文件，此时自动设置cid（正文中引用图片），默认cid为文件名
	 *
	 * @param files 附件文件列表
	 * @return this
	 */
	public Mail addFiles(final File... files) {
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
	 * 增加图片，图片的键对应到邮件模板中的占位字符串，图片类型默认为"image/jpeg"
	 *
	 * @param cid         图片与占位符，占位符格式为cid:${cid}
	 * @param imageStream 图片文件
	 * @return this
	 * @since 4.6.3
	 */
	public Mail addImage(final String cid, final InputStream imageStream) {
		return addImage(cid, imageStream, null);
	}

	/**
	 * 增加图片，图片的键对应到邮件模板中的占位字符串
	 *
	 * @param cid         图片与占位符，占位符格式为cid:${cid}
	 * @param imageStream 图片流，不关闭
	 * @param contentType 图片类型，null赋值默认的"image/jpeg"
	 * @return this
	 * @since 4.6.3
	 */
	public Mail addImage(final String cid, final InputStream imageStream, final String contentType) {
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
	 * 增加图片，图片的键对应到邮件模板中的占位字符串
	 *
	 * @param cid       图片与占位符，占位符格式为cid:${cid}
	 * @param imageFile 图片文件
	 * @return this
	 * @since 4.6.3
	 */
	public Mail addImage(final String cid, final File imageFile) {
		InputStream in = null;
		try {
			in = FileUtil.getInputStream(imageFile);
			return addImage(cid, in, FileTypeMap.getDefaultFileTypeMap().getContentType(imageFile));
		} finally {
			IoUtil.closeQuietly(in);
		}
	}

	/**
	 * 增加附件或图片，附件使用{@link DataSource} 形式表示，可以使用{@link FileDataSource}包装文件表示文件附件
	 *
	 * @param attachments 附件列表
	 * @return this
	 * @since 4.0.9
	 */
	public Mail addAttachments(final DataSource... attachments) {
		if(ArrayUtil.isNotEmpty(attachments)){
			if(null == this.attachments){
				this.attachments = attachments;
			}else{
				this.attachments = ArrayUtil.addAll(this.attachments, attachments);
			}
		}
		return this;
	}

	/**
	 * 设置字符集编码
	 *
	 * @param charset 字符集编码
	 * @return this
	 * @see MailAccount#setCharset(Charset)
	 */
	public Mail setCharset(final Charset charset) {
		this.mailAccount.setCharset(charset);
		return this;
	}

	/**
	 * 设置是否使用全局会话，默认为true
	 *
	 * @param isUseGlobalSession 是否使用全局会话，默认为true
	 * @return this
	 * @since 4.0.2
	 */
	public Mail setUseGlobalSession(final boolean isUseGlobalSession) {
		this.useGlobalSession = isUseGlobalSession;
		return this;
	}

	/**
	 * 设置debug输出位置，可以自定义debug日志
	 *
	 * @param debugOutput debug输出位置
	 * @return this
	 * @since 5.5.6
	 */
	public Mail setDebugOutput(final PrintStream debugOutput) {
		this.debugOutput = debugOutput;
		return this;
	}
	// --------------------------------------------------------------- Getters and Setters end

	@Override
	public SMTPMessage build() {
		return SMTPMessage.of(this.mailAccount, this.useGlobalSession, this.debugOutput)
			// 标题
			.setTitle(this.title)
			// 收件人
			.setTos(this.tos)
			// 抄送人
			.setCcs(this.ccs)
			// 密送人
			.setBccs(this.bccs)
			// 回复地址(reply-to)
			.setReply(this.reply)
			// 内容和附件
			.setContent(this.content, this.isHtml);
	}

	/**
	 * 发送
	 *
	 * @return message-id
	 * @throws MailException 邮件发送异常
	 */
	public String send() throws MailException {
		return build().send();
	}
}
