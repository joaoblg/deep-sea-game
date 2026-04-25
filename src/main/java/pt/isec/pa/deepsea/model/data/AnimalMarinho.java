package pt.isec.pa.deepsea.model.data;


/**
 * Representa a fauna hostil no fosso marinho.
 */
public class AnimalMarinho extends ObstaculoFosso{
    private static final long serialVersionUID = 1L;


    /**
     * Aplica dano à integridade do casco do drone ao haver contacto.
     * @param droneAtual O drone que sofre a interação.
     */
    @Override
    public void interage(Drone droneAtual){

        droneAtual.sofrerDano();
    }
}
