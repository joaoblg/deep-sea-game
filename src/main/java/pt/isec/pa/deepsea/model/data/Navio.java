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
    private Drone droneSelecionado;
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
        if (droneSelecionado == null) {
            droneSelecionado = drone;
        }
    }

    /**
     * Define o drone selecionado para operações na oficina e rampa de lançamento.
     * @param drone Drone da frota.
     * @return true se o drone pertencer à frota.
     */
    public boolean selecionarDrone(Drone drone) {
        if (drone == null || !drones.contains(drone)) {
            return false;
        }
        this.droneSelecionado = drone;
        return true;
    }

    /**
     * Seleciona um drone pelo índice (1 = primeiro na lista ordenada por casco).
     * @param indice Posição na lista (base 1).
     * @return true se o índice for válido.
     */
    public boolean selecionarDronePorIndice(int indice) {
        List<Drone> lista = getDronesOrdenadosPorCasco();
        if (indice < 1 || indice > lista.size()) {
            return false;
        }
        return selecionarDrone(lista.get(indice - 1));
    }

    /**
     * @return Índice (base 1) do drone selecionado na lista ordenada por casco, ou 0 se nenhum.
     */
    public int getIndiceDroneSelecionado() {
        List<Drone> lista = getDronesOrdenadosPorCasco();
        Drone ativo = getDroneAtivo();
        if (ativo == null) {
            return 0;
        }
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i) == ativo) {
                return i + 1;
            }
        }
        return 0;
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
            boolean jaExiste = false;
            for (Artefacto art : artefactosRecolhidos) {
                if (art.getIdArtefacto() == id) {
                    jaExiste = true;
                    break;
                }
            }
            if (!jaExiste) {
                artefactosRecolhidos.add(new Artefacto(id));
            }
        }
    }

    /**
     * @return Número de artefactos distintos recolhidos pelo navio.
     */
    public int contagemArtefactosUnicos() {
        return artefactosRecolhidos.size();
    }

    /**
     * Transfere combustível do navio para o drone.
     * @param drone Drone a abastecer.
     * @param quantidade Unidades a transferir.
     * @return true se a transferência for possível.
     */
    public boolean transferirCombustivelParaDrone(Drone drone, int quantidade) {
        if (drone == null || quantidade <= 0) {
            return false;
        }
        if (combustivel < quantidade) {
            return false;
        }
        combustivel -= quantidade;
        drone.adicionarCombustivel(quantidade);
        return true;
    }


    /**
     * Devolve o drone selecionado para operações; se inválido, escolhe o primeiro disponível.
     * @return O drone ativo ou null se não houver drones.
     */
    public Drone getDroneAtivo() {
        if (drones == null || drones.isEmpty()) {
            droneSelecionado = null;
            return null;
        }
        if (droneSelecionado != null && drones.contains(droneSelecionado)) {
            return droneSelecionado;
        }
        droneSelecionado = getDronesOrdenadosPorCasco().get(0);
        return droneSelecionado;
    }

    /**
     * Remove um drone da frota e atualiza a seleção.
     * @param drone Drone a remover.
     */
    public void removerDrone(Drone drone) {
        drones.remove(drone);
        if (droneSelecionado == drone) {
            droneSelecionado = null;
        }
    }


    /**
     * Gasta combustível do navio.
     * @param quantidade Unidades de combustivel a gastar.
     * @return true se tiver combustível suficiente e gastar, false caso contrário.
     */
    public boolean gastarCombustivel(int quantidade) {
        if (this.combustivel >= quantidade) {
            this.combustivel -= quantidade;
            return true;
        }else{
            this.combustivel=0;
            return false;

        }
    }
}
