package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;
import java.util.Random;

/**
 * Representa o fosso marinho (trench) para as fases de descida e ascensão.
 * Gere a geração procedimental de rochas laterais e a colocação de obstáculos.
 */
public class FossoMarinho implements Serializable {
    private static final long serialVersionUID = 1L;
    private int[][] grelha;
    private int largura, altura;

    /**
     * Constrói o fosso e despoleta a geração de terreno e obstáculos.
     * @param largura Número de colunas.
     * @param altura Número de linhas.
     */
    public FossoMarinho(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
        this.grelha = new int[altura][largura];
        gerarRochas();
    }

    /**
     * Gera as rochas laterais garantindo a navegabilidade (oscilação máx. 1 coluna por linha).
     */
    private void gerarRochas() {
        Random rd = new Random();
        int margemEsq = rd.nextInt(largura / 4);
        int margemDir = largura - 1 - rd.nextInt(largura / 4);

        for (int i = 0; i < altura; i++) {
            margemEsq += rd.nextInt(3) - 1;
            margemDir += rd.nextInt(3) - 1;

            if (margemEsq < 0) margemEsq = 0;
            if (margemDir >= largura) margemDir = largura - 1;
            if ((margemEsq + (largura - 1 - margemDir)) > largura * 0.5) {
                margemEsq--;
                margemDir++;
            }

            for (int j = 0; j <= margemEsq; j++) grelha[i][j] = 1;
            for (int j = margemDir; j < largura; j++) grelha[i][j] = 1;
        }
    }

/**
     * Distribui obstáculos aleatórios (animais/correntes) com IDs sequenciais.
     * Chamado apenas quando o drone inicia a descida/subida no fosso.
     */
public void gerarObstaculos() {
        Random rd = new Random();
        int numObstaculos = rd.nextInt(Constantes.NUM_MAX_OBSTACULOS + 1);
        int colocados = 0;
        int tentativas = 0;

        while (colocados < numObstaculos && tentativas < 100) {
            int l = rd.nextInt(altura);
            int c = rd.nextInt(largura);
            
            if (grelha[l][c] == 0) {
                grelha[l][c] = GeradorID.gerar();
                colocados++;
            }
            tentativas++;
        }
    }
    
    /**
     * Configura o fosso para o modo de apresentação (1 rocha por lado e obstáculos fixos).
     */
    public void configurarModoDefesa() {
        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {
                grelha[i][j] = 0;
            }
        }
        
        for (int i = 0; i < altura; i++) {
            grelha[i][0] = 1;
            grelha[i][largura - 1] = 1;
        }
        
        grelha[altura / 2][largura / 2] = GeradorID.gerar();
        grelha[(altura / 2) + 1][largura / 2] = GeradorID.gerar();
    }
}
