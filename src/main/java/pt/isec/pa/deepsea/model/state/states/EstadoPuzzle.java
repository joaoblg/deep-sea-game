package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.Artefacto;
import pt.isec.pa.deepsea.model.data.Constantes;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.ElemFundo;
import pt.isec.pa.deepsea.model.data.FundoDoMar;
import pt.isec.pa.deepsea.model.data.PuzzleRecuperacao;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

public class EstadoPuzzle extends DeepSeaStateAdapter {

    public EstadoPuzzle(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.PUZZLE;
    }

    @Override
    public boolean jogarPuzzle(Direcoes direcao) {
        PuzzleRecuperacao puzzle = modelo.getPuzzleAtual();
        if (puzzle == null) {
            return false;
        }

        boolean moveu = puzzle.moverPeca(direcao);

        if (moveu) {
            if (puzzle.verificarVitoria()) {
                FundoDoMar fundo = modelo.getFundo();
                ElemFundo elemento = fundo.getConteudo(
                        context.getDrone().getLinhaPos(),
                        context.getDrone().getColunaPos());

                if (elemento instanceof Artefacto art) {
                    context.getDrone().getArtefactosTransp().add(art.getIdArtefacto());
                    fundo.removerConteudo(context.getDrone().getLinhaPos(), context.getDrone().getColunaPos());
                }

                modelo.setPuzzleAtual(null);
                changeState(DeepSeaState.FUNDO_MAR);

            } else if (puzzle.getMovimentos() >= Constantes.MAX_MOV_PUZZLE) {
                modelo.setPuzzleAtual(null);
                context.getDrone().prepararPosicaoInicial("SUBIDA");
                changeState(DeepSeaState.SUBIDA);
            }
            return true;
        }
        return false;
    }
}
