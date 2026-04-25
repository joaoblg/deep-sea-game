package pt.isec.pa.deepsea.model.data;

/**
 * Interface que centraliza as configuracoes e parametros globais do jogo.
 */
public interface Constantes {
    
    /** Ativa o modo de apresentacao com regras simplificadas de mapa. */
    boolean modo_defesa = false;

    /** Quantidade inicial de combustivel do navio. */
    int combust_ini_navio = 10000;
    
    /** Quantidade inicial e capacidade do tanque de combustivel do drone. */
    int combust_ini_drone = 100;
    
    /** Valor inicial da integridade do casco do drone. */
    int max_integridade_drone = 100;
    
    /** Limite de monstros gerados por celula de fundo do mar. */
    int NUM_MAX_MONSTROS = 5;
    
    /** Limite maximo de jogadas permitidas no puzzle de recuperacao. */
    int MAX_MOV_PUZZLE = 20;
    
    /** Objetivo total de artefactos a recolher. */
    int NUM_ARTEFACTOS = 10;
    
    /** Limite de minerios gerados por celula de fundo do mar. */
    int MAX_MINERIOS_POR_FUNDO = 5;

    /** Limite de animais ou correntes num fosso marinho. */
    int NUM_MAX_OBSTACULOS = 5;

    /** Numero de linhas da matriz da superficie. */
    int LARGURA_SUPERFICIE = 30;
    
    /** Numero de colunas da matriz da superficie. */
    int COMPRIMENTO_SUPERFICIE = 15;

    /** Numero de colunas da matriz do fosso marinho. */
    int LARGURA_FOSSO = 10;
    
    /** Numero de linhas da matriz do fosso marinho. */
    int COMPRIMENTO_FOSSO = 20;

    /** Numero de colunas da matriz do fundo do mar. */
    int LARGURA_FUNDO = 15;
    
    /** Numero de linhas da matriz do fundo do mar. */
    int COMPRIMENTO_FUNDO = 15;

    /** Numero de vezes que o puzzle vai ser baralhado. */
    int NUM_BARALHAR=250;

    /** Ficheiro de save do jogo. */
    public static final String FICHEIRO_SAVE = "deepsea_save.dat";

    /** Ficheiro de log do jogo. */
    public static final String FICHEIRO_LOG = "deepsea_log.txt";
}