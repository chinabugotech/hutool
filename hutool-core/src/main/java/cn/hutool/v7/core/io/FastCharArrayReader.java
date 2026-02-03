/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.core.io;

import cn.hutool.v7.core.text.StrUtil;

import java.io.IOException;
import java.io.Reader;

/**
 * 基于{@link char[]}的{@link Reader}实现，用于支持{@link char[]}的读取<br>
 * 相比jdk的{@link java.io.CharArrayReader}非线程安全，速度更快。
 *
 * @author Looly
 * @since 7.0.0
 */
public class FastCharArrayReader extends Reader {
	protected char[] buf;
	protected int pos;
	protected int markedPos = 0;
	protected int count;

	/**
	 * 构造
	 *
	 * @param str 字符串
	 */
	public FastCharArrayReader(final CharSequence str){
		this(StrUtil.toCharArray(str));
	}

	/**
	 * 构造
	 *
	 * @param buf 字符数组
	 */
	public FastCharArrayReader(final char[] buf) {
		this.buf = buf;
		this.pos = 0;
		this.count = buf.length;
	}

	/**
	 * 构造
	 *
	 * @param buf    字符数组
	 * @param offset 起始位置
	 * @param length 长度
	 */
	public FastCharArrayReader(final char[] buf, final int offset, final int length) {
		if ((offset < 0) || (offset > buf.length) || (length < 0) || ((offset + length) < 0)) {
			throw new IllegalArgumentException();
		}
		this.buf = buf;
		this.pos = offset;
		this.count = Math.min(offset + length, buf.length);
		this.markedPos = offset;
	}

	@Override
	public int read() throws IOException {
		ensureOpen();
		if (pos >= count)
			return -1;
		else
			return buf[pos++];
	}

	@Override
	public int read(final char[] b, final int off, int len) throws IOException {
		ensureOpen();
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}

		if (pos >= count) {
			return -1;
		}
		if (pos + len > count) {
			len = count - pos;
		}
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(buf, pos, b, off, len);
		pos += len;
		return len;
	}

	@Override
	public long skip(long n) throws IOException {
		ensureOpen();
		if (pos + n > count) {
			n = count - pos;
		}
		if (n < 0) {
			return 0;
		}
		pos += n;
		return n;
	}

	@Override
	public boolean ready() throws IOException {
		ensureOpen();
		return (count - pos) > 0;
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public void mark(final int readAheadLimit) throws IOException {
		ensureOpen();
		markedPos = pos;
	}

	@Override
	public void reset() throws IOException {
		ensureOpen();
		pos = markedPos;
	}

	@Override
	public void close() {
		buf = null;
	}

	/**
	 * 确保流打开
	 *
	 * @throws IOException 流关闭
	 */
	private void ensureOpen() throws IOException {
		if (buf == null) {
			throw new IOException("Stream closed");
		}
	}
}
