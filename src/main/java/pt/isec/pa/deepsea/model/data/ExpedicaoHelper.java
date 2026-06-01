package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.ModoJogo;
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
                // Em modo defesa não repor a escuridão (fog of war)
                if (!pt.isec.pa.deepsea.model.ModoJogo.isModoDefesa()) {
                    fundo.resetEscuridao();
                }
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
        int target = Constantes.NUM_ARTEFACTOS;
        if (ModoJogo.getTargetArtefactos() > 0) target = ModoJogo.getTargetArtefactos();
        if (ModoJogo.isVitoriaPorRota()) {
            // Se estiver ativado, reduzir o alvo para metade (modo de demonstração)
            target = Math.max(1, target / 2);
        }

        if (modelo.getNavio().contagemArtefactosUnicos() >= target) {
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



    public static boolean moveFosso(Jogo modelo, Direcoes direcao) {
        Drone drone = modelo.getNavio().getDroneAtivo();

        var fosso = modelo.getFosso();

        if (drone == null || fosso == null) return false;

        int linhaAtual = drone.getLinhaPos();
        int colunaAtual = drone.getColunaPos();
        int alturaFosso = fosso.getAltura();
        int larguraFosso = fosso.getLargura();

        int novaLinha = linhaAtual;
        int novaColuna = colunaAtual;

        switch (direcao) {
            case BAIXO -> novaLinha++;
            case CIMA -> novaLinha--;
            case ESQUERDA -> novaColuna--;
            case DIREITA -> novaColuna++;
            default -> { return false; }
        }

        drone.consumirCombustivel(1);

        // Bater nas paredes
        if (novaColuna < 0 || novaColuna >= larguraFosso) {
            drone.sofrerDano();
            return true;
        }

        if (novaLinha >= alturaFosso || novaLinha < 0) {
            drone.setPosicao(novaLinha, novaColuna);
            return true;

        }

        var destino = fosso.getCont(novaLinha, novaColuna);

        if (destino instanceof pt.isec.pa.deepsea.model.data.Rocha) {
            destino.interage(drone);
        } else {
            drone.setPosicao(novaLinha, novaColuna);
            if (destino != null) {
                destino.interage(drone);
            }
        }

        return true;
    }

}
