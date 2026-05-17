package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class    DroneTest {
    @Test
    public void testConsumirCombustivel() {
        Drone drone = new Drone();
        int combustivelInicial = drone.getCombustivel();

        drone.consumirCombustivel(20);
        assertEquals(combustivelInicial - 20, drone.getCombustivel(), "O combustível devia ter descido 20 unidades.");

        drone.consumirCombustivel(1000);
        assertEquals(0, drone.getCombustivel(), "O combustível não pode ser inferior a 0.");
    }

    @Test
    void testSofrerDanoPrimos() {
        Drone d = new Drone();
        assertEquals(100, d.getIntegridadeCasco(), "A integridade inicial devia ser 100");

        d.sofrerDano();
        assertEquals(99, d.getIntegridadeCasco(), "A integridade devia ter descido 1 (o primeiro valor é o 1).");

        d.sofrerDano();
        assertEquals(97, d.getIntegridadeCasco(), "A integridade devia ter descido 2 (99 - 2 = 97).");

        d.sofrerDano();
        assertEquals(94, d.getIntegridadeCasco(), "A integridade devia ter descido 3 (97 - 3 = 94).");

        d.sofrerDano();
        assertEquals(89, d.getIntegridadeCasco(), "A integridade devia ter descido 5 (94 - 5 = 89).");
    }
}
