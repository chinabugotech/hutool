package cn.hutool.core.codec;

import cn.hutool.core.util.ArrayUtil;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Base85(Ascii85)编码解码实现<br>
 * 相比Base64，Base85使用更多的可打印字符，编码后体积更小（膨胀率约25%）<br>
 * 支持标准RFC1924风格和Z85(ZMQ)风格
 *
 * @author 红茶
 */
public class Base85Codec implements Encoder<byte[], byte[]>, Decoder<byte[], byte[]>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 每个编码块处理的原始字节数
	 */
	private static final int CHUNK_SIZE = 4;
	/**
	 * 每个编码块输出的字符数
	 */
	private static final int ENCODED_CHUNK_SIZE = 5;

	public static final Base85Codec INSTANCE = new Base85Codec();

	/**
	 * 编码指定消息bytes为Base85格式的bytes
	 *
	 * @param data 被编码的消息
	 * @return Base85内容
	 */
	@Override
	public byte[] encode(byte[] data) {
		return encode(data, false);
	}

	/**
	 * 编码指定消息bytes为Base85格式的bytes
	 *
	 * @param data   被编码的消息
	 * @param useZ85 是否使用Z85风格（ZeroMQ定义的安全字母表，无特殊符号）
	 * @return Base85内容
	 */
	public byte[] encode(byte[] data, boolean useZ85) {
		final Base85Encoder encoder = useZ85 ? Base85Encoder.Z85_ENCODER : Base85Encoder.STANDARD_ENCODER;
		return encoder.encode(data);
	}

	/**
	 * 解码Base85消息
	 *
	 * @param encoded Base85内容
	 * @return 消息
	 */
	@Override
	public byte[] decode(byte[] encoded) {
		return decode(encoded, false);
	}

	/**
	 * 解码Base85消息
	 *
	 * @param encoded Base85内容
	 * @param useZ85  是否使用Z85风格
	 * @return 消息
	 */
	public byte[] decode(byte[] encoded, boolean useZ85) {
		final Base85Decoder decoder = useZ85 ? Base85Decoder.Z85_DECODER : Base85Decoder.STANDARD_DECODER;
		return decoder.decode(encoded);
	}

	/**
	 * Base85编码器
	 *
	 * @since 5.8.x
	 */
	public static class Base85Encoder implements Encoder<byte[], byte[]> {

		/**
		 * 标准Ascii85字母表 (RFC 1924 / Adobe)
		 * 字符范围: '!' (33) ~ 'u' (117)
		 */
		private static final byte[] STANDARD = new byte[85];

		static {
			for (int i = 0; i < 85; i++) {
				STANDARD[i] = (byte) ('!' + i);
			}
		}

		/**
		 * Z85字母表 (ZeroMQ RFC 32)
		 * 排除了引号、反斜杠等对代码不友好的字符
		 */
		private static final byte[] Z85 = { //
			'0', '1', '2', '3', '4', '5', '6', '7', //
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f', //
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', //
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', //
			'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', //
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', //
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', //
			'U', 'V', 'W', 'X', 'Y', 'Z', '.', '-', //
			':', '+', '=', '^', '!', '/', '*', '?', //
			'&', '<', '>', '(', ')', '[', ']', '{', //
			'}', '@', '%', '$', '#' //
		};

		public static final Base85Encoder STANDARD_ENCODER = new Base85Encoder(STANDARD);
		public static final Base85Encoder Z85_ENCODER = new Base85Encoder(Z85);

		private final byte[] alphabet;

		/**
		 * 构造
		 *
		 * @param alphabet 字符表，长度必须为85
		 */
		public Base85Encoder(byte[] alphabet) {
			this.alphabet = alphabet;
		}

		@Override
		public byte[] encode(byte[] data) {
			if (ArrayUtil.isEmpty(data)) {
				return new byte[0];
			}

			final ByteArrayOutputStream out = new ByteArrayOutputStream(estimateOutputLength(data.length));

			// ★ 核心修复：使用 long 代替 int，避免4字节拼接后最高位为1导致负数
			// long 可容纳 0x00000000 ~ 0xFFFFFFFF 的全部无符号范围
			long tuple = 0;
			int count = 0;

			for (byte b : data) {
				tuple = (tuple << 8) | (b & 0xFF);
				count++;

				if (count == CHUNK_SIZE) {
					writeEncodedTuple(out, tuple, ENCODED_CHUNK_SIZE);
					tuple = 0;
					count = 0;
				}
			}

			// 处理尾部不足4字节的情况
			if (count > 0) {
				// 左移补齐到4字节宽度（long下不会溢出）
				tuple <<= (8 * (CHUNK_SIZE - count));
				// 只输出 (count + 1) 个编码字符
				writeEncodedTuple(out, tuple, count + 1);
			}

			return out.toByteArray();
		}

		/**
		 * 将一个32位无符号整数元组编码为Base85字符并写入流
		 * <p>
		 * ★ 核心修复：参数改为 long，保证 % 和 / 运算结果始终非负
		 *
		 * @param out       输出流
		 * @param tuple     32位无符号值（以long存储，范围 0 ~ 4294967295）
		 * @param charCount 需要输出的字符数量（完整块为5，尾部为2~4）
		 */
		private void writeEncodedTuple(ByteArrayOutputStream out, long tuple, int charCount) {
			final byte[] encoded = new byte[ENCODED_CHUNK_SIZE];
			for (int i = ENCODED_CHUNK_SIZE - 1; i >= 0; i--) {
				encoded[i] = alphabet[(int) (tuple % 85)];
				tuple /= 85;
			}
			for (int i = 0; i < charCount; i++) {
				out.write(encoded[i]);
			}
		}

		/**
		 * 估算编码后的输出长度
		 *
		 * @param inputLength 输入长度
		 * @return 估算长度
		 */
		private static int estimateOutputLength(int inputLength) {
			return (int) Math.ceil(inputLength * ENCODED_CHUNK_SIZE / (double) CHUNK_SIZE);
		}
	}

	/**
	 * Base85解码器
	 *
	 * @since 5.8.x
	 */
	public static class Base85Decoder implements Decoder<byte[], byte[]> {

		public static final Base85Decoder STANDARD_DECODER = new Base85Decoder(Base85Encoder.STANDARD);
		public static final Base85Decoder Z85_DECODER = new Base85Decoder(Base85Encoder.Z85);

		private final byte[] lookupTable;

		/**
		 * 构造
		 *
		 * @param alphabet 字母表
		 */
		public Base85Decoder(byte[] alphabet) {
			int maxChar = 0;
			for (byte b : alphabet) {
				maxChar = Math.max(maxChar, b & 0xFF);
			}
			lookupTable = new byte[maxChar + 1];
			Arrays.fill(lookupTable, (byte) -1);
			for (int i = 0; i < alphabet.length; i++) {
				lookupTable[alphabet[i] & 0xFF] = (byte) i;
			}
		}

		@Override
		public byte[] decode(byte[] encoded) {
			if (ArrayUtil.isEmpty(encoded)) {
				return new byte[0];
			}

			final ByteArrayOutputStream out = new ByteArrayOutputStream(estimateDecodedLength(encoded.length));

			long tuple = 0;
			int count = 0;

			for (byte b : encoded) {
				final int idx = b & 0xFF;
				if (idx >= lookupTable.length || lookupTable[idx] == -1) {
					throw new IllegalArgumentException("Invalid Base85 character: '" + (char) b + "'");
				}

				tuple = tuple * 85 + lookupTable[idx];
				count++;

				if (count == ENCODED_CHUNK_SIZE) {
					writeDecodedTuple(out, tuple, CHUNK_SIZE);
					tuple = 0;
					count = 0;
				}
			}

			// 处理尾部不完整块
			if (count > 0) {
				if (count == 1) {
					// 1个Base85字符无法表示任何有效字节，属于非法输入
					throw new IllegalArgumentException("Invalid Base85 encoding: single trailing character");
				}
				// 补齐到5个编码字符对应的数值宽度（用最大值84填充）
				for (int i = count; i < ENCODED_CHUNK_SIZE; i++) {
					tuple = tuple * 85 + 84;
				}
				// 尾部有效原始字节数 = count - 1
				writeDecodedTuple(out, tuple, count - 1);
			}

			return out.toByteArray();
		}

		/**
		 * 将解码后的32位整数写入输出流
		 *
		 * @param out       输出流
		 * @param tuple     解码后的值
		 * @param byteCount 需要写入的有效字节数
		 */
		private static void writeDecodedTuple(ByteArrayOutputStream out, long tuple, int byteCount) {
			for (int i = CHUNK_SIZE - 1; i >= CHUNK_SIZE - byteCount; i--) {
				out.write((int) ((tuple >> (i * 8)) & 0xFF));
			}
		}

		/**
		 * 估算解码后的输出长度
		 *
		 * @param encodedLength 编码数据长度
		 * @return 估算长度
		 */
		private static int estimateDecodedLength(int encodedLength) {
			return (int) Math.ceil(encodedLength * CHUNK_SIZE / (double) ENCODED_CHUNK_SIZE);
		}
	}
}
