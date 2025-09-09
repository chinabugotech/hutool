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

package cn.hutool.v7.extra.compress;

import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.extra.compress.archiver.Archiver;
import cn.hutool.v7.extra.compress.archiver.SevenZArchiver;
import cn.hutool.v7.extra.compress.archiver.StreamArchiver;
import cn.hutool.v7.extra.compress.extractor.Extractor;
import cn.hutool.v7.extra.compress.extractor.SevenZExtractor;
import cn.hutool.v7.extra.compress.extractor.StreamExtractor;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.StreamingNotSupportedException;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.function.Function;

/**
 * 压缩工具类<br>
 * 基于commons-compress的压缩解压封装
 *
 * @author Looly
 * @since 5.5.0
 */
public class CompressUtil {

	/**
	 * 获取压缩输出流，用于压缩指定内容，支持的格式例如：
	 * <ul>
	 *     <li>{@value CompressorStreamFactory#GZIP}</li>
	 *     <li>{@value CompressorStreamFactory#BZIP2}</li>
	 *     <li>{@value CompressorStreamFactory#XZ}</li>
	 *     <li>{@value CompressorStreamFactory#PACK200}</li>
	 *     <li>{@value CompressorStreamFactory#SNAPPY_FRAMED}</li>
	 *     <li>{@value CompressorStreamFactory#LZ4_BLOCK}</li>
	 *     <li>{@value CompressorStreamFactory#LZ4_FRAMED}</li>
	 *     <li>{@value CompressorStreamFactory#ZSTANDARD}</li>
	 *     <li>{@value CompressorStreamFactory#DEFLATE}</li>
	 * </ul>
	 *
	 * @param compressorName 压缩名称，见：{@link CompressorStreamFactory}
	 * @param out            输出流，可以输出到内存、网络或文件
	 * @return {@link CompressorOutputStream}
	 */
	public static CompressorOutputStream<?> getOut(final String compressorName, final OutputStream out) {
		try {
			return new CompressorStreamFactory().createCompressorOutputStream(compressorName, out);
		} catch (final CompressorException e) {
			throw new CompressException(e);
		}
	}

	/**
	 * 获取压缩输入流，用于解压缩指定内容，支持的格式例如：
	 * <ul>
	 *     <li>{@value CompressorStreamFactory#GZIP}</li>
	 *     <li>{@value CompressorStreamFactory#BZIP2}</li>
	 *     <li>{@value CompressorStreamFactory#XZ}</li>
	 *     <li>{@value CompressorStreamFactory#PACK200}</li>
	 *     <li>{@value CompressorStreamFactory#SNAPPY_FRAMED}</li>
	 *     <li>{@value CompressorStreamFactory#LZ4_BLOCK}</li>
	 *     <li>{@value CompressorStreamFactory#LZ4_FRAMED}</li>
	 *     <li>{@value CompressorStreamFactory#ZSTANDARD}</li>
	 *     <li>{@value CompressorStreamFactory#DEFLATE}</li>
	 * </ul>
	 *
	 * @param compressorName 压缩名称，见：{@link CompressorStreamFactory}，null表示自动检测
	 * @param in             输出流，可以输出到内存、网络或文件
	 * @return {@link CompressorOutputStream}
	 */
	public static CompressorInputStream getIn(String compressorName, InputStream in) {
		in = IoUtil.toMarkSupport(in);
		try {
			if (StrUtil.isBlank(compressorName)) {
				compressorName = CompressorStreamFactory.detect(in);
			}
			return new CompressorStreamFactory().createCompressorInputStream(compressorName, in);
		} catch (final CompressorException e) {
			throw new CompressException(e);
		}
	}

	/**
	 * 创建归档器，支持：
	 * <ul>
	 *     <li>{@link ArchiveStreamFactory#AR}</li>
	 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
	 *     <li>{@link ArchiveStreamFactory#JAR}</li>
	 *     <li>{@link ArchiveStreamFactory#TAR}</li>
	 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
	 *     <li>{@link ArchiveStreamFactory#SEVEN_Z}</li>
	 * </ul>
	 *
	 * @param charset      编码
	 * @param archiverName 归档类型名称，见{@link ArchiveStreamFactory}
	 * @param file         归档输出的文件
	 * @return Archiver
	 */
	public static Archiver createArchiver(final Charset charset, final String archiverName, final File file) {
		if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(archiverName)) {
			return new SevenZArchiver(file);
		}
		return StreamArchiver.of(charset, archiverName, file);
	}

	/**
	 * 创建归档器，支持：
	 * <ul>
	 *     <li>{@link ArchiveStreamFactory#AR}</li>
	 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
	 *     <li>{@link ArchiveStreamFactory#JAR}</li>
	 *     <li>{@link ArchiveStreamFactory#TAR}</li>
	 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
	 *     <li>{@link ArchiveStreamFactory#SEVEN_Z}</li>
	 * </ul>
	 *
	 * @param charset      编码
	 * @param archiverName 归档类型名称，见{@link ArchiveStreamFactory}
	 * @param out          归档输出的流
	 * @return Archiver
	 */
	public static Archiver createArchiver(final Charset charset, final String archiverName, final OutputStream out) {
		if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(archiverName)) {
			return new SevenZArchiver(out);
		}
		return StreamArchiver.of(charset, archiverName, out);
	}

	/**
	 * 创建归档解包器，支持：
	 * <ul>
	 *     <li>{@link ArchiveStreamFactory#AR}</li>
	 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
	 *     <li>{@link ArchiveStreamFactory#JAR}</li>
	 *     <li>{@link ArchiveStreamFactory#TAR}</li>
	 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
	 *     <li>{@link ArchiveStreamFactory#SEVEN_Z}</li>
	 * </ul>
	 *
	 * @param charset 编码，7z格式此参数无效
	 * @param file    归档文件
	 * @return {@link Extractor}
	 */
	public static Extractor createExtractor(final Charset charset, final File file) {
		return createExtractor(charset, null, file);
	}

	/**
	 * 创建归档解包器，支持：
	 * <ul>
	 *     <li>{@link ArchiveStreamFactory#AR}</li>
	 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
	 *     <li>{@link ArchiveStreamFactory#JAR}</li>
	 *     <li>{@link ArchiveStreamFactory#TAR}</li>
	 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
	 *     <li>{@link ArchiveStreamFactory#SEVEN_Z}</li>
	 * </ul>
	 *
	 * @param charset      编码，7z格式此参数无效
	 * @param archiverName 归档类型名称，见{@link ArchiveStreamFactory}，null表示自动识别
	 * @param file         归档文件
	 * @return {@link Extractor}
	 */
	public static Extractor createExtractor(final Charset charset, String archiverName, final File file) {
		if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(archiverName)) {
			return new SevenZExtractor(file);
		}

		if (StrUtil.isBlank(archiverName)) {
			final String name = file.getName().toLowerCase();
			if (name.endsWith(".tgz")) {
				archiverName = "tgz";
			} else if (name.endsWith(".tar.gz")) {
				archiverName = "tar.gz";
			}
		}
		try {
			return new StreamExtractor(charset, archiverName, file);
		} catch (final CompressException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof StreamingNotSupportedException && cause.getMessage().contains("7z")) {
				return new SevenZExtractor(file);
			}
			throw e;
		}
	}

	/**
	 * 创建归档解包器，支持：
	 * <ul>
	 *     <li>{@link ArchiveStreamFactory#AR}</li>
	 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
	 *     <li>{@link ArchiveStreamFactory#JAR}</li>
	 *     <li>{@link ArchiveStreamFactory#TAR}</li>
	 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
	 *     <li>{@link ArchiveStreamFactory#SEVEN_Z}</li>
	 * </ul>
	 *
	 * @param charset 编码，7z格式此参数无效
	 * @param in      归档输入的流
	 * @return {@link Extractor}
	 */
	public static Extractor createExtractor(final Charset charset, final InputStream in) {
		return createExtractor(charset, null, in);
	}

	/**
	 * 创建归档解包器，支持：
	 * <ul>
	 *     <li>{@link ArchiveStreamFactory#AR}</li>
	 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
	 *     <li>{@link ArchiveStreamFactory#JAR}</li>
	 *     <li>{@link ArchiveStreamFactory#TAR}</li>
	 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
	 *     <li>{@link ArchiveStreamFactory#SEVEN_Z}</li>
	 * </ul>
	 *
	 * @param charset      编码，7z格式此参数无效
	 * @param archiverName 归档类型名称，见{@link ArchiveStreamFactory}，null表示自动识别
	 * @param in           归档输入的流
	 * @return {@link Extractor}
	 */
	public static Extractor createExtractor(final Charset charset, final String archiverName, final InputStream in) {
		if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(archiverName)) {
			return new SevenZExtractor(in);
		}

		try {
			return new StreamExtractor(charset, archiverName, in);
		} catch (final CompressException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof StreamingNotSupportedException && cause.getMessage().contains("7z")) {
				return new SevenZExtractor(in);
			}
			throw e;
		}
	}

	/**
	 * 获取归档条目名
	 *
	 * @param fileName       文件名，包括主名称和扩展名，不包括路径
	 * @param path           路径
	 * @param fileNameEditor 文件名编辑器
	 * @return 归档条目名
	 * @since 7.0.0
	 */
	public static String getEntryName(final String fileName, final String path, final Function<String, String> fileNameEditor) {
		String entryName = (fileNameEditor == null) ? fileName : fileNameEditor.apply(fileName);
		if (StrUtil.isNotEmpty(path)) {
			// 非空拼接路径，格式为：path/name
			entryName = StrUtil.addSuffixIfNot(path, StrUtil.SLASH) + entryName;
		}

		return entryName;
	}
}
