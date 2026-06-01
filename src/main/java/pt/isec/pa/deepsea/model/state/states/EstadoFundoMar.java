package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.*;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

public class EstadoFundoMar extends DeepSeaStateAdapter {

    public EstadoFundoMar(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.FUNDO_MAR;
    }

    @Override
    public boolean mover(Direcoes direcao) {
        Drone drone = context.getDrone();
        if (drone == null) {
            return false;
        }

        int maxLinhas = modelo.getFundo().getAltura();
        int maxColunas = modelo.getFundo().getLargura();

        int novaLinha = drone.getLinhaPos();
        int novaColuna = drone.getColunaPos();

        switch (direcao) {
            case CIMA -> novaLinha--;
            case BAIXO -> novaLinha++;
            case ESQUERDA -> novaColuna--;
            case DIREITA -> novaColuna++;
        }

        if (novaLinha >= 0 && novaLinha < maxLinhas && novaColuna >= 0 && novaColuna < maxColunas) {
            drone.setPosicao(novaLinha, novaColuna);
            drone.consumirCombustivel(1);

            FundoDoMar fundo = modelo.getFundo();
            fundo.revelar(novaLinha, novaColuna);

            ElemFundo elemento = fundo.getConteudo(novaLinha, novaColuna);
            if (elemento instanceof Monstro) {
                elemento.interage(drone);
            }

            if (drone.getIntegridadeCasco() <= 0 || drone.getCombustivel() <= 0) {
                changeState(ExpedicaoHelper.processarDronePerdido(modelo, drone));
                return true;
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean avaliarFundo() {
        Drone drone = context.getDrone();
        if (drone == null) {
            return false;
        }

        FundoDoMar fundo = modelo.getFundo();
        ElemFundo elemento = fundo.getConteudo(drone.getLinhaPos(), drone.getColunaPos());

        if (elemento instanceof Artefacto) {
            modelo.setPuzzleAtual(new PuzzleRecuperacao());
            changeState(DeepSeaState.PUZZLE);
            return true;
        }
        return false;
    }

    @Override
    public boolean voltarAoNavio() {
        Drone drone = modelo.getNavio().getDroneAtivo();
        drone.prepararPosicaoInicial("SUBIDA");

        //modelo.getFosso().gerarObstaculos();


        changeState(DeepSeaState.FOSSO);
        return true;
    }

    @Override
    public boolean recolherMinerio() {
        Drone drone = context.getDrone();
        if (drone == null) {
            return false;
        }

        FundoDoMar fundo = modelo.getFundo();
        ElemFundo elemento = fundo.getConteudo(drone.getLinhaPos(), drone.getColunaPos());

        if (elemento instanceof Minerio minerio && minerio.getQuantidade() > 0) {
            int custo = Constantes.COMBUSTIVEL_EXTRA_RECOLHER_MINERIO;
            if (drone.getCombustivel() < custo) {
                return false;
            }
            drone.consumirCombustivel(custo);
            drone.adicionarMinerios(minerio.getQuantidade());
            fundo.removerConteudo(drone.getLinhaPos(), drone.getColunaPos());

            if (drone.getCombustivel() <= 0) {
                changeState(ExpedicaoHelper.processarDronePerdido(modelo, drone));
            }
            return true;
        }
        return false;
    }
}
