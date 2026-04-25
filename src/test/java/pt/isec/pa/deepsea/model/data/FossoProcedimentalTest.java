package pt.isec.pa.deepsea.model.data;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class FossoProcedimentalTest {

    @Test
    void testRegrasGeracaoRochas() {
        FossoMarinho fosso = new FossoMarinho(Constantes.LARGURA_FOSSO, Constantes.COMPRIMENTO_FOSSO);

        for (int i = 0; i < Constantes.COMPRIMENTO_FOSSO; i++) {
            int totalRochasNaLinha = 0;

            // Conta *todas* as rochas reais que estão na linha, sem ligar a margens
            for (int j = 0; j < Constantes.LARGURA_FOSSO; j++) {
                if (fosso.getCont(i, j) instanceof Rocha) {
                    totalRochasNaLinha++;
                }
            }

            // A regra de ouro, simples e direta
            assertTrue(totalRochasNaLinha <= (Constantes.LARGURA_FOSSO / 2.0),
                    "Linha " + i + " excede 50% de rochas. Encontrou " + totalRochasNaLinha + " rochas.");
        }
    }
}