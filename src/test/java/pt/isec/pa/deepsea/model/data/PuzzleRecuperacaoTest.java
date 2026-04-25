package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PuzzleRecuperacaoTest {

    private PuzzleRecuperacao puzzle;

    @BeforeEach
    void setUp() {
        puzzle = new PuzzleRecuperacao();
    }

    @Test
    void testMoverPecaValida() {
        boolean moveu = false;
        // Percorre o tabuleiro até encontrar uma peça encostada ao buraco e move-a
        for (int l = 0; l < 4 && !moveu; l++) {
            for (int c = 0; c < 4 && !moveu; c++) {
                if (puzzle.moverPeca(l, c)) {
                    moveu = true;
                }
            }
        }
        assertTrue(moveu, "Deveria ser possível mover pelo menos uma peça num puzzle válido.");
        assertEquals(1, puzzle.getMovimentos(), "O contador de movimentos deve ser 1.");
    }

    @Test
    void testMoverPecaInvalida() {
        boolean encontrouInvalida = false;
        // Tenta peças até encontrar uma que dê "false" (que esteja bloqueada)
        for (int l = 0; l < 4 && !encontrouInvalida; l++) {
            for (int c = 0; c < 4 && !encontrouInvalida; c++) {
                if (!puzzle.moverPeca(l, c)) {
                    encontrouInvalida = true;
                } else {
                    // Se moveu sem querer, temos de repor o puzzle a zeros
                    puzzle = new PuzzleRecuperacao();
                }
            }
        }
        assertTrue(encontrouInvalida, "Devia existir uma peça bloqueada.");
        assertEquals(0, puzzle.getMovimentos(), "Tentativas inválidas não podem contar como movimento.");
    }

    @Test
    void testLimiteMovimentos() {
        int movimentosFeitos = 0;

        // Joga até bater no limite exato definido nas Constantes
        while (movimentosFeitos < Constantes.MAX_MOV_PUZZLE) {
            boolean moveuNaRonda = false;
            for (int l = 0; l < 4 && !moveuNaRonda; l++) {
                for (int c = 0; c < 4 && !moveuNaRonda; c++) {
                    if (puzzle.moverPeca(l, c)) {
                        moveuNaRonda = true;
                        movimentosFeitos++;
                    }
                }
            }
        }

        assertEquals(Constantes.MAX_MOV_PUZZLE, puzzle.getMovimentos());

        // Tenta dar mais um passo depois do limite e tem de bloquear!
        boolean extraMove = false;
        for (int l = 0; l < 4; l++) {
            for (int c = 0; c < 4; c++) {
                if (puzzle.moverPeca(l, c)) extraMove = true;
            }
        }
        assertFalse(extraMove, "Não deve permitir movimentos além do limite.");
        assertEquals(Constantes.MAX_MOV_PUZZLE, puzzle.getMovimentos());
    }
}