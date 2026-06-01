package pt.isec.pa.deepsea.ui.gui;

/**
 * Calcula métricas de grelha para desenho e cliques no canvas.
 */
public final class GridLayoutHelper {

    private GridLayoutHelper() {
    }

    public record GridMetrics(double cellW, double cellH, double offsetX, double offsetY, int linhas, int colunas) {
    }

    /**
     * Grelha que preenche toda a área do canvas (células esticam com a janela).
     */
    public static GridMetrics computeFill(double width, double height, int linhas, int colunas) {
        if (width <= 0 || height <= 0 || linhas <= 0 || colunas <= 0) {
            return null;
        }
        return new GridMetrics(width / colunas, height / linhas, 0, 0, linhas, colunas);
    }

    /**
     * Grelha com células quadradas centradas (mantém proporção, pode deixar margens).
     */
    public static GridMetrics computeSquare(double width, double height, int linhas, int colunas) {
        if (width <= 0 || height <= 0 || linhas <= 0 || colunas <= 0) {
            return null;
        }
        double cellW = width / colunas;
        double cellH = height / linhas;
        double cellSize = Math.min(cellW, cellH);
        double gridW = cellSize * colunas;
        double gridH = cellSize * linhas;
        double offsetX = (width - gridW) / 2.0;
        double offsetY = (height - gridH) / 2.0;
        return new GridMetrics(cellSize, cellSize, offsetX, offsetY, linhas, colunas);
    }
}
