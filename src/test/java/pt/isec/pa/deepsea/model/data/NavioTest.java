package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class NavioTest {
    @Test
    public void testAdicionarDrone() {
        Navio navio = new Navio();
        Drone d1 = new Drone();

        navio.addDrone(d1);

        List<Drone> dronesNoNavio = navio.getDronesOrdenadosPorCasco();
        assertEquals(1, dronesNoNavio.size(), "O navio devia ter exatamente 1 drone.");
        assertTrue(dronesNoNavio.contains(d1), "O drone adicionado devia estar na lista.");
    }

    @Test
    public void testOrdenacaoDronesPorCombustivel() {
        Navio navio = new Navio();
        Drone droneComMuitoCombustivel = new Drone();
        Drone droneComPoucoCombustivel = new Drone();

        droneComPoucoCombustivel.consumirCombustivel(80);

        navio.addDrone(droneComMuitoCombustivel);
        navio.addDrone(droneComPoucoCombustivel);

        List<Drone> dronesOrdenados = navio.getDronesOrdenadosPorCombustivel();

        assertEquals(droneComPoucoCombustivel, dronesOrdenados.get(0), "O drone com menos combustível deve ser o primeiro da lista.");
        assertEquals(droneComMuitoCombustivel, dronesOrdenados.get(1), "O drone com mais combustível deve ser o segundo da lista.");
    }
}
