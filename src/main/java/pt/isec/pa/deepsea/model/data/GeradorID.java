package pt.isec.pa.deepsea.model.data;

/**
 * Utilitário para geração de identificadores únicos sequenciais.
 * Garante que obstáculos e monstros tenham IDs partilhados e incrementais.
 */
public class GeradorID {
    private static int proximoId = 1001;

    /**
     * Gera e devolve o próximo ID disponível.
     * @return Identificador inteiro positivo.
     */
    public static int gerar() {
        return proximoId++;
    }
}