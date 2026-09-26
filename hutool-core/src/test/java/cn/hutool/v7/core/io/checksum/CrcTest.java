/*
 * Copyright (c) 2013-2026 Hutool Team.
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

package cn.hutool.v7.core.io.checksum;

import cn.hutool.v7.core.codec.binary.HexUtil;
import cn.hutool.v7.core.io.checksum.crc16.*;
import cn.hutool.v7.core.util.ByteUtil;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CheckedInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CRC校验单元测试
 *
 * @author Looly
 *
 */
public class CrcTest {

	@Test
	public void crc8Test() {
		final int CRC_POLYNOM = 0x9C;
		final byte CRC_INITIAL = (byte) 0xFF;

		final byte[] data = { 1, 56, -23, 3, 0, 19, 0, 0, 2, 0, 3, 13, 8, -34, 7, 9, 42, 18, 26, -5, 54, 11, -94, //
				-46, -128, 4, 48, 52, 0, 0, 0, 0, 0, 0, 0, 0, 4, 1, 1, -32, -80, 0, 98, -5, 71, 0, 64, 0, 0, 0, 0, -116, 1, 104, 2 };
		final CRC8 crc8 = new CRC8(CRC_POLYNOM, CRC_INITIAL);
		crc8.update(data, 0, data.length);
		assertEquals(29, crc8.getValue());
	}

	@Test
	public void crc16Test() {
		final CRC16 crc = new CRC16();
		crc.update(12);
		crc.update(16);
		assertEquals("cc04", HexUtil.toHex(crc.getValue()));
	}

	@Test
	public void crc16Test2() {
		final String str = "QN=20160801085857223;ST=23;CN=2011;PW=123456;MN=010000A8900016F000169DC0;Flag=5;CP=&&DataTime=20160801085857; LA-Rtd=50.1&&";
		final CRC16 crc = new CRC16();
		crc.update(str.getBytes(), 0, str.getBytes().length);
		final String crc16 = HexUtil.toHex(crc.getValue());
		assertEquals("18c", crc16);
	}

	@Test
	public void paddingTest(){
		// I3B3RV@Gitee
		final String text = "000123FFFFFF";
		final CRC16XModem crc16 = new CRC16XModem();
		crc16.update(ByteUtil.toUtf8Bytes(text));
		final String hexValue = crc16.getHexValue(true);
		assertEquals("0e04", hexValue);
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

	@SuppressWarnings("StatementWithEmptyBody")
	@Test
	public void checkedInputStreamTest() throws IOException {
		// 通过流式读取（IoUtil.checksumValue等内部就是这么喂数据的）得到的结果，
		// 必须与一次性传入整个数组一致
		final byte[] bytes = new byte[20000];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (i * 31 + 7);
		}

		final CRC16USB crc16 = new CRC16USB();
		try (final CheckedInputStream in = new CheckedInputStream(new ByteArrayInputStream(bytes), crc16)) {
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
	private void assertSameResultByUpdateStyle(final CRC16Checksum crc16, final byte[] bytes) {
		final String name = crc16.getClass().getSimpleName();

		crc16.reset();
		crc16.update(bytes, 0, bytes.length);
		final long bulk = crc16.getValue();

		crc16.reset();
		crc16.update(bytes, 0, 3);
		crc16.update(bytes, 3, bytes.length - 3);
		final long chunked = crc16.getValue();

		crc16.reset();
		for (final byte b : bytes) {
			crc16.update(b);
		}
		final long perByte = crc16.getValue();

		assertEquals(bulk, chunked, "分块更新结果与一次性更新不一致：" + name);
		assertEquals(bulk, perByte, "逐字节更新结果与一次性更新不一致：" + name);
		assertTrue(bulk >= 0 && bulk <= 0xFFFF, "结果不是16位值：" + name + " -> " + bulk);
	}
}
