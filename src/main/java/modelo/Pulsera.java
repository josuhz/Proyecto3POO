package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pulsera extends DispositivoWearable {

    public Pulsera(String idDispositivo, String nombre, String marca) {
        super(idDispositivo, nombre, marca);
    }

    @Override
    public String getTipoDispositivo() {
        return "Pulsera de Actividad";
    }

    @Override
    public List<Metrica> sincronizarDatos(String usuarioId) {
        List<Metrica> metricas = new ArrayList<>();
        Random r = new Random();

        int pasos = 2000 + r.nextInt(8000);
        double dist = pasos * 0.0008;
        double cal = pasos * 0.04;
        metricas.add(new MetricaActividad(getIdDispositivo(), usuarioId, pasos, dist, cal));

        int fc = 65 + r.nextInt(35);
        metricas.add(new MetricaCardiaca(getIdDispositivo(), usuarioId, fc, 40));

        return metricas;
    }
}
