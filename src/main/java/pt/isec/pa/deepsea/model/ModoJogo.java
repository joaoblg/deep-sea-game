package pt.isec.pa.deepsea.model;

/**
 * Classe que guarda modos configuráveis do jogo que podem ser alterados em runtime.
 */
public class ModoJogo {
    private static boolean modoDefesa = false;
    private static boolean vitoriaPorRota = false;
    private static boolean aumentarCombustivelDrone = false;
    private static int targetArtefactos = 0; // 0 significa usar Constantes.NUM_ARTEFACTOS

    public static boolean isModoDefesa() {
        return modoDefesa;
    }

    public static void setModoDefesa(boolean v) {
        modoDefesa = v;
    }

    public static boolean isVitoriaPorRota() { return vitoriaPorRota; }
    public static void setVitoriaPorRota(boolean v) { vitoriaPorRota = v; }

    public static boolean isAumentarCombustivelDrone() { return aumentarCombustivelDrone; }
    public static void setAumentarCombustivelDrone(boolean v) { aumentarCombustivelDrone = v; }

    public static int getTargetArtefactos() { return targetArtefactos; }
    public static void setTargetArtefactos(int t) { targetArtefactos = t; }
}
