package pt.isec.pa.deepsea.ui.res;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Carrega e mantém em cache as imagens do jogo em {@code /images/} nos resources.
 */
public class ImageManager {

    private ImageManager() {
    }

    private static final HashMap<String, Image> images = new HashMap<>();

    /**
     * Obtém uma imagem pelo nome de ficheiro (ex.: {@code "drone.png"}).
     * @param filename Nome do ficheiro dentro de {@code src/main/resources/images/}.
     * @return Imagem carregada ou {@code null} se o recurso não existir.
     */
    public static Image getImage(String filename) {
        Image image = images.get(filename);

        if (image == null) {
            try (InputStream is = ImageManager.class.getResourceAsStream("/images/" + filename)) {
                if (is != null) {
                    image = new Image(is);
                    images.put(filename, image);
                } else {
                    System.err.println("Aviso: Imagem não encontrada - /images/" + filename);
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar a imagem: " + filename);
                return null;
            }
        }

        return image;
    }


}