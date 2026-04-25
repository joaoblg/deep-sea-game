package pt.isec.pa.deepsea.model.data;



/**
 * Representa correntes que dificultam a navegação.
 */
public class CorrenteMarinha extends ObstaculoFosso{

    private static final long serialVersionUID = 1L;


    /**
     * Aumenta o consumo de combustível devido à força da corrente.
     * @param droneAtual O drone afetado pela corrente.
     */
    @Override
    public void interage(Drone droneAtual){
        droneAtual.consumirCombustivel(1);
    }
}
