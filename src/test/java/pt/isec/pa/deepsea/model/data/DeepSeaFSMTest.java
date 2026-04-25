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
    public void testEstadoInicialETransicaoParaNavio() {
        assertEquals(DeepSeaState.SETUP, context.getState(), "O estado inicial deve ser SETUP.");

        boolean iniciou = context.iniciarJogo();

        assertTrue(iniciou, "A transição iniciarJogo() deve ser permitida no SETUP.");
        assertEquals(DeepSeaState.NAVIO, context.getState(), "O estado deve ter mudado para NAVIO.");
    }

    @Test
    public void testBloqueioDeTransicoesInvalidas() {
        context.iniciarJogo();
        assertEquals(DeepSeaState.NAVIO, context.getState());

        // Tentar avaliar o fundo estando no Navio tem de ser bloqueado!
        assertFalse(context.avaliarFundo(), "Não é permitido avaliar o fundo a partir do Navio.");

        // Tentar jogar o puzzle estando no Navio tem de ser bloqueado!
        assertFalse(context.jogarPuzzle(Direcoes.CIMA), "Não é permitido jogar o puzzle no Navio.");

        // O estado deve manter-se intacto
        assertEquals(DeepSeaState.NAVIO, context.getState(), "O estado não se pode alterar após ações inválidas.");
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

            assertEquals(DeepSeaState.NAVIO, context.getState(), "Se o drone for destruído, deve voltar ao Navio.");
        }
    }
}