package pt.isec.pa.deepsea.model.data;


/**
 * Representa uma criatura hostil que habita o fundo do mar.
 * Os monstros surgem em áreas não exploradas e causam danos à integridade do casco do drone. [cite: 66, 96]
 */
public class Monstro extends ElemFundo {
    private final int id;


    /**
     * Construtor da classe Monstro.
     * Atribui um identificador único sequencial partilhado com os obstáculos do fosso.
     */
    public Monstro(){
        super();
        this.id=GeradorID.gerar();
    }

    /**
     * @return O identificador único do monstro.
     */
    public int getId() {
        return id;
    }


    /**
     * Aplica uma penalização na integridade do casco do drone após um encontro hostil. [cite: 66]
     * @param droneAtual O drone que sofre o dano do monstro.
     */
    @Override
    public void interage(Drone droneAtual) {

        droneAtual.sofrerDano();
    }
}
