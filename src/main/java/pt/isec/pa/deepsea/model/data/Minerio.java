package pt.isec.pa.deepsea.model.data;

/**
 * Representa um depósito de minérios no fundo do mar.
 * Contém uma quantidade específica de minérios que pode ser recolhida pelo drone.
 */
public class Minerio extends ElemFundo{

    private static final long serialVersionUID=1L;
    private int quantidade;


    /**
     * Inicializa um novo depósito de minério com uma quantidade definida.
     * @param quantidade Unidades de minério disponíveis.
     */
    public Minerio(int quantidade){
        this.quantidade=quantidade;
    }

    /**
     * @return A quantidade de minério ainda disponível neste depósito.
     */
    public int getQuantidade(){
        return quantidade;
    }


    /**
     * Realiza a recolha do minério pelo drone, aplicando as regras de consumo extra de combustível.
     * @param droneAtual O drone que tenta minerar o depósito.
     */
    @Override
    public void interage(Drone droneAtual) {
        droneAtual.adicionarMinerios(this.quantidade);
        this.quantidade = 0;
    }


}
