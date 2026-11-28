package cn.hutool.core.io;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BOMInputStreamTest {

	@ParameterizedTest(name = "{0}, payload={1}, chunk={2}")
	@MethodSource("bomInputs")
	void detectsCompleteBomAcrossShortReads(String charset, boolean withPayload, int chunkSize,
			byte[] input, byte[] expected) throws IOException {
		try (BOMInputStream stream = new BOMInputStream(shortReads(input, chunkSize), "ISO-8859-1")) {
			assertEquals(charset, stream.getCharset());
			assertEquals(charset, stream.getCharset());
			assertArrayEquals(expected, IoUtil.readBytes(stream, false));
		}
	}

	@ParameterizedTest(name = "prefix={0}, chunk={1}")
	@MethodSource("plainInputs")
	void preservesBytesWithoutCompleteBom(String name, int chunkSize, byte[] input) throws IOException {
		try (BOMInputStream stream = new BOMInputStream(shortReads(input, chunkSize), "ISO-8859-1")) {
			assertEquals("ISO-8859-1", stream.getCharset());
			assertArrayEquals(input, IoUtil.readBytes(stream, false));
		}
	}

	private static Stream<Arguments> bomInputs() {
		return Stream.of(
				new Object[]{"UTF-8", new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}},
				new Object[]{"UTF-16BE", new byte[]{(byte) 0xFE, (byte) 0xFF}},
				new Object[]{"UTF-16LE", new byte[]{(byte) 0xFF, (byte) 0xFE}},
				new Object[]{"UTF-32BE", new byte[]{0, 0, (byte) 0xFE, (byte) 0xFF}},
				new Object[]{"UTF-32LE", new byte[]{(byte) 0xFF, (byte) 0xFE, 0, 0}}
		).flatMap(bom -> Stream.of(false, true).flatMap(withPayload -> IntStream.rangeClosed(1, 4)
				.mapToObj(chunkSize -> {
					byte[] prefix = (byte[]) bom[1];
					byte[] payload = withPayload ? new byte[]{65, 66, 67, 68, 69} : new byte[0];
					byte[] input = Arrays.copyOf(prefix, prefix.length + payload.length);
					System.arraycopy(payload, 0, input, prefix.length, payload.length);
					return Arguments.of(bom[0], withPayload, chunkSize, input, payload);
				})));
	}

	private static Stream<Arguments> plainInputs() {
		return Stream.of(
				new byte[0], new byte[]{65}, new byte[]{65, 66, 67, 68, 69},
				new byte[]{(byte) 0xEF}, new byte[]{(byte) 0xEF, (byte) 0xBB},
				new byte[]{(byte) 0xFE}, new byte[]{(byte) 0xFF},
				new byte[]{0, 0, (byte) 0xFE}
		).flatMap(input -> IntStream.rangeClosed(1, 4)
				.mapToObj(chunkSize -> Arguments.of(Arrays.toString(input), chunkSize, input)));
	}

	private static InputStream shortReads(byte[] input, int chunkSize) {
		return new ByteArrayInputStream(input) {
			@Override
			public synchronized int read(byte[] bytes, int offset, int length) {
				return super.read(bytes, offset, Math.min(length, chunkSize));
			}
		};
	}
}
