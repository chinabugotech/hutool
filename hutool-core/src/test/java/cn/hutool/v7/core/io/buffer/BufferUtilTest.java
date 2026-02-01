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

package cn.hutool.v7.core.io.buffer;

import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BufferUtil单元测试
 *
 * @author Looly
 *
 */
public class BufferUtilTest {

	@Test
	public void copyTest() {
		final byte[] bytes = "AAABBB".getBytes();
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);

		final ByteBuffer buffer2 = BufferUtil.copy(buffer, ByteBuffer.allocate(5));
		assertEquals("AAABB", StrUtil.utf8Str(buffer2));
	}

	@Test
	public void readBytesTest() {
		final byte[] bytes = "AAABBB".getBytes();
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);

		final byte[] bs = BufferUtil.readBytes(buffer, 5);
		assertEquals("AAABB", StrUtil.utf8Str(bs));
	}

	@Test
	public void readBytes2Test() {
		final byte[] bytes = "AAABBB".getBytes();
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);

		final byte[] bs = BufferUtil.readBytes(buffer, 5);
		assertEquals("AAABB", StrUtil.utf8Str(bs));
	}

	@Test
	public void readLineTest() {
		final String text = "aa\r\nbbb\ncc";
		final ByteBuffer buffer = ByteBuffer.wrap(text.getBytes());

		// 第一行
		String line = BufferUtil.readLine(buffer, CharsetUtil.UTF_8);
		assertEquals("aa", line);

		// 第二行
		line = BufferUtil.readLine(buffer, CharsetUtil.UTF_8);
		assertEquals("bbb", line);

		// 第三行因为没有行结束标志，因此返回null
		line = BufferUtil.readLine(buffer, CharsetUtil.UTF_8);
		assertNull(line);

		// 读取剩余部分
		assertEquals("cc", StrUtil.utf8Str(BufferUtil.readBytes(buffer)));
	}

	/**
	 * 测试正常范围内的拷贝功能
	 */
	@Test
	public void copyNormalRangeTest() {
		// 准备测试数据
		final byte[] originalData = {65, 66, 67, 68, 69, 70}; // 对应 "ABCDEF"
		final ByteBuffer srcBuffer = ByteBuffer.wrap(originalData);

		// 执行拷贝操作，从索引1到4（不包含4），即拷贝BCD
		final ByteBuffer resultBuffer = BufferUtil.copy(srcBuffer, 1, 4);

		// 验证结果
		final byte[] resultArray = new byte[3];
		resultBuffer.get(resultArray);
		assertArrayEquals(new byte[]{66, 67, 68}, resultArray); // BCD
	}

	/**
	 * 测试从开头开始拷贝
	 */
	@Test
	public void copyFromStartTest() {
		final byte[] originalData = {65, 66, 67, 68, 69, 70}; // 对应 "ABCDEF"
		final ByteBuffer srcBuffer = ByteBuffer.wrap(originalData);

		// 从索引0拷贝到3，即拷贝ABC
		final ByteBuffer resultBuffer = BufferUtil.copy(srcBuffer, 0, 3);

		final byte[] resultArray = new byte[3];
		resultBuffer.get(resultArray);
		assertArrayEquals(new byte[]{65, 66, 67}, resultArray); // ABC
	}

	/**
	 * 测试拷贝到末尾
	 */
	@Test
	public void copyToEndTest() {
		final byte[] originalData = {65, 66, 67, 68, 69, 70}; // 对应 "ABCDEF"
		final ByteBuffer srcBuffer = ByteBuffer.wrap(originalData);

		// 从索引3拷贝到末尾，即拷贝DEF
		final ByteBuffer resultBuffer = BufferUtil.copy(srcBuffer, 3, 6);

		final byte[] resultArray = new byte[3];
		resultBuffer.get(resultArray);
		assertArrayEquals(new byte[]{68, 69, 70}, resultArray); // DEF
	}

	/**
	 * 测试空拷贝（start等于end）
	 */
	@Test
	public void copyEmptyRangeTest() {
		final byte[] originalData = {65, 66, 67, 68, 69, 70}; // 对应 "ABCDEF"
		final ByteBuffer srcBuffer = ByteBuffer.wrap(originalData);

		// 拷贝相同起始和结束位置，应该得到空数组
		final ByteBuffer resultBuffer = BufferUtil.copy(srcBuffer, 2, 2);

		assertEquals(0, resultBuffer.remaining()); // 应该为空
	}

	/**
	 * 测试整个数组的拷贝
	 */
	@Test
	public void copyFullRangeTest() {
		final byte[] originalData = {65, 66, 67, 68, 69, 70}; // 对应 "ABCDEF"
		final ByteBuffer srcBuffer = ByteBuffer.wrap(originalData);

		// 拷贝整个数组
		final ByteBuffer resultBuffer = BufferUtil.copy(srcBuffer, 0, 6);

		final byte[] resultArray = new byte[6];
		resultBuffer.get(resultArray);
		assertArrayEquals(originalData, resultArray);
	}
}
