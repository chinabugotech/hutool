package cn.hutool.core.io.checksum;

import cn.hutool.core.io.checksum.crc16.CRC16Ansi;
import cn.hutool.core.io.checksum.crc16.CRC16CCITT;
import cn.hutool.core.io.checksum.crc16.CRC16CCITTFalse;
import cn.hutool.core.io.checksum.crc16.CRC16Checksum;
import cn.hutool.core.io.checksum.crc16.CRC16DNP;
import cn.hutool.core.io.checksum.crc16.CRC16IBM;
import cn.hutool.core.io.checksum.crc16.CRC16Maxim;
import cn.hutool.core.io.checksum.crc16.CRC16Modbus;
import cn.hutool.core.io.checksum.crc16.CRC16USB;
import cn.hutool.core.io.checksum.crc16.CRC16X25;
import cn.hutool.core.io.checksum.crc16.CRC16XModem;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CheckedInputStream;

public class CRC16Test {

	private final String data = "QN=20160801085857223;ST=23;CN=2011;PW=123456;MN=010000A8900016F000169DC0;Flag=5;CP=&&DataTime=20160801085857; LA-Rtd=50.1&&";

	@Test
	public void ccittTest(){
		final CRC16CCITT crc16 = new CRC16CCITT();
		crc16.update(data.getBytes());
		assertEquals("c852", crc16.getHexValue());
	}

	@Test
	public void ccittFalseTest(){
		final CRC16CCITTFalse crc16 = new CRC16CCITTFalse();
		crc16.update(data.getBytes());
		assertEquals("a5e4", crc16.getHexValue());
	}

	@Test
	public void xmodemTest(){
		final CRC16XModem crc16 = new CRC16XModem();
		crc16.update(data.getBytes());
		assertEquals("5a8d", crc16.getHexValue());
	}

	@Test
	public void x25Test(){
		final CRC16X25 crc16 = new CRC16X25();
		crc16.update(data.getBytes());
		assertEquals("a152", crc16.getHexValue());
	}

	@Test
	public void modbusTest(){
		final CRC16Modbus crc16 = new CRC16Modbus();
		crc16.update(data.getBytes());
		assertEquals("25fb", crc16.getHexValue());
	}

	@Test
	public void ibmTest(){
		final CRC16IBM crc16 = new CRC16IBM();
		crc16.update(data.getBytes());
		assertEquals("18c", crc16.getHexValue());
	}

	@Test
	public void maximTest(){
		final CRC16Maxim crc16 = new CRC16Maxim();
		crc16.update(data.getBytes());
		assertEquals("fe73", crc16.getHexValue());
	}

	@Test
	public void usbTest(){
		final CRC16USB crc16 = new CRC16USB();
		crc16.update(data.getBytes());
		assertEquals("da04", crc16.getHexValue());
	}

	@Test
	public void dnpTest(){
		final CRC16DNP crc16 = new CRC16DNP();
		crc16.update(data.getBytes());
		assertEquals("3d1a", crc16.getHexValue());
	}

	@Test
	public void ansiTest(){
		final CRC16Ansi crc16 = new CRC16Ansi();
		crc16.update(data.getBytes());
		assertEquals("1e00", crc16.getHexValue());

		crc16.reset();
		String str2 = "QN=20160801085857223;ST=32;CN=1062;PW=100000;MN=010000A8900016F000169DC0;Flag=5;CP=&&RtdInterval=30&&";
		crc16.update(str2.getBytes());
		assertEquals("1c80", crc16.getHexValue());
	}

	@Test
	public void updateStyleTest(){
		// 同一段字节，无论一次性传入、分块传入还是逐字节传入，CRC16结果都必须一致
		final byte[] bytes = "123456789".getBytes();
		assertSameResultByUpdateStyle(new CRC16CCITT(), bytes);
		assertSameResultByUpdateStyle(new CRC16CCITTFalse(), bytes);
		assertSameResultByUpdateStyle(new CRC16XModem(), bytes);
		assertSameResultByUpdateStyle(new CRC16X25(), bytes);
		assertSameResultByUpdateStyle(new CRC16Modbus(), bytes);
		assertSameResultByUpdateStyle(new CRC16IBM(), bytes);
		assertSameResultByUpdateStyle(new CRC16Maxim(), bytes);
		assertSameResultByUpdateStyle(new CRC16USB(), bytes);
		assertSameResultByUpdateStyle(new CRC16DNP(), bytes);
		assertSameResultByUpdateStyle(new CRC16Ansi(), bytes);
	}

	@Test
	public void checkedInputStreamTest() throws IOException {
		// 通过流式读取（IoUtil.checksumValue等内部就是这么喂数据的）得到的结果，
		// 必须与一次性传入整个数组一致
		final byte[] bytes = new byte[20000];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (i * 31 + 7);
		}

		final CRC16USB crc16 = new CRC16USB();
		try (CheckedInputStream in = new CheckedInputStream(new ByteArrayInputStream(bytes), crc16)) {
			final byte[] buffer = new byte[1024];
			// 只负责驱动流，校验值由CheckedInputStream调用crc16.update完成
			while (in.read(buffer) > 0) {
				// ignore
			}
		}

		final CRC16USB expected = new CRC16USB();
		expected.update(bytes, 0, bytes.length);
		assertEquals(expected.getValue(), crc16.getValue());
	}

	/**
	 * 断言同一个字节序列无论以哪种方式喂入{@link java.util.zip.Checksum}，结果都一致，且为16位值
	 *
	 * @param crc16 被测试的CRC16实现
	 * @param bytes 数据
	 */
	private void assertSameResultByUpdateStyle(CRC16Checksum crc16, byte[] bytes) {
		final String name = crc16.getClass().getSimpleName();

		crc16.reset();
		crc16.update(bytes, 0, bytes.length);
		final long bulk = crc16.getValue();

		crc16.reset();
		crc16.update(bytes, 0, 3);
		crc16.update(bytes, 3, bytes.length - 3);
		final long chunked = crc16.getValue();

		crc16.reset();
		for (byte b : bytes) {
			crc16.update(b);
		}
		final long perByte = crc16.getValue();

		assertEquals(bulk, chunked, "分块更新结果与一次性更新不一致：" + name);
		assertEquals(bulk, perByte, "逐字节更新结果与一次性更新不一致：" + name);
		assertTrue(bulk >= 0 && bulk <= 0xFFFF, "结果不是16位值：" + name + " -> " + bulk);
	}
}
