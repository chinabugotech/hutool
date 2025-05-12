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

package cn.hutool.v7.extra.ssh.engine.sshj;

import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.io.IORuntimeException;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.extra.ftp.AbstractFtp;
import cn.hutool.v7.extra.ftp.FtpConfig;
import cn.hutool.v7.extra.ftp.FtpException;
import cn.hutool.v7.extra.ssh.Connector;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.sftp.RemoteFile;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.xfer.FileSystemFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 在使用jsch 进行sftp协议下载文件时，总是中文乱码，而该框架源码又不允许设置编码。故：站在巨人的肩膀上，此类便孕育而出。
 *
 * <p>
 * 基于sshj 框架适配。<br>
 * 参考：<a href="https://github.com/hierynomus/sshj">https://github.com/hierynomus/sshj</a>
 * </p>
 *
 * @author youyongkun
 * @since 5.7.19
 */
public class SshjSftp extends AbstractFtp {

	// region ----- of

	/**
	 * 构造
	 *
	 * @param sshHost 主机
	 * @param sshUser 用户名
	 * @param sshPass 密码
	 * @return SshjSftp
	 */
	public static SshjSftp of(final String sshHost, final String sshUser, final String sshPass) {
		return of(sshHost, 22, sshUser, sshPass);
	}

	/**
	 * 构造
	 *
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @param sshUser 用户名
	 * @param sshPass 密码
	 * @return SshjSftp
	 */
	public static SshjSftp of(final String sshHost, final int sshPort, final String sshUser, final String sshPass) {
		return of(sshHost, sshPort, sshUser, sshPass, DEFAULT_CHARSET);
	}

	/**
	 * 构造
	 *
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @param sshUser 用户名
	 * @param sshPass 密码
	 * @param charset 编码
	 * @return SshjSftp
	 */
	public static SshjSftp of(final String sshHost, final int sshPort, final String sshUser, final String sshPass, final Charset charset) {
		return new SshjSftp(new FtpConfig(Connector.of(sshHost, sshPort, sshUser, sshPass), charset));
	}
	//endregion

	private SSHClient ssh;
	private SFTPClient sftp;
	private Session session;
	private String workingDir;

	/**
	 * 构造
	 *
	 * @param config FTP配置
	 * @since 5.3.3
	 */
	public SshjSftp(final FtpConfig config) {
		super(config);
		init();
	}

	/**
	 * 构造
	 *
	 * @param sshClient {@link SSHClient}
	 * @param charset   编码
	 */
	public SshjSftp(final SSHClient sshClient, final Charset charset) {
		super(FtpConfig.of().setCharset(charset));
		this.ssh = sshClient;
		init();
	}

	/**
	 * SSH 初始化并创建一个sftp客户端.
	 *
	 * @author youyongkun
	 * @since 5.7.18
	 */
	public void init() {
		if (null == this.ssh) {
			this.ssh = SshjUtil.openClient(this.ftpConfig.getConnector());
		}

		try {
			ssh.setRemoteCharset(ftpConfig.getCharset());
			this.sftp = ssh.newSFTPClient();
		} catch (final IOException e) {
			throw new FtpException("sftp 初始化失败.", e);
		}
	}

	@Override
	public AbstractFtp reconnectIfTimeout() {
		if (StrUtil.isBlank(this.ftpConfig.getConnector().getHost())) {
			throw new FtpException("Host is blank!");
		}
		try {
			this.cd(StrUtil.SLASH);
		} catch (final FtpException e) {
			close();
			init();
		}
		return this;
	}

	@Override
	public boolean cd(final String directory) {
//		final String exec = String.format("cd %s", directory);
//		command(exec);
//		final String pwd = pwd();
//		return pwd.equals(directory);
		String newPath = getPath(directory);
		try {
			sftp.ls(newPath);
			this.workingDir = newPath;
			return true;
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public String pwd() {
//		return command("pwd");
		return getPath(null);
	}

	@Override
	public boolean mkdir(final String dir) {
		try {
			sftp.mkdir(getPath(dir));
		} catch (final IOException e) {
			throw new FtpException(e);
		}
		return containsFile(getPath(dir));
	}

	@Override
	public List<String> ls(final String path) {
		final List<RemoteResourceInfo> infoList;
		try {
			infoList = sftp.ls(getPath(path));
		} catch (final IOException e) {
			throw new FtpException(e);
		}
		if (CollUtil.isNotEmpty(infoList)) {
			return CollUtil.map(infoList, RemoteResourceInfo::getName);
		}
		return null;
	}

	@Override
	public boolean rename(String oldPath, String newPath) {
		try {
			sftp.rename(oldPath, newPath);
			return containsFile(newPath);
		} catch (final IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public boolean delFile(final String path) {
		try {
			sftp.rm(getPath(path));
			return !containsFile(getPath(path));
		} catch (final IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public boolean delDir(final String dirPath) {
		try {
			sftp.rmdir(getPath(dirPath));
			return !containsFile(getPath(dirPath));
		} catch (final IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public boolean uploadFile(String destPath, final File file) {
		try {
			if (StrUtil.endWith(destPath, StrUtil.SLASH)) {
				destPath += file.getName();
			}
			sftp.put(new FileSystemFile(file), getPath(destPath));
			return containsFile(getPath(destPath));
		} catch (final IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public void download(final String path, final File outFile) {
		try {
			sftp.get(getPath(path), new FileSystemFile(outFile));
		} catch (final IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public void recursiveDownloadFolder(final String sourcePath, final File targetDir) {
		if (!targetDir.exists()) {
			if (!targetDir.mkdirs()) {
				throw new FtpException("Dir {} create failed!", targetDir.getAbsolutePath());
			}
		}else if(!targetDir.isDirectory()){
			throw new FtpException("Target is not a directory!");
		}

		List<String> files = ls(getPath(sourcePath));
		if (CollUtil.isNotEmpty(files)) {
			files.forEach(file -> download(sourcePath + StrUtil.SLASH + file, FileUtil.file(targetDir, file)));
		}
	}

	/**
	 * 读取远程文件输入流
	 *
	 * @param path 远程文件路径
	 * @return {@link InputStream}
	 */
	@Override
	public InputStream getFileStream(final String path) {
		final RemoteFile remoteFile;
		try {
			remoteFile = sftp.open(path);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		return remoteFile.new ReadAheadRemoteFileInputStream(16);
	}

	@Override
	public void close() {
		IoUtil.closeQuietly(this.session);
		IoUtil.closeQuietly(this.sftp);
		IoUtil.closeQuietly(this.ssh);
	}

	/**
	 * 是否包含该文件
	 *
	 * @param fileDir 文件绝对路径
	 * @return true:包含 false:不包含
	 * @author youyongkun
	 * @since 5.7.18
	 */
	public boolean containsFile(final String fileDir) {
		try {
			sftp.lstat(getPath(fileDir));
			return true;
		} catch (final IOException e) {
			return false;
		}
	}


	/**
	 * 执行Linux 命令
	 *
	 * @param exec 命令
	 * @return 返回响应结果.
	 * @author youyongkun
	 * @since 5.7.19
	 */
	public String command(final String exec) {
		final Session session = this.initSession();

		Session.Command command = null;
		try {
			command = session.exec(exec);
			final InputStream inputStream = command.getInputStream();
			return IoUtil.read(inputStream, this.ftpConfig.getCharset());
		} catch (final Exception e) {
			throw new FtpException(e);
		} finally {
			IoUtil.closeQuietly(command);
		}
	}

	/**
	 * 初始化Session并返回
	 *
	 * @return session
	 */
	private Session initSession() {
		Session session = this.session;
		if (null == session || !session.isOpen()) {
			IoUtil.closeQuietly(session);
			try {
				session = this.ssh.startSession();
			} catch (final Exception e) {
				throw new FtpException(e);
			}
			this.session = session;
		}
		return session;
	}

	/**
	 * 获取对应路径的绝对路径<br>
	 * 如果提供的为绝对路径，直接返回，否则拼接当前路径
	 *
	 * @param path 路径
	 * @return 绝对路径
	 */
	private String getPath(final String path) {
		if (StrUtil.isBlank(this.workingDir)) {
			try {
				this.workingDir = sftp.canonicalize(StrUtil.EMPTY);
			} catch (IOException e) {
				throw new FtpException(e);
			}
		}

		if (StrUtil.isBlank(path)) {
			return this.workingDir;
		}

		// 如果是绝对路径，则返回
		if (StrUtil.startWith(path, StrUtil.SLASH)) {
			return path;
		}

		final String tmp = StrUtil.removeSuffix(this.workingDir, StrUtil.SLASH);
		return StrUtil.format("{}/{}", tmp, path);
	}
}
