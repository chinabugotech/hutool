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

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

/**
 * 用户名密码授权
 *
 * @author looly
 */
public class UserPassMailAuthenticator extends Authenticator {
	private final PasswordAuthentication auth;

	/**
	 * 构造
	 *
	 * @param mailAccount 邮箱账号信息
	 */
	public UserPassMailAuthenticator(final MailAccount mailAccount) {
		this.auth = new PasswordAuthentication(mailAccount.getUser(), String.valueOf(mailAccount.getPass()));
	}

	/**
	 * 构造
	 *
	 * @param userName 用户名
	 * @param password 密码
	 */
	public UserPassMailAuthenticator(final String userName, final String password) {
		this.auth = new PasswordAuthentication(userName, password);
	}

	/**
	 * 构造
	 *
	 * @param auth 密码授权信息
	 */
	public UserPassMailAuthenticator(final PasswordAuthentication auth) {
		this.auth = auth;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return this.auth;
	}
}
