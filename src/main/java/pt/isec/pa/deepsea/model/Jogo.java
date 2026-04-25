package pt.isec.pa.deepsea.model;

import java.io.Serializable;

import pt.isec.pa.deepsea.model.data.CelulaSuperficie;
import pt.isec.pa.deepsea.model.data.Constantes;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.data.Navio;
import pt.isec.pa.deepsea.model.data.SuperficieMarinha;

/**
 * Classe principal do modelo de dados que agrega todos os componentes do jogo.
 * Atua como a fachada para a gestão do Navio, Superfície e lógica de inicialização.
 */
public class Jogo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Navio navio;
    private SuperficieMarinha superficie;

    /**
     * Cria uma nova instância de jogo, inicializando o navio e a superfície.
     */
    public Jogo(){
        this.navio=new Navio();
        this.superficie= new SuperficieMarinha();

        inicioJogo();
    }

    /**
     * Obtém o navio principal do jogo.
     *
     * @return Instância do Navio.
     */
    public Navio getNavio() {
        return navio;
    }

    /**
     * Obtém a superfície marinha associada ao jogo.
     *
     * @return Instância da Superfície Marinha.
     */
    public SuperficieMarinha getSuperficie() {
        return superficie;
    }
    
    /**
     * Configura o estado inicial: posiciona o navio, cria a frota de drones e gera os recursos.
     */
    private void inicioJogo(){

        int centroLin= Constantes.LARGURA_SUPERFICIE/2;
        int centoCol= Constantes.COMPRIMENTO_SUPERFICIE /2;

        navio.setPosicao(centroLin,centoCol);

        for (int i = 0; i < 3; i++) {
            Drone d = new Drone();
            d.setPosicao(centroLin, centoCol);
            navio.addDrone(d);
        }

        if (Constantes.modo_defesa){
            modoDefesa();
        }else{
            modoNormal();
        }
    }

    /**
     * Executa a distribuição aleatória de recursos padrão.
     */
    private void modoNormal(){
        superficie.distribuiRecursos();
    }

    /**
     * Configura todas as células para o modo de apresentação conforme os requisitos da defesa.
     */
    private void modoDefesa(){
        int nLin = superficie.getLinhas();
        int nCol = superficie.getColunas();

        for (int i = 0; i < nLin; i++) {
            for (int j = 0; j < nCol; j++) {
                CelulaSuperficie celula = superficie.getCelula(i, j);
                celula.getFossoM().configurarModoDefesa();
                celula.getFundoM().configurarModoDefesa();
            }
        }

        int centroLin = nLin / 2;
        int centroCol = nCol / 2;

        int metadeArtefactos = Constantes.NUM_ARTEFACTOS / 2;
        int restoArtefactos = Constantes.NUM_ARTEFACTOS - metadeArtefactos;

        CelulaSuperficie zona1 = superficie.getCelula(centroLin, centroCol);
        zona1.getFundoM().addArtefactosDefesa(1, metadeArtefactos);

        CelulaSuperficie zona2 = superficie.getCelula(centroLin, centroCol + 1);
        zona2.getFundoM().addArtefactosDefesa(metadeArtefactos + 1, restoArtefactos);
    }
}
