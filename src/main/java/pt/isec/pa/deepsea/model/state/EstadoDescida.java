package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.Drone;

public class EstadoDescida extends DeepSeaStateAdapter {

    public EstadoDescida(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.DESCIDA;
    }

    @Override
    public boolean mover(Direcoes direcao) {
        Drone drone = context.getDrone();
        if (drone == null) return false;

        if (direcao == Direcoes.BAIXO) {
            int novaLinha = drone.getLinhaPos() + 1;

            drone.setPosicao(novaLinha, drone.getColunaPos());
            drone.consumirCombustivel(1);

            // Se chegou ao fundo VIVO
            if (novaLinha >= 10 && drone.getIntegridadeCasco() > 0 && drone.getCombustivel() > 0) {
                changeState(DeepSeaState.FUNDO_MAR.createState(context, modelo));
                return true;
            }

            // --- VERIFICAÇÃO DE MORTE ---
            if (drone.getIntegridadeCasco() <= 0 || drone.getCombustivel() <= 0) {
                changeState(DeepSeaState.NAVIO.createState(context, modelo));
                return true;
            }
            return true;
        }
        return false;
    }
}