package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;

import java.io.File;

/**
 * Barra de menus da aplicação (Game e Log), conforme a Etapa 3 do trabalho prático.
 * Toda a interação com dados passa pela {@link DeepSeaManager}.
 */
public class DeepSeaMenuBar extends MenuBar {

    private final DeepSeaManager manager;
    private final Stage primaryStage;
    private final DeepSeaLogStage logStage;
    private final Menu menuRecent;

    /**
     * @param manager Facade do modelo.
     * @param primaryStage Janela principal (para diálogos FileChooser).
     */
    public DeepSeaMenuBar(DeepSeaManager manager, Stage primaryStage) {
        this.manager = manager;
        this.primaryStage = primaryStage;
        this.logStage = new DeepSeaLogStage(manager);
        this.menuRecent = new Menu("Open recent");
        criarMenus();
    }

    private void criarMenus() {
        Menu menuGame = new Menu("Game");

        MenuItem itemNew = new MenuItem("New");
        itemNew.setOnAction(e -> manager.iniciarJogo());

        MenuItem itemOpen = new MenuItem("Open...");
        itemOpen.setOnAction(e -> abrirJogo());

        MenuItem itemSaveAs = new MenuItem("Save as...");
        itemSaveAs.setOnAction(e -> gravarJogoComo());

        MenuItem itemExit = new MenuItem("Exit");
        itemExit.setOnAction(e -> Platform.exit());

        menuGame.getItems().addAll(
                itemNew, itemOpen, menuRecent, itemSaveAs, new SeparatorMenuItem(), itemExit);

        Menu menuLog = new Menu("Log");

        MenuItem itemShowHide = new MenuItem("Show/Hide");
        itemShowHide.setOnAction(e -> logStage.toggle());

        MenuItem itemSaveLogs = new MenuItem("Save logs");
        itemSaveLogs.setOnAction(e -> exportarLogs());

        MenuItem itemClearLogs = new MenuItem("Clear logs");
        itemClearLogs.setOnAction(e -> manager.limparLogs());

        menuLog.getItems().addAll(itemShowHide, itemSaveLogs, itemClearLogs);

        this.getMenus().addAll(menuGame, menuLog);
        atualizarMenuRecent();
    }

    private void abrirJogo() {
        FileChooser chooser = criarChooserSave();
        chooser.setTitle("Abrir jogo");
        File file = chooser.showOpenDialog(primaryStage);
        if (file != null && manager.carregarJogo(file.getAbsolutePath())) {
            RecentGames.register(file.getAbsolutePath());
            atualizarMenuRecent();
        } else if (file != null) {
            mostrarErro("Não foi possível carregar o jogo.");
        }
    }

    private void gravarJogoComo() {
        FileChooser chooser = criarChooserSave();
        chooser.setTitle("Gravar jogo");
        File file = chooser.showSaveDialog(primaryStage);
        if (file != null) {
            String path = file.getAbsolutePath();
            if (!path.endsWith(".dat")) {
                path += ".dat";
            }
            if (manager.gravarJogo(path)) {
                RecentGames.register(path);
                atualizarMenuRecent();
            } else {
                mostrarErro("Não foi possível gravar o jogo.");
            }
        }
    }

    private void exportarLogs() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Gravar logs");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Ficheiros de texto", "*.txt"));
        File file = chooser.showSaveDialog(primaryStage);
        if (file != null && !manager.exportarLogs(file.getAbsolutePath())) {
            mostrarErro("Não foi possível exportar os logs.");
        }
    }

    private FileChooser criarChooserSave() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Save Deep Sea", "*.dat"));
        return chooser;
    }

    private void atualizarMenuRecent() {
        menuRecent.getItems().clear();
        for (String path : RecentGames.getRecent()) {
            MenuItem item = new MenuItem(path);
            item.setOnAction(e -> {
                if (manager.carregarJogo(path)) {
                    RecentGames.register(path);
                    atualizarMenuRecent();
                } else {
                    mostrarErro("Não foi possível carregar: " + path);
                }
            });
            menuRecent.getItems().add(item);
        }
        if (menuRecent.getItems().isEmpty()) {
            MenuItem vazio = new MenuItem("(nenhum)");
            vazio.setDisable(true);
            menuRecent.getItems().add(vazio);
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
