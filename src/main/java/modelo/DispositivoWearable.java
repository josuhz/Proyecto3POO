package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public abstract class DispositivoWearable implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idDispositivo;
    private String nombre;
    private String marca;
    private LocalDateTime fechaVinculacion;
    private boolean activo;

    public DispositivoWearable(String idDispositivo, String nombre, String marca) {
        this.idDispositivo = idDispositivo;
        this.nombre = nombre;
        this.marca = marca;
        this.fechaVinculacion = LocalDateTime.now();
        this.activo = true;
    }

    public abstract List<Metrica> sincronizarDatos(String usuarioId);
    public abstract String getTipoDispositivo();

    public String getIdDispositivo() { return idDispositivo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getMarca() { return marca; }
    public LocalDateTime getFechaVinculacion() { return fechaVinculacion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
