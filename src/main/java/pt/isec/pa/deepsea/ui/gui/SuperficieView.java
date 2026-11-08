package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.ui.res.ImageManager;

/**
 * Vista Canvas da superfície (estado SUPERFICIE).
 */
public class SuperficieView extends CanvasPane {
    private final DeepSeaManager manager;

    /**
     * @param manager Facade do modelo
     */
    public SuperficieView(DeepSeaManager manager) {
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
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt ->
                Platform.runLater(this::update));
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        update();
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
        gc.setFill(Color.web("#0a1f33"));
        gc.fillRect(0, 0, w, h);

        desenharFundoMar(gc, w, h);

        // Subtle dark blue tint over sea image for high-tech aesthetic
        gc.setFill(Color.web("rgba(6, 25, 45, 0.45)"));
        gc.fillRect(0, 0, w, h);

        int linhas = manager.getLinhasSuperficie();
        int colunas = manager.getColunasSuperficie();
        GridLayoutHelper.GridMetrics grid = GridLayoutHelper.computeFill(w, h, linhas, colunas);
        if (grid == null) {
            return;
        }

        double cellW = grid.cellW();
        double cellH = grid.cellH();
        Image imgArtefacto = ImageManager.getImage("artefacto.png");

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                double x = j * cellW;
                double y = i * cellH;

                // Subtle tactical radar grid line
                gc.setStroke(Color.web("rgba(56, 189, 248, 0.22)"));
                gc.setLineWidth(1.0);
                gc.strokeRect(x, y, cellW, cellH);

                if (manager.contemArtefactoNaSuperficie(i, j)) {
                    // Glowing target ring for artifact location
                    gc.setFill(Color.web("rgba(245, 158, 11, 0.3)"));
                    gc.fillOval(x + cellW * 0.25, y + cellH * 0.25, cellW * 0.5, cellH * 0.5);

                    gc.setStroke(Color.web("#f59e0b"));
                    gc.setLineWidth(1.5);
                    gc.strokeOval(x + cellW * 0.25, y + cellH * 0.25, cellW * 0.5, cellH * 0.5);

                    if (imgArtefacto != null && !imgArtefacto.isError()) {
                        gc.drawImage(imgArtefacto, x + cellW * 0.28, y + cellH * 0.28, cellW * 0.44, cellH * 0.44);
                    }
                }
            }
        }

        int navioLinha = manager.getLinhaNavio();
        int navioColuna = manager.getColunaNavio();
        Image imgNavio = ImageManager.getImage("navioSagres.png");
        double nx = navioColuna * cellW;
        double ny = navioLinha * cellH;

        if (imgNavio != null && !imgNavio.isError()) {
            // Tactical active ship indicator ring
            gc.setStroke(Color.web("#38bdf8"));
            gc.setLineWidth(2.0);
            gc.strokeRect(nx + 2, ny + 2, cellW - 4, cellH - 4);

            // Water wake shadow
            gc.setFill(Color.web("rgba(2, 6, 23, 0.6)"));
            gc.fillOval(nx + cellW * 0.08, ny + cellH * 0.65, cellW * 0.84, cellH * 0.28);

            gc.drawImage(imgNavio, nx + cellW * 0.05, ny + cellH * 0.05, cellW * 0.9, cellH * 0.9);
        }
    }

    private void desenharFundoMar(GraphicsContext gc, double w, double h) {
        Image imgMar = ImageManager.getImage("beautiful-photo-sea-waves.jpg");
        if (imgMar == null || imgMar.isError() || imgMar.getWidth() <= 0) {
            imgMar = ImageManager.getImage("mar.png");
        }
        if (imgMar == null || imgMar.isError() || imgMar.getWidth() <= 0) {
            return;
        }

        double imgRatio = imgMar.getWidth() / imgMar.getHeight();
        double canvasRatio = w / h;
        double drawW;
        double drawH;
        if (imgRatio > canvasRatio) {
            drawH = h;
            drawW = h * imgRatio;
        } else {
            drawW = w;
            drawH = w / imgRatio;
        }
        gc.drawImage(imgMar, (w - drawW) / 2.0, (h - drawH) / 2.0, drawW, drawH);
    }
}
