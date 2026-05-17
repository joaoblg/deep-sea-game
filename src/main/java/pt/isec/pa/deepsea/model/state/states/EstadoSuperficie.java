package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.Constantes;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.ExpedicaoHelper;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

public class EstadoSuperficie extends DeepSeaStateAdapter {

    public EstadoSuperficie(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.SUPERFICIE;
    }

    @Override
    public boolean entrarOficina() {
        changeState(DeepSeaState.OFICINA);
        return true;
    }

    @Override
    public boolean iniciarMergulho(int linha, int coluna) {
        if (modelo.getSuperficie().getCelula(linha, coluna) == null) {
            return false;
        }

        modelo.getNavio().setPosicao(linha, coluna);
        modelo.setZonaExpedicao(linha, coluna);

        if (context.getDrone() != null) {
            context.getDrone().prepararPosicaoInicial("DESCIDA");
            context.getDrone().reiniciarExpedicao();
            modelo.getSuperficie().getCelula(linha, coluna).getFossoM().gerarObstaculos();
            changeState(DeepSeaState.DESCIDA);
            return true;
        }
        return false;
    }

    @Override
    public boolean evoluirDrone() {
        return false;
    }

    @Override
    public boolean mover(Direcoes direcao) {
        if (modelo.getNavio().getCombustivel() < Constantes.COMBUPERMOVE) {
            changeState(DeepSeaState.FIM_JOGO);
            return false;
        }

        int linhaAtual = modelo.getNavio().getLinhaPos();
        int colunaAtual = modelo.getNavio().getColunaPos();
        int maxLin = modelo.getSuperficie().getLinhas();
        int maxCol = modelo.getSuperficie().getColunas();

        int novaLin = linhaAtual;
        int novaCol = colunaAtual;

        switch (direcao) {
            case CIMA -> novaLin--;
            case BAIXO -> novaLin++;
            case ESQUERDA -> novaCol--;
            case DIREITA -> novaCol++;
        }

        if (novaLin >= 0 && novaLin < maxLin && novaCol >= 0 && novaCol < maxCol) {
            modelo.getNavio().setPosicao(novaLin, novaCol);
            modelo.getNavio().gastarCombustivel(Constantes.COMBUPERMOVE);
            if (modelo.getNavio().getCombustivel() <= 0) {
                changeState(DeepSeaState.FIM_JOGO);
            }
            return true;
        }

        return false;
    }
}
