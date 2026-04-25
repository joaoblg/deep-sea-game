package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;


/**
 * Classe que atua como a Facade global do modelo de dados do jogo.
 * Coordena o acesso à máquina de estados (FSM) e garante uma API estável para a UI.
 */
public class DeepSeaManager {
    private final DeepSeaContext jogo;


    /**
     * inicializa o manager
     */
    public DeepSeaManager(){
        this.jogo=new DeepSeaContext();
    }


    /**
     * Obtém o estado atual da máquina de estados.
     * @return Estado lógico atual do jogo.
     */
    public DeepSeaState getState(){
        return jogo.getState();
    }

    /**
     * Tenta iniciar uma nova partida.
     * @return true quando a transição é válida e executada.
     */
    public boolean iniciarJogo(){
        return jogo.iniciarJogo();
    }


    /**
     * Tenta executar um movimento de acordo com o estado atual.
     * @param direcao Direção pretendida.
     * @return true quando o movimento é aceite.
     */
    public boolean mover(Direcoes direcao){
        return jogo.mover(direcao);
    }


    /**
     * Obtém a quantidade atual de combustível do navio.
     * @return Valor inteiro do combustível disponível.
     */
    public int getCombustivel() { return jogo.getCombustivelNavio(); }

    /**
     * Obtém a quantidade total de minérios armazenados no navio.
     * @return Valor inteiro dos minérios recolhidos.
     */
    public int getMinerios() { return jogo.getMineriosNavio(); }

    /**
     * Obtém a coordenada da linha atual do navio à superfície.
     * @return Índice da linha.
     */
    public int getLinhaNavio() { return jogo.getLinhaNavio(); }

    /**
     * Obtém a coordenada da coluna atual do navio à superfície.
     * @return Índice da coluna.
     */
    public int getColunaNavio() { return jogo.getColunaNavio(); }

    /**
     * Tenta evoluir o drone na oficina
     * @return true se conseguir evoluir
     */
    public boolean evoluirDrone(){
        return jogo.evoluirDrone();
    }


    /**
     *inicia o mergulho nas coordenadas fornecidas
     * @param lin - linha da grelha
     * @param col - coluna da grelha
     * @return true se conseguir inciar a descida
     */
    public boolean iniciarMergulho(int lin, int col){
        return jogo.iniciarMergulho(lin,col);
    }


    /**
     * avalia o fundo do mar na posição atual em que o drone está
     * @return true se for feito a avaliação
     */
    public boolean avaliarFUndo(){
        return jogo.avaliarFundo();
    }


    /**
     * inicia a subida do drone de volta ao navio
     * @return true se conseguir subir
     */
    public boolean voltarNavio(){
        return jogo.voltarAoNavio();
    }

    /**
     * Executa um movimento no minijogo de puzzle.
     * @param direcao Direção para onde deslizar a peça (ou o espaço vazio).
     * @return true se a peça foi movida.
     */
    public boolean jogarPuzzle(Direcoes direcao){
        return jogo.jogarPuzzle(direcao);

    }

    /**
     * Guarda o estado atual do jogo no ficheiro de persistência.
     * @return true se a gravação for concluída com sucesso.
     */
    public boolean gravarJogo() {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Jogo gravado manualmente.");
        return jogo.gravarJogo();
    }

    /**
     * Carrega um jogo previamente gravado.
     * @return true se o carregamento for concluído com sucesso.
     */
    public boolean carregarJogo() {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Jogo carregado a partir de ficheiro.");
        return jogo.carregarJogo();
    }
}
