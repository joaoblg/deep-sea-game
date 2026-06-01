package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

/**
 * StackPane que organiza as diferentes vistas do jogo consoante o estado.
 */
public class DeepSeaBoard extends StackPane {
    private final DeepSeaManager manager;
    private SuperficieView superficieView;
    private DescidaView descidaView;
    private FundoMarView fundoMarView;
    private OficinaView oficinaView;
    private FimJogoView fimJogoView;
    private PuzzleView puzzleView;

    /**
     * @param manager Facade do modelo
     */
    public DeepSeaBoard(DeepSeaManager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.superficieView = new SuperficieView(manager);
        this.descidaView = new DescidaView(manager);
        this.fundoMarView = new FundoMarView(manager, this);
        this.puzzleView = new PuzzleView(manager);
        this.oficinaView = new OficinaView(manager);
        this.fimJogoView = new FimJogoView(manager);

        this.getChildren().addAll(superficieView, descidaView, fundoMarView, puzzleView, oficinaView, fimJogoView);
        this.setFocusTraversable(true);
    }

    private void registerHandlers() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt ->
                Platform.runLater(this::update));
        widthProperty().addListener((obs, oldVal, newVal) -> Platform.runLater(this::update));
        heightProperty().addListener((obs, oldVal, newVal) -> Platform.runLater(this::update));
    }

    private void update() {
        DeepSeaState estado = manager.getState();
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
            case SUPERFICIE:
                configurarCamada(superficieView, true);
                break;
            case FOSSO:
            case DESCIDA:
            case SUBIDA:
                configurarCamada(descidaView, true);
                break;
            case FUNDO_MAR:
                configurarCamada(fundoMarView, true);
                break;
            case PUZZLE:
                configurarCamada(puzzleView, true);
                break;
            case OFICINA:
                configurarCamada(oficinaView, true);
                break;
            case FIM_JOGO:
                configurarCamada(fimJogoView, true);
                break;
        }
    }

    private void configurarCamada(Node node, boolean ativa) {
        node.setVisible(ativa);
        node.setMouseTransparent(!ativa);
        node.setDisable(!ativa);
        node.setFocusTraversable(ativa);
    }
}
