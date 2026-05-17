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
        this.estadoAtual = DeepSeaState.SUPERFICIE.createState(this, modelo);
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
        pt.isec.pa.deepsea.model.data.DeepSeaLog log = pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance();
        log.limparLogs();
        log.addLog("Comando: Iniciar Jogo");
        this.modelo = new Jogo();
        this.estadoAtual = DeepSeaState.SUPERFICIE.createState(this, modelo);
        return true;
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
     * Efetua a recolha de minério e regista a operação.
     * @return true se a recolha for efetuada, false caso contrário.
     */
    public boolean recolherMinerio() {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Comando: Recolher Minerio");
        return estadoAtual.recolherMinerio();
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
     * Entra na oficina do navio (disponível na superfície).
     * @return true se a transição for aceite.
     */
    public boolean entrarOficina() {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Comando: Entrar na Oficina");
        return estadoAtual.entrarOficina();
    }

    /**
     * Sai da oficina e regressa à superfície.
     * @return true se a transição for aceite.
     */
    public boolean sairOficina() {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Comando: Sair da Oficina");
        return estadoAtual.sairOficina();
    }

    /**
     * Repara o casco do drone na oficina.
     * @return true se a operação for bem-sucedida.
     */
    public boolean melhorarCasco() {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Comando: Melhorar Casco");
        return estadoAtual.melhorarCasco();
    }

    /**
     * Reabastece combustível do drone na oficina.
     * @return true se a operação for bem-sucedida.
     */
    public boolean comprarCombustivel() {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().addLog("Comando: Comprar Combustivel");
        return estadoAtual.comprarCombustivel();
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
     * Grava o estado atual no ficheiro de save predefinido.
     * @return true se guardado com sucesso.
     */
    public boolean gravarJogo() {
        return gravarJogo(pt.isec.pa.deepsea.model.data.Constantes.FICHEIRO_SAVE);
    }

    /**
     * Grava o estado atual do jogo no ficheiro indicado.
     * @param ficheiro Caminho do ficheiro de destino.
     * @return true se guardado com sucesso.
     */
    public boolean gravarJogo(String ficheiro) {
        modelo.setEstadoGravado(this.getState());
        return pt.isec.pa.deepsea.model.data.GestorFicheiros.gravarJogo(this.modelo, ficheiro);
    }

    /**
     * Carrega o jogo a partir do ficheiro de save predefinido.
     * @return true se carregado com sucesso.
     */
    public boolean carregarJogo() {
        return carregarJogo(pt.isec.pa.deepsea.model.data.Constantes.FICHEIRO_SAVE);
    }

    /**
     * Carrega o estado do jogo a partir do ficheiro indicado.
     * @param ficheiro Caminho do ficheiro de origem.
     * @return true se carregado com sucesso.
     */
    public boolean carregarJogo(String ficheiro) {
        Jogo carregado = pt.isec.pa.deepsea.model.data.GestorFicheiros.carregarJogo(ficheiro);
        if (carregado != null) {
            this.modelo = carregado;
            pt.isec.pa.deepsea.model.state.DeepSeaState estadoRecuperado = modelo.getEstadoGravado() != null
                    ? modelo.getEstadoGravado()
                    : pt.isec.pa.deepsea.model.state.DeepSeaState.SUPERFICIE;
            this.estadoAtual = estadoRecuperado.createState(this, modelo);
            return true;
        }
        return false;
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
     * Devolve o modelo de dados atual (necessário para a Facade gravar o jogo).
     * @return Instância de Jogo.
     */
    public Jogo getModelo() {
        return this.modelo;
    }

    /**
     * Atualiza o modelo de dados e reinicia o estado (usado ao carregar um jogo).
     * @param novoModelo Modelo restaurado de ficheiro.
     */
    public void setModelo(Jogo novoModelo) {
        this.modelo = novoModelo;
        this.estadoAtual = DeepSeaState.SUPERFICIE.createState(this, modelo);
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