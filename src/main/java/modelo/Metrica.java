package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Metrica implements Serializable {
    private String idMetrica;
    private LocalDateTime timestamp;
    private String dispositivoOrigen;
    private String usuarioId;

    /**
     * Constructor que crea una métrica con timestamp actual.
     * Genera automáticamente un ID único para la métrica.
     * @param dispositivoOrigen
     * @param usuarioId
     */
    public Metrica(String dispositivoOrigen, String usuarioId) {
        this.idMetrica = "MET-" + System.currentTimeMillis() + "-" + ((int)(Math.random() * 1000));
        this.timestamp = LocalDateTime.now();
        this.dispositivoOrigen = dispositivoOrigen;
        this.usuarioId = usuarioId;
    }

    /**
     * Constructor que crea una métrica con timestamp específico.
     * Genera automáticamente un ID único para la métrica.
     * @param dispositivoOrigen
     * @param usuarioId
     * @param timestamp
     */
    public Metrica(String dispositivoOrigen, String usuarioId, LocalDateTime timestamp) {
        this.idMetrica = "MET-" + System.currentTimeMillis() + "-" + ((int)(Math.random() * 1000));
        this.timestamp = timestamp;
        this.dispositivoOrigen = dispositivoOrigen;
        this.usuarioId = usuarioId;
    }

    /**
     * Calcula el indicador de salud específico de la métrica.
     * @return
     */
    public abstract double calcularIndicador();

    /**
     * Obtiene el tipo específico de la métrica.
     * @return
     */
    public abstract String getTipo();

    /**
     * Obtiene el identificador único de la métrica.
     * @return
     */
    public String getIdMetrica() {
        return idMetrica;
    }

    /**
     * Obtiene la fecha y hora de la medición.
     * @return
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Obtiene el ID del dispositivo que generó la métrica.
     * @return
     */
    public String getDispositivoOrigen() {
        return dispositivoOrigen;
    }

    /**
     * Obtiene el ID del usuario propietario de la métrica.
     * @return
     */
    public String getUsuarioId() {
        return usuarioId;
    }

    /**
     * Establece una nueva fecha y hora para la métrica.
     * @param timestamp
     */
    protected void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}