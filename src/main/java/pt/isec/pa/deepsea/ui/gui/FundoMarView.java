package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.ui.res.ImageManager;

/**
 * Vista Canvas do fundo do mar (estado {@link pt.isec.pa.deepsea.model.state.DeepSeaState#FUNDO_MAR}).
 */
public class FundoMarView extends Canvas {
    private DeepSeaManager manager;

    /**
     * @param manager Facade do modelo.
     */
    public FundoMarView(DeepSeaManager manager) {
        this.manager = manager;
        this.widthProperty().addListener(e -> desenhar());
        this.heightProperty().addListener(e -> desenhar());
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt ->
                Platform.runLater(this::desenhar));
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt ->
                Platform.runLater(this::desenhar));

        this.setOnMouseClicked(event -> {
            Drone drone = manager.getDrone();
            if (drone == null) {
                return;
            }
            int linhas = manager.getLinhasFundo();
            int colunas = manager.getColunasFundo();
            int[] celula = GridMouseHelper.celulaDoClique(
                    event.getX(), event.getY(), getWidth(), getHeight(), linhas, colunas);
            if (celula == null) {
                return;
            }
            int lin = celula[0];
            int col = celula[1];
            if (lin == drone.getLinhaPos() && col == drone.getColunaPos()) {
                manager.avaliarFundo();
                return;
            }
            Direcoes dir = GridMouseHelper.direcaoEntreCelulas(
                    drone.getLinhaPos(), drone.getColunaPos(), lin, col);
            if (dir != null) {
                manager.mover(dir);
            }
        });
    }

    /** Redesenha a grelha do fundo do mar e o drone. */
    public void desenhar() {
        GraphicsContext gc = getGraphicsContext2D();
        double w = getWidth();
        double h = getHeight();

        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.NAVY);
        gc.fillRect(0, 0, w, h);

        int linhas = manager.getLinhasFundo();
        int colunas = manager.getColunasFundo();
        if (linhas == 0 || colunas == 0) return;

        double cellW = w / colunas;
        double cellH = h / linhas;

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (!manager.isFundoRevelado(i, j)) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(j * cellW, i * cellH, cellW, cellH);
                } else {
                    Object elem = manager.getElemFundo(i, j);
                    if (elem != null) {
                        String tipo = elem.getClass().getSimpleName();
                        Image img = obterImagemFundo(tipo);
                        double x = j * cellW;
                        double y = i * cellH;
                        if (img != null) {
                            gc.drawImage(img, x, y, cellW, cellH);
                        } else {
                            desenharFormaAlternativaFundo(gc, tipo, x, y, cellW, cellH);
                        }
                    }
                    gc.setStroke(Color.rgb(255, 255, 255, 0.2));
                    gc.strokeRect(j * cellW, i * cellH, cellW, cellH);
                }
            }
        }

        Drone drone = manager.getDrone();
        if (drone != null) {
            int linha = drone.getLinhaPos();
            int coluna = drone.getColunaPos();
            Image imgDrone = ImageManager.getImage("drone.png");
            if (imgDrone != null) {
                gc.drawImage(imgDrone, coluna * cellW, linha * cellH, cellW, cellH);
            } else {
                gc.setFill(Color.YELLOW);
                gc.fillOval(coluna * cellW + cellW * 0.1, linha * cellH + cellH * 0.1, cellW * 0.8, cellH * 0.8);
            }
        }
    }

    private Image obterImagemFundo(String tipo) {
        if (tipo.equals("Minerio")) {
            return ImageManager.getImage("minerios.png");
        }
        if (tipo.equals("Monstro")) {
            return ImageManager.getImage("monstro.png");
        }
        if (tipo.equals("AnimalMarinho")) {
            return ImageManager.getImage("animal.png");
        }
        if (tipo.equals("Artefacto")) {
            return ImageManager.getImage("artefacto.png");
        }
        return null;
    }

    private void desenharFormaAlternativaFundo(GraphicsContext gc, String tipo, double x, double y, double w, double h) {
        if (tipo.equals("Minerio")) {
            gc.setFill(Color.ORANGE);
        } else if (tipo.equals("Monstro")) {
            gc.setFill(Color.RED);
        } else if (tipo.equals("Artefacto")) {
            gc.setFill(Color.GOLD);
        } else {
            gc.setFill(Color.MAGENTA);
        }
        gc.fillOval(x + w * 0.2, y + h * 0.2, w * 0.6, h * 0.6);
    }
}