package pt.isec.pa.deepsea.model.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import pt.isec.pa.deepsea.model.Jogo;

/**
 * Utilitário de persistência binária para guardar e restaurar jogos.
 */
public class GestorFicheiros {

    /**
     * Grava o objeto de jogo em ficheiro binário.
     * @param jogo Instância de jogo a serializar.
     * @param ficheiro Caminho do ficheiro de destino.
     * @return true quando a gravação é bem-sucedida.
     */
    public static boolean gravarJogo(Jogo jogo, String ficheiro) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheiro))) {
            oos.writeObject(jogo);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao gravar ficheiro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carrega um jogo serializado a partir de ficheiro binário.
     * @param ficheiro Caminho do ficheiro de origem.
     * @return Instância de jogo carregada, ou null em caso de erro.
     */
    public static Jogo carregarJogo(String ficheiro) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheiro))) {
            return (Jogo) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar ficheiro: " + e.getMessage());
            return null;
        }
    }
}