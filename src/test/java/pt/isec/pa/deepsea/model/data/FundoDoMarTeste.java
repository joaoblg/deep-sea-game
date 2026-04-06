package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FundoDoMarTeste {
    @Test
    public void testRevelarCelula() {
        FundoDoMar fundo = new FundoDoMar(10, 10);

        assertFalse(fundo.estaRevelada(5, 5), "A célula deve começar na escuridão (false).");

        fundo.revelar(5, 5);
        assertTrue(fundo.estaRevelada(5, 5), "A célula deve estar revelada (true) após o método ser chamado.");
    }

    @Test
    public void testModoDefesa() {
        FundoDoMar fundo = new FundoDoMar(15, 15);
        fundo.configurarModoDefesa();

        // No modo defesa, a grelha fica toda revelada
        assertTrue(fundo.estaRevelada(0, 0), "O mapa deve ficar todo revelado no modo defesa.");

        // Vai buscar o ID que está no centro da grelha
        int idNoCentro = fundo.getConteudo(Constantes.COMPRIMENTO_FUNDO / 2, Constantes.LARGURA_FUNDO / 2);

        // Verifica se é um ID de monstro válido (>= 1001)
        assertTrue(idNoCentro >= 1001, "Deve haver um monstro (ID >= 1001) no centro do mapa no Modo Defesa.");
    }
}
