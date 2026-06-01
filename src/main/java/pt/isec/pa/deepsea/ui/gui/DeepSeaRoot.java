package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.ui.res.SoundManager;

/**
 * RootPane da UI (padrão MVC@PA). Organiza menu, área central de jogo e HUD.
 */
public class DeepSeaRoot extends BorderPane {
    private final DeepSeaManager manager;
    private DeepSeaBoard board;
    private DeepSeaHUD hud;
    private final Stage stage;
    private Stage segundaJanela;
    private DeepSeaState estadoAnterior;
    private int prevIntegridadeDrone = -1;
    private int prevFossoLinha = -1;
    private int prevFossoColuna = -1;
    /**
     * @param manager Facade do modelo
     * @param stage Janela principal
     */
    public DeepSeaRoot(DeepSeaManager manager, Stage stage) {
        this.manager = manager;
        this.stage = stage;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        SoundManager sm = SoundManager.getInstance();
        sm.loadSound("move_navio", "mollyroselee-ocean-waves-crashing-the-shoreline-423649.mp3");
        sm.loadSound("move_drone", "dominiquelacaud-submarine-289985.mp3");
        sm.loadSound("hit_animal", "hit_animal.mp3");
        sm.loadSound("destruicao", "destruicao.mp3");
        sm.loadSound("collect", "collect.mp3");
        sm.loadSound("sucesso", "sucesso.mp3");
        sm.loadSound("corrente", "corrente.mp3");
        sm.loadSound("consertar", "arranjaDrone.mp3");

        this.board = new DeepSeaBoard(manager);
        this.hud = new DeepSeaHUD(manager);

        this.setTop(new DeepSeaMenuBar(manager, stage, this));
        this.setCenter(board);
        this.setLeft(hud.getPainelEsquerdo());
        this.setRight(hud.getPainelDireito());
    }

    private void registerHandlers() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt -> {
            DeepSeaState novoEstado = manager.getState();
            SoundManager sm = SoundManager.getInstance();

            if (novoEstado == DeepSeaState.FIM_JOGO && estadoAnterior != DeepSeaState.FIM_JOGO) {
                sm.playActionSound("destruicao");
            }
            if (novoEstado == DeepSeaState.SUPERFICIE &&
                    (estadoAnterior == DeepSeaState.FOSSO || estadoAnterior == DeepSeaState.FUNDO_MAR)) {
                // parar imediatamente o som de movimento do drone/descida e tocar sucesso
                sm.stopMoveSound();
                sm.playActionSound("sucesso");
            }
            // Puzzle resolved -> FUNDO_MAR: tocar sucesso
            if (novoEstado == DeepSeaState.FUNDO_MAR && estadoAnterior == DeepSeaState.PUZZLE) {
                SoundManager.getInstance().playActionSound("sucesso");
            }
            estadoAnterior = novoEstado;
            Platform.runLater(this::update);
        });

        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt ->
                Platform.runLater(() -> {
                    // detectar dano ao drone e tocar hit_animal
                    int atual = manager.getIntegridadeCascoDroneAtivo();
                    if (prevIntegridadeDrone != -1 && atual < prevIntegridadeDrone) {
                        SoundManager.getInstance().playActionSound("hit_animal");
                    }
                    prevIntegridadeDrone = atual;

                    // tocar som de corrente quando o drone entra numa célula de corrente no fosso
                    if (manager.getState() == DeepSeaState.FOSSO) {
                        int linha = manager.getLinhaDrone();
                        int coluna = manager.getColunaDrone();
                        if (linha != prevFossoLinha || coluna != prevFossoColuna) {
                            String elem = manager.getElemFosso(linha, coluna);
                            if ("CorrenteMarinha".equals(elem)) {
                                SoundManager.getInstance().playActionSound("corrente");
                            }
                            prevFossoLinha = linha;
                            prevFossoColuna = coluna;
                        }
                    } else {
                        prevFossoLinha = -1;
                        prevFossoColuna = -1;
                    }

                    update();
                }));
    }

    private void update() {
        restaurarFocoJogo();
    }

    /**
     * Abre uma segunda janela com o mesmo conteúdo da janela principal.
     */
    public void abrirSegundaJanela() {
        if (segundaJanela != null && segundaJanela.isShowing()) {
            segundaJanela.requestFocus();
            return;
        }
        segundaJanela = new Stage();
        segundaJanela.setTitle("Deep Sea Mining - Segunda Janela");
        segundaJanela.setWidth(1024);
        segundaJanela.setHeight(768);

        DeepSeaRoot secondRoot = new DeepSeaRoot(manager, segundaJanela);
        secondRoot.setTop(new DeepSeaMenuBar(manager, segundaJanela, secondRoot));

        Scene scene = new Scene(secondRoot);
        segundaJanela.setScene(scene);
        secondRoot.registarTeclado(scene);
        segundaJanela.show();
    }

    /**
     * Regista o handler de teclado na cena.
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

        SoundManager sm = SoundManager.getInstance();

        switch (event.getCode()) {
            case UP -> {
                if (estado == DeepSeaState.PUZZLE) {
                    manager.jogarPuzzle(Direcoes.CIMA);
                } else if (estado != DeepSeaState.OFICINA && estado != DeepSeaState.FIM_JOGO) {
                    String sound = estado == DeepSeaState.SUPERFICIE ? "move_navio" : "move_drone";
                    sm.playMoveSound(sound);
                    manager.mover(Direcoes.CIMA);
                }
                event.consume();
            }
            case DOWN -> {
                if (estado == DeepSeaState.PUZZLE) {
                    manager.jogarPuzzle(Direcoes.BAIXO);
                } else if (estado != DeepSeaState.OFICINA && estado != DeepSeaState.FIM_JOGO) {
                    String sound = estado == DeepSeaState.SUPERFICIE ? "move_navio" : "move_drone";
                    sm.playMoveSound(sound);
                    manager.mover(Direcoes.BAIXO);
                }
                event.consume();
            }
            case LEFT -> {
                if (estado == DeepSeaState.PUZZLE) {
                    manager.jogarPuzzle(Direcoes.ESQUERDA);
                } else if (estado != DeepSeaState.OFICINA && estado != DeepSeaState.FIM_JOGO) {
                    String sound = estado == DeepSeaState.SUPERFICIE ? "move_navio" : "move_drone";
                    sm.playMoveSound(sound);
                    manager.mover(Direcoes.ESQUERDA);
                }
                event.consume();
            }
            case RIGHT -> {
                if (estado == DeepSeaState.PUZZLE) {
                    manager.jogarPuzzle(Direcoes.DIREITA);
                } else if (estado != DeepSeaState.OFICINA && estado != DeepSeaState.FIM_JOGO) {
                    String sound = estado == DeepSeaState.SUPERFICIE ? "move_navio" : "move_drone";
                    sm.playMoveSound(sound);
                    manager.mover(Direcoes.DIREITA);
                }
                event.consume();
            }
            case SPACE -> {
                if (estado == DeepSeaState.FUNDO_MAR) {
                    manager.avaliarFundo();
                    event.consume();
                } else if (estado == DeepSeaState.SUPERFICIE) {
                    manager.iniciarMergulho(manager.getLinhaNavio(), manager.getColunaNavio());
                    event.consume();
                }

            }
            case R -> {
                if (estado == DeepSeaState.FUNDO_MAR) {
                    boolean ok = manager.recolherMinerio();
                    if (ok) {
                        SoundManager.getInstance().playActionSound("collect");
                    }
                    event.consume();
                }
            }
            case V -> {
                if (estado == DeepSeaState.OFICINA) {
                    manager.sairOficina();
                } else {
                    sm.stopMoveSound();
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

    private void restaurarFocoJogo() {
        Platform.runLater(() -> {
            this.requestFocus();
            if (board != null) {
                board.requestFocus();
            }
        });
    }
}