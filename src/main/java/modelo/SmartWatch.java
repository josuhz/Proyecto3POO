package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmartWatch extends DispositivoWearable{
    public SmartWatch(String idDispositivo, String nombre, String marca) {
        super(idDispositivo, nombre, marca);
    }

    @Override
    public String getTipoDispositivo() {
        return "SmartWatch";
    }

    @Override
    public List<Metrica> sincronizarDatos(String usuarioId) {
        List<Metrica> metricas = new ArrayList<>();
        Random r = new Random();

        // Frecuencia cardíaca
        int fc = 60 + r.nextInt(40);
        double var = 30 + r.nextDouble() * 50;
        metricas.add(new MetricaCardiaca(getIdDispositivo(), usuarioId, fc, var));

        // Actividad
        int pasos = 3000 + r.nextInt(7000);
        double dist = pasos * 0.0008;
        double cal = pasos * 0.04;
        metricas.add(new MetricaActividad(getIdDispositivo(), usuarioId, pasos, dist, cal));

        // Sueño
        double horas = 6 + r.nextDouble() * 3;
        int calidad = 60 + r.nextInt(40);
        int rem = 2 + r.nextInt(3);
        metricas.add(new MetricaSueno(getIdDispositivo(), usuarioId, horas, calidad, rem));

        return metricas;
    }
}
