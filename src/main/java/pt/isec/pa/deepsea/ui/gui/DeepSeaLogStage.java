package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;

/**
 * Janela secundária para visualização dos logs do jogo.
 */
public class DeepSeaLogStage {

    private final DeepSeaManager manager;
    private final Stage stage;
    private final TextArea textArea;

    /**
     * @param manager Facade do modelo
     */
    public DeepSeaLogStage(DeepSeaManager manager) {
        this.manager = manager;
        this.stage = new Stage();
        this.textArea = new TextArea();
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(500, 400);

        stage.setTitle("Logs - Deep Sea Mining");
        stage.setScene(new Scene(textArea));
    }

    private void registerHandlers() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_LOG, evt ->
                Platform.runLater(this::update));
    }

    private void update() {
        textArea.setText(String.join("\n", manager.getLogs()));
    }

    /** Alterna a visibilidade da janela de logs. */
    public void toggle() {
        if (stage.isShowing()) {
            stage.hide();
        } else {
            update();
            stage.show();
        }
    }
}
