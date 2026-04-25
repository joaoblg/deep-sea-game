package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Jogo;

public class EstadoFimJogo extends DeepSeaStateAdapter {

    public EstadoFimJogo(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.FIM_JOGO;
    }
}