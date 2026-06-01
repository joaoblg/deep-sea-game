package pt.isec.pa.deepsea.model.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.DeepSeaManager;

public class DeepSeaManagerTest {

    private DeepSeaManager manager;

    @BeforeEach
    public void setUp() {
        manager = new DeepSeaManager();
    }

    @Test
    public void testFluxoInicialEMergulho() {

        manager.iniciarJogo();
        assertEquals(DeepSeaState.SUPERFICIE, manager.getState());

        boolean sucessoMergulho = manager.iniciarMergulho(5, 5);
        assertTrue(sucessoMergulho);
        assertEquals(DeepSeaState.FOSSO, manager.getState());
    }
}