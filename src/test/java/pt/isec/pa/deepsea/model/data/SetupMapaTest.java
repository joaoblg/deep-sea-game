package pt.isec.pa.deepsea.model.data;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Valida o setup complexo do mapa e a integridade das constantes.
 * @author Grupo 05
 * @version 1.0
 */
public class SetupMapaTest {
    private SuperficieMarinha superficie;

    @BeforeEach
    void setUp() {
        superficie = new SuperficieMarinha();
        superficie.distribuiRecursos();
    }

    @Test
    void testContagemEUnicidadeArtefactos() {
        Set<Integer> idsEncontrados = new HashSet<>();
        int totalArtefactos = 0;

        for (int i = 0; i < superficie.getLinhas(); i++) {
            for (int j = 0; j < superficie.getColunas(); j++) {
                FundoDoMar fundo = superficie.getCelula(i, j).getFundoM();
                for (int fi = 0; fi < Constantes.COMPRIMENTO_FUNDO; fi++) {
                    for (int fj = 0; fj < Constantes.LARGURA_FUNDO; fj++) {
                        ElemFundo conteudo = fundo.getConteudo(fi, fj);
                        if (conteudo instanceof pt.isec.pa.deepsea.model.data.Artefacto) {
                            totalArtefactos++;
                            idsEncontrados.add(((Artefacto) conteudo).getIdArtefacto());
                        }
                    }
                }
            }
        }

        assertEquals(Constantes.NUM_ARTEFACTOS, totalArtefactos, "Número total de artefactos no mapa incorreto.");
        assertEquals(Constantes.NUM_ARTEFACTOS, idsEncontrados.size(), "Existem IDs de artefactos repetidos ou fora do intervalo.");
    }
}