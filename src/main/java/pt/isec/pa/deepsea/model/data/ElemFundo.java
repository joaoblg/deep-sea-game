package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;


/**
 * Classe abstrata que serve de base para todos os elementos presentes no fundo do mar.
 * Garante que todos os objetos (minérios, artefactos, monstros) possam interagir com o drone.
 */
public abstract class ElemFundo implements Serializable {

    private static final long serialVersionUID=1L;


    /**
     * Construtor protegido para as subclasses de ElementoFundo.
     */
    protected ElemFundo(){
        super();
    }


    /**
     * Define o comportamento e as consequências da interação do drone com este elemento.
     * @param droneAtual O drone que realiza a interação.
     */
    public abstract void interage(Drone droneAtual);

}
