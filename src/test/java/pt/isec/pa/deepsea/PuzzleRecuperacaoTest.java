package pt.isec.pa.deepsea;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.isec.pa.deepsea.model.data.Constantes;
import pt.isec.pa.deepsea.model.data.PuzzleRecuperacao;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleRecuperacaoTest {

    private PuzzleRecuperacao puzzle;

    @BeforeEach
    void setUp() {
        puzzle = new PuzzleRecuperacao();
    }

    @Test
    void testMoverPecaValida() {
        assertTrue(puzzle.moverPeca(3, 2));
        assertEquals(1, puzzle.getMovimentos());
    }

    @Test
    void testMoverPecaInvalida() {
        assertFalse(puzzle.moverPeca(0, 0));
        assertEquals(0, puzzle.getMovimentos());
    }

    @Test
    void testLimiteMovimentos() {
        for (int i = 0; i < Constantes.MAX_MOV_PUZZLE; i++) {
            if (i % 2 == 0) {
                puzzle.moverPeca(3, 2);
            } else {
                puzzle.moverPeca(3, 3);
            }
        }
        
        assertFalse(puzzle.moverPeca(3, 2));
        assertEquals(Constantes.MAX_MOV_PUZZLE, puzzle.getMovimentos());
    }
}