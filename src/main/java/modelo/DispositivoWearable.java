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

    /**
     * Constructor para crear un nuevo dispositivo wearable.
     * Establece la fecha de vinculación actual y marca el dispositivo como activo.
     * @param idDispositivo
     * @param nombre
     * @param marca
     */
    public DispositivoWearable(String idDispositivo, String nombre, String marca) {
        this.idDispositivo = idDispositivo;
        this.nombre = nombre;
        this.marca = marca;
        this.fechaVinculacion = LocalDateTime.now();
        this.activo = true;
    }

    /**
     * Sincroniza los datos del dispositivo con el sistema.
     * Metodo abstracto que debe ser implementado por cada tipo específico de dispositivo.
     * @param usuarioId
     * @return
     */
    public abstract List<Metrica> sincronizarDatos(String usuarioId);

    /**
     * Verifica si el dispositivo está activo.
     * @return
     */
    public boolean isActivo() {
        return activo;
    }


    /**
     * Metedos getters y setters de la clase.
     */
    //getters y setters
    public abstract String getTipoDispositivo();
    public String getIdDispositivo() {
        return idDispositivo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getMarca() {
        return marca;
    }
    public LocalDateTime getFechaVinculacion() {
        return fechaVinculacion;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}