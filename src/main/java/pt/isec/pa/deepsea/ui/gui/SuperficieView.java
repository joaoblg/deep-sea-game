package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.ui.res.ImageManager;

/**
 * Vista Canvas da superfície marinha (estado {@link pt.isec.pa.deepsea.model.state.DeepSeaState#SUPERFICIE}).
 */
public class SuperficieView extends Canvas {
    private DeepSeaManager manager;

    /**
     * @param manager Facade do modelo.
     */
    public SuperficieView(DeepSeaManager manager) {
        this.manager = manager;
        this.widthProperty().addListener(e -> desenhar());
        this.heightProperty().addListener(e -> desenhar());

        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt -> {
            Platform.runLater(this::desenhar);
        });

        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt -> {
            Platform.runLater(this::desenhar);
        });

        this.setOnMouseClicked(event -> {
            int linhas = manager.getLinhasSuperficie();
            int colunas = manager.getColunasSuperficie();
            int[] celula = GridMouseHelper.celulaDoClique(
                    event.getX(), event.getY(), getWidth(), getHeight(), linhas, colunas);
            if (celula == null) {
                return;
            }
            // Rampa de lançamento: clique numa célula escolhe o ponto de mergulho (como antes).
            manager.iniciarMergulho(celula[0], celula[1]);
        });
    }

    /** Redesenha o mapa da superfície e o navio. */
    public void desenhar() {
        GraphicsContext gc = getGraphicsContext2D();
        double w = getWidth();
        double h = getHeight();

        gc.clearRect(0, 0, w, h);

        int linhas = manager.getLinhasSuperficie();
        int colunas = manager.getColunasSuperficie();
        if (linhas == 0 || colunas == 0) return;

        double cellW = w / colunas;
        double cellH = h / linhas;

        gc.setFill(Color.web("#0a192f"));
        gc.fillRect(0, 0, w, h);

        Image imgAgua = ImageManager.getImage("mar.png");

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {

                if (imgAgua != null) {
                    gc.drawImage(imgAgua, j * cellW, i * cellH, cellW + 1.0, cellH + 1.0);
                } else {
                    gc.setFill(Color.web("#172a45"));
                    gc.fillRect(j * cellW, i * cellH, cellW + 1.0, cellH + 1.0);
                }

                if (manager.contemArtefactoNaSuperficie(i, j)) {
                    Image imgArt = ImageManager.getImage("artefacto.png");
                    if (imgArt != null) {
                        gc.drawImage(imgArt, j * cellW, i * cellH, cellW, cellH);
                    } else {
                        gc.setFill(Color.rgb(255, 215, 0, 0.5));
                        gc.fillOval(j * cellW + cellW * 0.2, i * cellH + cellH * 0.2, cellW * 0.6, cellH * 0.6);
                    }
                }
            }
        }


        gc.setStroke(Color.rgb(23, 42, 69, 0.4));
        gc.setLineWidth(1.0);

        for (int i = 0; i <= linhas; i++) {
            gc.strokeLine(0, i * cellH, w, i * cellH);
        }
        for (int j = 0; j <= colunas; j++) {
            gc.strokeLine(j * cellW, 0, j * cellW, h);
        }

        // ==========================================
        // DESENHO DO NAVIO
        // ==========================================

        int navioLinha = manager.getLinhaNavio();
        int navioColuna = manager.getColunaNavio();

        Image imgNavio = ImageManager.getImage("navioSagres.png");

        if (imgNavio != null) {
            double imgW = imgNavio.getWidth();
            double imgH = imgNavio.getHeight();

            double scaleX = cellW / imgW;
            double scaleY = cellH / imgH;
            double scaleBase = Math.min(scaleX, scaleY);

            double multiplicador = 2;
            double scaleFinal = scaleBase * multiplicador;

            double drawW = imgW * scaleFinal;
            double drawH = imgH * scaleFinal;


            double drawX = (navioColuna * cellW) + (cellW - drawW) / 2;
            double drawY = (navioLinha * cellH) + (cellH - drawH) / 2 - 10;

            // 5. Desenhar!
            gc.drawImage(imgNavio, drawX, drawY, drawW, drawH);


        } else {
            gc.setFill(Color.DARKGRAY);
            gc.fillRect(navioColuna * cellW, navioLinha * cellH, cellW, cellH);
            gc.setFill(Color.WHITE);
            gc.fillText("NAVIO", navioColuna * cellW + cellW / 4, navioLinha * cellH + cellH / 2);
        }
    }
}