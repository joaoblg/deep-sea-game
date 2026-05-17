package pt.isec.pa.deepsea.ui.gui;

import pt.isec.pa.deepsea.model.data.Direcoes;

/**
 * Utilitário para converter cliques em grelhas em direções de movimento.
 */
public final class GridMouseHelper {

    private GridMouseHelper() {
    }

    /**
     * Calcula a direção de um passo entre duas células adjacentes.
     * @return Direção ou {@code null} se as células não forem vizinhas ortogonalmente.
     */
    public static Direcoes direcaoEntreCelulas(int origemLin, int origemCol, int destLin, int destCol) {
        if (destLin == origemLin - 1 && destCol == origemCol) {
            return Direcoes.CIMA;
        }
        if (destLin == origemLin + 1 && destCol == origemCol) {
            return Direcoes.BAIXO;
        }
        if (destLin == origemLin && destCol == origemCol - 1) {
            return Direcoes.ESQUERDA;
        }
        if (destLin == origemLin && destCol == origemCol + 1) {
            return Direcoes.DIREITA;
        }
        return null;
    }

    /**
     * Direção para deslizar uma peça do puzzle clicada em direção ao espaço vazio.
     */
    public static Direcoes direcaoPuzzle(int linhaVazia, int colunaVazia, int linhaPeca, int colunaPeca) {
        if (linhaPeca == linhaVazia + 1 && colunaPeca == colunaVazia) {
            return Direcoes.CIMA;
        }
        if (linhaPeca == linhaVazia - 1 && colunaPeca == colunaVazia) {
            return Direcoes.BAIXO;
        }
        if (linhaPeca == linhaVazia && colunaPeca == colunaVazia + 1) {
            return Direcoes.ESQUERDA;
        }
        if (linhaPeca == linhaVazia && colunaPeca == colunaVazia - 1) {
            return Direcoes.DIREITA;
        }
        return null;
    }

    /**
     * Converte coordenadas de rato numa grelha rectangular.
     */
    public static int[] celulaDoClique(double mouseX, double mouseY, double largura, double altura,
                                       int linhas, int colunas) {
        if (linhas <= 0 || colunas <= 0 || largura <= 0 || altura <= 0) {
            return null;
        }
        int col = (int) (mouseX / (largura / colunas));
        int lin = (int) (mouseY / (altura / linhas));
        if (lin < 0 || lin >= linhas || col < 0 || col >= colunas) {
            return null;
        }
        return new int[]{lin, col};
    }
}
