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
    private ElemFundo[][] grelha;
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
        this.grelha = new ElemFundo[altura][largura];
        this.revelada = new boolean[altura][largura];

    }

    /**
     * Distribui depósitos de minério pela grelha do fundo do mar.
     * @param numMinerios Número de depósitos a gerar.
     */
    public void gerarMinerios(int numMinerios) {
        Random rd = new Random();
        int minColocados = 0;

        while (minColocados < numMinerios) {
            int l = rd.nextInt(altura);
            int c = rd.nextInt(largura);

            if (grelha[l][c] == null) {
                int quanti=rd.nextInt(10)+1;

                grelha[l][c]=new Minerio(quanti);
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
     * Devolve o objeto presente numa coordenada específica do fundo do mar.
     * @param l Linha da grelha.
     * @param c Coluna da grelha.
     * @return O ElementoFundo presente ou null se a célula estiver vazia.
     */
    public ElemFundo getConteudo(int l, int c) {

        if (l >= 0 && l < altura && c >= 0 && c < largura) {
            return grelha[l][c];
        }
        return null;
    }

    /**
     * Adiciona artefactos para o modo defesa em posições vagas com IDs únicos.
     *
     * @param idInicial  O primeiro ID a ser atribuído.
     * @param quantidade Número de artefactos a adicionar.
     */
    public void addArtefactosDefesa(int idInicial, int quantidade) {
        int colocados = 0;
        int idAtual = idInicial;

        for (int i = 0; i < altura && colocados < quantidade; i++) {
            for (int j = 0; j < largura && colocados < quantidade; j++) {

                if (grelha[i][j] == null) {
                    grelha[i][j] = new Artefacto(idAtual);
                    idAtual++;
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

            if (!revelada[l][c] && grelha[l][c] == null) {
                grelha[l][c] = new Monstro();
                colocados++;
            }
            tentativas++;
        }
    }

    /**
     * Configura o fundo do mar para o modo de apresentação.
     * Define um cenário fixo com um monstro, minérios e utiliza o método
     * auxiliar para colocar artefactos em posições previsíveis.
     */
    public void configurarModoDefesa() {
        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {
                revelada[i][j] = true;
                grelha[i][j] = null;
            }
        }

        if (altura > 2 && largura > 2) {
            grelha[altura / 2][largura / 2] = new Monstro();
        }

        if (altura > 1 && largura > 2) {
            grelha[1][1] = new Minerio(1);
            grelha[1][2] = new Minerio(100);
        }


    }

    /**
     * Remove o conteúdo de uma célula específica (colocando a nulo).
     * Útil para quando um artefacto é recolhido pelo drone.
     */
    public void removerConteudo(int l, int c) {
        if (l >= 0 && l < altura && c >= 0 && c < largura) {
            grelha[l][c] = null;
        }
    }

    /**
     * Coloca um artefacto específico na grelha, criando um novo objeto Artefacto.
     * @param idArtefacto O ID único do artefacto (1 a 10).
     */
    public void colocarArtefacto(int idArtefacto) {
        Random rd = new Random();
        boolean colocado = false;

        while (!colocado) {
            int l = rd.nextInt(altura);
            int c = rd.nextInt(largura);
            if (grelha[l][c] == null) {
                grelha[l][c]= new Artefacto(idArtefacto);
                colocado = true;
            }
        }
    }

    /**
     * Verifica se existe pelo menos um artefacto nesta grelha do fundo do mar.
     * @return true se encontrar um objeto da classe Artefacto.
     */
    public boolean temAlgumArtefacto() {
        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {
                if (grelha[i][j] instanceof Artefacto) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Repõe todas as células em escuridão (após perda do drone na subida).
     */
    public void resetEscuridao() {
        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {
                revelada[i][j] = false;
            }
        }
    }

    /**
     * Coloca minérios e artefactos do drone em células aleatórias do fundo.
     * @param drone Drone que perdeu a carga.
     */
    public void depositarLootDoDrone(Drone drone) {
        if (drone == null) {
            return;
        }
        Random rd = new Random();
        if (drone.getMineriosTransp() > 0) {
            int restantes = drone.getMineriosTransp();
            int tentativas = 0;
            while (restantes > 0 && tentativas < 200) {
                int l = rd.nextInt(altura);
                int c = rd.nextInt(largura);
                if (grelha[l][c] == null) {
                    int q = Math.min(restantes, rd.nextInt(5) + 1);
                    grelha[l][c] = new Minerio(q);
                    restantes -= q;
                }
                tentativas++;
            }
        }
        for (Integer id : new java.util.ArrayList<>(drone.getArtefactosTransp())) {
            int tentativas = 0;
            boolean colocado = false;
            while (!colocado && tentativas < 100) {
                int l = rd.nextInt(altura);
                int c = rd.nextInt(largura);
                if (grelha[l][c] == null) {
                    grelha[l][c] = new Artefacto(id);
                    colocado = true;
                }
                tentativas++;
            }
        }
    }

    public int getAltura() { return altura; }
    public int getLargura() { return largura; }
}