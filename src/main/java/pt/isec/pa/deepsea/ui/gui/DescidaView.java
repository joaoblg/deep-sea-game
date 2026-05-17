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
 * Vista Canvas do fosso marinho (estados {@link pt.isec.pa.deepsea.model.state.DeepSeaState#DESCIDA}
 * e {@link pt.isec.pa.deepsea.model.state.DeepSeaState#SUBIDA}).
 */
public class DescidaView extends Canvas {
    private final DeepSeaManager manager;

    /**
     * @param manager Facade do modelo.
     */
    public DescidaView(DeepSeaManager manager) {
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
            int linhas = manager.getLinhasFosso();
            int colunas = manager.getColunasFosso();
            int[] celula = GridMouseHelper.celulaDoClique(
                    event.getX(), event.getY(), getWidth(), getHeight(), linhas, colunas);
            if (celula == null) {
                return;
            }
            Direcoes dir = GridMouseHelper.direcaoEntreCelulas(
                    drone.getLinhaPos(), drone.getColunaPos(), celula[0], celula[1]);
            if (dir != null) {
                manager.mover(dir);
            }
        });
    }

    /** Redesenha a grelha do fosso e o drone. */
    public void desenhar() {
        GraphicsContext gc = getGraphicsContext2D();
        double w = getWidth();
        double h = getHeight();

        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(0, 0, w, h);

        int linhas = manager.getLinhasFosso();
        int colunas = manager.getColunasFosso();
        if (linhas == 0 || colunas == 0) {
            return;
        }

        double cellW = w / colunas;
        double cellH = h / linhas;

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Object elem = manager.getElemFosso(i, j);
                if (elem != null) {
                    String tipo = elem.getClass().getSimpleName();
                    Image img = obterImagemFosso(tipo);

                    if (img != null && !img.isError() && img.getWidth() > 0) {
                        gc.drawImage(img, j * cellW, i * cellH, cellW, cellH);
                    } else {
                        desenharFormaAlternativaFosso(gc, tipo, j * cellW, i * cellH, cellW, cellH);
                    }
                }
            }
        }

        Drone drone = manager.getDrone();
        if (drone != null) {
            int linha = drone.getLinhaPos();
            int coluna = drone.getColunaPos();
            Image imgDrone = ImageManager.getImage("drone.png");

            if (imgDrone != null && !imgDrone.isError() && imgDrone.getWidth() > 0) {
                gc.drawImage(imgDrone, coluna * cellW, linha * cellH, cellW, cellH);
            } else {
                gc.setFill(Color.YELLOW);
                gc.fillOval(coluna * cellW + cellW * 0.1, linha * cellH + cellH * 0.1, cellW * 0.8, cellH * 0.8);
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
            return (animal != null && !animal.isError()) ? animal : ImageManager.getImage("monstro.png");
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
