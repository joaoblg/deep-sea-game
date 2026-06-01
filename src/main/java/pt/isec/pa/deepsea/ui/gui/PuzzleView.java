package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Direcoes;

/**
 * Vista Canvas do minijogo de puzzle (estado PUZZLE).
 */
public class PuzzleView extends CanvasPane {
    private final DeepSeaManager manager;

    /**
     * @param manager Facade do modelo
     */
    public PuzzleView(DeepSeaManager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        // Canvas criado em CanvasPane
    }

    private void registerHandlers() {
        widthProperty().addListener((obs, oldVal, newVal) -> Platform.runLater(this::update));
        heightProperty().addListener((obs, oldVal, newVal) -> Platform.runLater(this::update));
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt ->
                Platform.runLater(this::update));

        this.setOnMouseClicked(event -> {
            int tamanho = 4;
            double size = Math.min(getWidth(), getHeight()) * 0.8;
            double offsetX = (getWidth() - size) / 2;
            double offsetY = (getHeight() - size) / 2;
            double relX = event.getX() - offsetX;
            double relY = event.getY() - offsetY;
            if (relX < 0 || relY < 0 || relX >= size || relY >= size) {
                return;
            }
            double cellSize = size / tamanho;
            int col = (int) (relX / cellSize);
            int lin = (int) (relY / cellSize);

            int linVazia = -1;
            int colVazia = -1;
            for (int i = 0; i < tamanho; i++) {
                for (int j = 0; j < tamanho; j++) {
                    if (manager.getPecaPuzzle(i, j) == 0) {
                        linVazia = i;
                        colVazia = j;
                    }
                }
            }
            if (linVazia < 0) {
                return;
            }
            Direcoes dir = GridMouseHelper.direcaoPuzzle(linVazia, colVazia, lin, col);
            if (dir != null) {
                manager.jogarPuzzle(dir);
            }
        });
    }

    private void update() {
        GraphicsContext gc = getGraphicsContext2D();
        if (gc == null) {
            return;
        }

        double w = getWidth();
        double h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }

        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(0, 0, w, h);

        int tamanho = 4;
        double size = Math.min(w, h) * 0.8;
        double offsetX = (w - size) / 2;
        double offsetY = (h - size) / 2;
        double cellW = size / tamanho;
        double cellH = size / tamanho;

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                int peca = manager.getPecaPuzzle(i, j);
                double x = offsetX + j * cellW;
                double y = offsetY + i * cellH;

                if (peca != 0) {
                    gc.setFill(Color.LIGHTGRAY);
                    gc.fillRect(x + 2, y + 2, cellW - 4, cellH - 4);
                    gc.strokeRect(x + 2, y + 2, cellW - 4, cellH - 4);

                    gc.setFill(Color.BLACK);
                    gc.setFont(new Font("Arial", 24));
                    gc.fillText(String.valueOf(peca), x + cellW / 2 - 10, y + cellH / 2 + 10);
                } else {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(x + 2, y + 2, cellW - 4, cellH - 4);
                }
            }
        }
    }
}
