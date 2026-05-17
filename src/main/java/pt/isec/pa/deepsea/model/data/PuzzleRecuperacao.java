package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;
import java.util.Random;

/**
 * Gere o estado do minijogo de recuperação de artefactos (Puzzle deslizante 4x4).
 * Controla a disposição das peças, a contagem de movimentos e a validação da vitória.
 */
public class PuzzleRecuperacao implements Serializable {
    private static final long serialVersionUID = 1L;
    private int[][] grelha;
    private int movimentos;
    private static final int TAMANHO = 4;

    /**
     * Inicializa o puzzle e coloca as peças na posição final correta para posterior baralhamento.
     */
    public PuzzleRecuperacao() {
        this.grelha = new int[TAMANHO][TAMANHO];
        this.movimentos = 0;
        inicializar();
        baralhar();
    }

    /**
     * Preenche a grelha com números de 1 a 15 e define a última posição como vazia (0).
     */
    private void inicializar() {
        int cont = 1;
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (i == TAMANHO - 1 && j == TAMANHO - 1) {
                    grelha[i][j] = 0;
                } else {
                    grelha[i][j] = cont++;
                }
            }
        }
    }

    /**
     * Baralha o puzzle X vezes (onde X é uma ocnstante) a partir do puzzle resolvido, usando movimentos aleatorios.
     * Com isto garante-se que o puzzle é solúvel 100% das vezes
     */
    public void baralhar(){
        Random rand= new Random();

        for (int i=0; i< Constantes.NUM_BARALHAR;i++){
            int linVazia=-1;
            int colVazia=-1;

            for (int lin=0; lin<TAMANHO;lin++){
                for(int col=0; col<TAMANHO;col++){
                    if (grelha[lin][col]==0){
                        linVazia=lin;
                        colVazia=col;
                    }
                }
            }

            int dir=rand.nextInt(4);
            int newLin= linVazia;
            int newCol= colVazia;

            switch (dir){
                case 0->newLin--;
                case 1->newCol--;
                case 2->newLin++;
                case 3->newCol++;
            }

            if (newLin>=0 && newLin<TAMANHO && newCol>=0 && newCol<TAMANHO){
                grelha[linVazia][colVazia]=grelha[newLin][newCol];
                grelha[newLin][newCol] = 0;
            }

        }
        this.movimentos=0;


    }

    /**
     * Tenta mover uma peça para o espaço vazio.
     * Verifica a adjacência e o limite máximo de movimentos.
     * @param lin Linha da peça a mover.
     * @param col Coluna da peça a mover.
     * @return true se o movimento foi bem-sucedido.
     */
    public boolean moverPeca(int lin, int col) {
        if (movimentos >= Constantes.MAX_MOV_PUZZLE) {
            return false;
        }

        int linZero = -1, colZero = -1;
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (grelha[i][j] == 0) {
                    linZero = i;
                    colZero = j;
                }
            }
        }

        if (Math.abs(lin - linZero) + Math.abs(col - colZero) == 1) {
            grelha[linZero][colZero] = grelha[lin][col];
            grelha[lin][col] = 0;
            movimentos++;
            return true;
        }
        return false;
    }


    /**
     * Tenta mover uma peça na direção indicada em direção ao espaço vazio.
     * @param direcao A direção para onde a PEÇA se deve deslocar.
     * @return true se o movimento foi possível.
     */
    public boolean moverPeca(Direcoes direcao) {
        if (movimentos >= Constantes.MAX_MOV_PUZZLE) {
            return false;
        }

        int linZero = -1, colZero = -1;
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (grelha[i][j] == 0) {
                    linZero = i;
                    colZero = j;
                }
            }
        }


        int targetLin = linZero;
        int targetCol = colZero;

        switch (direcao) {
            case CIMA -> targetLin = linZero + 1;
            case BAIXO -> targetLin = linZero - 1;
            case ESQUERDA -> targetCol = colZero + 1;
            case DIREITA -> targetCol = colZero - 1;
        }


        if (targetLin >= 0 && targetLin < TAMANHO && targetCol >= 0 && targetCol < TAMANHO) {
            return moverPeca(targetLin, targetCol);
        }

        return false;
    }



    /**
     * Verifica se todas as peças estão ordenadas de 1 a 15.
     * @return true se o puzzle estiver resolvido.
     */
    public boolean verificarVitoria() {
        int cont = 1;
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (i == TAMANHO - 1 && j == TAMANHO - 1) {
                    return grelha[i][j] == 0;
                }
                if (grelha[i][j] != cont++) {
                    return false;
                }
            }
        }
        return true;
    }

    /** @return Número de movimentos realizados pelo jogador. */
    public int getMovimentos() {
        return movimentos;
    }
    
    public int[][] getGrelha() {
        return grelha;
    }
}