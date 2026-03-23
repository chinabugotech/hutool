package cn.hutool.extra.ssh;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.AbstractFtp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpException;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
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
 * 参考：https://github.com/hierynomus/sshj
 * </p>
 *
 * @author youyongkun
 * @since 5.7.19
 */
public class SshjSftp extends AbstractFtp {

	private SSHClient ssh;
	private SFTPClient sftp;
	private Session session;
	private String workingDir;

	/**
	 * 构造，使用默认端口
	 *
	 * @param sshHost 主机
	 */
	public SshjSftp(String sshHost) {
		this(new FtpConfig(sshHost, 22, null, null, DEFAULT_CHARSET));
	}

	/**
	 * 构造
	 *
	 * @param sshHost 主机
	 * @param sshUser 用户名
	 * @param sshPass 密码
	 */
	public SshjSftp(String sshHost, String sshUser, String sshPass) {
		this(new FtpConfig(sshHost, 22, sshUser, sshPass, CharsetUtil.CHARSET_UTF_8));
	}

	/**
	 * 构造
	 *
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @param sshUser 用户名
	 * @param sshPass 密码
	 */
	public SshjSftp(String sshHost, int sshPort, String sshUser, String sshPass) {
		this(new FtpConfig(sshHost, sshPort, sshUser, sshPass, CharsetUtil.CHARSET_UTF_8));
	}

	/**
	 * 构造
	 *
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @param sshUser 用户名
	 * @param sshPass 密码
	 * @param charset 编码
	 */
	public SshjSftp(String sshHost, int sshPort, String sshUser, String sshPass, Charset charset) {
		this(new FtpConfig(sshHost, sshPort, sshUser, sshPass, charset));
	}

	/**
	 * 构造
	 *
	 * @param config FTP配置
	 * @since 5.3.3
	 */
	public SshjSftp(FtpConfig config) {
		super(config);
		init();
	}

	/**
	 * SSH 初始化并创建一个sftp客户端.
	 *
	 * @author youyongkun
	 * @since 5.7.18
	 */
	public void init() {
		this.ssh = new SSHClient();
		ssh.addHostKeyVerifier(new PromiscuousVerifier());
		try {
			ssh.connect(ftpConfig.getHost(), ftpConfig.getPort());
			ssh.authPassword(ftpConfig.getUser(), ftpConfig.getPassword());
			ssh.setRemoteCharset(ftpConfig.getCharset());
			this.sftp = ssh.newSFTPClient();
		} catch (IOException e) {
			throw new FtpException("sftp 初始化失败.", e);
		}
	}

	@Override
	public AbstractFtp reconnectIfTimeout() {
		if (StrUtil.isBlank(this.ftpConfig.getHost())) {
			throw new FtpException("Host is blank!");
		}
		try {
			this.cd(StrUtil.SLASH);
		} catch (FtpException e) {
			close();
			init();
		}
		return this;
	}

	/**
	 * 改变目录，注意目前不支持..
	 * @param directory directory
	 * @return true:成功
	 */
	@Override
	public boolean cd(String directory) {
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
		return getPath(null);
	}

	@Override
	public boolean mkdir(String dir) {
		try {
			sftp.mkdir(getPath(dir));
		} catch (IOException e) {
			throw new FtpException(e);
		}
		return containsFile(getPath(dir));
	}

	@Override
	public List<String> ls(String path) {
		List<RemoteResourceInfo> infoList;
		try {
			infoList = sftp.ls(getPath(path));
		} catch (IOException e) {
			throw new FtpException(e);
		}
		if (CollUtil.isNotEmpty(infoList)) {
			return CollUtil.map(infoList, RemoteResourceInfo::getName, true);
		}
		return null;
	}

	@Override
	public boolean delFile(String path) {
		try {
			sftp.rm(getPath(path));
			return !containsFile(getPath(path));
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public boolean delDir(String dirPath) {
		try {
			sftp.rmdir(getPath(dirPath));
			return !containsFile(getPath(dirPath));
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public boolean upload(String destPath, File file) {
		try {
			if (StrUtil.endWith(destPath, StrUtil.SLASH)) {
				destPath += file.getName();
			}
			sftp.put(new FileSystemFile(file), getPath(destPath));
			return containsFile(getPath(destPath));
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public void download(String path, File outFile) {
		try {
			sftp.get(getPath(path), new FileSystemFile(outFile));
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public void recursiveDownloadFolder(String sourcePath, File destDir) {
		if (!destDir.exists() || !destDir.isDirectory()) {
			if (!destDir.mkdirs()) {
				throw new FtpException("创建目录" + destDir.getAbsolutePath() + "失败");
			}
		}

		List<String> files = ls(getPath(sourcePath));
		if (files != null && !files.isEmpty()) {
			files.forEach(file -> download(sourcePath + "/" + file, FileUtil.file(destDir, file)));
		}
	}

	@Override
	public void rename(String from, String to) {
		try {
			sftp.rename(from, to);
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public void close() {
		IoUtil.close(this.session);
		IoUtil.close(this.sftp);
		IoUtil.close(this.ssh);
	}

	/**
	 * 是否包含该文件
	 *
	 * @param fileDir 文件绝对路径
	 * @return true:包含 false:不包含
	 * @author youyongkun
	 * @since 5.7.18
	 */
	public boolean containsFile(String fileDir) {
		try {
			sftp.lstat(getPath(fileDir));
			return true;
		} catch (IOException e) {
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
	public String command(String exec) {
		final Session session = this.initSession();

		Session.Command command = null;
		try {
			command = session.exec(exec);
			InputStream inputStream = command.getInputStream();
			return IoUtil.read(inputStream, this.ftpConfig.getCharset());
		} catch (Exception e) {
			throw new FtpException(e);
		} finally {
			IoUtil.close(command);
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
			IoUtil.close(session);
			try {
				session = this.ssh.startSession();
			} catch (final Exception e) {
				throw new FtpException(e);
			}
			this.session = session;
		}
		return session;
	}

	private String getPath(String path) {
		if (StrUtil.isBlank(this.workingDir)) {
			try {
				this.workingDir = sftp.canonicalize("");
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
		} else {
			String tmp = StrUtil.removeSuffix(this.workingDir, StrUtil.SLASH);
			return StrUtil.format("{}/{}", tmp, path);
		}
	}
}
