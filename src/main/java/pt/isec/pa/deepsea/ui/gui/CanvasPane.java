package pt.isec.pa.deepsea.ui.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;

/**
 * Region redimensionável que contém um Canvas para desenho.
 * O Canvas interno redimensiona automaticamente para preencher este Region.
 */
public class CanvasPane extends Region {
    private final Canvas canvas;

    public CanvasPane() {
        this.canvas = new Canvas();
        getChildren().add(canvas);
    }

    public GraphicsContext getGraphicsContext2D() {
        return canvas.getGraphicsContext2D();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        double w = getWidth();
        double h = getHeight();
        if (w > 0 && h > 0) {
            canvas.setLayoutX(0);
            canvas.setLayoutY(0);
            canvas.setWidth(w);
            canvas.setHeight(h);
        }
    }

    @Override
    protected double computePrefWidth(double height) {
        return 800;
    }

    @Override
    protected double computePrefHeight(double width) {
        return 600;
    }
}