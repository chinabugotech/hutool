package cn.hutool.ai.core;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
class AIConfigRegistryNullTest {
    @Test
    void getConfigClassNullThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> AIConfigRegistry.getConfigClass(null),
            "getConfigClass(null) should throw IllegalArgumentException, not NullPointerException");
    }
}
