package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Pulsera extends DispositivoWearable implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor para crear una pulsera de actividad.
     * @param idDispositivo
     * @param nombre
     * @param marca
     */
    public Pulsera(String idDispositivo, String nombre, String marca) {
        super(idDispositivo, nombre, marca);
    }

    /**
     * Obtiene el tipo de dispositivo.
     * @return
     */
    @Override
    public String getTipoDispositivo() {
        return "Pulsera de Actividad";
    }

    /**
     * Sincroniza y genera datos simulados de la pulsera.
     * @param usuarioId
     * @return
     */
    @Override
    public List<Metrica> sincronizarDatos(String usuarioId) {
        List<Metrica> metricas = new ArrayList<>();
        Random r = new Random();

        // Actividad física
        int pasos = 2000 + r.nextInt(8000); // 2,000-10,000 pasos
        double dist = pasos * 0.0008; // Distancia en km
        double cal = pasos * 0.04; // Calorías quemadas
        metricas.add(new MetricaActividad(getIdDispositivo(), usuarioId, pasos, dist, cal));

        // Frecuencia cardíaca básica
        int fc = 65 + r.nextInt(35); // 65-100 BPM
        metricas.add(new MetricaCardiaca(getIdDispositivo(), usuarioId, fc, 40));

        return metricas;
    }
}