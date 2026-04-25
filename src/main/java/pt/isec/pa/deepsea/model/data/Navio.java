package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Representa o navio de comando na superfície.
 * Gere o inventário global de minérios e artefactos, o combustível total e a frota de drones.
 */
public class Navio implements Serializable {
    private static final long serialVersionUID = 1L;
    private int combustivel;
    private int minerios;
    private List<Artefacto> artefactosRecolhidos;
    private Set<Drone> drones;
    private int linhaPos;
    private int colunaPos;
    private SuperficieMarinha superficie;

    /**
     * Inicializa o navio com o combustível definido nas constantes e prepara os contentores de recursos.
     */
    public Navio() {
        
        this.combustivel = Constantes.combust_ini_navio;
        this.minerios = 0;
        this.artefactosRecolhidos = new ArrayList<>();
        this.drones = new HashSet<>();
        this.superficie = new SuperficieMarinha();
    }

    /**
     * @return Quantidade de combustível disponível no depósito do navio.
     */
    public int getCombustivel() {
        return combustivel;
    }

    /**
     * Adiciona minérios ao inventário do navio.
     *
     * @param quantidade Unidades de minério a adicionar.
     */
    public void adicionarMinerios(int quantidade) {
        this.minerios += quantidade;
    }

    public SuperficieMarinha getSuperficie() {
        return this.superficie;
    }

    /**
     * Define a posição do navio na grelha da superfície.
     *
     * @param lin Coordenada da linha.
     * @param col Coordenada da coluna.
     */
    public void setPosicao(int lin, int col) {
        this.linhaPos = lin;
        this.colunaPos = col;
    }

    /**
     * Adiciona um drone à frota do navio.
     *
     * @param drone Instância do drone a registar.
     */
    public void addDrone(Drone drone) {
        this.drones.add(drone);
    }

    /**
     * Devolve uma lista de drones ordenada crescentemente pelo nível de combustível.
     *
     * @return List de drones ordenada.
     */
    public List<Drone> getDronesOrdenadosPorCombustivel() {
        List<Drone> listaOrdenada = new ArrayList<>(this.drones);
        listaOrdenada.sort((d1, d2) -> Integer.compare(d1.getCombustivel(), d2.getCombustivel()));
        return listaOrdenada;
    }

    /**
     * Devolve uma lista de drones ordenada crescentemente pela integridade do casco.
     *
     * @return List de drones ordenada.
     */
    public List<Drone> getDronesOrdenadosPorCasco() {
        List<Drone> listaOrdenada = new ArrayList<>(this.drones);
        listaOrdenada.sort((d1, d2) -> Integer.compare(d1.getIntegridadeCasco(), d2.getIntegridadeCasco()));
        return listaOrdenada;
    }


    /**
     * @return a quantidade de minerios guardados no navio
     */

    public int getMinerios() {
        return minerios;
    }

    /**
     * @return lista de artefactos no navio
     */
    public List<Artefacto> getArtefactos() {
        return artefactosRecolhidos;
    }

    /**
     * @return coordenada da linha onde o navio se encontra
     */
    public int getLinhaPos(){
        return linhaPos;
    }

    /**
     * @return coordenada da coluna onde o navio se encontra
     */
    public int getColunaPos(){
        return colunaPos;
    }

    /**
     * @return conjunto dos drones no navio
     */
    public Set<Drone> getDrones(){
        return drones;
    }

    /**
     * Gasta minérios do navio.
     * @param quantidade Unidades a gastar.
     * @return true se tiver minérios suficientes e gastar, false caso contrário.
     */
    public boolean gastarMinerios(int quantidade) {
        if (this.minerios >= quantidade) {
            this.minerios -= quantidade;
            return true;
        }
        return false;
    }

    public void recolherArtefactosDoDrone(Drone drone) {
        for (Integer id : drone.getArtefactosTransp()) {
            this.artefactosRecolhidos.add(new Artefacto(id));
        }
    }


    /**
     * Devolve o drone que está atualmente em operação.
     * Como o drones é um Set, vamos buscar o primeiro elemento disponível.
     * @return O drone ativo ou null se não houver drones.
     */
    public Drone getDroneAtivo() {
        if (drones == null || drones.isEmpty()) {
            return null;
        }
        return drones.iterator().next();
    }
}
