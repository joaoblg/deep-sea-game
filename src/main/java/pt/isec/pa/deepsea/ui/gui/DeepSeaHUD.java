package pt.isec.pa.deepsea.ui.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Constantes;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.ui.res.ImageManager;

public class DeepSeaHUD {

    private static final String ESTILO_PAINEL =
            "-fx-background-color: linear-gradient(to bottom, #081726, #040d16); -fx-border-color: #163852;";
    private static final double LARGURA_PAINEL = 245;

    private final DeepSeaManager manager;
    private VBox painelEsquerdo;
    private VBox painelDireito;

    private Label lblEstado;
    private ProgressBar barraCombustivelNavio;
    private Label lblMineriosNavio;
    private Label lblArtefactosNavio;
    private Label lblNavioPosicao;
    private Label lblDroneAtivo;
    private ProgressBar barraCombustivelDrone;
    private ProgressBar barraCascoDrone;
    private final List<Label> lblsFrota = new ArrayList<>();
    private VBox boxFrota;
    private Label lblAtalhos;
    // Debug/demo controls shown when Modo Defesa ativo
    private VBox debugBox;
    private Button btnFimJogo;
    private Button btnDeclararVitoria;
    private Button btnAddCombustivel50;
    private Button btnResolverPuzzle;

    public DeepSeaHUD(DeepSeaManager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    public VBox getPainelEsquerdo() {
        return painelEsquerdo;
    }

    public VBox getPainelDireito() {
        return painelDireito;
    }

    private void createViews() {
        painelEsquerdo = new VBox(12);
        painelEsquerdo.setPadding(new Insets(16));
        painelEsquerdo.setAlignment(Pos.TOP_LEFT);
        painelEsquerdo.setPrefWidth(LARGURA_PAINEL);
        painelEsquerdo.setMinWidth(LARGURA_PAINEL);
        painelEsquerdo.setStyle(ESTILO_PAINEL + " -fx-border-width: 0 1 0 0;");

        Label tituloEsq = criarTitulo("INFORMAÇÕES");
        lblEstado = criarTexto();

        HBox linhaCombNavio = criarLinhaComIcone("navioSagres.png", "Combustível Navio");
        barraCombustivelNavio = criarBarraProgresso();

        HBox linhaRecursos = new HBox(12);
        linhaRecursos.setAlignment(Pos.CENTER_LEFT);
        lblMineriosNavio = adicionarBlocoRecurso(linhaRecursos, "minerios.png", "Minérios");
        lblArtefactosNavio = adicionarBlocoRecurso(linhaRecursos, "artefacto.png", "Artefactos");

        lblNavioPosicao = criarTexto();

        painelEsquerdo.getChildren().addAll(
                tituloEsq, new Separator(), lblEstado, criarSubtitulo("Navio de Superfície"),
                linhaCombNavio, barraCombustivelNavio, linhaRecursos, lblNavioPosicao
        );

        painelDireito = new VBox(12);
        painelDireito.setPadding(new Insets(16));
        painelDireito.setAlignment(Pos.TOP_LEFT);
        painelDireito.setPrefWidth(LARGURA_PAINEL);
        painelDireito.setMinWidth(LARGURA_PAINEL);
        painelDireito.setStyle(ESTILO_PAINEL + " -fx-border-width: 0 0 0 1;");

        Label tituloDir = criarTitulo("DRONES SUBMARINOS");
        lblDroneAtivo = criarTexto();
        barraCombustivelDrone = criarBarraProgresso();
        barraCascoDrone = criarBarraProgresso();

        boxFrota = new VBox(6);
        lblAtalhos = criarTexto();
        lblAtalhos.setWrapText(true);
        lblAtalhos.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px; -fx-line-spacing: 2px;");

        painelDireito.getChildren().addAll(
                tituloDir, new Separator(), lblDroneAtivo,
                criarLinhaComIcone("drone.png", "Combustível"), barraCombustivelDrone,
                criarLinhaLabel("Integridade do Casco"), barraCascoDrone,
                criarSubtitulo("Frota"), boxFrota, new Separator(), criarLinhaLabel("Comandos & Atalhos"), lblAtalhos
        );

        // debug box
        debugBox = new VBox(8);
        debugBox.setPadding(new Insets(8));
        btnFimJogo = new Button("Declarar Fim de Jogo");
        btnDeclararVitoria = new Button("Declarar Vitória");
        btnAddCombustivel50 = new Button("Combustível Drone +50");
        btnResolverPuzzle = new Button("Resolver Puzzle");
        btnResolverPuzzle.setVisible(false);
        btnResolverPuzzle.setManaged(false);
        debugBox.getChildren().addAll(btnFimJogo, btnDeclararVitoria, btnAddCombustivel50, btnResolverPuzzle);
        debugBox.setVisible(false);
        debugBox.setManaged(false);
        painelDireito.getChildren().add(debugBox);
    }

    private void registerHandlers() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt ->
                Platform.runLater(this::update));
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt ->
                Platform.runLater(this::update));
        // handlers for debug buttons
        btnFimJogo.setOnAction(e -> manager.declararFimJogo());
        btnDeclararVitoria.setOnAction(e -> manager.declararVitoria());
        btnAddCombustivel50.setOnAction(e -> manager.adicionarCombustivelDrone(50));
        btnResolverPuzzle.setOnAction(e -> manager.autoResolverPuzzle());
    }

    private void update() {
        DeepSeaState estado = manager.getState();
        if (estado == null) {
            return;
        }

        lblEstado.setText("ESTADO: " + formatarEstado(estado));
        lblEstado.setStyle("-fx-text-fill: #38bdf8; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-color: rgba(56, 189, 248, 0.12); -fx-padding: 4 8 4 8; -fx-background-radius: 6;");

        aplicarBarra(barraCombustivelNavio, manager.getCombustivelNavio(), Constantes.COMBUSTIVEL_INI_NAVIO);

        lblMineriosNavio.setText(String.valueOf(manager.getMineriosNavio()));
        lblArtefactosNavio.setText(String.valueOf(manager.getArtefactosNavio()));

        int linhaPos = manager.getLinhaNavio();
        int colunaPos = manager.getColunaNavio();
        String tipoPos = "Navio";
        if (estado == DeepSeaState.FOSSO || estado == DeepSeaState.FUNDO_MAR || estado == DeepSeaState.PUZZLE) {
            linhaPos = manager.getLinhaDrone();
            colunaPos = manager.getColunaDrone();
            tipoPos = "Drone";
        }
        lblNavioPosicao.setText(String.format("📍 %s: Linha %d, Coluna %d", tipoPos, linhaPos + 1, colunaPos + 1));
        lblNavioPosicao.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");

        if (manager.hasDroneAtivo()) {
            lblDroneAtivo.setText("⚡ Drone Ativo: #" + manager.getNumeroDroneAtivo());
            lblDroneAtivo.setStyle("-fx-text-fill: #facc15; -fx-font-size: 13px; -fx-font-weight: bold;");

            aplicarBarra(barraCombustivelDrone, manager.getCombustivelDroneAtivo(), manager.getCapCombustivelDroneAtivo());
            aplicarBarra(barraCascoDrone, manager.getIntegridadeCascoDroneAtivo(), manager.getMaxIntegridadeCascoDroneAtivo());
            barraCascoDrone.setStyle("-fx-accent: #38bdf8; -fx-control-inner-background: #050f18;");
        } else {
            lblDroneAtivo.setText("⚪ Nenhum Drone em Operação");
            lblDroneAtivo.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
            barraCombustivelDrone.setProgress(0);
            barraCascoDrone.setProgress(0);
        }

        atualizarFrota();
        lblAtalhos.setText(atalhosPara(estado));

        // mostrar controles de debug quando em modo defesa
        boolean modoDef = manager.isModoDefesa();
        debugBox.setVisible(modoDef);
        debugBox.setManaged(modoDef);

        // mostrar botão de resolver puzzle apenas em modo defesa durante PUZZLE
        boolean emPuzzle = modoDef && estado == DeepSeaState.PUZZLE;
        btnResolverPuzzle.setVisible(emPuzzle);
        btnResolverPuzzle.setManaged(emPuzzle);
    }

    private String formatarEstado(DeepSeaState estado) {
        return switch (estado) {
            case SUPERFICIE -> "Superfície";
            case OFICINA -> "Oficina do Navio";
            case FOSSO -> "Fosso Marinho";
            case DESCIDA -> "Descida ao Fundo";
            case SUBIDA -> "Subida à Superfície";
            case FUNDO_MAR -> "Fundo do Oceano";
            case PUZZLE -> "Puzzle de Recuperação";
            case FIM_JOGO -> "Fim da Expedição";
        };
    }

    private String atalhosPara(DeepSeaState estado) {
        return switch (estado) {
            case SUPERFICIE -> "• Setas: Navegar Navio\n• 1, 2, 3: Selecionar Drone\n• O: Entrar na Oficina\n• Espaço: Iniciar Mergulho";
            case OFICINA -> "• 1, 2, 3: Escolher Drone\n• Esc / V: Sair da Oficina";
            case FOSSO, DESCIDA, SUBIDA -> "• Setas / Clique: Pilotar Drone";
            case FUNDO_MAR -> "• Setas: Mover Drone\n• Espaço: Avaliar Fundo\n• R: Recolher Minério\n• V: Regressar ao Navio";
            case PUZZLE -> "• Setas / Clique: Mover Peça";
            case FIM_JOGO -> "• Expedição finalizada.";
        };
    }

    private void aplicarBarra(ProgressBar barra, double atual, double maximo) {
        double percentagem = maximo > 0 ? atual / maximo : 0;
        percentagem = Math.max(0.0, Math.min(1.0, percentagem));
        barra.setProgress(percentagem);

        if (percentagem > 0.5) {
            barra.setStyle("-fx-accent: #10b981; -fx-control-inner-background: #050f18;");
        } else if (percentagem > 0.2) {
            barra.setStyle("-fx-accent: #f59e0b; -fx-control-inner-background: #050f18;");
        } else {
            barra.setStyle("-fx-accent: #ef4444; -fx-control-inner-background: #050f18;");
        }
    }

    private void atualizarFrota() {
        boxFrota.getChildren().clear();
        lblsFrota.clear();

        int numeroAtivo = manager.getNumeroDroneAtivo();
        String estiloNormal = "-fx-text-fill: #94a3b8; -fx-font-size: 11px; -fx-padding: 3 6 3 6; -fx-background-color: rgba(15, 23, 42, 0.4); -fx-background-radius: 4;";
        String estiloDestaque = "-fx-text-fill: #38bdf8; -fx-font-weight: bold; -fx-font-size: 11px; -fx-padding: 3 6 3 6; -fx-background-color: rgba(56, 189, 248, 0.15); -fx-background-radius: 4; -fx-border-color: rgba(56, 189, 248, 0.4); -fx-border-radius: 4;";

        for (String linha : manager.getResumoFrotaDrones()) {
            Label lbl = criarTexto();
            lbl.setText(linha.replace("->", "➔"));
            lbl.setStyle(numeroAtivo > 0 && (linha.startsWith("Drone " + numeroAtivo + " ") || linha.startsWith("Drone " + numeroAtivo + " ->"))
                    ? estiloDestaque : estiloNormal);
            lblsFrota.add(lbl);
            boxFrota.getChildren().add(lbl);
        }

        if (boxFrota.getChildren().isEmpty()) {
            Label vazio = criarTexto();
            vazio.setText("(sem drones)");
            boxFrota.getChildren().add(vazio);
        }
    }

    private Label adicionarBlocoRecurso(HBox linha, String imagem, String titulo) {
        HBox box = new HBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-background-color: rgba(15, 23, 42, 0.6); -fx-border-color: rgba(56, 189, 248, 0.2); -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 6 10 6 10;");

        ImageView iv = criarIcone(imagem);
        if (iv != null) {
            box.getChildren().add(iv);
        }

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 10px; -fx-font-weight: 600;");

        Label lblValor = new Label("0");
        lblValor.setStyle("-fx-text-fill: #f8fafc; -fx-font-size: 15px; -fx-font-weight: 800;");

        VBox textos = new VBox(1);
        textos.getChildren().addAll(lblTitulo, lblValor);
        box.getChildren().add(textos);
        linha.getChildren().add(box);
        return lblValor;
    }

    private HBox criarLinhaComIcone(String imagem, String texto) {
        HBox box = new HBox(8);
        box.setAlignment(Pos.CENTER_LEFT);

        ImageView iv = criarIcone(imagem);
        if (iv != null) {
            box.getChildren().add(iv);
        }

        box.getChildren().add(criarLinhaLabel(texto));
        return box;
    }

    private ImageView criarIcone(String imagem) {
        Image img = ImageManager.getImage(imagem);
        if (img == null || img.isError()) {
            return null;
        }
        ImageView iv = new ImageView(img);
        iv.setFitWidth(20);
        iv.setFitHeight(20);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        return iv;
    }

    private Label criarLinhaLabel(String texto) {
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 11px; -fx-font-weight: 600;");
        return lbl;
    }

    private Label criarSubtitulo(String texto) {
        Label lbl = new Label("── " + texto.toUpperCase() + " ──");
        lbl.setStyle("-fx-text-fill: #64748b; -fx-font-size: 10px; -fx-font-weight: bold; -fx-letter-spacing: 1px;");
        return lbl;
    }

    private ProgressBar criarBarraProgresso() {
        ProgressBar barra = new ProgressBar(0);
        barra.setPrefWidth(LARGURA_PAINEL - 32);
        barra.setPrefHeight(12);
        barra.setStyle("-fx-accent: #10b981; -fx-control-inner-background: #050f18;");
        return barra;
    }

    private Label criarTitulo(String texto) {
        Label lbl = new Label(texto);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lbl.setStyle("-fx-text-fill: #38bdf8; -fx-letter-spacing: 1.2px;");
        return lbl;
    }

    private Label criarTexto() {
        Label lbl = new Label();
        lbl.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        lbl.setStyle("-fx-text-fill: #f1f5f9;");
        lbl.setWrapText(true);
        return lbl;
    }
}
