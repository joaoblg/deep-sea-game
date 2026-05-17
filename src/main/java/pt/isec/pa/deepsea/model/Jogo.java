package pt.isec.pa.deepsea.model;



import java.io.Serializable;



import pt.isec.pa.deepsea.model.data.CelulaSuperficie;

import pt.isec.pa.deepsea.model.data.Constantes;

import pt.isec.pa.deepsea.model.data.Drone;

import pt.isec.pa.deepsea.model.data.Navio;

import pt.isec.pa.deepsea.model.data.SuperficieMarinha;

import pt.isec.pa.deepsea.model.data.FundoDoMar;

import pt.isec.pa.deepsea.model.data.FossoMarinho;



/**

 * Classe principal do modelo de dados que agrega todos os componentes do jogo.

 * Atua como a fachada para a gestão do Navio, Superfície e lógica de inicialização.

 */

public class Jogo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Navio navio;

    private SuperficieMarinha superficie;

    private pt.isec.pa.deepsea.model.state.DeepSeaState estadoGravado;

    private pt.isec.pa.deepsea.model.data.PuzzleRecuperacao puzzleAtual;

    private int zonaExpedicaoLin = -1;

    private int zonaExpedicaoCol = -1;



    /**

     * Cria uma nova instância de jogo, inicializando o navio e a superfície.

     */

    public Jogo(){

        this.navio=new Navio();

        this.superficie= new SuperficieMarinha();



        inicioJogo();

    }



    public Navio getNavio() {

        return navio;

    }



    public SuperficieMarinha getSuperficie() {

        return superficie;

    }



    /**

     * Fixa a zona de mergulho (célula da superfície) para fosso e fundo desta expedição.

     */

    public void setZonaExpedicao(int linha, int coluna) {

        this.zonaExpedicaoLin = linha;

        this.zonaExpedicaoCol = coluna;

    }



    public void limparZonaExpedicao() {

        this.zonaExpedicaoLin = -1;

        this.zonaExpedicaoCol = -1;

    }



    private CelulaSuperficie getCelulaZonaAtiva() {

        if (zonaExpedicaoLin >= 0 && zonaExpedicaoCol >= 0) {

            return superficie.getCelula(zonaExpedicaoLin, zonaExpedicaoCol);

        }

        return superficie.getCelula(navio.getLinhaPos(), navio.getColunaPos());

    }



    public FundoDoMar getFundoDaZonaAtiva() {

        CelulaSuperficie cel = getCelulaZonaAtiva();

        return cel != null ? cel.getFundoM() : null;

    }



    public FossoMarinho getFossoDaZonaAtiva() {

        CelulaSuperficie cel = getCelulaZonaAtiva();

        return cel != null ? cel.getFossoM() : null;

    }



    private void inicioJogo(){

        limparZonaExpedicao();

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



    private void modoNormal(){

        superficie.distribuiRecursos();

    }



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



    public FundoDoMar getFundo() {

        FundoDoMar fundo = getFundoDaZonaAtiva();

        if (fundo != null) {

            return fundo;

        }

        return superficie.getCelula(navio.getLinhaPos(), navio.getColunaPos()).getFundoM();

    }

    

    public FossoMarinho getFosso() {

        FossoMarinho fosso = getFossoDaZonaAtiva();

        if (fosso != null) {

            return fosso;

        }

        return superficie.getCelula(navio.getLinhaPos(), navio.getColunaPos()).getFossoM();

    }



    public void setEstadoGravado(pt.isec.pa.deepsea.model.state.DeepSeaState estado) {

        this.estadoGravado = estado;

    }



    public pt.isec.pa.deepsea.model.state.DeepSeaState getEstadoGravado() {

        return this.estadoGravado;

    }

    public void setPuzzleAtual(pt.isec.pa.deepsea.model.data.PuzzleRecuperacao p) { this.puzzleAtual = p; }

    public pt.isec.pa.deepsea.model.data.PuzzleRecuperacao getPuzzleAtual() { return this.puzzleAtual; }

}


