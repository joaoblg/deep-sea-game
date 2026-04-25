package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;

/**
 * Representa uma celula da superficie marinha.
 * Agrega as grelhas subaquaticas correspondentes a esta coordenada.
 */
public class CelulaSuperficie implements Serializable {
    private static final long serialVersionUID = 1L;
    private FossoMarinho fosso;
    private FundoDoMar fundoM;


    /**
     * Inicializa a celula e gera as respetivas instancias de fosso e fundo do mar.
     */
    public CelulaSuperficie(){
        this.fosso=new FossoMarinho(Constantes.LARGURA_FOSSO, Constantes.COMPRIMENTO_FOSSO);
        this.fundoM=new FundoDoMar(Constantes.LARGURA_FUNDO, Constantes.COMPRIMENTO_FUNDO);

    }
    /**
     * Devolve o fosso marinho associado.
     * @return instancia de FossoMarinho.
     */
    public FossoMarinho getFossoM(){
        return fosso;
    }

    /**
     * Devolve o fundo do mar associado.
     * @return instancia de FundoDoMar.
     */
    public FundoDoMar getFundoM(){
        return fundoM;
    }

}
