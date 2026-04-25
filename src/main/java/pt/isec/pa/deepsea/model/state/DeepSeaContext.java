package pt.isec.pa.deepsea.model.state;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.Artefacto;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.Drone;

/**
 * Contexto da FSM do jogo.
 * Mantém o estado atual e delega as transições para o estado concreto ativo.
 */
public class DeepSeaContext {
    private IDeepSeaState estadoAtual;
    private Jogo modelo;

    /**
     * Inicializa o contexto no estado de setup com um novo modelo de jogo.
     */
    public DeepSeaContext() {
        this.modelo = new Jogo();
        this.estadoAtual = DeepSeaState.SETUP.createState(this, modelo);
    }

    /**
     * Efetua a mudança interna para um novo estado concreto.
     * @param newState Novo estado a ativar.
     */
    void changeState(IDeepSeaState newState) {
        this.estadoAtual = newState;
    }

    /**
     * Obtém o identificador do estado atual.
     * @return Estado lógico atual.
     */
    public DeepSeaState getState() {
        return estadoAtual.getState();
    }

    /**
     * Inicia o jogo e efetua o respetivo registo no log.
     * @return true se a operação for permitida no estado atual, false caso contrário.
     */
    public boolean iniciarJogo() {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Comando: Iniciar Jogo");
        return estadoAtual.iniciarJogo();
    }

    /**
     * Tenta evoluir as capacidades do drone ativo e regista a operação.
     * @return true se a evolução for concluída com sucesso, false caso contrário.
     */
    public boolean evoluirDrone() {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Comando: Evoluir Drone");
        return estadoAtual.evoluirDrone();
    }

    /**
     * Envia a ordem de mergulho para uma coordenada específica e regista a ação.
     * @param linha Coordenada da linha na superfície.
     * @param coluna Coordenada da coluna na superfície.
     * @return true se o mergulho for iniciado validamente, false caso contrário.
     */
    public boolean iniciarMergulho(int linha, int coluna) {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Comando: Iniciar Mergulho para [" + linha + ", " + coluna + "]");
        return estadoAtual.iniciarMergulho(linha, coluna);
    }

    /**
     * Move a entidade ativa numa determinada direção e regista a operação.
     * @param direcao Direção do movimento.
     * @return true se o movimento for possível e executado, false caso contrário.
     */
    public boolean mover(Direcoes direcao) {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Comando: Mover para " + direcao);
        return estadoAtual.mover(direcao);
    }

    /**
     * Realiza a avaliação dos recursos presentes no fundo do mar na posição atual.
     * @return true se a avaliação for concluída com sucesso, false caso contrário.
     */
    public boolean avaliarFundo() {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Comando: Avaliar Fundo");
        return estadoAtual.avaliarFundo();
    }

    /**
     * Ordena o regresso do drone ao navio base e regista o evento.
     * @return true se a ordem de regresso for aceite, false caso contrário.
     */
    public boolean voltarAoNavio() {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Comando: Voltar ao Navio");
        return estadoAtual.voltarAoNavio();
    }

    /**
     * Executa um movimento na grelha do puzzle e regista a operação.
     * @param direcao Direção do movimento a efetuar.
     * @return true se o movimento for válido e processado, false caso contrário.
     */
    public boolean jogarPuzzle(Direcoes direcao) {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Comando: Jogar Puzzle na direcao " + direcao);
        return estadoAtual.jogarPuzzle(direcao);
    }

    /**
     * Devolve o drone ativo para consulta (usado por estados e testes).
     * @return Instância do drone ativo.
     */
    public pt.isec.pa.deepsea.model.data.Drone getDrone() {
        if (modelo == null || modelo.getNavio() == null) return null;
        return modelo.getNavio().getDroneAtivo();
    }
    
    /**
         * Guarda o estado atual do jogo em ficheiro.
         * @return true se gravado com sucesso.
         */
    public boolean gravarJogo() {
        return pt.isec.pa.deepsea.model.data.GestorFicheiros.gravarJogo(modelo, pt.isec.pa.deepsea.model.data.Constantes.FICHEIRO_SAVE);
    }

    /**
     * Carrega um estado de jogo guardado.
     * @return true se carregado com sucesso.
     */
    public boolean carregarJogo() {
        pt.isec.pa.deepsea.model.Jogo carregado = pt.isec.pa.deepsea.model.data.GestorFicheiros.carregarJogo(pt.isec.pa.deepsea.model.data.Constantes.FICHEIRO_SAVE);
        if (carregado != null) {
            this.modelo = carregado;
            this.estadoAtual = pt.isec.pa.deepsea.model.state.DeepSeaState.NAVIO.createState(this, modelo);
            return true;
        }
        return false;
    }

    /**
     * Obtém o combustível disponível no navio.
     * @return Quantidade de combustível.
     */
    public int getCombustivelNavio() {
        return modelo.getNavio().getCombustivel();
    }

    /**
     * Obtém a coleção de artefactos recolhidos de forma imutável.
     * @return Lista imutável de artefactos.
     */
    public List<Artefacto> getArtefactosRecolhidos() {
        return Collections.unmodifiableList(modelo.getNavio().getArtefactos());
    }

    /**
     * Obtém a coleção de drones disponíveis de forma imutável.
     * @return Conjunto imutável de drones.
     */
    public Set<Drone> getDrones() {
        return Collections.unmodifiableSet(modelo.getNavio().getDrones());
    }

    /**
     * Obtém a coluna atual do navio.
     * @return Índice da coluna.
     */
    public int getColunaNavio(){
        return modelo.getNavio().getColunaPos();
    }

    /**
     * Obtém a linha atual do navio.
     * @return Índice da linha.
     */
    public int getLinhaNavio(){
        return modelo.getNavio().getLinhaPos();
    }

    /**
     * Obtém a quantidade de minérios armazenada no navio.
     * @return Quantidade de minérios.
     */
    public int getMineriosNavio(){
        return modelo.getNavio().getMinerios();
    }
}