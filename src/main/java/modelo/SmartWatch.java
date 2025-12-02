package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Dispositivo wearable tipo SmartWatch.
 * Es el dispositivo más completo que mide frecuencia cardíaca,
 * actividad física y calidad del sueño.
 */
public class SmartWatch extends DispositivoWearable implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor para crear un SmartWatch.
     * @param idDispositivo ID único del dispositivo
     * @param nombre Nombre descriptivo del dispositivo
     * @param marca Marca o fabricante del dispositivo
     */
    public SmartWatch(String idDispositivo, String nombre, String marca) {
        super(idDispositivo, nombre, marca);
    }

    /**
     * Obtiene el tipo de dispositivo.
     * @return "SmartWatch"
     */
    @Override
    public String getTipoDispositivo() {
        return "SmartWatch";
    }

    /**
     * Sincroniza y genera datos simulados del SmartWatch.
     * Genera métricas completas de:
     * - Frecuencia cardíaca (60-100 BPM)
     * - Actividad física (3,000-10,000 pasos)
     * - Sueño (6-9 horas)
     * @param usuarioId ID del usuario propietario
     * @return Lista con métricas cardíacas, de actividad y de sueño
     */
    @Override
    public List<Metrica> sincronizarDatos(String usuarioId) {
        List<Metrica> metricas = new ArrayList<>();
        Random r = new Random();

        // Frecuencia cardíaca
        int fc = 60 + r.nextInt(40); // 60-100 BPM
        double var = 30 + r.nextDouble() * 50; // Variabilidad 30-80
        metricas.add(new MetricaCardiaca(getIdDispositivo(), usuarioId, fc, var));

        // Actividad física
        int pasos = 3000 + r.nextInt(7000); // 3,000-10,000 pasos
        double dist = pasos * 0.0008; // Distancia en km
        double cal = pasos * 0.04; // Calorías quemadas
        metricas.add(new MetricaActividad(getIdDispositivo(), usuarioId, pasos, dist, cal));

        // Sueño
        double horas = 6 + r.nextDouble() * 3; // 6-9 horas
        int calidad = 60 + r.nextInt(40); // Calidad 60-100
        int rem = 2 + r.nextInt(3); // Fases REM 2-5
        metricas.add(new MetricaSueno(getIdDispositivo(), usuarioId, horas, calidad, rem));

        return metricas;
    }
}