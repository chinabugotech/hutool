package cn.hutool.ai.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AIConfigBuilderExceptionTest {

	@Test
	void unsupportedModelThrowsIllegalArgumentException() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> new AIConfigBuilder("__no_such_model__"),
				"an unsupported model must surface as IllegalArgumentException, not a generic RuntimeException");
		assertTrue(ex.getMessage().contains("Unsupported model"),
				"message should name the unsupported model");
	}
}
