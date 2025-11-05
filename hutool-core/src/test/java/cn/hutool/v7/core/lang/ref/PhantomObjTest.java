package cn.hutool.v7.core.lang.ref;

import java.lang.ref.ReferenceQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhantomObjTest {

	private ReferenceQueue<String> queue;
    private String testObject;
    private PhantomObj<String> phantomObj;

    @BeforeEach
    void setUp() {
        queue = new ReferenceQueue<>();
        testObject = "test";
        phantomObj = new PhantomObj<>(testObject, queue);
    }

	@Test
	@DisplayName("测试 equals 方法与不同引用对象比较")
    void testEqualsWithDifferentReferent() {
        String differentObject = "different";
        PhantomObj<String> anotherPhantomObj = new PhantomObj<>(differentObject, queue);
        assertFalse(phantomObj.equals(anotherPhantomObj));
    }
}
