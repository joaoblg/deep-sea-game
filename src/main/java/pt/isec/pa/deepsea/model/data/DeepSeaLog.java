package pt.isec.pa.deepsea.model.data;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Registo central de eventos do jogo.
 * Implementado com padrão Singleton.
 */
public class DeepSeaLog {

    private static DeepSeaLog instance = null;
    private List<String> logs;

    private DeepSeaLog() {
        logs = new ArrayList<>();
    }

    /**
     * Obtém a instância única do log.
     * @return Instância singleton.
     */
    public static DeepSeaLog getInstance() {
        if (instance == null) {
            instance = new DeepSeaLog();
        }
        return instance;
    }

    /**
     * Adiciona uma nova entrada ao log com data/hora.
     * @param msg Mensagem a registar.
     */
    public void addLog(String msg) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logs.add("[" + time + "] " + msg);
    }

    /**
     * Obtém uma cópia das mensagens de log em memória.
     * @return Lista com as entradas atuais do log.
     */
    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }

    /**
     * Remove todas as mensagens em memória.
     */
    public void limparLogs() {
        logs.clear();
    }

    /**
     * Grava o conteúdo do log para ficheiro de texto.
     * @return true quando a gravação termina com sucesso.
     */
    public boolean gravarNoFicheiro() {
        try (PrintWriter out = new PrintWriter(new FileWriter(Constantes.FICHEIRO_LOG, true))) {
            for (String log : logs) {
                out.println(log);
            }
            limparLogs();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}