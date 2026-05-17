package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.state.states.*;

/**
 * Enumeração dos estados lógicos do jogo.
 * Implementa o factory method para criação dos estados concretos.
 */
public enum DeepSeaState {
    OFICINA, SUPERFICIE, DESCIDA, FUNDO_MAR, SUBIDA, PUZZLE, FIM_JOGO;

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
            case FUNDO_MAR -> new EstadoFundoMar(context, modelo);
            case SUBIDA -> new EstadoSubida(context, modelo);
            case PUZZLE -> new EstadoPuzzle(context, modelo);
            case FIM_JOGO -> new EstadoFimJogo(context, modelo);
        };
    }

}