package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.ui.res.ImageManager;
import pt.isec.pa.deepsea.ui.res.SoundManager;

/**
 * Vista da oficina do navio (estado {@link pt.isec.pa.deepsea.model.state.DeepSeaState#OFICINA}).
 */
public class OficinaView extends VBox {

    private final DeepSeaManager manager;
    private Label lblInfo;
    private ImageView imgDrone;
    private final HBox boxSelecao = new HBox(10);
    private Button btnReparar;
    private Button btnCombustivel;
    private Button btnEvoluir;
    private Button btnSair;

    private final String estiloNormal = "-fx-background-color: #1b263b; -fx-text-fill: #00d2ff; -fx-font-weight: bold; -fx-border-color: #00d2ff; -fx-border-radius: 5; -fx-background-radius: 5;";
    private final String estiloHover = "-fx-background-color: #00d2ff; -fx-text-fill: #1b263b; -fx-font-weight: bold; -fx-border-color: #00d2ff; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;";

    /**
     * @param manager Facade do modelo.
     */
    public OficinaView(DeepSeaManager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setStyle("-fx-background-color: #0d1b2a;");

        imgDrone = new ImageView(ImageManager.getImage("drone.png"));
        imgDrone.setPreserveRatio(true);
        imgDrone.setSmooth(true);
        imgDrone.setFitHeight(120);

        Label titulo = new Label("OFICINA DO NAVIO");
        titulo.setFont(new Font("Arial Bold", 26));
        titulo.setStyle("-fx-text-fill: #3eb4e8; -fx-effect: dropshadow(gaussian, #3eb4e8, 4, 0.2, 0, 0);");

        Label lblSelecao = new Label("Selecionar drone:");
        lblSelecao.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");

        boxSelecao.setAlignment(Pos.CENTER);

        lblInfo = new Label();
        lblInfo.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
        lblInfo.setWrapText(true);
        lblInfo.setAlignment(Pos.CENTER);
        lblInfo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        btnReparar = new Button("Reparar casco (combustível navio → +20%)");
        btnReparar.setPrefSize(350, 40);
        aplicarEstiloBotao(btnReparar);

        btnCombustivel = new Button("Abastecer drone (combustível navio → drone)");
        btnCombustivel.setPrefSize(350, 40);
        aplicarEstiloBotao(btnCombustivel);

        btnEvoluir = new Button("Evoluir drone (3 minérios → tanque + blindagem)");
        btnEvoluir.setPrefSize(350, 40);
        aplicarEstiloBotao(btnEvoluir);

        btnSair = new Button("Sair da oficina");
        btnSair.setPrefSize(350, 40);
        aplicarEstiloBotao(btnSair);

        this.getChildren().addAll(
                titulo, imgDrone, lblSelecao, boxSelecao, lblInfo,
                btnReparar, btnCombustivel, btnEvoluir, btnSair);
    }

    private void registerHandlers() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt ->
                Platform.runLater(this::update));
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt ->
                Platform.runLater(this::update));

        btnReparar.setOnAction(e -> {
            manager.melhorarCasco();
            SoundManager.getInstance().playActionSound("consertar");
        });
        btnCombustivel.setOnAction(e -> manager.comprarCombustivel());
        btnEvoluir.setOnAction(e -> manager.evoluirDrone());
        btnSair.setOnAction(e -> {
            manager.sairOficina();
            getParent().requestFocus();
        });
    }

    private void update() {
        atualizarBotoesSelecao();
        int numero = manager.getNumeroDroneAtivo();
        String navioInfo = String.format("Navio — Minérios: %d", manager.getMineriosNavio());

        if (manager.hasDroneAtivo()) {
            imgDrone.setImage(ImageManager.getImage(manager.getNomeImagemDroneAtivo()));

            lblInfo.setText(navioInfo + String.format(
                    "\nDrone %d — Combustível: %d | Casco: %d%%",
                    numero,
                    manager.getCombustivelDroneAtivo(),
                    manager.getIntegridadeCascoDroneAtivo()));
        } else {
            lblInfo.setText(navioInfo + "\nSem drone disponível.");
        }
    }

    private void atualizarBotoesSelecao() {
        boxSelecao.getChildren().clear();
        int selecionado = manager.getNumeroDroneAtivo();

        for (int numero : manager.getNumerosDronesVivos()) {
            Button btn = new Button("Drone " + numero);
            btn.setPrefWidth(90);

            if (numero == selecionado) {
                btn.setStyle("-fx-background-color: #3eb4e8; -fx-text-fill: #1b263b; -fx-font-weight: bold; -fx-background-radius: 5;");
            } else {
                btn.setStyle("-fx-background-color: #1b263b; -fx-text-fill: #ecf0f1; -fx-border-color: #3a506b; -fx-border-radius: 5; -fx-background-radius: 5;");
            }

            btn.setOnAction(e -> manager.selecionarDrone(numero));
            boxSelecao.getChildren().add(btn);
        }
    }

    private void aplicarEstiloBotao(Button btn) {
        btn.setStyle(estiloNormal);
        btn.setOnMouseEntered(e -> btn.setStyle(estiloHover));
        btn.setOnMouseExited(e -> btn.setStyle(estiloNormal));
    }
}
