package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OximetroPulso extends DispositivoWearable{
    public OximetroPulso(String idDispositivo, String nombre, String marca) {
        super(idDispositivo, nombre, marca);
    }

    @Override
    public String getTipoDispositivo() {
        return "Oxímetro de Pulso";
    }

    @Override
    public List<Metrica> sincronizarDatos(String usuarioId) {
        List<Metrica> metricas = new ArrayList<>();
        Random r = new Random();

        // Saturación de oxígeno - normal: 95-100%
        double spo2 = 95 + r.nextDouble() * 5;
        metricas.add(new MetricaOxigeno(getIdDispositivo(), usuarioId, spo2));

        // mide FC
        int fc = 60 + r.nextInt(40);
        metricas.add(new MetricaCardiaca(getIdDispositivo(), usuarioId, fc, 45));

        return metricas;
    }
}
