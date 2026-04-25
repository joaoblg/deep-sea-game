package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Jogo;

public class EstadoSetup extends DeepSeaStateAdapter {

    public EstadoSetup(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.SETUP;
    }

    @Override
    public boolean iniciarJogo() {
        changeState(DeepSeaState.NAVIO.createState(context, modelo));
        return true;
    }
}