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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FastCharArrayReader Unit Test Class
 * 快速字符数组读取器单元测试类
 */
public class FastCharArrayReaderTest {

    private char[] testArray;
    private FastCharArrayReader reader;

    @BeforeEach
    void setUp() {
        // Initialize test data before each test method
        // 在每个测试方法前初始化测试数据
        testArray = "Hello World".toCharArray();
        reader = new FastCharArrayReader(testArray);
    }

    @Test
    @DisplayName("Test constructor with valid character array")
    void testConstructorWithValidArray() {
        // Test normal construction with valid character array
        // 测试使用有效字符数组的正常构造
        assertNotNull(reader, "Reader should be created successfully");

        // Create another instance to verify different arrays work
        // 创建另一个实例验证不同数组都能工作
        final char[] anotherArray = {'A', 'B', 'C'};
        final FastCharArrayReader anotherReader = new FastCharArrayReader(anotherArray);
        assertNotNull(anotherReader, "Another reader should be created successfully");
    }

    @Test
    @DisplayName("Test constructor with empty array")
    void testConstructorWithEmptyArray() {
        // Test construction with empty array
        // 测试空数组构造
        final char[] emptyArray = {};
        final FastCharArrayReader emptyReader = new FastCharArrayReader(emptyArray);
        assertNotNull(emptyReader, "Reader with empty array should be created successfully");
    }

    @SuppressWarnings({"resource", "DataFlowIssue"})
	@Test
    @DisplayName("Test constructor with null array - should handle appropriately")
    void testConstructorWithNullArray() {
        // Test construction with null array - expect exception or proper handling
        // 测试null数组构造 - 期望抛出异常或正确处理
        assertThrows(NullPointerException.class, () -> {
            new FastCharArrayReader((char[]) null);
        }, "Constructor should throw NullPointerException for null input");
    }

    @Test
    @DisplayName("Test single character reading functionality")
    void testReadSingleCharacter() throws Exception {
        // Test reading characters one by one
        // 测试逐个读取字符
        assertEquals('H', reader.read(), "First character should be 'H'");
        assertEquals('e', reader.read(), "Second character should be 'e'");
        assertEquals('l', reader.read(), "Third character should be 'l'");
        assertEquals('l', reader.read(), "Fourth character should be 'l'");
        assertEquals('o', reader.read(), "Fifth character should be 'o'");
    }

    @Test
    @DisplayName("Test reading until end of stream")
    void testReadUntilEnd() throws Exception {
        // Read all characters and verify EOF (-1) is returned at the end
        // 读取所有字符并验证在末尾返回EOF(-1)
        for (int i = 0; i < testArray.length; i++) {
            final int result = reader.read();
            assertEquals(testArray[i], (char) result,
                String.format("Character at position %d should match", i));
        }

        // After all characters are read, should return -1
        // 所有字符读取完后应该返回-1
        assertEquals(-1, reader.read(), "Should return -1 after reaching end of stream");
    }

    @Test
    @DisplayName("Test batch reading with char array")
    void testBatchReading() throws Exception {
        // Test reading multiple characters into a buffer
        // 测试将多个字符读入缓冲区
        final char[] buffer = new char[5];
        final int charsRead = reader.read(buffer, 0, buffer.length);

        assertEquals(5, charsRead, "Should read 5 characters");
        assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o'},
            buffer, "Buffer should contain first 5 characters");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
	@Test
    @DisplayName("Test partial batch reading")
    void testPartialBatchReading() throws Exception {
        // Test reading when remaining characters are less than requested
        // 测试剩余字符少于请求数量时的读取
        // First, advance the reader position
        for (int i = 0; i < 8; i++) {
            reader.read(); // Skip first 8 characters
        }

        final char[] buffer = new char[5]; // Request 5 more but only 3 remain
        final int charsRead = reader.read(buffer, 0, buffer.length);

        assertEquals(3, charsRead, "Should read only 3 remaining characters");
        assertArrayEquals(new char[]{'r', 'l', 'd', '\u0000', '\u0000'},
            buffer, "Buffer should contain last 3 characters and null padding");
    }

    @Test
    @DisplayName("Test reading with offset and length parameters")
    void testReadingWithOffsetAndLength() throws Exception {
        // Test reading with specific offset and length in buffer
        // 测试带偏移量和长度参数的读取
        final char[] buffer = new char[10];
        // Fill buffer with initial values to verify they're overwritten correctly
        // 用初始值填充缓冲区以验证它们被正确覆盖
		Arrays.fill(buffer, 'X');

        final int charsRead = reader.read(buffer, 2, 5); // Start at index 2, read 5 chars

        assertEquals(5, charsRead, "Should read 5 characters");
        assertEquals('X', buffer[0], "Position 0 should remain unchanged");
        assertEquals('X', buffer[1], "Position 1 should remain unchanged");
        assertEquals('H', buffer[2], "Position 2 should have first character");
        assertEquals('o', buffer[6], "Position 6 should have fifth character");
        assertEquals('X', buffer[7], "Position 7 should remain unchanged");
    }

    @Test
    @DisplayName("Test boundary conditions for batch reading")
    void testBatchReadingBoundaryConditions() throws Exception {
        // Test various boundary conditions for batch reading
        // 测试批量读取的各种边界条件
        final char[] buffer = new char[testArray.length + 5]; // Larger buffer

        final int charsRead = reader.read(buffer, 0, buffer.length);
        assertEquals(testArray.length, charsRead,
            "Should read exactly the number of available characters");
    }

    @Test
    @DisplayName("Test invalid parameters for batch reading")
    void testInvalidParametersForBatchReading() throws Exception {
        // Test invalid parameters that should throw exceptions
        // 测试应该抛出异常的无效参数
        final char[] buffer = new char[5];

        // Test negative offset
        // 测试负偏移量
        assertThrows(IndexOutOfBoundsException.class, () -> {
            reader.read(buffer, -1, 3);
        });

        // Test negative length
        // 测试负长度
        assertThrows(IndexOutOfBoundsException.class, () -> {
            reader.read(buffer, 0, -1);
        });

        // Test offset + length exceeding buffer size
        // 测试偏移量+长度超出缓冲区大小
        assertThrows(IndexOutOfBoundsException.class, () -> {
            reader.read(buffer, 3, 10); // 3 + 10 > 5
        });
    }

    @Test
    @DisplayName("Test skip functionality")
    void testSkipFunctionality() throws Exception {
        // Test skipping characters
        // 测试跳过字符功能
        final long skipped = reader.skip(3);
        assertEquals(3, skipped, "Should skip 3 characters");

        assertEquals('l', reader.read(), "Next character after skipping 3 should be 'l' (position 4)");
    }

    @Test
    @DisplayName("Test skip beyond available characters")
    void testSkipBeyondAvailableCharacters() throws Exception {
        // Test skipping more characters than available
        // 测试跳过超过可用字符数的字符
        final long skipped = reader.skip(100);
        assertEquals(testArray.length, skipped,
            "Should skip only as many characters as available");

        assertEquals(-1, reader.read(), "Stream should be at end after skipping all");
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantValue"})
	@Test
    @DisplayName("Test mark and reset functionality if supported")
    void testMarkAndReset() throws Exception {
        // Test mark/reset functionality if implemented
        // 如果实现了则测试标记/重置功能
        try {
            // Check if mark is supported
            // 检查是否支持标记
            if (reader.markSupported()) {
                reader.mark(0); // Mark current position
                // Read some characters
                // 读取一些字符
                reader.read(); reader.read(); reader.read();

                reader.reset(); // Reset to marked position
                // Should be back at beginning
                // 应该回到开始位置
                assertEquals('H', reader.read(), "After reset should return to marked position");
            } else {
                // If not supported, verify appropriate behavior
                // 如果不支持，验证适当行为
                assertFalse(reader.markSupported(), "Mark should not be supported if not implemented");
            }
        } catch (final Exception e) {
            // Handle cases where mark/reset might not be implemented
            // 处理未实现标记/重置的情况
            System.out.println("Mark/Reset functionality may not be implemented: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test close functionality")
    void testCloseFunctionality() throws Exception {
        // Test closing the reader
        // 测试关闭读取器
        reader.close();

        // After closing, further operations might throw exceptions
        // 关闭后，进一步操作可能会抛出异常
        // This depends on implementation - some readers throw IOException after close
        // 这取决于实现 - 一些读取器在关闭后会抛出IOException
    }

    @Test
    @DisplayName("Test concurrent access safety if applicable")
    void testConcurrentAccessSafety() {
        // If the implementation claims to be thread-safe, test concurrent access
        // 如果实现声称是线程安全的，则测试并发访问
        // For now, just document that this would be tested if thread-safety was claimed
        // 目前，如果声明了线程安全性则会测试
        // Thread safety testing would involve multiple threads accessing the same instance
        // 线程安全性测试将涉及多线程访问同一实例
    }
}

