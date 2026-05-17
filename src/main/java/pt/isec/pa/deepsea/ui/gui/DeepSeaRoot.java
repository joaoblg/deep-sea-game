package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.data.Direcoes;

/**
 * RootPane da UI (padrão MVC@PA).
 * Organiza menu, área central de jogo ({@link DeepSeaBoard}) e HUD informativo.
 */
public class DeepSeaRoot extends BorderPane {
    private DeepSeaManager manager;
    private DeepSeaBoard board;
    private DeepSeaHUD hud;

    /**
     * @param manager Facade do modelo.
     * @param stage Janela principal para diálogos do menu.
     */
    public DeepSeaRoot(DeepSeaManager manager, Stage stage) {
        this.manager = manager;
        this.board = new DeepSeaBoard(manager);
        this.hud = new DeepSeaHUD(manager);
        criarLayout(stage);
        registarListeners();
        atualizarEcra();
    }

    private void criarLayout(Stage stage) {
        this.setTop(new DeepSeaMenuBar(manager, stage));
        this.setCenter(board);
        this.setLeft(hud.getPainelEsquerdo());
        this.setRight(hud.getPainelDireito());
    }

    private void registarListeners() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt ->
                Platform.runLater(this::atualizarEcra));
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt ->
                Platform.runLater(this::atualizarEcra));
    }

    /**
     * Regista o handler de teclado na cena (funciona mesmo após foco nos botões da oficina).
     */
    public void registarTeclado(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        this.setFocusTraversable(true);
    }

    private void onKeyPressed(KeyEvent event) {
        if (event.isConsumed()) {
            return;
        }

        DeepSeaState estado = manager.getState();
        if (estado == null) {
            return;
        }

        switch (event.getCode()) {
            case UP -> {
                if (estado == DeepSeaState.PUZZLE) {
                    manager.jogarPuzzle(Direcoes.CIMA);
                } else if (estado != DeepSeaState.OFICINA && estado != DeepSeaState.FIM_JOGO) {
                    manager.mover(Direcoes.CIMA);
                }
                event.consume();
            }
            case DOWN -> {
                if (estado == DeepSeaState.PUZZLE) {
                    manager.jogarPuzzle(Direcoes.BAIXO);
                } else if (estado != DeepSeaState.OFICINA && estado != DeepSeaState.FIM_JOGO) {
                    manager.mover(Direcoes.BAIXO);
                }
                event.consume();
            }
            case LEFT -> {
                if (estado == DeepSeaState.PUZZLE) {
                    manager.jogarPuzzle(Direcoes.ESQUERDA);
                } else if (estado != DeepSeaState.OFICINA && estado != DeepSeaState.FIM_JOGO) {
                    manager.mover(Direcoes.ESQUERDA);
                }
                event.consume();
            }
            case RIGHT -> {
                if (estado == DeepSeaState.PUZZLE) {
                    manager.jogarPuzzle(Direcoes.DIREITA);
                } else if (estado != DeepSeaState.OFICINA && estado != DeepSeaState.FIM_JOGO) {
                    manager.mover(Direcoes.DIREITA);
                }
                event.consume();
            }
            case SPACE -> {
                if (estado == DeepSeaState.FUNDO_MAR) {
                    manager.avaliarFundo();
                    event.consume();
                }
            }
            case R -> {
                if (estado == DeepSeaState.FUNDO_MAR) {
                    manager.recolherMinerio();
                    event.consume();
                }
            }
            case V -> {
                if (estado == DeepSeaState.OFICINA) {
                    manager.sairOficina();
                } else {
                    manager.voltarAoNavio();
                }
                event.consume();
            }
            case O -> {
                if (estado == DeepSeaState.SUPERFICIE) {
                    manager.entrarOficina();
                    event.consume();
                }
            }
            case DIGIT1 -> {
                if (estado == DeepSeaState.SUPERFICIE || estado == DeepSeaState.OFICINA) {
                    manager.selecionarDrone(1);
                    event.consume();
                }
            }
            case DIGIT2 -> {
                if (estado == DeepSeaState.SUPERFICIE || estado == DeepSeaState.OFICINA) {
                    manager.selecionarDrone(2);
                    event.consume();
                }
            }
            case DIGIT3 -> {
                if (estado == DeepSeaState.SUPERFICIE || estado == DeepSeaState.OFICINA) {
                    manager.selecionarDrone(3);
                    event.consume();
                }
            }
            case ESCAPE -> {
                if (estado == DeepSeaState.OFICINA) {
                    manager.sairOficina();
                    event.consume();
                }
            }
            default -> { }
        }
    }

    private void atualizarEcra() {
        board.atualizar(manager.getState());
        hud.atualizar();
        restaurarFocoJogo();
    }

    private void restaurarFocoJogo() {
        Platform.runLater(() -> {
            this.requestFocus();
            if (board != null) {
                board.requestFocus();
            }
        });
    }
}
