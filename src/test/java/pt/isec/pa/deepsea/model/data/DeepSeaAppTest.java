package pt.isec.pa.deepsea.model.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class DeepSeaAppTest {
    @Test
    void testHello() {
        assertEquals(
                "DeepSea".toLowerCase(),
                "dEEPsEA".toLowerCase()
        );
    }
}