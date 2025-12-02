package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class OximetroPulso extends DispositivoWearable implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor para crear un oxímetro de pulso.
     * @param idDispositivo
     * @param nombre
     * @param marca
     */
    public OximetroPulso(String idDispositivo, String nombre, String marca) {
        super(idDispositivo, nombre, marca);
    }

    /**
     * Obtiene el tipo de dispositivo.
     * @return
     */
    @Override
    public String getTipoDispositivo() {
        return "Oxímetro de Pulso";
    }

    /**
     * Sincroniza y genera datos simulados del oxímetro.
     * @param usuarioId
     * @return
     */
    @Override
    public List<Metrica> sincronizarDatos(String usuarioId) {
        List<Metrica> metricas = new ArrayList<>();
        Random r = new Random();

        // Saturación de oxígeno (normal: 95-100%)
        double spo2 = 92 + r.nextDouble() * 6; // 92-98%
        metricas.add(new MetricaOxigeno(getIdDispositivo(), usuarioId, spo2));

        // Frecuencia cardíaca
        int fc = 60 + r.nextInt(40);
        metricas.add(new MetricaCardiaca(getIdDispositivo(), usuarioId, fc, 45));

        return metricas;
    }
}