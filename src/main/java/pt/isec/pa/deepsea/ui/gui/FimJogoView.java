package pt.isec.pa.deepsea.ui.gui;

import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.ModoJogo;
import pt.isec.pa.deepsea.model.data.Constantes;

/**
 * Vista de fim de jogo (estado {@link pt.isec.pa.deepsea.model.state.DeepSeaState#FIM_JOGO}).
 */
public class FimJogoView extends VBox {

    private final DeepSeaManager manager;
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
        this.setSpacing(20);
        this.setStyle("-fx-background-color: #000000;");

        Label titulo = new Label("FIM DE JOGO");
        titulo.setFont(new Font("Arial Bold", 36));
        titulo.setStyle("-fx-text-fill: red;");

        lblInfo = new Label();
        lblInfo.setStyle("-fx-text-fill: white;");
        lblInfo.setWrapText(true);
        lblInfo.setMaxWidth(500);

        btnNovo = new Button("Novo jogo");

        this.getChildren().addAll(titulo, lblInfo, btnNovo);
    }

    private void registerHandlers() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_LOG, evt ->
                Platform.runLater(this::update));
        btnNovo.setOnAction(e -> manager.iniciarJogo());
    }

    private void update() {
        List<String> logs = manager.getLogs();
        String ultimo = logs.isEmpty() ? "Consulta o menu Log para detalhes." : logs.get(logs.size() - 1);
        int target = Constantes.NUM_ARTEFACTOS;
        if (ModoJogo.getTargetArtefactos() > 0) {
            target = ModoJogo.getTargetArtefactos();
        }
        if (ModoJogo.isVitoriaPorRota()) {
            target = Math.max(1, target / 2);
        }
        boolean vitoria = manager.getArtefactosNavio() >= target;
        lblInfo.setText(String.format(
                "%s%nMinérios no navio: %d | Artefactos: %d / %d%nÚltimo evento: %s",
                vitoria ? "Vitória! Coleção completa." : "Missão terminada.",
                manager.getMineriosNavio(),
                manager.getArtefactosNavio(),
                target,
                ultimo));
    }
}
