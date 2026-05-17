package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Drone;

import java.util.ArrayList;
import java.util.List;

/**
 * Painéis laterais de informação do jogo (navio, frota de drones e atalhos).
 * Atualiza-se via {@link DeepSeaManager#PROP_DADOS} e {@link DeepSeaManager#PROP_ESTADO}.
 */
public class DeepSeaHUD {

    private static final String ESTILO_PAINEL =
            "-fx-background-color: #2c3e50; -fx-border-color: #1a252f;";
    private static final double LARGURA_PAINEL = 240;

    private final DeepSeaManager manager;
    private final VBox painelEsquerdo;
    private final VBox painelDireito;

    private Label lblEstado;
    private Label lblNavioCombustivel;
    private Label lblNavioRecursos;
    private Label lblNavioPosicao;
    private Label lblDroneAtivo;
    private final List<Label> lblsFrota = new ArrayList<>();
    private VBox boxFrota;
    private Label lblAtalhos;

    /**
     * @param manager Facade do modelo.
     */
    public DeepSeaHUD(DeepSeaManager manager) {
        this.manager = manager;
        this.painelEsquerdo = criarPainelEsquerdo();
        this.painelDireito = criarPainelDireito();
        registarListeners();
        atualizar();
    }

    /** @return Painel lateral esquerdo (navio e estado). */
    public VBox getPainelEsquerdo() {
        return painelEsquerdo;
    }

    /** @return Painel lateral direito (drones e atalhos). */
    public VBox getPainelDireito() {
        return painelDireito;
    }

    private VBox criarPainelEsquerdo() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(16));
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setPrefWidth(LARGURA_PAINEL);
        panel.setMinWidth(LARGURA_PAINEL);
        panel.setStyle(ESTILO_PAINEL + " -fx-border-width: 0 2 0 0;");

        Label titulo = criarTitulo("INFORMAÇÃO");
        lblEstado = criarTexto();
        lblNavioCombustivel = criarTexto();
        lblNavioRecursos = criarTexto();
        lblNavioPosicao = criarTexto();

        panel.getChildren().addAll(
                titulo,
                new Separator(),
                lblEstado,
                new Label("— Navio —"),
                lblNavioCombustivel,
                lblNavioRecursos,
                lblNavioPosicao
        );
        return panel;
    }

    private VBox criarPainelDireito() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(16));
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setPrefWidth(LARGURA_PAINEL);
        panel.setMinWidth(LARGURA_PAINEL);
        panel.setStyle(ESTILO_PAINEL + " -fx-border-width: 0 0 0 2;");

        Label titulo = criarTitulo("DRONES");
        lblDroneAtivo = criarTexto();
        boxFrota = new VBox(6);
        lblAtalhos = criarTexto();
        lblAtalhos.setWrapText(true);
        lblAtalhos.setStyle("-fx-text-fill: #bdc3c7;");

        panel.getChildren().addAll(
                titulo,
                new Separator(),
                lblDroneAtivo,
                new Label("— Frota —"),
                boxFrota,
                new Separator(),
                new Label("Atalhos"),
                lblAtalhos
        );
        return panel;
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

    private void registarListeners() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt ->
                Platform.runLater(this::atualizar));
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt ->
                Platform.runLater(this::atualizar));
    }

    /** Atualiza o conteúdo dos painéis com os dados atuais do modelo. */
    public void atualizar() {
        if (manager.getState() == null) {
            return;
        }

        lblEstado.setText("Estado: " + manager.getState().name());
        lblNavioCombustivel.setText("Combustível: " + manager.getCombustivelNavio());
        lblNavioRecursos.setText(String.format(
                "Minérios: %d  |  Artefactos: %d",
                manager.getMineriosNavio(),
                manager.getArtefactosNavio()));
        lblNavioPosicao.setText(String.format(
                "Posição: [%d, %d]",
                manager.getLinhaNavio(),
                manager.getColunaNavio()));

        Drone drone = manager.getDrone();
        if (drone != null) {
            int indice = manager.getIndiceDroneSelecionado();
            lblDroneAtivo.setText(String.format(
                    "Drone %d — Comb.: %d | Min.: %d | Casco: %d%%",
                    indice > 0 ? indice : 1,
                    drone.getCombustivel(),
                    drone.getMineriosTransp(),
                    drone.getIntegridadeCasco()));
            lblDroneAtivo.setStyle("-fx-text-fill: #f1c40f;");
        } else {
            lblDroneAtivo.setText("Nenhum drone em missão");
            lblDroneAtivo.setStyle("-fx-text-fill: #95a5a6;");
        }

        atualizarFrota();
        lblAtalhos.setText("""
                Setas — Mover / Puzzle
                Espaço — Avaliar fundo
                R — Recolher minério
                V — Voltar ao navio / Sair oficina
                O — Oficina (superfície)
                Esc — Sair da oficina
                Rato — Superfície: mergulho na célula
                Rato — Fundo: célula atual: avaliar
                Rato — Puzzle: deslizar peça""");
    }

    private void atualizarFrota() {
        boxFrota.getChildren().clear();
        lblsFrota.clear();
        int i = 1;
        for (String linha : manager.getResumoFrotaDrones()) {
            Label lbl = criarTexto();
            lbl.setText(i + ". " + linha);
            lblsFrota.add(lbl);
            boxFrota.getChildren().add(lbl);
            i++;
        }
        if (boxFrota.getChildren().isEmpty()) {
            Label vazio = criarTexto();
            vazio.setText("(sem drones)");
            boxFrota.getChildren().add(vazio);
        }
    }
}
