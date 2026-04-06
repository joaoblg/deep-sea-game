package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;
import java.util.Random;

/**
 * Gere a grelha do fundo do mar para exploração.
 * Controla a visibilidade (fog of war), minérios, monstros e artefactos.
 */
public class FundoDoMar implements Serializable {
    private static final long serialVersionUID = 1L;
    private int largura, altura;
    private int[][] grelha; // 0: vazio, 1: minerio, 2: artefacto
    private int[][] quanMinerios;
    private boolean[][] revelada;

    /**
     * Inicializa a grelha do fundo e o sistema de visibilidade.
     *
     * @param largura Colunas da grelha.
     * @param altura  Linhas da grelha.
     */
    public FundoDoMar(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
        this.grelha = new int[altura][largura];
        this.quanMinerios = new int[altura][largura];
        this.revelada = new boolean[altura][largura];

    }

    /**
     * Distribui minérios na grelha inicial (usa -1 para não chocar com os IDs dos artefactos).
     */
    public void gerarMinerios(int numMinerios) {
        Random rd = new Random();
        int minColocados = 0;

        while (minColocados < numMinerios) {
            int l = rd.nextInt(altura);
            int c = rd.nextInt(largura);

            if (grelha[l][c] == 0) {
                grelha[l][c] = -1; // -1 representa Minério!
                quanMinerios[l][c] = rd.nextInt(10) + 1;
                minColocados++;
            }
        }
    }

    /**
     * Revela uma célula da grelha (retira a escuridão).
     *
     * @param l Linha.
     * @param c Coluna.
     */
    public void revelar(int l, int c) {
        if (l >= 0 && l < altura && c >= 0 && c < largura) {
            revelada[l][c] = true;
        }
    }

    /**
     * @return true se a célula já foi visitada/revelada.
     */
    public boolean estaRevelada(int l, int c) {
        return revelada[l][c];
    }

    /**
     * @return ID do conteúdo da célula.
     */
    public int getConteudo(int l, int c) {
        return grelha[l][c];
    }

    /**
     * Adiciona artefactos para o modo defesa em posições vagas com IDs únicos.
     *
     * @param idInicial  O primeiro ID a ser atribuído.
     * @param quantidade Número de artefactos a adicionar.
     */
    public void addArtefactosDefesa(int idInicial, int quantidade) {
        int colocados = 0;
        int idAtual = idInicial; // Começa a contar a partir do ID que lhe passarmos

        for (int i = 0; i < altura && colocados < quantidade; i++) {
            for (int j = 0; j < largura && colocados < quantidade; j++) {

                if (grelha[i][j] == 0) {
                    grelha[i][j] = idAtual; // Coloca o ID único (ex: 1, 2, 3...)
                    idAtual++; // Prepara o próximo ID
                    colocados++;
                }
            }
        }
    }

    /**
     * Gera monstros em células não reveladas e vazias.
     */
    public void gerarMonstros() {
        Random rd = new Random();
        int numMonstros = rd.nextInt(Constantes.NUM_MAX_MONSTROS + 1);
        int colocados = 0;
        int tentativas = 0;

        while (colocados < numMonstros && tentativas < 100) {
            int l = rd.nextInt(altura);
            int c = rd.nextInt(largura);

            if (!revelada[l][c] && grelha[l][c] == 0) {
                grelha[l][c] = GeradorID.gerar();
                colocados++;
            }
            tentativas++;
        }
    }

    /**
     * Configura o fundo do mar para o modo defesa (todo revelado e recursos fixos).
     */
    public void configurarModoDefesa() {
        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {
                revelada[i][j] = true;
                grelha[i][j] = 0;
                quanMinerios[i][j] = 0;
            }
        }

        // Um monstro (usando o teu GeradorID)
        grelha[altura / 2][largura / 2] = GeradorID.gerar();

        // Minério 1: Quantidade 1 (Agora com ID -1 para não chocar com artefactos!)
        grelha[1][1] = -1;
        quanMinerios[1][1] = 1;

        // Minério 2: Quantidade Máxima (Agora com ID -1)
        grelha[1][2] = -1;
        quanMinerios[1][2] = 50;
    }

    /**
     * Coloca um artefacto específico com o seu ID único.
     */
    public void colocarArtefacto(int idArtefacto) {
        Random rd = new Random();
        boolean colocado = false;

        while (!colocado) {
            int l = rd.nextInt(altura);
            int c = rd.nextInt(largura);
            if (grelha[l][c] == 0) {
                grelha[l][c] = idArtefacto; // Guarda o ID único (ex: 1, 2, 3...)
                colocado = true;
            }
        }
    }

    /**
     * Verifica se existe pelo menos um artefacto nesta grelha.
     */
    public boolean temAlgumArtefacto() {
        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {
                // Se for maior que 0 e menor/igual ao total de artefactos, é um artefacto!
                if (grelha[i][j] > 0 && grelha[i][j] <= Constantes.NUM_ARTEFACTOS) {
                    return true;
                }
            }
        }
        return false;
    }
}