package cn.hutool.extra.ssh;

import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 基于sshj 框架SFTP 封装测试.
 *
 * @author youyongkun
 * @since 5.7.18
 */
@Disabled
class SshjSftpTest {

	private static SshjSftp sshjSftp;

	@BeforeAll
	public static void init() {
		sshjSftp = new SshjSftp("localhost", 8022, "test", "test", CharsetUtil.CHARSET_UTF_8);
	}

	@Test
	@Disabled
	public void lsTest() {
		List<String> files = sshjSftp.ls("/");
		if (files != null && !files.isEmpty()) {
			files.forEach(System.out::println);
		}
	}

	@Test
	@Disabled
	public void downloadTest() {
		sshjSftp.recursiveDownloadFolder("/home/test/temp", new File("C:\\Users\\akwangl\\Downloads\\temp"));
	}

	@Test
	@Disabled
	public void uploadTest() {
		sshjSftp.upload("/home/test/temp/", new File("C:\\Users\\akwangl\\Downloads\\temp\\辽宁_20190718_104324.CIME"));
	}

	@Test
	@Disabled
	public void mkDirTest() {
		boolean flag = sshjSftp.mkdir("/home/test/temp");
		System.out.println("是否创建成功: " + flag);
	}

	@Test
	@Disabled
	public void mkDirsTest() {
		// 在当前目录下批量创建目录
		sshjSftp.mkDirs("/home/test/temp");
	}

	@Test
	@Disabled
	public void delDirTest() {
		sshjSftp.delDir("/home/test/temp");
	}

	@Test
	@Disabled
	public void pwdTest() {
//		mkDirsTest();
		sshjSftp.cd("/ftp");
		String pwd = sshjSftp.pwd();
		System.out.println("当前目录: " + pwd);
		assertEquals("/ftp", pwd);
	}

	@Test
	@Disabled
	public void renameTest() {
//		sshjSftp.mkdir("/ftp-1");
		assertTrue(sshjSftp.exist("/ftp-1"));
		sshjSftp.rename("/ftp-1", "/ftp-2");
		assertTrue(sshjSftp.exist("/ftp-2"));
	}
}
