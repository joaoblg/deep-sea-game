package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

/**
 * Regras partilhadas de expedição: morte do drone, vitória e derrota.
 */
public final class ExpedicaoHelper {

    private ExpedicaoHelper() {
    }

    /**
     * Deposita loot no fundo da zona ativa, remove o drone e devolve o próximo estado.
     */
    public static DeepSeaState processarDronePerdido(Jogo modelo, Drone drone) {
        if (drone != null) {
            FundoDoMar fundo = modelo.getFundoDaZonaAtiva();
            if (fundo != null) {
                fundo.depositarLootDoDrone(drone);
                fundo.resetEscuridao();
            }
            drone.descarregarMinerios();
            drone.descarregarArtefactos();
            modelo.getNavio().removerDrone(drone);
        }
        modelo.limparZonaExpedicao();
        if (modelo.getNavio().getDrones().isEmpty()) {
            return DeepSeaState.FIM_JOGO;
        }
        return DeepSeaState.SUPERFICIE;
    }

    /**
     * @return FIM_JOGO se todos os artefactos foram recolhidos.
     */
    public static DeepSeaState verificarVitoria(Jogo modelo) {
        if (modelo.getNavio().contagemArtefactosUnicos() >= Constantes.NUM_ARTEFACTOS) {
            return DeepSeaState.FIM_JOGO;
        }
        return null;
    }

    /**
     * @return FIM_JOGO se o navio ficou sem combustível.
     */
    public static DeepSeaState verificarNavioSemCombustivel(Jogo modelo) {
        if (modelo.getNavio().getCombustivel() <= 0) {
            return DeepSeaState.FIM_JOGO;
        }
        return null;
    }

    /**
     * Prepara o fundo ao chegar da descida: monstros e célula inicial visível.
     */
    public static void prepararEntradaNoFundo(Jogo modelo, Drone drone) {
        FundoDoMar fundo = modelo.getFundoDaZonaAtiva();
        if (fundo == null || drone == null) {
            return;
        }
        fundo.gerarMonstros();
        fundo.revelar(drone.getLinhaPos(), drone.getColunaPos());
    }
}
