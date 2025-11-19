package modelo;

import java.time.LocalDateTime;

public class MetricaCardiaca extends Metrica {
    private int frecuenciaCardiaca;
    private double variabilidadCardiaca;

    public MetricaCardiaca(String dispositivoOrigen, String usuarioId,
                           int frecuenciaCardiaca, double variabilidadCardiaca) {
        super(dispositivoOrigen, usuarioId);
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.variabilidadCardiaca = variabilidadCardiaca;
    }

    // POLIMORFISMO
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

    @Override
    public String getTipo() {
        return "Frecuencia Cardíaca";
    }

    // ENCAPSULAMIENTO
    public int getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public double getVariabilidadCardiaca() { return variabilidadCardiaca; }
}
