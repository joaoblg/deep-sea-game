package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class NavioTeste {
    @Test
    public void testAdicionarDrone() {
        Navio navio = new Navio();
        Drone d1 = new Drone();

        navio.addDrone(d1);

        // Vamos usar a lista ordenada para verificar se o drone está lá dentro
        List<Drone> dronesNoNavio = navio.getDronesOrdenadosPorCasco();
        assertEquals(1, dronesNoNavio.size(), "O navio devia ter exatamente 1 drone.");
        assertTrue(dronesNoNavio.contains(d1), "O drone adicionado devia estar na lista.");
    }

    @Test
    public void testOrdenacaoDronesPorCombustivel() {
        Navio navio = new Navio();
        Drone droneComMuitoCombustivel = new Drone();
        Drone droneComPoucoCombustivel = new Drone();

        // Gastar combustível no segundo drone
        droneComPoucoCombustivel.consumirCombustivel(80);

        // Adicionar ambos ao navio (como é um Set, a ordem de inserção não importa)
        navio.addDrone(droneComMuitoCombustivel);
        navio.addDrone(droneComPoucoCombustivel);

        // Obter a lista ordenada
        List<Drone> dronesOrdenados = navio.getDronesOrdenadosPorCombustivel();

        // O drone com MENOS combustível deve aparecer em primeiro lugar (índice 0)
        assertEquals(droneComPoucoCombustivel, dronesOrdenados.get(0), "O drone com menos combustível deve ser o primeiro da lista.");
        assertEquals(droneComMuitoCombustivel, dronesOrdenados.get(1), "O drone com mais combustível deve ser o segundo da lista.");
    }
}
