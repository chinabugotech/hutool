package cn.hutool.extra.ssh;

import cn.hutool.core.util.CharsetUtil;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.io.File;
import java.util.List;

/**
 * 基于sshj 框架SFTP 封装测试.
 *
 * @author youyongkun
 * @since 5.7.18
 */
public class SftpTest {

	private Sftp sftp;

	@Before
	@Disabled
	public void init() {
		sftp = new Sftp("127.0.0.1", 8022, "test", "test", CharsetUtil.CHARSET_UTF_8);
	}

	@Test
	@Disabled
	public void lsTest() {
		List<String> files = sftp.ls("/");
		if (files != null && !files.isEmpty()) {
			files.forEach(System.out::println);
		}
	}

	@Test
	@Disabled
	public void downloadTest() {
		sftp.recursiveDownloadFolder("/temp/20250427/", new File("D:\\temp\\20250430\\20250427\\"));
	}

	@Test
	@Disabled
	public void uploadTest() {
		sftp.upload("/ftp-2/20250430/", new File("D:\\temp\\20250430\\test.txt"));
	}

	@Test
	@Disabled
	public void mkDirTest() {
		boolean flag = sftp.mkdir("/ftp-2/20250430-1");
		System.out.println("是否创建成功: " + flag);
	}

	@Test
	@Disabled
	public void pwdTest() {
		String pwd = sftp.pwd();
		System.out.println("PWD: " + pwd);
	}

	@Test
	@Disabled
	public void mkDirsTest() {
		// 在当前目录下批量创建目录
		sftp.mkDirs("/ftp-2/20250430-2/t1/t2/");
	}

	@Test
	@Disabled
	public void delDirTest() {
		sftp.delDir("/ftp-2/20250430-2/t1/t2");
	}

	@Test
	@Disabled
	public void cdTest() {
		System.out.println(sftp.cd("/ftp-2"));
		System.out.println(sftp.cd("/ftp-4"));
	}
}
