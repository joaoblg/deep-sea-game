package pt.isec.pa.deepsea.ui.gui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Registo persistente dos últimos ficheiros de gravação abertos ou guardados.
 */
public final class RecentGames {

    private static final int MAX_RECENT = 5;
    private static final Path STORE = Path.of(
            System.getProperty("user.home"), ".deepsea_mining", "recent_saves.txt");
    private static final LinkedList<String> recent = new LinkedList<>();

    static {
        carregar();
    }

    private RecentGames() {
    }

    /**
     * Adiciona um caminho à lista de jogos recentes e grava em disco.
     * @param path Caminho absoluto do ficheiro de save.
     */
    public static void register(String path) {
        if (path == null || path.isBlank()) {
            return;
        }
        recent.remove(path);
        recent.addFirst(path);
        while (recent.size() > MAX_RECENT) {
            recent.removeLast();
        }
        guardar();
    }

    /**
     * @return Lista dos últimos ficheiros (mais recente primeiro).
     */
    public static List<String> getRecent() {
        return new ArrayList<>(recent);
    }

    private static void carregar() {
        try {
            if (!Files.exists(STORE)) {
                return;
            }
            recent.clear();
            for (String linha : Files.readAllLines(STORE)) {
                if (!linha.isBlank()) {
                    recent.add(linha.trim());
                }
            }
            while (recent.size() > MAX_RECENT) {
                recent.removeLast();
            }
        } catch (IOException e) {
            System.err.println("Aviso: não foi possível carregar jogos recentes.");
        }
    }

    private static void guardar() {
        try {
            Files.createDirectories(STORE.getParent());
            Files.write(STORE, recent);
        } catch (IOException e) {
            System.err.println("Aviso: não foi possível guardar jogos recentes.");
        }
    }
}
