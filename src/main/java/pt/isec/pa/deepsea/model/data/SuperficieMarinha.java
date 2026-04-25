package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;
import java.util.Random;

/**
 * Gere a grelha bidimensional que representa a superfície do oceano.
 * Coordena a distribuição inicial de recursos (minérios e artefactos) pelas diversas células.
 */
public class SuperficieMarinha implements Serializable {
    private static final long serialVersionUID = 1L;
    private CelulaSuperficie mapa[][];
    private int nLin;
    private int nCol;

    /** @return Número de linhas da superfície. */
    public int getLinhas(){
        return nLin;
    }

    /** @return Número de colunas da superfície. */
    public int getColunas(){
        return nCol;
    }

    /**
     * Obtém a célula correspondente às coordenadas fornecidas.
     * @param nLinha Índice da linha.
     * @param nColuna Índice da coluna.
     * @return A instância de CelulaSuperficie ou null se as coordenadas forem inválidas.
     */
    public CelulaSuperficie getCelula(int nLinha,int nColuna){
        if (nLinha>=0 && nLinha<nLin && nColuna>=0 && nColuna<nCol ){
            return mapa[nLinha][nColuna];
        }
        return null;
    }

    /**
     * Inicializa a grelha da superfície com as dimensões definidas nas constantes.
     */
    public SuperficieMarinha(){
        this.nLin= Constantes.LARGURA_SUPERFICIE;
        this.nCol= Constantes.COMPRIMENTO_SUPERFICIE;

        this.mapa=new CelulaSuperficie[nLin][nCol];

        for (int i=0; i<nLin; i++){
            for (int j=0; j<nCol;j++){
                mapa[i][j]= new CelulaSuperficie();
            }
        }
    }

    /**
     * Distribui aleatoriamente minérios e artefactos por todas as células do mapa no início do jogo.
     */
    public void distribuiRecursos(){
        Random rand = new Random();

        for (int i = 0; i < nLin; i++){
            for (int j = 0; j < nCol; j++){
                int nMinerios = rand.nextInt(Constantes.MAX_MINERIOS_POR_FUNDO + 1);
                mapa[i][j].getFundoM().gerarMinerios(nMinerios);
            }
        }

        for (int idArtefacto = 1; idArtefacto <= Constantes.NUM_ARTEFACTOS; idArtefacto++){
            int l = rand.nextInt(nLin);
            int c = rand.nextInt(nCol);

            mapa[l][c].getFundoM().colocarArtefacto(idArtefacto);
        }
    }

    /**
     * Verifica se uma coordenada da superfície possui artefactos no seu fundo marinho.
     * @param lin Linha da superfície.
     * @param col Coluna da superfície.
     * @return true se existir um artefacto no fundo desta coordenada.
     */
    public boolean contemArtefacto(int lin, int col){
        CelulaSuperficie loc= getCelula(lin, col);
        if (loc!=null){
            return loc.getFundoM().temAlgumArtefacto();
        }
        return false;
    }
}
