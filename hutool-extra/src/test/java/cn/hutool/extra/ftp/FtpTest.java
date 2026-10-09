package cn.hutool.extra.ftp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.extra.ftp.FtpException;
import cn.hutool.extra.ssh.Sftp;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FtpTest {

	@Test
	@Disabled
	public void cdTest() {
		final Ftp ftp = new Ftp("looly.centos");

		ftp.cd("/file/aaa");
		Console.log(ftp.pwd());

		IoUtil.close(ftp);
	}

	@Test
	@Disabled
	public void uploadTest() {
		final Ftp ftp = new Ftp("localhost");

		final boolean upload = ftp.upload("/temp", FileUtil.file("d:/test/test.zip"));
		Console.log(upload);

		IoUtil.close(ftp);
	}

	@Test
	@Disabled
	public void uploadDirectorTest() {
		final Ftp ftp = new Ftp("localhost");

		ftp.uploadFileOrDirectory("/temp", FileUtil.file("d:/test/"));
		IoUtil.close(ftp);
	}

	@Test
	@Disabled
	public void reconnectIfTimeoutTest() throws InterruptedException {
		final Ftp ftp = new Ftp("looly.centos");

		Console.log("打印pwd: " + ftp.pwd());

		Console.log("休眠一段时间，然后再次发送pwd命令，抛出异常表明连接超时");
		Thread.sleep(35 * 1000);

		try{
			Console.log("打印pwd: " + ftp.pwd());
		}catch (final FtpException e) {
			e.printStackTrace();
		}

		Console.log("判断是否超时并重连...");
		ftp.reconnectIfTimeout();

		Console.log("打印pwd: " + ftp.pwd());

		IoUtil.close(ftp);
	}

	@Test
	@Disabled
	public void recursiveDownloadFolder() {
		final Ftp ftp = new Ftp("looly.centos");
		ftp.recursiveDownloadFolder("/",FileUtil.file("d:/test/download"));

		IoUtil.close(ftp);
	}

	@Test
	@Disabled
	public void recursiveDownloadFolderSftp() {
		final Sftp ftp = new Sftp("127.0.0.1", 22, "test", "test");

		ftp.cd("/file/aaa");
		Console.log(ftp.pwd());
		ftp.recursiveDownloadFolder("/",FileUtil.file("d:/test/download"));

		IoUtil.close(ftp);
	}

	@Test
	@Disabled
	public void downloadTest() {
		String downloadPath = "d:/test/download/";
		try (final Ftp ftp = new Ftp("localhost")) {
			final List<FTPFile> ftpFiles = ftp.lsFiles("temp/", null);
			for (final FTPFile ftpFile : ftpFiles) {
				String name = ftpFile.getName();
				if (ftpFile.isDirectory()) {
					File dp = new File(downloadPath + name);
					if (!dp.exists()) {
						dp.mkdir();
					}
				} else {
					ftp.download("", name, FileUtil.file(downloadPath + name));
				}
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Test
	@Disabled
	public void isDirTest() throws Exception {
		try (final Ftp ftp = new Ftp("127.0.0.1", 21)) {
			Console.log(ftp.pwd());
			ftp.isDir("/test");
			Console.log(ftp.pwd());
		}
	}

	@Test
	@Disabled
	public void existSftpTest() throws Exception {
		try (final Sftp ftp = new Sftp("127.0.0.1", 22, "test", "test")) {
			Console.log(ftp.pwd());
			Console.log(ftp.exist(null));
			Console.log(ftp.exist(""));
			Console.log(ftp.exist("."));
			Console.log(ftp.exist(".."));
			Console.log(ftp.exist("/"));
			Console.log(ftp.exist("a"));
			Console.log(ftp.exist("/home/test"));
			Console.log(ftp.exist("/home/test/"));
			Console.log(ftp.exist("/home/test//////"));
			Console.log(ftp.exist("/home/test/file1"));
			Console.log(ftp.exist("/home/test/file1/"));
			Console.log(ftp.exist("///////////"));
			Console.log(ftp.exist("./"));
			Console.log(ftp.exist("./file1"));
			Console.log(ftp.pwd());
		}
	}

	@Test
	@Disabled
	public void existFtpTest() throws Exception {
		try (final Ftp ftp = new Ftp("127.0.0.1", 21)) {
			Console.log(ftp.pwd());
			Console.log(ftp.exist(null));
			Console.log(ftp.exist(""));
			Console.log(ftp.exist("."));
			Console.log(ftp.exist(".."));
			Console.log(ftp.exist("/"));
			Console.log(ftp.exist("a"));
			Console.log(ftp.exist("/test"));
			Console.log(ftp.exist("/test/"));
			Console.log(ftp.exist("/test//////"));
			Console.log(ftp.exist("/test/.."));
			Console.log(ftp.exist("/test/."));
			Console.log(ftp.exist("/file1"));
			Console.log(ftp.exist("/file1/"));
			Console.log(ftp.exist("///////////"));
			Console.log(ftp.exist("./"));
			Console.log(ftp.exist("./file1"));
			Console.log(ftp.exist("./2/3/4/.."));
			Console.log(ftp.pwd());
		}
	}

	@Test
	@Disabled
	public void renameTest() {
		final Ftp ftp = new Ftp("localhost", 21, "test", "test");

		ftp.mkdir("/ftp-1");
		assertTrue(ftp.exist("/ftp-1"));
		ftp.rename("/ftp-1", "/ftp-2");
		assertTrue(ftp.exist("/ftp-2"));
	}

	// -----------------------------------------------------------------------
	// issue #4304: Ftp.download 失败时不应留下 0 字节目标文件
	// -----------------------------------------------------------------------

	/**
	 * 构造一个不会真实连接 FTP 的 Ftp 实例。
	 * <p>
	 * 通过覆盖 {@link FTPClient} 的关键方法（setFileType、changeWorkingDirectory、retrieveFile），
	 * 使其行为可由测试用例控制，避免依赖真实 FTP 服务端。
	 * <p>
	 * 使用 {@link Ftp#Ftp(FTPClient)} 构造器，跳过 init() 中的真实连接。
	 *
	 * @param retrieveResult  retrieveFile 的返回结果（true=下载成功，false=服务端返回 550/425/426 等）
	 * @param payload         当 retrieveFile 返回 true 时写出的字节内容
	 * @return 配置好的 Ftp 实例
	 */
	private static Ftp buildMockFtp(final boolean retrieveResult, final byte[] payload) {
		// 注意：FTPClient 是具体类，setFileType/changeWorkingDirectory 等方法的签名必须严格匹配父类。
		// setFileType(int) 的父类签名是 public boolean setFileType(int)，因此这里必须返回 boolean。
		// retrieveFile 返回 boolean 决定本次下载成功与否（true=完整下载，false=服务端返回 4xx/5xx）。
		final FTPClient client = new FTPClient() {
			@Override
			public boolean setFileType(final int fileType) {
				return true;
			}

			@Override
			public boolean changeWorkingDirectory(final String pathname) {
				return true;
			}

			@Override
			public boolean retrieveFile(final String remote, final OutputStream local) {
				if (retrieveResult && null != payload) {
					try {
						local.write(payload);
					} catch (final IOException e) {
						throw new RuntimeException(e);
					}
				}
				return retrieveResult;
			}
		};
		return new Ftp(client);
	}

	/**
	 * 回归测试：下载失败时（retrieveFile 返回 false），目标文件不应被创建。
	 * <p>
	 * 修复前：Ftp.download 会先 touch 出一个 0 字节文件，再触发 retrieveFile 返回 false，留下了脏文件。
	 * 修复后：先下载到临时文件，失败时清理临时文件，目标文件根本不会被创建。
	 */
	@Test
	public void downloadFailedShouldNotLeaveZeroByteFileTest() {
		final File target = new File(System.getProperty("java.io.tmpdir"), "ftp-4304-target-no-leak.bin");
		// 确认前置状态：文件不存在
		assertFalse(target.exists(), "测试前置条件：目标文件应该不存在");

		final Ftp ftp = buildMockFtp(false, null);

		// 修复后应抛 FtpException（retrieveFile 返回 false 的契约异常）
		final FtpException ex = assertThrows(FtpException.class,
				() -> ftp.download("/", "not-exists.bin", target));
		assertNotNull(ex);

		// 核心断言：目标文件不应残留
		assertFalse(target.exists(),
				"修复 issue #4304：下载失败时不应留下 0 字节目标文件，实际文件存在: " + target.getAbsolutePath());

		// 清理：确保测试运行后无残留
		FileUtil.del(target);
	}

	/**
	 * 回归测试：正常下载时，文件应被正确写入。
	 * <p>
	 * 验证修复未破坏正常下载流程。
	 */
	@Test
	public void downloadSuccessShouldWriteAllBytesTest() {
		final File target = new File(System.getProperty("java.io.tmpdir"), "ftp-4304-target-success.bin");
		assertFalse(target.exists(), "测试前置条件：目标文件应该不存在");

		final byte[] payload = "hello-ftp-4304".getBytes();
		final Ftp ftp = buildMockFtp(true, payload);

		ftp.download("/", "exists.bin", target);

		// 核心断言：文件内容应完整
		assertTrue(target.exists(), "下载成功后目标文件应存在");
		assertEquals(payload.length, target.length(), "下载成功后文件大小应与 payload 一致");
		assertEquals("hello-ftp-4304", new String(FileUtil.readBytes(target)));

		// 清理：移除目标文件及可能残留的临时文件
		FileUtil.del(target);
	}

	/**
	 * 回归测试：下载失败时，临时文件（.download.<nanos>）不应残留。
	 * <p>
	 * 验证 fix 的清理逻辑（catch 块中 FileUtil.del(tmpFile)）。
	 */
	@Test
	public void downloadFailedShouldCleanTmpFileTest() {
		final File target = new File(System.getProperty("java.io.tmpdir"), "ftp-4304-tmpleak-test.bin");
		final File targetDir = target.getParentFile();
		assertNotNull(targetDir);

		// 枚举前清空，保证测试前没有 .download.* 残留
		final File[] before = targetDir.listFiles((f) -> f.getName().startsWith("ftp-4304-tmpleak-test.bin.download."));
		if (null != before) {
			for (final File f : before) {
				FileUtil.del(f);
			}
		}

		final Ftp ftp = buildMockFtp(false, null);
		assertThrows(FtpException.class,
				() -> ftp.download("/", "not-exists.bin", target));

		// 核心断言：临时文件不应残留
		final File[] after = targetDir.listFiles((f) -> f.getName().startsWith("ftp-4304-tmpleak-test.bin.download."));
		if (null != after) {
			assertEquals(0, after.length,
					"修复 issue #4304：失败路径不应残留临时文件，实际残留: " + java.util.Arrays.toString(after));
		}

		// 清理
		if (target.exists()) FileUtil.del(target);
	}

	/**
	 * 回归测试：覆盖已有文件时（REPLACE_EXISTING 行为），下载成功应能覆盖旧文件。
	 * <p>
	 * 验证修复未破坏 Files.move(... REPLACE_EXISTING) 的覆盖语义。
	 */
	@Test
	public void downloadSuccessShouldReplaceExistingFile() {
		final File target = new File(System.getProperty("java.io.tmpdir"), "ftp-4304-replace-test.bin");
		// 预先写入旧内容
		FileUtil.writeBytes("old-content".getBytes(), target);
		assertTrue(target.exists());
		assertEquals("old-content".length(), (int) target.length());

		final byte[] payload = "new-content".getBytes();
		final Ftp ftp = buildMockFtp(true, payload);

		ftp.download("/", "exists.bin", target);

		assertTrue(target.exists());
		assertEquals("new-content", new String(FileUtil.readBytes(target)),
				"下载成功后应覆盖旧文件");

		// 清理
		FileUtil.del(target);
	}
}
