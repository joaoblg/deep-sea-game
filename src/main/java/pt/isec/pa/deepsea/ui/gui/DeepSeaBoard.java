package pt.isec.pa.deepsea.ui.gui;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

/**
 * Área central do jogo: {@link StackPane} com vistas Canvas sobrepostas.
 * Apenas a vista do estado ativo permanece visível.
 */
public class DeepSeaBoard extends StackPane {
    private DeepSeaManager manager;
    private SuperficieView superficieView;
    private DescidaView descidaView;
    private FundoMarView fundoMarView;
    private OficinaView oficinaView;
    private FimJogoView fimJogoView;
    private PuzzleView puzzleView;

    /**
     * @param manager Facade do modelo.
     */
    public DeepSeaBoard(DeepSeaManager manager) {
        this.manager = manager;
        
        this.superficieView = new SuperficieView(manager);
        this.superficieView.widthProperty().bind(this.widthProperty());
        this.superficieView.heightProperty().bind(this.heightProperty());

        this.descidaView = new DescidaView(manager);
        this.descidaView.widthProperty().bind(this.widthProperty());
        this.descidaView.heightProperty().bind(this.heightProperty());

        this.fundoMarView = new FundoMarView(manager);
        this.fundoMarView.widthProperty().bind(this.widthProperty());
        this.fundoMarView.heightProperty().bind(this.heightProperty());

        this.puzzleView = new PuzzleView(manager);
        this.puzzleView.widthProperty().bind(this.widthProperty());
        this.puzzleView.heightProperty().bind(this.heightProperty());

        this.oficinaView = new OficinaView(manager);
        this.fimJogoView = new FimJogoView(manager);

        this.getChildren().addAll(superficieView, descidaView, fundoMarView, puzzleView, oficinaView, fimJogoView);
        this.setFocusTraversable(true);
    }

    private void configurarCamada(Node node, boolean ativa) {
        node.setVisible(ativa);
        node.setMouseTransparent(!ativa);
        node.setDisable(!ativa);
        node.setFocusTraversable(ativa);
    }

    /**
     * Atualiza a visibilidade das vistas consoante o estado lógico do jogo.
     * @param estado Estado atual; ignorado se for {@code null}.
     */
    public void atualizar(DeepSeaState estado) {
        if (estado == null) {
            return;
        }

        configurarCamada(superficieView, false);
        configurarCamada(descidaView, false);
        configurarCamada(fundoMarView, false);
        configurarCamada(puzzleView, false);
        configurarCamada(oficinaView, false);
        configurarCamada(fimJogoView, false);

        switch (estado) {
            case SUPERFICIE -> {
                configurarCamada(superficieView, true);
                superficieView.desenhar();
            }
            case DESCIDA, SUBIDA -> {
                configurarCamada(descidaView, true);
                descidaView.desenhar();
            }
            case FUNDO_MAR -> {
                configurarCamada(fundoMarView, true);
                fundoMarView.desenhar();
            }
            case PUZZLE -> {
                configurarCamada(puzzleView, true);
                puzzleView.desenhar();
            }
            case OFICINA -> configurarCamada(oficinaView, true);
            case FIM_JOGO -> configurarCamada(fimJogoView, true);
        }
    }
}