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

package cn.hutool.v7.extra.ftp;

/**
 * FTP连接模式
 *
 * <p>
 * 见：https://www.cnblogs.com/huhaoshida/p/5412615.html
 *
 * @author Looly
 * @since 4.1.19
 */
public enum FtpMode {

	/**
	 * 主动模式
	 */
	Active,
	/**
	 * 被动模式
	 */
	Passive
}
