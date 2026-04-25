package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;

public abstract class ObstaculoFosso extends ElemFosso {

    private final int id;


    /**
     * Construtor da classe ObstaculoFosso.
     * Invoca o construtor da superclasse e atribui um identificador único
     * sequencial ao obstáculo através do GeradorID.
     */
    public ObstaculoFosso(){
        super();
        this.id=GeradorID.gerar();
    }

}
