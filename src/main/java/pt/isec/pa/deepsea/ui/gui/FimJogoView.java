package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pt.isec.pa.deepsea.model.DeepSeaManager;

import java.util.List;

/**
 * Vista de fim de jogo (estado {@link pt.isec.pa.deepsea.model.state.DeepSeaState#FIM_JOGO}).
 */
public class FimJogoView extends VBox {

    private final DeepSeaManager manager;
    private Label lblInfo;

    /**
     * @param manager Facade do modelo.
     */
    public FimJogoView(DeepSeaManager manager) {
        this.manager = manager;
        criarLayout();
        manager.addPropertyChangeListener(DeepSeaManager.PROP_LOG, evt ->
                Platform.runLater(this::atualizarInfo));
    }

    private void criarLayout() {
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

        Button btnNovo = new Button("Novo jogo");
        btnNovo.setOnAction(e -> manager.iniciarJogo());

        this.getChildren().addAll(titulo, lblInfo, btnNovo);
        atualizarInfo();
    }

    private void atualizarInfo() {
        List<String> logs = manager.getLogs();
        String ultimo = logs.isEmpty() ? "Consulta o menu Log para detalhes." : logs.get(logs.size() - 1);
        boolean vitoria = manager.getArtefactosNavio() >= pt.isec.pa.deepsea.model.data.Constantes.NUM_ARTEFACTOS;
        lblInfo.setText(String.format(
                "%s%nMinérios no navio: %d | Artefactos: %d / %d%nÚltimo evento: %s",
                vitoria ? "Vitória! Coleção completa." : "Missão terminada.",
                manager.getMineriosNavio(),
                manager.getArtefactosNavio(),
                pt.isec.pa.deepsea.model.data.Constantes.NUM_ARTEFACTOS,
                ultimo));
    }
}
