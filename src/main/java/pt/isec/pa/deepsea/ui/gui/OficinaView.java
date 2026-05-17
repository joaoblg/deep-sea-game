package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pt.isec.pa.deepsea.model.DeepSeaManager;

/**
 * Vista da oficina do navio (estado {@link pt.isec.pa.deepsea.model.state.DeepSeaState#OFICINA}).
 */
public class OficinaView extends VBox {

    private final DeepSeaManager manager;
    private Label lblInfo;
    private final HBox boxSelecao = new HBox(10);

    /**
     * @param manager Facade do modelo.
     */
    public OficinaView(DeepSeaManager manager) {
        this.manager = manager;
        criarLayout();
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt ->
                Platform.runLater(this::atualizarInfo));
    }

    private void criarLayout() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setStyle("-fx-background-color: #2c3e50;");

        Label titulo = new Label("OFICINA DO NAVIO");
        titulo.setFont(new Font("Arial Bold", 24));
        titulo.setStyle("-fx-text-fill: white;");

        Label lblSelecao = new Label("Selecionar drone:");
        lblSelecao.setStyle("-fx-text-fill: #ecf0f1;");

        boxSelecao.setAlignment(Pos.CENTER);
        atualizarBotoesSelecao();

        lblInfo = new Label();
        lblInfo.setStyle("-fx-text-fill: #ecf0f1;");
        lblInfo.setWrapText(true);

        Button btnReparar = new Button("Reparar casco (combustível navio → +20%)");
        btnReparar.setPrefSize(300, 40);
        btnReparar.setOnAction(e -> manager.melhorarCasco());

        Button btnCombustivel = new Button("Abastecer drone (combustível navio → drone)");
        btnCombustivel.setPrefSize(300, 40);
        btnCombustivel.setOnAction(e -> manager.comprarCombustivel());

        Button btnEvoluir = new Button("Evoluir drone (3 minérios → tanque + blindagem)");
        btnEvoluir.setPrefSize(300, 40);
        btnEvoluir.setOnAction(e -> manager.evoluirDrone());

        Button btnSair = new Button("Sair da oficina");
        btnSair.setPrefSize(300, 40);
        btnSair.setOnAction(e -> {
            manager.sairOficina();
            getParent().requestFocus();
        });

        this.getChildren().addAll(
                titulo, lblSelecao, boxSelecao, lblInfo,
                btnReparar, btnCombustivel, btnEvoluir, btnSair);
        atualizarInfo();
    }

    private void atualizarBotoesSelecao() {
        boxSelecao.getChildren().clear();
        int num = manager.getNumDrones();
        int selecionado = manager.getIndiceDroneSelecionado();
        for (int i = 1; i <= num; i++) {
            final int indice = i;
            Button btn = new Button("Drone " + i);
            btn.setPrefWidth(90);
            if (i == selecionado) {
                btn.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");
            }
            btn.setOnAction(e -> manager.selecionarDrone(indice));
            boxSelecao.getChildren().add(btn);
        }
    }

    private void atualizarInfo() {
        atualizarBotoesSelecao();
        var drone = manager.getDrone();
        String navioInfo = String.format("Navio — Minérios: %d", manager.getMineriosNavio());
        if (drone != null) {
            lblInfo.setText(navioInfo + String.format(
                    "%nDrone %d — Comb.: %d | Casco: %d%%",
                    manager.getIndiceDroneSelecionado(),
                    drone.getCombustivel(),
                    drone.getIntegridadeCasco()));
        } else {
            lblInfo.setText(navioInfo + "\nSem drone disponível.");
        }
    }
}
