package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Jogo;

public class EstadoNavio extends DeepSeaStateAdapter {

    public EstadoNavio(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.NAVIO;
    }

    @Override
    public boolean iniciarMergulho(int linha, int coluna) {
        if (modelo.getSuperficie().getCelula(linha, coluna) == null) {
            return false;
        }

        modelo.getNavio().setPosicao(linha, coluna);

        if (context.getDrone() != null) {
            context.getDrone().prepararPosicaoInicial("DESCIDA");
            changeState(DeepSeaState.DESCIDA.createState(context, modelo));
            return true;
        }
        return false;
    }

    @Override
    public boolean evoluirDrone() {
        return true;
    }
}