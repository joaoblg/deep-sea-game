package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SuperficieMarinhaTeste {
    @Test
    public void testDimensoesSuperficie() {
        SuperficieMarinha sup = new SuperficieMarinha();

        assertEquals(Constantes.LARGURA_SUPERFICIE, sup.getLinhas(), "As linhas devem corresponder à constante.");
        assertEquals(Constantes.COMPRIMENTO_SUPERFICIE, sup.getColunas(), "As colunas devem corresponder à constante.");
    }

    @Test
    public void testGetCelulaInvalida() {
        SuperficieMarinha sup = new SuperficieMarinha();

        // Tentar ir buscar uma célula fora do mapa (ex: linha -1) tem de devolver null e não dar erro fatal
        assertNull(sup.getCelula(-1, 0), "O método deve devolver null para coordenadas inválidas.");
        assertNull(sup.getCelula(1000, 1000), "O método deve devolver null para coordenadas muito grandes.");
        assertNotNull(sup.getCelula(0, 0), "O método deve devolver uma célula válida para a coordenada 0,0.");
    }
}
