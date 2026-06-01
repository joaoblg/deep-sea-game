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
            "-fx-background-color: #2c3e50; -fx-border-color: #1a252f;";
    private static final double LARGURA_PAINEL = 240;

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
        painelEsquerdo.setStyle(ESTILO_PAINEL + " -fx-border-width: 0 2 0 0;");

        Label tituloEsq = criarTitulo("INFORMAÇÃO");
        lblEstado = criarTexto();

        HBox linhaCombNavio = criarLinhaComIcone("navioSagres.png", "Combustível");
        barraCombustivelNavio = criarBarraProgresso();

        HBox linhaRecursos = new HBox(20);
        linhaRecursos.setAlignment(Pos.CENTER_LEFT);
        lblMineriosNavio = adicionarBlocoRecurso(linhaRecursos, "minerios.png", "Minérios");
        lblArtefactosNavio = adicionarBlocoRecurso(linhaRecursos, "artefacto.png", "Artefactos");

        lblNavioPosicao = criarTexto();

        painelEsquerdo.getChildren().addAll(
                tituloEsq, new Separator(), lblEstado, criarSubtitulo("Navio"),
                linhaCombNavio, barraCombustivelNavio, linhaRecursos, lblNavioPosicao
        );

        painelDireito = new VBox(12);
        painelDireito.setPadding(new Insets(16));
        painelDireito.setAlignment(Pos.TOP_LEFT);
        painelDireito.setPrefWidth(LARGURA_PAINEL);
        painelDireito.setMinWidth(LARGURA_PAINEL);
        painelDireito.setStyle(ESTILO_PAINEL + " -fx-border-width: 0 0 0 2;");

        Label tituloDir = criarTitulo("DRONES");
        lblDroneAtivo = criarTexto();
        barraCombustivelDrone = criarBarraProgresso();
        barraCascoDrone = criarBarraProgresso();

        boxFrota = new VBox(6);
        lblAtalhos = criarTexto();
        lblAtalhos.setWrapText(true);
        lblAtalhos.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");

        painelDireito.getChildren().addAll(
                tituloDir, new Separator(), lblDroneAtivo,
                criarLinhaComIcone("drone.png", "Combustível"), barraCombustivelDrone,
                criarLinhaLabel("Casco"), barraCascoDrone,
                criarSubtitulo("Frota"), boxFrota, new Separator(), criarLinhaLabel("Atalhos"), lblAtalhos
        );

        // debug box
        debugBox = new VBox(8);
        debugBox.setPadding(new Insets(8));
        btnFimJogo = new Button("Declarar Fim Jogo");
        btnDeclararVitoria = new Button("Declarar Vitória");
        btnAddCombustivel50 = new Button("Adicionar Combustível +50");
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

        lblEstado.setText(estado.name());
        lblEstado.setStyle("-fx-text-fill: #3eb4e8; -fx-font-weight: bold; -fx-font-size: 14px;");

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
        lblNavioPosicao.setText(String.format("%s Posição: [%d, %d]", tipoPos, linhaPos, colunaPos));
        lblNavioPosicao.setStyle("-fx-text-fill: #bdc3c7; -fx-font-size: 12px;");

        if (manager.hasDroneAtivo()) {
            lblDroneAtivo.setText("Drone " + manager.getNumeroDroneAtivo());
            lblDroneAtivo.setStyle("-fx-text-fill: #f1c40f; -fx-font-size: 14px; -fx-font-weight: bold;");

            aplicarBarra(barraCombustivelDrone, manager.getCombustivelDroneAtivo(), manager.getCapCombustivelDroneAtivo());
            aplicarBarra(barraCascoDrone, manager.getIntegridadeCascoDroneAtivo(), manager.getMaxIntegridadeCascoDroneAtivo());
            barraCascoDrone.setStyle("-fx-accent: #3498db; -fx-control-inner-background: #1a252f;");
        } else {
            lblDroneAtivo.setText("—");
            lblDroneAtivo.setStyle("-fx-text-fill: #95a5a6;");
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

    private String atalhosPara(DeepSeaState estado) {
        return switch (estado) {
            case SUPERFICIE -> "Setas — Navio | 1-3 — Drone | O — Oficina | Espaço — Mergulho";
            case OFICINA -> "1-3 — Drone | Esc/V — Sair";
            case FOSSO, DESCIDA, SUBIDA -> "Setas/Clique — Drone";
            case FUNDO_MAR -> "Setas — Mover | Espaço — Avaliar | R — Minério | V — Navio";
            case PUZZLE -> "Setas — Peça";
            case FIM_JOGO -> "Fim da expedição.";
        };
    }

    private void aplicarBarra(ProgressBar barra, double atual, double maximo) {
        double percentagem = maximo > 0 ? atual / maximo : 0;
        percentagem = Math.max(0.0, Math.min(1.0, percentagem));
        barra.setProgress(percentagem);

        if (percentagem > 0.5) {
            barra.setStyle("-fx-accent: #2ecc71; -fx-control-inner-background: #1a252f;");
        } else if (percentagem > 0.2) {
            barra.setStyle("-fx-accent: #f1c40f; -fx-control-inner-background: #1a252f;");
        } else {
            barra.setStyle("-fx-accent: #e74c3c; -fx-control-inner-background: #1a252f;");
        }
    }

    private void atualizarFrota() {
        boxFrota.getChildren().clear();
        lblsFrota.clear();

        int numeroAtivo = manager.getNumeroDroneAtivo();
        String estiloNormal = "-fx-text-fill: #ecf0f1; -fx-font-size: 11px;";
        String estiloDestaque = "-fx-text-fill: #2ecc71; -fx-font-weight: bold; -fx-font-size: 11px;";

        for (String linha : manager.getResumoFrotaDrones()) {
            Label lbl = criarTexto();
            lbl.setText(linha);
            lbl.setStyle(numeroAtivo > 0 && linha.startsWith("Drone " + numeroAtivo + " —")
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
        HBox box = new HBox(6);
        box.setAlignment(Pos.CENTER_LEFT);

        ImageView iv = criarIcone(imagem);
        if (iv != null) {
            box.getChildren().add(iv);
        }

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 10px;");

        Label lblValor = new Label("0");
        lblValor.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px; -fx-font-weight: bold;");

        VBox textos = new VBox(0);
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
        lbl.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 11px;");
        return lbl;
    }

    private Label criarSubtitulo(String texto) {
        Label lbl = new Label("— " + texto + " —");
        lbl.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
        return lbl;
    }

    private ProgressBar criarBarraProgresso() {
        ProgressBar barra = new ProgressBar(0);
        barra.setPrefWidth(LARGURA_PAINEL - 32);
        barra.setPrefHeight(12);
        barra.setStyle("-fx-accent: #2ecc71; -fx-control-inner-background: #1a252f;");
        return barra;
    }

    private Label criarTitulo(String texto) {
        Label lbl = new Label(texto);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lbl.setStyle("-fx-text-fill: #ecf0f1;");
        return lbl;
    }

    private Label criarTexto() {
        Label lbl = new Label();
        lbl.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        lbl.setStyle("-fx-text-fill: #ecf0f1;");
        lbl.setWrapText(true);
        return lbl;
    }
}
