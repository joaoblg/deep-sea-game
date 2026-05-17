package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.data.Direcoes;

/**
 * Interface com a declaração de todas as transições de estado possíveis no jogo.
 */
public interface IDeepSeaState {
    /**
     * Transição para iniciar o jogo.
     * @return true quando a transição é válida.
     */
    boolean iniciarJogo();
    /**
     * Transição para evoluir o drone.
     * @return true quando a transição é válida.
     */
    boolean evoluirDrone();
    /**
     * Transição para iniciar mergulho.
     * @param linha Linha de destino na superfície.
     * @param coluna Coluna de destino na superfície.
     * @return true quando a transição é válida.
     */
    boolean iniciarMergulho(int linha, int coluna);
    /**
     * Transição de movimento.
     * @param direcao Direção do movimento.
     * @return true quando a transição é válida.
     */
    boolean mover(Direcoes direcao);
    /**
     * Transição de avaliação de célula no fundo.
     * @return true quando a transição é válida.
     */
    boolean avaliarFundo();
    /**
     * Transição de regresso ao navio.
     * @return true quando a transição é válida.
     */
    boolean voltarAoNavio();
    /**
     * Transição de jogada do puzzle.
     * @param direcao Direção da jogada.
     * @return true quando a transição é válida.
     */
    boolean jogarPuzzle(Direcoes direcao);

    /**
     * Identificação do estado concreto atual.
     * @return Valor do estado.
     */
    DeepSeaState getState();

    boolean recolherMinerio();

    boolean entrarOficina();

    boolean sairOficina();

    boolean melhorarCasco();

    boolean comprarCombustivel();
}
