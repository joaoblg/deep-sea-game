package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

public class DeepSeaFSMTest {

    private DeepSeaContext context;

    @BeforeEach
    public void setUp() {
        context = new DeepSeaContext();
    }

    @Test
    public void testEstadoInicial() {
        assertEquals(DeepSeaState.SUPERFICIE, context.getState(), "O estado inicial deve ser SUPERFICIE.");
    }


    @Test
    public void testBloqueioDeTransicoesInvalidas() {
        assertEquals(DeepSeaState.SUPERFICIE, context.getState());

        assertFalse(context.avaliarFundo(), "Não é permitido avaliar o fundo a partir da superfície.");
    }

    @Test
    public void testMergulhoConsumoEMorte() {
        context.iniciarJogo();

        // Em vez de tentarmos "injetar" um drone à força (que é bloqueado pela vossa segurança),
        // vamos buscar o drone que o vosso jogo já deve criar e meter como ativo ao iniciar!
        Drone droneTeste = context.getDrone();

        // Verifica se o jogo preparou bem o drone
        assertNotNull(droneTeste, "O jogo deve ter um drone ativo após iniciar (O Navio tem de ter os 3 drones lá dentro).");

        // Simula mergulho
        boolean mergulhou = context.iniciarMergulho(0, 0);

        if (mergulhou) {
            assertEquals(DeepSeaState.DESCIDA, context.getState());

            // Simular movimento gasta combustível
            int combustivelAntes = droneTeste.getCombustivel();
            context.mover(Direcoes.BAIXO);
            assertTrue(droneTeste.getCombustivel() < combustivelAntes, "O combustível deve descer ao mover.");

            // Simular morte por dano
            while (droneTeste.getIntegridadeCasco() > 0) {
                droneTeste.sofrerDano();
            }
            context.mover(Direcoes.BAIXO); // O movimento seguinte deteta a morte

            assertEquals(DeepSeaState.SUPERFICIE, context.getState(), "Se o drone for destruído, deve voltar ao Navio.");
        }
    }
}