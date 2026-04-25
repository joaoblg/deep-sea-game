package pt.isec.pa.deepsea.model.data;


/**
 * Representa um artefacto antigo de uma civilização perdida.
 * É o objetivo principal da missão e a sua recolha despoleta o Puzzle de Recuperação.
 */
public class Artefacto extends ElemFundo {
    private static final long serialVersionUID=1L;

    private final int idArtefacto;

    /**
     * Cria um artefacto com um identificador específico.
     * @param id O número do artefacto (entre 1 e NUM_ARTEFACTOS).
     */
    public Artefacto(int id){
        super();
        this.idArtefacto=id;
    }


    /**
     * @return O identificador único deste artefacto.
     */
    public int getIdArtefacto(){
        return idArtefacto;
    }

    /**
     * Tenta iniciar o processo de recuperação do artefacto através de um minijogo.
     * @param droneAtual O drone que tenta recuperar o artefacto.
     */
    @Override
    public void interage(Drone droneAtual){

    }

}
