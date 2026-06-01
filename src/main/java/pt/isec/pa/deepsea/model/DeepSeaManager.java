package pt.isec.pa.deepsea.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.Drone;

/**
 * Facade do modelo de dados do jogo Deep Sea Mining.
 * Expõe as operações do jogo à camada de UI e notifica alterações via {@link PropertyChangeSupport}.
 */
public class DeepSeaManager {
    private final DeepSeaContext context;
    private final PropertyChangeSupport pcs;

    /** Propriedade notificada quando o estado lógico do jogo muda. */
    public static final String PROP_ESTADO = "estado";
    /** Propriedade notificada quando dados do modelo (posições, inventário, etc.) mudam. */
    public static final String PROP_DADOS = "dados";
    /** Propriedade notificada quando o histórico de logs é alterado. */
    public static final String PROP_LOG = "log";

    /**
     * Cria um novo gestor com contexto de jogo inicial.
     */
    public DeepSeaManager() {
        this.context = DeepSeaContext.createContext();
        this.pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public DeepSeaState getState() {
        return context.getState();
    }

    private void notificarLog() {
        pcs.firePropertyChange(PROP_LOG, null, null);
    }

    private void notificarDados() {
        pcs.firePropertyChange(PROP_DADOS, null, null);
    }

    private void notificarEstado() {
        pcs.firePropertyChange(PROP_ESTADO, null, getState());
    }

    /** Notifica estado, dados e logs após um comando do jogo. */
    private void notificarComando() {
        notificarEstado();
        notificarDados();
        notificarLog();
    }

    public boolean iniciarJogo() {
        boolean result = context.iniciarJogo();
        notificarComando();
        return result;
    }

    /**
     * Ativa ou desativa o modo defesa global do jogo.
     * @param ativo true para modo defesa.
     */
    public void setModoDefesa(boolean ativo) {
        ModoJogo.setModoDefesa(ativo);
        notificarComando();
    }

    /** @return true se o modo defesa estiver ativo. */
    public boolean isModoDefesa() {
        return ModoJogo.isModoDefesa();
    }

    public void setModoDefesaAumentarCombustivel(boolean ativo) {
        ModoJogo.setAumentarCombustivelDrone(ativo);
        notificarComando();
    }

    public boolean isModoDefesaAumentarCombustivel() {
        return ModoJogo.isAumentarCombustivelDrone();
    }

    public void setModoDefesaVitoriaRota(boolean ativo) {
        ModoJogo.setVitoriaPorRota(ativo);
        notificarComando();
    }

    public boolean isModoDefesaVitoriaRota() {
        return ModoJogo.isVitoriaPorRota();
    }

    public boolean evoluirDrone() {
        boolean result = context.evoluirDrone();
        notificarComando();
        return result;
    }

    /**
     * Seleciona o drone ativo na oficina ou na superfície (índice 1..n).
     * @param indice Posição na frota (base 1).
     * @return true se a seleção for válida no estado atual.
     */
    public boolean selecionarDrone(int indice) {
        DeepSeaState estado = getState();
        if (estado != DeepSeaState.SUPERFICIE && estado != DeepSeaState.OFICINA) {
            return false;
        }
        boolean result = context.getModelo().getNavio().selecionarDronePorIndice(indice);
        if (result) {
            notificarDados();
        }
        return result;
    }

    /**
     * @return Índice (base 1) do drone atualmente selecionado.
     */
    public int getIndiceDroneSelecionado() {
        return context.getModelo().getNavio().getIndiceDroneSelecionado();
    }

    /** @return true se existir um drone ativo na expedição. */
    public boolean hasDroneAtivo() {
        return context.getDrone() != null;
    }

    /** @return Número fixo do drone ativo (1, 2 ou 3), ou 0 se nenhum. */
    public int getNumeroDroneAtivo() {
        Drone drone = context.getDrone();
        return drone != null ? drone.getNumero() : 0;
    }

    /** @return Nome do ficheiro de imagem do drone ativo (identidade visual estável). */
    public String getNomeImagemDroneAtivo() {
        int num = getNumeroDroneAtivo();
        if (num <= 0) {
            return "drone.png";
        }
        return num == 1 ? "drone.png" : "drone" + num + ".png";
    }

    /** @return Linha do drone ativo na grelha, ou -1 se inexistente. */
    public int getLinhaDrone() {
        Drone drone = context.getDrone();
        return drone != null ? drone.getLinhaPos() : -1;
    }

    /** @return Coluna do drone ativo na grelha, ou -1 se inexistente. */
    public int getColunaDrone() {
        Drone drone = context.getDrone();
        return drone != null ? drone.getColunaPos() : -1;
    }

    /** @return Combustível atual do drone ativo. */
    public int getCombustivelDroneAtivo() {
        Drone drone = context.getDrone();
        return drone != null ? drone.getCombustivel() : 0;
    }

    /** @return Integridade do casco do drone ativo. */
    public int getIntegridadeCascoDroneAtivo() {
        Drone drone = context.getDrone();
        return drone != null ? drone.getIntegridadeCasco() : 0;
    }

    /** @return Capacidade máxima de combustível do drone ativo. */
    public int getCapCombustivelDroneAtivo() {
        Drone drone = context.getDrone();
        return drone != null ? drone.getCapCombustivel() : 0;
    }

    /** @return Integridade máxima do casco do drone ativo. */
    public int getMaxIntegridadeCascoDroneAtivo() {
        Drone drone = context.getDrone();
        return drone != null ? drone.getMaxIntegridadeCasco() : 0;
    }

    /**
     * @return Número de drones disponíveis na frota.
     */
    public int getNumDrones() {
        return context.getModelo().getNavio().getDrones().size();
    }

    /**
     * @return Números fixos (1, 2, 3…) dos drones ainda vivos na frota, ordenados.
     */
    public List<Integer> getNumerosDronesVivos() {
        List<Integer> numeros = new ArrayList<>();
        for (Drone drone : context.getModelo().getNavio().getDrones()) {
            numeros.add(drone.getNumero());
        }
        numeros.sort(Integer::compareTo);
        return numeros;
    }

    public boolean iniciarMergulho(int linha, int coluna) {
        boolean result = context.iniciarMergulho(linha, coluna);
        notificarComando();
        return result;
    }

    public boolean mover(Direcoes direcao) {
        boolean result = context.mover(direcao);
        notificarComando();
        return result;
    }

    public boolean avaliarFundo() {
        boolean result = context.avaliarFundo();
        notificarComando();
        return result;
    }

    public boolean recolherMinerio() {
        boolean result = context.recolherMinerio();
        notificarComando();
        return result;
    }

    public boolean voltarAoNavio() {
        boolean result = context.voltarAoNavio();
        notificarComando();
        return result;
    }

    /**
     * Entra na oficina do navio.
     * @return true se a transição for aceite.
     */
    public boolean entrarOficina() {
        boolean result = context.entrarOficina();
        notificarComando();
        return result;
    }

    /**
     * Sai da oficina.
     * @return true se a transição for aceite.
     */
    public boolean sairOficina() {
        boolean result = context.sairOficina();
        notificarComando();
        return result;
    }

    /** Métodos de debug/demo ativáveis em Modo Defesa */
    public void declararFimJogo() {
        // força o estado lógico para FIM_JOGO através de API pública do contexto
        this.context.forceChangeState(DeepSeaState.FIM_JOGO);
        notificarComando();
    }

    public void declararVitoria() {
        // também força fim de jogo — serve como declaração manual de vitória
        declararFimJogo();
    }

    public void adicionarCombustivelDrone(int quant) {
        var drone = context.getDrone();
        if (drone != null) {
            drone.adicionarCombustivel(quant);
            notificarDados();
        }
    }

    /** Resolve automaticamente o puzzle atual (usado por debug/demo). */
    public boolean autoResolverPuzzle() {
        var modelo = context.getModelo();
        var puzzle = modelo.getPuzzleAtual();
        if (puzzle == null) return false;

        var fundo = modelo.getFundo();
        var drone = context.getDrone();
        if (fundo != null && drone != null) {
            var elem = fundo.getConteudo(drone.getLinhaPos(), drone.getColunaPos());
            if (elem instanceof pt.isec.pa.deepsea.model.data.Artefacto art) {
                drone.getArtefactosTransp().add(art.getIdArtefacto());
                fundo.removerConteudo(drone.getLinhaPos(), drone.getColunaPos());
            }
        }

        modelo.setPuzzleAtual(null);
        context.forceChangeState(pt.isec.pa.deepsea.model.state.DeepSeaState.FUNDO_MAR);
        notificarComando();
        return true;
    }

    /**
     * Repara o casco do drone ativo na oficina.
     * @return true se a reparação for efetuada.
     */
    public boolean melhorarCasco() {
        boolean result = context.melhorarCasco();
        notificarDados();
        notificarLog();
        return result;
    }

    /**
     * Compra combustível para o drone ativo na oficina.
     * @return true se a compra for efetuada.
     */
    public boolean comprarCombustivel() {
        boolean result = context.comprarCombustivel();
        notificarDados();
        notificarLog();
        return result;
    }

    public boolean jogarPuzzle(Direcoes direcao) {
        boolean result = context.jogarPuzzle(direcao);
        notificarComando();
        return result;
    }

    public boolean gravarJogo() {
        boolean result = context.gravarJogo();
        notificarLog();
        return result;
    }

    /**
     * Grava o jogo no ficheiro indicado.
     * @param ficheiro Caminho do ficheiro de destino.
     * @return true se a gravação for bem-sucedida.
     */
    public boolean gravarJogo(String ficheiro) {
        boolean result = context.gravarJogo(ficheiro);
        notificarLog();
        return result;
    }

    public boolean carregarJogo() {
        boolean result = context.carregarJogo();
        notificarComando();
        return result;
    }

    /**
     * Restaura o jogo a partir do ficheiro indicado.
     * @param ficheiro Caminho do ficheiro de origem.
     * @return true se o carregamento for bem-sucedido.
     */
    public boolean carregarJogo(String ficheiro) {
        boolean result = context.carregarJogo(ficheiro);
        notificarComando();
        return result;
    }

    /**
     * Obtém uma cópia dos logs em memória.
     * @return Lista de entradas de log.
     */
    public List<String> getLogs() {
        return pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().getLogs();
    }

    /**
     * Remove todas as entradas de log em memória.
     */
    public void limparLogs() {
        pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().limparLogs();
        pcs.firePropertyChange(PROP_LOG, null, null);
    }

    public int getCombustivelNavio() {
        return context.getCombustivelNavio();
    }

    public int getLinhaNavio() {
        return context.getLinhaNavio();
    }

    public int getColunaNavio() {
        return context.getColunaNavio();
    }

    public int getMineriosNavio() {
        return context.getMineriosNavio();
    }

    public int getLinhasSuperficie() {
        return context.getModelo().getSuperficie().getLinhas();
    }

    public int getColunasSuperficie() {
        return context.getModelo().getSuperficie().getColunas();
    }

    /**
     * Exporta o histórico de logs do jogo para um ficheiro de texto.
     * Resolve a violação arquitetural de fazer I/O diretamente na View.
     * @param nomeFicheiro O nome do ficheiro de destino.
     * @return true se exportado com sucesso, false caso ocorra um erro.
     */
    public boolean exportarLogs(String nomeFicheiro) {
        try (java.io.FileWriter writer = new java.io.FileWriter(nomeFicheiro)) {
            for (String log : pt.isec.pa.deepsea.model.data.DeepSeaLog.getInstance().getLogs()) {
                writer.write(log + "\n");
            }
            notificarLog();
            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }

    public boolean contemArtefactoNaSuperficie(int lin, int col) {
        return context.getModelo().getSuperficie().contemArtefacto(lin, col);
    }

    /**
     * @param lin Linha da grelha do puzzle.
     * @param col Coluna da grelha do puzzle.
     * @return Valor da peça (0 representa o espaço vazio).
     */
    public int getPecaPuzzle(int lin, int col) {
        var puzzle = context.getModelo().getPuzzleAtual();
        if (puzzle == null || lin < 0 || col < 0 || lin >= 4 || col >= 4) {
            return 0;
        }
        return puzzle.getGrelha()[lin][col];
    }

    /**
     * @return Número de artefactos recolhidos pelo navio.
     */
    public int getArtefactosNavio() {
        return context.getModelo().getNavio().getArtefactos().size();
    }

    /** @return Altura da grelha do fosso ativo. */
    public int getLinhasFosso() {
        return context.getModelo().getFosso().getAltura();
    }

    /** @return Largura da grelha do fosso ativo. */
    public int getColunasFosso() {
        return context.getModelo().getFosso().getLargura();
    }

    /**
     * @param l Linha.
     * @param c Coluna.
     * @return O tipo de elemento presente no fosso (ex: "Rocha", "CorrenteMarinha") ou null se vazio.
     */
    public String getElemFosso(int l, int c) {
        Object obj = context.getModelo().getFosso().getCont(l, c);
        return obj != null ? obj.getClass().getSimpleName() : null;
    }

    /** @return Altura da grelha do fundo do mar ativo. */
    public int getLinhasFundo() {
        return context.getModelo().getFundo().getAltura();
    }

    /** @return Largura da grelha do fundo do mar ativo. */
    public int getColunasFundo() {
        return context.getModelo().getFundo().getLargura();
    }

    /**
     * @param l Linha.
     * @param c Coluna.
     * @return O tipo de conteúdo da célula do fundo ou null se vazio.
     */
    public String getElemFundo(int l, int c) {
        Object obj = context.getModelo().getFundo().getConteudo(l, c);
        return obj != null ? obj.getClass().getSimpleName() : null;
    }

    /**
     * @param l Linha.
     * @param c Coluna.
     * @return true se a célula do fundo estiver revelada.
     */
    public boolean isFundoRevelado(int l, int c) {
        return context.getModelo().getFundo().estaRevelada(l, c);
    }

    /**
     * @return Linhas de texto com o estado de cada drone da frota.
     */
    public List<String> getResumoFrotaDrones() {
        List<String> resumo = new ArrayList<>();
        for (Drone drone : context.getModelo().getNavio().getDronesOrdenadosPorCasco()) {
            resumo.add(String.format(
                    "Drone %d — Comb.%d | Casco %d%% | Min.%d",
                    drone.getNumero(),
                    drone.getCombustivel(),
                    drone.getIntegridadeCasco(),
                    drone.getMineriosTransp()));
        }
        return resumo;
    }
}
