package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        gc.setFill(Color.web("#061320"));
        gc.fillRect(0, 0, w, h);

        int tamanho = 4;
        double size = Math.min(w, h) * 0.78;
        double offsetX = (w - size) / 2;
        double offsetY = (h - size) / 2;
        double cellW = size / tamanho;
        double cellH = size / tamanho;

        // Outer cabinet frame for puzzle
        gc.setFill(Color.web("#0a1f33"));
        gc.fillRoundRect(offsetX - 10, offsetY - 10, size + 20, size + 20, 16, 16);
        gc.setStroke(Color.web("#1e4d6d"));
        gc.setLineWidth(2);
        gc.strokeRoundRect(offsetX - 10, offsetY - 10, size + 20, size + 20, 16, 16);

        // Header Title above puzzle
        gc.setFill(Color.web("#38bdf8"));
        gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        gc.fillText("MECANISMO DO ARTEFACTO (PUZZLE 15)", offsetX, offsetY - 22);

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                int peca = manager.getPecaPuzzle(i, j);
                double x = offsetX + j * cellW;
                double y = offsetY + i * cellH;

                if (peca != 0) {
                    // Modern 3D beveled puzzle tile
                    gc.setFill(Color.web("#142f4c"));
                    gc.fillRoundRect(x + 3, y + 3, cellW - 6, cellH - 6, 10, 10);

                    // Inner border highlight
                    gc.setStroke(Color.web("#38bdf8"));
                    gc.setLineWidth(1.5);
                    gc.strokeRoundRect(x + 3, y + 3, cellW - 6, cellH - 6, 10, 10);

                    // Tile number text
                    gc.setFill(Color.web("#f8fafc"));
                    gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
                    String text = String.valueOf(peca);
                    double tw = text.length() > 1 ? 14 : 7;
                    gc.fillText(text, x + cellW / 2 - tw, y + cellH / 2 + 8);
                } else {
                    // Empty slot (dark recessed hole)
                    gc.setFill(Color.web("#040a12"));
                    gc.fillRoundRect(x + 3, y + 3, cellW - 6, cellH - 6, 10, 10);
                    gc.setStroke(Color.web("#0b1a29"));
                    gc.setLineWidth(1);
                    gc.strokeRoundRect(x + 3, y + 3, cellW - 6, cellH - 6, 10, 10);
                }
            }
        }
    }
}
