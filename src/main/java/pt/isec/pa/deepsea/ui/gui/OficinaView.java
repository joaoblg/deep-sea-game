package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    private final HBox boxSelecao = new HBox(12);
    private Button btnReparar;
    private Button btnCombustivel;
    private Button btnEvoluir;
    private Button btnSair;

    private final String estiloNormal =
            "-fx-background-color: linear-gradient(to bottom, #163852, #0d2133); " +
            "-fx-text-fill: #38bdf8; -fx-font-weight: bold; -fx-font-size: 13px; " +
            "-fx-border-color: #1e4d6d; -fx-border-radius: 8; -fx-background-radius: 8; " +
            "-fx-cursor: hand; -fx-padding: 10 18 10 18;";

    private final String estiloHover =
            "-fx-background-color: linear-gradient(to bottom, #1e4d6d, #163852); " +
            "-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 13px; " +
            "-fx-border-color: #38bdf8; -fx-border-radius: 8; -fx-background-radius: 8; " +
            "-fx-cursor: hand; -fx-padding: 10 18 10 18; " +
            "-fx-effect: dropshadow(gaussian, rgba(56,189,248,0.4), 10, 0.3, 0, 0);";

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
        this.setSpacing(16);
        this.setPadding(new Insets(24));
        this.setStyle("-fx-background-color: radial-gradient(center 50% 40%, radius 70%, #0d2238 0%, #050f18 100%);");

        Label titulo = new Label("⚓ OFICINA & MANUTENÇÃO NAVAL");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titulo.setStyle("-fx-text-fill: #38bdf8; -fx-effect: dropshadow(gaussian, rgba(56,189,248,0.5), 10, 0.3, 0, 0);");

        imgDrone = new ImageView(ImageManager.getImage("drone.png"));
        imgDrone.setPreserveRatio(true);
        imgDrone.setSmooth(true);
        imgDrone.setFitHeight(110);
        imgDrone.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 15, 0.4, 0, 6);");

        Label lblSelecao = new Label("SELECIONAR DRONE:");
        lblSelecao.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px; -fx-font-weight: bold; -fx-letter-spacing: 1px;");

        boxSelecao.setAlignment(Pos.CENTER);

        lblInfo = new Label();
        lblInfo.setStyle("-fx-text-fill: #f1f5f9; -fx-font-size: 14px; -fx-background-color: rgba(15, 23, 42, 0.7); -fx-padding: 10 20 10 20; -fx-background-radius: 8; -fx-border-color: rgba(56, 189, 248, 0.2); -fx-border-radius: 8;");
        lblInfo.setWrapText(true);
        lblInfo.setAlignment(Pos.CENTER);
        lblInfo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        VBox boxAcoes = new VBox(10);
        boxAcoes.setAlignment(Pos.CENTER);
        boxAcoes.setMaxWidth(420);

        btnReparar = new Button("🔧 Reparar Casco (Combustível Navio ➔ +20% Casco)");
        btnReparar.setPrefWidth(400);
        aplicarEstiloBotao(btnReparar);

        btnCombustivel = new Button("⛽ Abastecer Drone (Combustível Navio ➔ Drone)");
        btnCombustivel.setPrefWidth(400);
        aplicarEstiloBotao(btnCombustivel);

        btnEvoluir = new Button("⚡ Evoluir Drone (3 Minérios ➔ Tanque + Blindagem)");
        btnEvoluir.setPrefWidth(400);
        aplicarEstiloBotao(btnEvoluir);

        btnSair = new Button("🚪 Sair da Oficina (Voltar à Superfície)");
        btnSair.setPrefWidth(400);
        aplicarEstiloBotao(btnSair);

        boxAcoes.getChildren().addAll(btnReparar, btnCombustivel, btnEvoluir, btnSair);

        this.getChildren().addAll(
                titulo, imgDrone, lblSelecao, boxSelecao, lblInfo, boxAcoes);
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
            if (getParent() != null) {
                getParent().requestFocus();
            }
        });
    }

    private void update() {
        atualizarBotoesSelecao();
        int numero = manager.getNumeroDroneAtivo();
        String navioInfo = String.format("🚢 Navio • Minérios em Reserva: %d", manager.getMineriosNavio());

        if (manager.hasDroneAtivo()) {
            imgDrone.setImage(ImageManager.getImage(manager.getNomeImagemDroneAtivo()));

            lblInfo.setText(navioInfo + String.format(
                    "\n⚡ Drone #%d ➔ Combustível: %d | Integridade Casco: %d%%",
                    numero,
                    manager.getCombustivelDroneAtivo(),
                    manager.getIntegridadeCascoDroneAtivo()));
        } else {
            lblInfo.setText(navioInfo + "\n⚪ Nenhum drone selecionado no momento.");
        }
    }

    private void atualizarBotoesSelecao() {
        boxSelecao.getChildren().clear();
        int selecionado = manager.getNumeroDroneAtivo();

        for (int numero : manager.getNumerosDronesVivos()) {
            Button btn = new Button("Drone #" + numero);
            btn.setPrefWidth(100);

            if (numero == selecionado) {
                btn.setStyle("-fx-background-color: linear-gradient(to bottom, #0284c7, #0369a1); -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-border-color: #38bdf8; -fx-border-radius: 6; -fx-background-radius: 6; -fx-effect: dropshadow(gaussian, rgba(56,189,248,0.5), 8, 0.3, 0, 0);");
            } else {
                btn.setStyle("-fx-background-color: #0b1a29; -fx-text-fill: #94a3b8; -fx-border-color: #1e3a5f; -fx-border-radius: 6; -fx-background-radius: 6;");
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
