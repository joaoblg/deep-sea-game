package pt.isec.pa.deepsea;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeepSeaAppTest {
    @Test
    void testHello() {
        assertEquals(
                "DeepSea".toLowerCase(),
                "dEEPsEA".toLowerCase()
        );
    }
}