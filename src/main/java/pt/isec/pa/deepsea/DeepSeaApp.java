package pt.isec.pa.deepsea;

import javafx.application.Application;
import pt.isec.pa.deepsea.ui.gui.MainJFX;

public class DeepSeaApp {
    public static void main(String[] args) {
        // Em vez de escrever na consola, lança a aplicação gráfica
        Application.launch(MainJFX.class, args);
    }
}
