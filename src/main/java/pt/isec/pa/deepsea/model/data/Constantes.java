package pt.isec.pa.deepsea.model.data;

/**
 * Interface que centraliza as configuracoes e parametros globais do jogo.
 */
public interface Constantes {
    
    /** Ativa o modo de apresentacao com regras simplificadas de mapa. */
    boolean MODO_DEFESA = false;

    /** Quantidade inicial de combustivel do navio. */
    int COMBUSTIVEL_INI_NAVIO = 10000;
    
    /** Quantidade inicial e capacidade do tanque de combustivel do drone. */
    int COMBUSTIVEL_INI_DRONE = 100;
    
    /** Valor inicial da integridade do casco do drone. */
    int MAX_INTEGRIDADE_DRONE = 100;
    
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
    int LARGURA_SUPERFICIE = 15;
    
    /** Numero de colunas da matriz da superficie. */
    int COMPRIMENTO_SUPERFICIE = 10;

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

    /** Combustivel que o navio gasta por movimento. */
    int COMBUPERMOVE = 15;

    /** Combustivel do navio gasto por reparacao de casco na oficina. */
    int CUSTO_COMBUSTIVEL_REPARACAO = 25;

    /** Combustivel do navio transferido para o drone na oficina. */
    int COMBUSTIVEL_TRANSFERENCIA_DRONE = 15;

    /** Minerios do navio para evoluir o drone na oficina. */
    int CUSTO_MINERIOS_EVOLUIR = 3;

    /** Combustivel extra ao recolher minério no fundo (para além do movimento). */
    int COMBUSTIVEL_EXTRA_RECOLHER_MINERIO = 1;

    /** Ganho de capacidade do tanque na evolução. */
    int GANHO_CAPACIDADE_TANQUE = 25;

    /** Ganho de blindagem máxima na evolução. */
    int GANHO_BLINDAGEM_MAXIMA = 15;
}