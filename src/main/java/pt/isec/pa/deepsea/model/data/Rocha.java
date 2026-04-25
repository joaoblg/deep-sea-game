package pt.isec.pa.deepsea.model.data;

public class Rocha extends ElemFosso{
    private static final long serialVersionUID = 1L;



    @Override
    public void interage(Drone droneAtual) {
        droneAtual.sofrerDano();
    }
}
