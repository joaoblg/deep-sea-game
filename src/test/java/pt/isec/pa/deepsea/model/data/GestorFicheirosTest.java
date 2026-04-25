package pt.isec.pa.deepsea.model.data;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import pt.isec.pa.deepsea.model.Jogo;

public class GestorFicheirosTest {

    private final String FICHEIRO_TESTE = "teste_savegame.dat";

    @Test
    public void testGravarECarregarJogo() {
        Jogo jogoOriginal = new Jogo();
        boolean gravado = GestorFicheiros.gravarJogo(jogoOriginal, FICHEIRO_TESTE);
        assertTrue(gravado);

        Jogo jogoCarregado = GestorFicheiros.carregarJogo(FICHEIRO_TESTE);
        assertNotNull(jogoCarregado);
    }

    @AfterEach
    public void limparFicheiro() {
        File ficheiro = new File(FICHEIRO_TESTE);
        if (ficheiro.exists()) {
            ficheiro.delete();
        }
    }
}