package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.Direcoes;

public class EstadoPuzzle extends DeepSeaStateAdapter {

    public EstadoPuzzle(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.PUZZLE;
    }

    @Override
    public boolean jogarPuzzle(Direcoes direcao) {
        changeState(DeepSeaState.FUNDO_MAR.createState(context, modelo));
        return true;
    }
}