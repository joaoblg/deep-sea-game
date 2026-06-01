package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.state.states.EstadoDescida;
import pt.isec.pa.deepsea.model.state.states.EstadoFimJogo;
import pt.isec.pa.deepsea.model.state.states.EstadoFosso;
import pt.isec.pa.deepsea.model.state.states.EstadoFundoMar;
import pt.isec.pa.deepsea.model.state.states.EstadoOficina;
import pt.isec.pa.deepsea.model.state.states.EstadoPuzzle;
import pt.isec.pa.deepsea.model.state.states.EstadoSubida;
import pt.isec.pa.deepsea.model.state.states.EstadoSuperficie;

/**
 * Enumeração dos estados lógicos do jogo.
 * Implementa o factory method para criação dos estados concretos.
 */
public enum DeepSeaState {
    OFICINA, SUPERFICIE, DESCIDA, SUBIDA, FUNDO_MAR, PUZZLE, FIM_JOGO, FOSSO;

    /**
     * Cria a instância concreta correspondente ao estado.
     * @param context Contexto da FSM.
     * @param modelo Modelo de dados do jogo.
     * @return Instância concreta do estado.
     */
    public IDeepSeaState createState(DeepSeaContext context, Jogo modelo) {
        return switch (this) {
            case OFICINA -> new EstadoOficina(context, modelo);
            case SUPERFICIE -> new EstadoSuperficie(context, modelo);
            case DESCIDA -> new EstadoDescida(context, modelo);
            case SUBIDA -> new EstadoSubida(context, modelo);
            case FUNDO_MAR -> new EstadoFundoMar(context, modelo);
            case PUZZLE -> new EstadoPuzzle(context, modelo);
            case FIM_JOGO -> new EstadoFimJogo(context, modelo);
            case FOSSO -> new EstadoFosso(context,modelo);
        };
    }

}