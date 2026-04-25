package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.Artefacto;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.data.ElemFundo;
import pt.isec.pa.deepsea.model.data.FundoDoMar;

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
        if (drone == null) return false;

        int novaLinha = drone.getLinhaPos();
        int novaColuna = drone.getColunaPos();

        switch (direcao) {
            case CIMA -> novaLinha--;
            case BAIXO -> novaLinha++;
            case ESQUERDA -> novaColuna--;
            case DIREITA -> novaColuna++;
        }

        drone.setPosicao(novaLinha, novaColuna);
        drone.consumirCombustivel(1);

        FundoDoMar fundo = modelo.getNavio().getSuperficie().getCelula(modelo.getNavio().getLinhaPos(), modelo.getNavio().getColunaPos()).getFundoM();
        fundo.revelar(novaLinha, novaColuna);

        ElemFundo elemento = fundo.getConteudo(novaLinha, novaColuna);
        if (elemento != null) {
            elemento.interage(drone);
        }

        // --- VERIFICAÇÃO DE MORTE ---
        if (drone.getIntegridadeCasco() <= 0 || drone.getCombustivel() <= 0) {
            changeState(DeepSeaState.NAVIO.createState(context, modelo));
            return true;
        }

        return true;
    }

    @Override
    public boolean avaliarFundo() {
        Drone drone = context.getDrone();
        if (drone == null) return false;

        FundoDoMar fundo = modelo.getSuperficie().getCelula(modelo.getNavio().getLinhaPos(), modelo.getNavio().getColunaPos()).getFundoM();
        ElemFundo elemento = fundo.getConteudo(drone.getLinhaPos(), drone.getColunaPos());

        if (elemento instanceof Artefacto) {
            changeState(DeepSeaState.PUZZLE.createState(context, modelo));
            return true;
        }
        return false;
    }

    @Override
    public boolean voltarAoNavio() {
        Drone drone = context.getDrone();
        if (drone != null) {
            drone.prepararPosicaoInicial("SUBIDA");
            changeState(DeepSeaState.SUBIDA.createState(context, modelo));
            return true;
        }
        return false;
    }
}