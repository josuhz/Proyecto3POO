package modelo;

import java.time.LocalDateTime;

public class MetricaOxigeno extends Metrica {
    private double spo2; // Saturación de oxígeno en porcentaje (95-100% normal)

    public MetricaOxigeno(String dispositivoOrigen, String usuarioId, double spo2) {
        super(dispositivoOrigen, usuarioId);
        this.spo2 = spo2;
    }

    public MetricaOxigeno(String dispositivoOrigen, String usuarioId, double spo2, LocalDateTime timestamp) {
        super(dispositivoOrigen, usuarioId, timestamp);
        this.spo2 = spo2;
    }

    @Override
    public double calcularIndicador() {
        // Indicador de salud basado en SpO2
        if (spo2 >= 95.0) return 100.0; // Excelente
        else if (spo2 >= 90.0) return 70.0; // Aceptable
        else if (spo2 >= 85.0) return 40.0; // Preocupante
        else return 10.0; // Crítico
    }

    @Override
    public String getTipo() {
        return "SATURACION_OXIGENO";
    }

    public double getSpo2() {
        return spo2;
    }

    public String obtenerValor() {
        return String.format("%.1f%%", spo2);
    }

    public String obtenerEstado() {
        if (spo2 >= 95.0) return "NORMAL";
        else if (spo2 >= 90.0) return "BAJA";
        else return "CRITICA";
    }
}