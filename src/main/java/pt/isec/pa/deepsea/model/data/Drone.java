package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um drone de exploração subaquática.
 * Gere recursos como combustível, integridade do casco e inventário local.
 */
public class Drone implements Serializable {
    private static final long serialVersionUID = 1L;
    private int combustivel;
    private int capCombustivel;
    private int integridadeCasco;
    private int indiceDanoPrimo;
    private boolean noNavio;
    private int linhaPos;
    private int linhaCol;
    private int mineriosTransp;
    private List<Integer> artefactosTransp;

    /**
     * Construtor que inicializa o drone com valores padrão das constantes.
     */
    public Drone(){
        this.capCombustivel = Constantes.combust_ini_drone;
        this.combustivel = this.capCombustivel;
        this.integridadeCasco = Constantes.max_integridade_drone;
        this.noNavio = true;
        this.mineriosTransp = 0;
        this.artefactosTransp = new ArrayList<>();
    }

    /**
     * Reinicia o índice da sequência de danos para o início de uma nova expedição.
     */
    private static final int[] PRIMOS = {1, 2, 3, 5, 7, 11, 13, 17, 19, 23};

    public void reiniciarExpedicao() {
        this.indiceDanoPrimo = 0;
    }

    /** @return Nível atual de combustível. */
    public int getCombustivel(){
        return combustivel;
    }

    /** @return Nível atual de integridade do casco (0-100). */
    public int getIntegridadeCasco(){
        return integridadeCasco;
    }

    /**
     * Aplica dano ao casco com base no próximo número primo da sequência.
     */
    public void sofrerDano(){
        int valorDano = PRIMOS[Math.min(indiceDanoPrimo, PRIMOS.length - 1)];
        this.integridadeCasco = Math.max(0, this.integridadeCasco - valorDano);
        this.indiceDanoPrimo++;
    }

    /**
     * Reduz a quantidade de combustível disponível.
     * @param quant Unidades de combustível a consumir.
     */
    public void consumirCombustivel(int quant){
        this.combustivel -= quant;
        if(this.combustivel < 0){
            this.combustivel = 0;
        }
    }

    /**
     * Atualiza a localização do drone na grelha ativa.
     * @param lin Coordenada da linha.
     * @param col Coordenada da coluna.
     */
    public void setPosicao(int lin, int col) {
        this.linhaPos = lin;
        this.linhaCol = col;
    }

    public void adicionarMinerios(int quant) {
        this.mineriosTransp += quant;
    }

    public int getLinhaPos() { return linhaPos; }
    public int getColunaPos() { return linhaCol; }

    /** @return Quantidade de minérios transportados pelo drone. */
    public int getMineriosTransp() {
        return mineriosTransp;
    }

    public List<Integer> getArtefactosTransp() {
        return this.artefactosTransp;
    }

    public void descarregarArtefactos() {
        this.artefactosTransp.clear();
    }

    /** Esvazia o compartimento de minérios do drone após descarregar. */
    public void descarregarMinerios() {
        this.mineriosTransp = 0;
    }

    /** Recupera a integridade do casco e atesta o combustível ao máximo. */
    public void repararEEvoluir() {
        this.integridadeCasco = Constantes.max_integridade_drone;
        this.combustivel = this.capCombustivel;
    }

    /**
     * Configura a posição inicial do drone dependendo da fase de jogo.
     * @param fase Identificador da fase ("DESCIDA", "SUBIDA" ou "FUNDO").
     */
    public void prepararPosicaoInicial(String fase) {
        this.noNavio = false;

        switch (fase.toUpperCase()) {
            case "DESCIDA" -> {

                this.linhaPos = 0;
                this.linhaCol = Constantes.LARGURA_FOSSO / 2;
            }
            case "SUBIDA" -> {
                this.linhaPos = Constantes.COMPRIMENTO_FOSSO - 1;
                this.linhaCol = Constantes.LARGURA_FOSSO / 2;
            }
            case "FUNDO" -> {

                this.linhaPos = Constantes.COMPRIMENTO_FUNDO / 2;
                this.linhaCol = Constantes.LARGURA_FUNDO / 2;
            }
            default -> this.noNavio = true;
        }
    }
}



