package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.ui.res.ImageManager;

/**
 * Vista Canvas do fosso marinho (estado FOSSO).
 */
public class DescidaView extends CanvasPane {
    private final DeepSeaManager manager;

    /**
     * @param manager Facade do modelo
     */
    public DescidaView(DeepSeaManager manager) {
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

        this.setOnMouseClicked(event -> {
            if (!manager.hasDroneAtivo()) {
                return;
            }
            int linhas = manager.getLinhasFosso();
            int colunas = manager.getColunasFosso();
            int[] celula = GridMouseHelper.celulaDoClique(
                    event.getX(), event.getY(), getWidth(), getHeight(), linhas, colunas);
            if (celula == null) {
                return;
            }
            Direcoes dir = GridMouseHelper.direcaoEntreCelulas(
                    manager.getLinhaDrone(), manager.getColunaDrone(), celula[0], celula[1]);
            if (dir != null) {
                manager.mover(dir);
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
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(0, 0, w, h);

        int linhas = manager.getLinhasFosso();
        int colunas = manager.getColunasFosso();
        GridLayoutHelper.GridMetrics grid = GridLayoutHelper.computeFill(w, h, linhas, colunas);
        if (grid == null) {
            return;
        }

        double cellW = grid.cellW();
        double cellH = grid.cellH();

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                String tipo = manager.getElemFosso(i, j);
                double x = j * cellW;
                double y = i * cellH;

                if (tipo != null) {
                    Image img = obterImagemFosso(tipo);

                    if (img != null && !img.isError() && img.getWidth() > 0) {
                        gc.drawImage(img, x, y, cellW, cellH);
                    } else {
                        desenharFormaAlternativaFosso(gc, tipo, x, y, cellW, cellH);
                    }
                }
            }
        }

        if (manager.hasDroneAtivo()) {
            int linha = manager.getLinhaDrone();
            int coluna = manager.getColunaDrone();
            double x = coluna * cellW;
            double y = linha * cellH;

            Image imgDrone = ImageManager.getImage(manager.getNomeImagemDroneAtivo());

            if (imgDrone != null && !imgDrone.isError() && imgDrone.getWidth() > 0) {
                gc.drawImage(imgDrone, x, y, cellW, cellH);
            } else {
                gc.setFill(Color.YELLOW);
                gc.fillOval(x + cellW * 0.1, y + cellH * 0.1, cellW * 0.8, cellH * 0.8);
            }
        }
    }

    private Image obterImagemFosso(String tipo) {
        if (tipo.equals("Rocha")) {
            Image rocha = ImageManager.getImage("icons8-rocha-100.png");
            if (rocha != null && !rocha.isError() && rocha.getWidth() > 0) {
                return rocha;
            }
            return ImageManager.getImage("rocha.png");
        }
        if (tipo.equals("AnimalMarinho")) {
            Image animal = ImageManager.getImage("animal.png");
            return (animal != null && !animal.isError()) ? animal : ImageManager.getImage("monstoMar-removebg-preview.png");
        }
        if (tipo.equals("CorrenteMarinha")) {
            Image corrente = ImageManager.getImage("corrente.png");
            if (corrente != null && !corrente.isError()) {
                return corrente;
            }
            return ImageManager.getImage("icons8-linhas-de-onda-96.png");
        }
        return null;
    }

    private void desenharFormaAlternativaFosso(GraphicsContext gc, String tipo, double x, double y, double w, double h) {
        if (tipo.equals("Rocha")) {
            gc.setFill(Color.DARKGRAY);
        } else if (tipo.equals("AnimalMarinho")) {
            gc.setFill(Color.RED);
        } else if (tipo.equals("CorrenteMarinha")) {
            gc.setFill(Color.CYAN);
        } else {
            gc.setFill(Color.MAGENTA);
        }
        gc.fillRect(x, y, w, h);
    }
}
