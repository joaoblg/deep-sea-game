package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;
import java.util.Random;

/**
 * Representa o fosso marinho (trench) para as fases de descida e ascensão.
 * Gere a geração procedimental de rochas laterais e a colocação de obstáculos.
 */
public class FossoMarinho implements Serializable {
    private static final long serialVersionUID = 1L;
    private ElemFosso[][] grelha;
    private int largura, altura;

    /**
     * Constrói o fosso e despoleta a geração de terreno e obstáculos.
     * @param largura Número de colunas.
     * @param altura Número de linhas.
     */
    public FossoMarinho(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
        this.grelha = new ElemFosso[altura][largura];
        gerarRochas();
    }

    public ElemFosso getCont(int l, int c) { return grelha[l][c]; }

    /**
     * Gera as rochas laterais do fosso de forma procedimental.
     * Garante que a passagem permanece navegável e que o volume de rochas
     * não ultrapassa 50% da largura total em cada linha.
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

            while (((margemEsq + 1) + (largura - margemDir)) > (largura / 2)) {
                if (margemEsq > 0 && (margemEsq + 1) >= (largura - margemDir)) {
                    margemEsq--;
                } else if (margemDir < largura - 1) {
                    margemDir++;
                } else if (margemEsq > 0) {
                    margemEsq--;
                } else {
                    break;
                }
            }

            for (int j = 0; j <= margemEsq; j++) grelha[i][j] = new Rocha();
            for (int j = margemDir; j < largura; j++) grelha[i][j] = new Rocha();
        }
    }

/**
     * Distribui obstáculos aleatórios (animais/correntes) com IDs sequenciais.
     * Chamado apenas quando o drone inicia a descida/subida no fosso.
     */
    /**
     * Remove animais e correntes, mantendo as rochas laterais.
     */
    public void limparObstaculos() {
        for (int l = 0; l < altura; l++) {
            for (int c = 0; c < largura; c++) {
                if (grelha[l][c] != null && !(grelha[l][c] instanceof Rocha)) {
                    grelha[l][c] = null;
                }
            }
        }
    }

    public void gerarObstaculos() {
        limparObstaculos();
        Random rd = new Random();
        int numObstaculos = rd.nextInt(Constantes.NUM_MAX_OBSTACULOS + 1);
        int colocados = 0;
        int tentativas = 0;

        while (colocados < numObstaculos && tentativas < 100) {
            int l = rd.nextInt(altura);
            int c = rd.nextInt(largura);
            
            if (grelha[l][c] == null) {
                if (rd.nextBoolean()){
                    grelha[l][c]=new AnimalMarinho();
                }else{
                    grelha[l][c]=new CorrenteMarinha();
                }
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
                grelha[i][j] = null;
            }
        }
        
        for (int i = 0; i < altura; i++) {
            grelha[i][0] = new Rocha();
            grelha[i][largura - 1] = new Rocha();
        }

        if (altura > 5 && largura > 5) {
            grelha[2][largura / 2] = new AnimalMarinho();
            grelha[5][largura / 2] = new CorrenteMarinha();
        }
    }

    public int getAltura() { return altura; }
    public int getLargura() { return largura; }
}
