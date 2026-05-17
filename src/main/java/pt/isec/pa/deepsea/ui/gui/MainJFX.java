package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;

/**
 * Ponto de entrada da interface gráfica JavaFX do Deep Sea Mining.
 */
public class MainJFX extends Application {
    private DeepSeaManager manager;

    @Override
    public void init() throws Exception {
        super.init();
        manager = new DeepSeaManager();
    }

    @Override
    public void start(Stage stage) {
        DeepSeaRoot root = new DeepSeaRoot(manager, stage);
        Scene scene = new Scene(root, 1024, 768);

        stage.setTitle("Deep Sea Mining");
        stage.setScene(scene);
        root.registarTeclado(scene);
        stage.show();

        root.requestFocus();
    }
}