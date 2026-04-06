package pt.isec.pa.deepsea.model.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.Serializable;

/**
 * Representa o navio de comando na superfície.
 * Gere o inventário global de minérios e artefactos, o combustível total e a frota de drones.
 */
public class Navio implements Serializable {
    private static final long serialVersionUID = 1L;
    private int combustivel;
    private int minerios;
    private List<Integer> artefactosRecolhidos;
    private Set<Drone> drones;
    private int linhaPos;
    private int colunaPos;

    /**
     * Inicializa o navio com o combustível definido nas constantes e prepara os contentores de recursos.
     */
    public Navio() {

        this.combustivel = Constantes.combust_ini_navio;
        this.minerios = 0;
        this.artefactosRecolhidos = new ArrayList<>();
        this.drones = new HashSet<>();

    }

    /** @return Quantidade de combustível disponível no depósito do navio. */
    public int getCombustivel(){
        return combustivel;
    }
    
    /**
     * Adiciona minérios ao inventário do navio.
     * @param quantidade Unidades de minério a adicionar.
     */
    public void adicionarMinerios(int quantidade){
        this.minerios += quantidade;
    }

    /**
     * Define a posição do navio na grelha da superfície.
     * @param lin Coordenada da linha.
     * @param col Coordenada da coluna.
     */
    public void setPosicao(int lin, int col){
        this.linhaPos=lin;
        this.colunaPos=col;
    }

    /**
     * Adiciona um drone à frota do navio.
     * @param drone Instância do drone a registar.
     */
    public void addDrone(Drone drone){
        this.drones.add(drone);
    }

    /**
     * Devolve uma lista de drones ordenada crescentemente pelo nível de combustível.
     * @return List de drones ordenada.
     */
    public List<Drone> getDronesOrdenadosPorCombustivel() {
        List<Drone> listaOrdenada = new ArrayList<>(this.drones);
        // usamos o 'sort' e ensinamos o Java a comparar usando o getCombustivel()
        listaOrdenada.sort((d1, d2) -> Integer.compare(d1.getCombustivel(), d2.getCombustivel()));
        return listaOrdenada;
    }

    /**
     * Devolve uma lista de drones ordenada crescentemente pela integridade do casco.
     * @return List de drones ordenada.
     */
    public List<Drone> getDronesOrdenadosPorCasco() {
        List<Drone> listaOrdenada = new ArrayList<>(this.drones);
        listaOrdenada.sort((d1, d2) -> Integer.compare(d1.getIntegridadeCasco(), d2.getIntegridadeCasco()));
        return listaOrdenada;
    }
}
