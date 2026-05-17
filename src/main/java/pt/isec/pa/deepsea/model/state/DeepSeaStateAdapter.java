package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.Direcoes;

/**
 * Adaptador base para estados da FSM.
 * Fornece implementação por omissão das transições (todas inválidas).
 */
public abstract class DeepSeaStateAdapter implements IDeepSeaState {
    protected DeepSeaContext context;
    protected Jogo modelo;

    /**
     * Inicializa o estado com contexto e modelo.
     * @param context Contexto da FSM.
     * @param modelo Modelo de jogo.
     */
    public DeepSeaStateAdapter(DeepSeaContext context, Jogo modelo) {
        this.context = context;
        this.modelo = modelo;
    }

    /**
     * Pede ao contexto para trocar o estado ativo.
     * @param newState Novo estado.
     */
    protected void changeState(DeepSeaState newState) {
        context.changeState(newState.createState(context, modelo));
    }
    @Override
    public boolean recolherMinerio() {
        return false;
    }
    @Override public boolean iniciarJogo() { return false; }
    @Override public boolean evoluirDrone() { return false; }
    @Override public boolean iniciarMergulho(int linha, int coluna) { return false; }
    @Override public boolean mover(Direcoes direcao) { return false; }
    @Override public boolean avaliarFundo() { return false; }
    @Override public boolean voltarAoNavio() { return false; }
    @Override public boolean jogarPuzzle(Direcoes direcao) { return false; }
    @Override public boolean entrarOficina() { return false; }
    @Override public boolean sairOficina() { return false; }
    @Override public boolean melhorarCasco() { return false; }
    @Override public boolean comprarCombustivel() { return false; }
}