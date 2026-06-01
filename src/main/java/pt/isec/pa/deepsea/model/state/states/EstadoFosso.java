package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.data.ExpedicaoHelper;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

public class EstadoFosso extends DeepSeaStateAdapter {

    public EstadoFosso(DeepSeaContext context, Jogo jogo) {
        super(context, jogo);
    }

    @Override
    public boolean mover(Direcoes direcao) {
        if (ExpedicaoHelper.moveFosso(modelo, direcao)) {

            Drone drone = modelo.getNavio().getDroneAtivo();
            int alturaFosso = modelo.getFosso().getAltura();

            if (drone.getIntegridadeCasco() <= 0 || drone.getCombustivel() <= 0) {
                changeState(ExpedicaoHelper.processarDronePerdido(modelo, drone));
                return true;
            }

            if (drone.getLinhaPos() >= alturaFosso) {
                drone.prepararPosicaoInicial("FUNDO");
                ExpedicaoHelper.prepararEntradaNoFundo(modelo, drone);
                changeState(DeepSeaState.FUNDO_MAR);
            }
            else if (drone.getLinhaPos() < 0) {
                drone.prepararPosicaoInicial("NAVIO");

                // Descarrega o loot no navio
                modelo.getNavio().recolherArtefactosDoDrone(drone);
                modelo.getNavio().adicionarMinerios(drone.getMineriosTransp());
                drone.descarregarMinerios();
                drone.descarregarArtefactos();

                DeepSeaState proximo = ExpedicaoHelper.verificarVitoria(modelo);
                if (proximo != null) {
                    changeState(proximo);
                } else {
                    changeState(DeepSeaState.SUPERFICIE);
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.FOSSO;
    }
}