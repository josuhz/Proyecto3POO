package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MetricaOxigeno extends Metrica implements Serializable {
    private double spo2;

    /**
     * Constructor que crea una métrica de oxígeno con timestamp actual.
     * @param dispositivoOrigen
     * @param usuarioId
     * @param spo2
     */
    public MetricaOxigeno(String dispositivoOrigen, String usuarioId, double spo2) {
        super(dispositivoOrigen, usuarioId);
        this.spo2 = spo2;
    }

    /**
     * Constructor que crea una métrica de oxígeno con timestamp específico.
     * @param dispositivoOrigen
     * @param usuarioId
     * @param spo2
     * @param timestamp
     */
    public MetricaOxigeno(String dispositivoOrigen, String usuarioId, double spo2, LocalDateTime timestamp) {
        super(dispositivoOrigen, usuarioId, timestamp);
        this.spo2 = spo2;
    }

    /**
     * Calcula el indicador de salud basado en la saturación de oxígeno.
     * @return
     */
    @Override
    public double calcularIndicador() {
        if (spo2 >= 95.0) return 100.0; // Excelente
        else if (spo2 >= 90.0) return 70.0; // Aceptable
        else if (spo2 >= 85.0) return 40.0; // Preocupante
        else return 10.0; // Crítico
    }

    /**
     * Obtiene el tipo de métrica.
     * @return
     */
    @Override
    public String getTipo() {
        return "SATURACION_OXIGENO";
    }

    /**
     * Obtiene el valor de saturación de oxígeno.
     * @return
     */
    public double getSpo2() {
        return spo2;
    }

    /**
     * Obtiene el valor formateado de SpO2 con símbolo de porcentaje.
     * @return
     */
    public String obtenerValor() {
        return String.format("%.1f%%", spo2);
    }

    /**
     * Determina el estado de salud según el nivel de SpO2.
     * @return
     */
    public String obtenerEstado() {
        if (spo2 >= 95.0) return "NORMAL";
        else if (spo2 >= 90.0) return "BAJA";
        else return "CRITICA";
    }
}