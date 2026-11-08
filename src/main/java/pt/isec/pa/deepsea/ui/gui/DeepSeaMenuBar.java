package pt.isec.pa.deepsea.ui.gui;

import java.io.File;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.ui.res.SoundManager;

/**
 * Barra de menus da aplicação.
 */
public class DeepSeaMenuBar extends MenuBar {

    private final DeepSeaManager manager;
    private final Stage primaryStage;
    private final DeepSeaRoot root;
    private DeepSeaLogStage logStage;

    private Menu menuRecent;
    private CheckMenuItem muteItem;
    private CheckMenuItem itemModoDefesa;
    private MenuItem itemNew;
    private MenuItem itemOpen;
    private MenuItem itemSaveAs;
    private MenuItem itemExit;
    private MenuItem itemShowHide;
    private MenuItem itemSaveLogs;
    private MenuItem itemClearLogs;
    private MenuItem itemSecondWindow;

    /**
     * @param manager Facade do modelo
     * @param primaryStage Janela principal
     * @param root Referência ao root para sincronização da segunda janela
     */
    public DeepSeaMenuBar(DeepSeaManager manager, Stage primaryStage, DeepSeaRoot root) {
        this.manager = manager;
        this.primaryStage = primaryStage;
        this.root = root;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        logStage = new DeepSeaLogStage(manager);
        menuRecent = new Menu("Recentes");

        Menu menuGame = new Menu("Jogo");
        itemNew = new MenuItem("Novo Jogo");
        itemOpen = new MenuItem("Abrir...");
        itemSaveAs = new MenuItem("Gravar como...");
        itemExit = new MenuItem("Sair");
        itemModoDefesa = new CheckMenuItem("Modo Defesa (Avaliação)");

        menuGame.getItems().addAll(
            itemNew, itemOpen, menuRecent, itemSaveAs, new SeparatorMenuItem(), itemModoDefesa, itemExit);

        Menu menuLog = new Menu("Logs");
        itemShowHide = new MenuItem("Ver / Ocultar Logs");
        itemSaveLogs = new MenuItem("Exportar Logs...");
        itemClearLogs = new MenuItem("Limpar Logs");
        menuLog.getItems().addAll(itemShowHide, itemSaveLogs, itemClearLogs);

        Menu menuSound = new Menu("Áudio");
        muteItem = new CheckMenuItem("Silenciar Sons");
        menuSound.getItems().add(muteItem);

        Menu menuView = new Menu("Vista");
        itemSecondWindow = new MenuItem("Abrir Segunda Janela (Sincronizada)");
        menuView.getItems().add(itemSecondWindow);

        getMenus().addAll(menuGame, menuLog, menuSound, menuView);
    }

    private void registerHandlers() {
        itemNew.setOnAction(e -> manager.iniciarJogo());
        itemOpen.setOnAction(e -> abrirJogo());
        itemSaveAs.setOnAction(e -> gravarJogoComo());
        itemExit.setOnAction(e -> Platform.exit());

        itemShowHide.setOnAction(e -> logStage.toggle());
        itemSaveLogs.setOnAction(e -> exportarLogs());
        itemClearLogs.setOnAction(e -> manager.limparLogs());

        itemModoDefesa.setOnAction(e -> {
            manager.setModoDefesa(itemModoDefesa.isSelected());
            manager.iniciarJogo();
        });

        SoundManager sm = SoundManager.getInstance();
        muteItem.selectedProperty().addListener((obs, oldVal, newVal) -> sm.setMuted(newVal));

        itemSecondWindow.setOnAction(e -> root.abrirSegundaJanela());
    }

    private void update() {
        muteItem.setSelected(SoundManager.getInstance().isMuted());
        if (itemModoDefesa != null) itemModoDefesa.setSelected(manager.isModoDefesa());
        atualizarMenuRecent();
    }

    private void abrirJogo() {
        FileChooser chooser = criarChooserSave();
        chooser.setTitle("Abrir jogo");
        File file = chooser.showOpenDialog(primaryStage);
        if (file != null && manager.carregarJogo(file.getAbsolutePath())) {
            RecentGames.register(file.getAbsolutePath());
            update();
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
                update();
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
                new FileChooser.ExtensionFilter("Save Deep Sea (*.dat)", "*.dat"));
        return chooser;
    }

    private void atualizarMenuRecent() {
        menuRecent.getItems().clear();
        for (String path : RecentGames.getRecent()) {
            MenuItem item = new MenuItem(path);
            item.setOnAction(e -> {
                if (manager.carregarJogo(path)) {
                    RecentGames.register(path);
                    update();
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
