package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.Constantes;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.data.Navio;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

/**
 * Estado da oficina do navio: reparações e reabastecimento dos drones.
 */
public class EstadoOficina extends DeepSeaStateAdapter {

    public EstadoOficina(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.OFICINA;
    }

    @Override
    public boolean sairOficina() {
        changeState(DeepSeaState.SUPERFICIE);
        return true;
    }

    @Override
    public boolean melhorarCasco() {
        Drone drone = context.getDrone();
        Navio navio = modelo.getNavio();
        if (drone == null) {
            return false;
        }
        if (navio.gastarCombustivel(Constantes.CUSTO_COMBUSTIVEL_REPARACAO)) {
            drone.repararCasco(20);
            return true;
        }
        return false;
    }

    @Override
    public boolean comprarCombustivel() {
        Drone drone = context.getDrone();
        Navio navio = modelo.getNavio();
        if (drone == null) {
            return false;
        }
        return navio.transferirCombustivelParaDrone(drone, Constantes.COMBUSTIVEL_TRANSFERENCIA_DRONE);
    }

    @Override
    public boolean evoluirDrone() {
        Drone drone = context.getDrone();
        Navio navio = modelo.getNavio();
        if (drone == null) {
            return false;
        }
        if (navio.gastarMinerios(Constantes.CUSTO_MINERIOS_EVOLUIR)) {
            drone.evoluirCapacidadeTanque(Constantes.GANHO_CAPACIDADE_TANQUE);
            drone.evoluirBlindagem(Constantes.GANHO_BLINDAGEM_MAXIMA);
            return true;
        }
        return false;
    }
}
