package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FundoDoMarTest {
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

        assertTrue(fundo.estaRevelada(0, 0), "No modo defesa atual, o mapa começa visivel.");

        ElemFundo conteudo = fundo.getConteudo(15 / 2, 15 / 2);

        assertNotNull(conteudo, "Deve haver algo no centro do mapa.");

        assertTrue(conteudo instanceof Monstro, "O conteúdo no centro deve ser um Monstro.");

        Monstro m = (Monstro) conteudo;
        assertTrue(m.getId() >= 1001, "O ID do monstro deve ser >= 1001.");
    }
}
