package pt.isec.pa.deepsea.ui.gui;

import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.ModoJogo;
import pt.isec.pa.deepsea.model.data.Constantes;

/**
 * Vista de fim de jogo (estado {@link pt.isec.pa.deepsea.model.state.DeepSeaState#FIM_JOGO}).
 */
public class FimJogoView extends VBox {

    private final DeepSeaManager manager;
    private Label titulo;
    private Label lblInfo;
    private Button btnNovo;

    /**
     * @param manager Facade do modelo.
     */
    public FimJogoView(DeepSeaManager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(18);
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 65%, #0f1d2e 0%, #03080e 100%);");

        titulo = new Label("FIM DA EXPEDIÇÃO");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));

        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(520);
        card.setStyle("-fx-background-color: rgba(15, 23, 42, 0.85); -fx-border-color: rgba(56, 189, 248, 0.3); -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 24;");

        lblInfo = new Label();
        lblInfo.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 14px; -fx-line-spacing: 4px;");
        lblInfo.setWrapText(true);
        lblInfo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        btnNovo = new Button("🚀 Iniciar Nova Expedição");
        btnNovo.setPrefSize(260, 42);
        btnNovo.setStyle("-fx-background-color: linear-gradient(to bottom, #0284c7, #0369a1); -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 14px; -fx-border-color: #38bdf8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(56,189,248,0.4), 10, 0.3, 0, 0);");

        btnNovo.setOnMouseEntered(e -> btnNovo.setStyle("-fx-background-color: linear-gradient(to bottom, #0369a1, #0284c7); -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 14px; -fx-border-color: #7dd3fc; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(56,189,248,0.6), 15, 0.4, 0, 0);"));
        btnNovo.setOnMouseExited(e -> btnNovo.setStyle("-fx-background-color: linear-gradient(to bottom, #0284c7, #0369a1); -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 14px; -fx-border-color: #38bdf8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(56,189,248,0.4), 10, 0.3, 0, 0);"));

        card.getChildren().addAll(lblInfo);
        this.getChildren().addAll(titulo, card, btnNovo);
    }

    private void registerHandlers() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_LOG, evt ->
                Platform.runLater(this::update));
        btnNovo.setOnAction(e -> manager.iniciarJogo());
    }

    private void update() {
        List<String> logs = manager.getLogs();
        String ultimo = logs.isEmpty() ? "Consulta o menu Log para mais detalhes." : logs.get(logs.size() - 1);
        int target = Constantes.NUM_ARTEFACTOS;
        if (ModoJogo.getTargetArtefactos() > 0) {
            target = ModoJogo.getTargetArtefactos();
        }
        if (ModoJogo.isVitoriaPorRota()) {
            target = Math.max(1, target / 2);
        }
        boolean vitoria = manager.getArtefactosNavio() >= target;

        if (vitoria) {
            titulo.setText("🏆 VITÓRIA ALCANÇADA!");
            titulo.setStyle("-fx-text-fill: #34d399; -fx-effect: dropshadow(gaussian, rgba(52,211,153,0.5), 15, 0.3, 0, 0);");
        } else {
            titulo.setText("⚠️ FIM DA EXPEDIÇÃO");
            titulo.setStyle("-fx-text-fill: #f87171; -fx-effect: dropshadow(gaussian, rgba(248,113,113,0.5), 15, 0.3, 0, 0);");
        }

        lblInfo.setText(String.format(
                "%s%n%n💎 Minérios Resgatados: %d%n🏺 Artefactos Recuperados: %d / %d%n%n📋 Último Evento:%n\"%s\"",
                vitoria ? "Parabéns! Completaste com sucesso todos os objetivos da expedição." : "A expedição marítima terminou.",
                manager.getMineriosNavio(),
                manager.getArtefactosNavio(),
                target,
                ultimo));
    }
}
