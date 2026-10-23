package cn.hutool.extra.ssh;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.ftp.FtpConfig;
import com.jcraft.jsch.Session;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * SFTP会话复用测试.
 */
public class SftpSessionReuseTest {

	private static final String SESSION_KEY = "hutool-test@127.0.0.1:1";

	@Test
	public void close_whenSessionIsReused_keepsSessionInPool() throws Exception {
		Session session = JschUtil.createSession("127.0.0.1", 1, "hutool-test", "password");
		JschSessionPool.INSTANCE.put(SESSION_KEY, session);
		try {
			Sftp sftp = new Sftp(createConfig(), false, true);
			setSession(sftp, session);

			sftp.close();

			assertSame(session, JschSessionPool.INSTANCE.get(SESSION_KEY));
		} finally {
			JschUtil.close(session);
		}
	}

	@Test
	public void close_whenSessionIsNotReused_removesSessionFromPool() throws Exception {
		Session session = JschUtil.createSession("127.0.0.1", 1, "hutool-test", "password");
		JschSessionPool.INSTANCE.put(SESSION_KEY, session);
		try {
			Sftp sftp = new Sftp(createConfig(), false);
			setSession(sftp, session);

			sftp.close();

			assertNull(JschSessionPool.INSTANCE.get(SESSION_KEY));
		} finally {
			JschUtil.close(session);
		}
	}

	private static FtpConfig createConfig() {
		return new FtpConfig("127.0.0.1", 1, "hutool-test", "password", CharsetUtil.CHARSET_UTF_8);
	}

	private static void setSession(Sftp sftp, Session session) throws Exception {
		Field sessionField = Sftp.class.getDeclaredField("session");
		sessionField.setAccessible(true);
		sessionField.set(sftp, session);
	}
}
