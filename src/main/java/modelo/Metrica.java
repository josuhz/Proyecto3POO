package modelo;
import java.time.LocalDateTime;

public abstract class Metrica {
    private String idMetrica;
    private LocalDateTime timestamp;
    private String dispositivoOrigen;
    private String usuarioId;

    public Metrica(String dispositivoOrigen, String usuarioId) {
        this.idMetrica = "MET-" + System.currentTimeMillis() + "-" + ((int)(Math.random() * 1000));
        this.timestamp = LocalDateTime.now();
        this.dispositivoOrigen = dispositivoOrigen;
        this.usuarioId = usuarioId;
    }

    public Metrica(String dispositivoOrigen, String usuarioId, LocalDateTime timestamp) {
        this.idMetrica = "MET-" + System.currentTimeMillis() + "-" + ((int)(Math.random() * 1000));
        this.timestamp = timestamp;
        this.dispositivoOrigen = dispositivoOrigen;
        this.usuarioId = usuarioId;
    }

    public abstract double calcularIndicador();
    public abstract String getTipo();

    public String getIdMetrica() { return idMetrica; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDispositivoOrigen() { return dispositivoOrigen; }
    public String getUsuarioId() { return usuarioId; }
    protected void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
