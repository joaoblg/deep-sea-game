package pt.isec.pa.deepsea.model.data;


import java.io.Serializable;

public abstract class ElemFosso implements Serializable {

    private static final long serialVersionUID=1L;

    protected ElemFosso(){

    }



    /**
     * Método abstrato que define o que acontece quando o drone
     * entra ou bate nesta célula.
     */
    public abstract void interage(Drone droneAtual);


}
