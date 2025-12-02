package modelo;

import java.io.Serializable;

public class MetricaCardiaca extends Metrica implements Serializable {
    private int frecuenciaCardiaca;
    private double variabilidadCardiaca;

    /**
     * Constructor para crear una métrica cardíaca.
     * @param dispositivoOrigen
     * @param usuarioId
     * @param frecuenciaCardiaca
     * @param variabilidadCardiaca
     */
    public MetricaCardiaca(String dispositivoOrigen, String usuarioId,
                           int frecuenciaCardiaca, double variabilidadCardiaca) {
        super(dispositivoOrigen, usuarioId);
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.variabilidadCardiaca = variabilidadCardiaca;
    }

    /**
     * Calcula el indicador de salud cardíaca basado en la frecuencia.
     * @return
     */
    @Override
    public double calcularIndicador() {
        if (frecuenciaCardiaca >= 60 && frecuenciaCardiaca <= 100) {
            return 100.0;
        } else if (frecuenciaCardiaca < 60) {
            return Math.max(0, 100 - (60 - frecuenciaCardiaca) * 2);
        } else {
            return Math.max(0, 100 - (frecuenciaCardiaca - 100) * 1.5);
        }
    }

    /**
     * Obtiene el tipo de métrica.
     * @return
     */
    @Override
    public String getTipo() {
        return "Frecuencia Cardíaca";
    }

    /**
     * Obtiene la frecuencia cardíaca registrada.
     * @return
     */
    public int getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    /**
     * Obtiene la variabilidad de la frecuencia cardíaca.
     * @return
     */
    public double getVariabilidadCardiaca() {
        return variabilidadCardiaca;
    }
}