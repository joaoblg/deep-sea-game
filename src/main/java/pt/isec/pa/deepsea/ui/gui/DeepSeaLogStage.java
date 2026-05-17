package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;

/**
 * Janela secundária para visualização dos logs do jogo.
 * Atualiza-se de forma assíncrona quando o modelo notifica {@link DeepSeaManager#PROP_LOG}.
 */
public class DeepSeaLogStage {

    private final DeepSeaManager manager;
    private final Stage stage;
    private final TextArea textArea;

    /**
     * Cria a janela de logs e regista o listener de atualização.
     * @param manager Facade do modelo.
     */
    public DeepSeaLogStage(DeepSeaManager manager) {
        this.manager = manager;
        this.textArea = new TextArea();
        this.textArea.setEditable(false);
        this.textArea.setWrapText(true);
        this.textArea.setPrefSize(500, 400);

        this.stage = new Stage();
        this.stage.setTitle("Logs - Deep Sea Mining");
        this.stage.setScene(new Scene(textArea));

        manager.addPropertyChangeListener(DeepSeaManager.PROP_LOG, evt ->
                Platform.runLater(this::atualizarConteudo));
        atualizarConteudo();
    }

    /** Alterna a visibilidade da janela de logs. */
    public void toggle() {
        if (stage.isShowing()) {
            stage.hide();
        } else {
            atualizarConteudo();
            stage.show();
        }
    }

    private void atualizarConteudo() {
        textArea.setText(String.join("\n", manager.getLogs()));
    }
}
