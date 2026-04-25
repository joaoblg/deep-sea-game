package pt.isec.pa.deepsea.model.data;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeepSeaLogTest {

    @BeforeEach
    public void setUp() {
        DeepSeaLog.getInstance().limparLogs();
    }

    @Test
    public void testSingletonEAdicaoLog() {
        DeepSeaLog log1 = DeepSeaLog.getInstance();
        DeepSeaLog log2 = DeepSeaLog.getInstance();
        assertSame(log1, log2);

        log1.addLog("Teste de operacao");
        List<String> logs = log1.getLogs();
        
        assertEquals(1, logs.size());
        assertTrue(logs.get(0).contains("Teste de operacao"));
    }
}