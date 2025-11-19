package modelo;

public class MetricaOxigeno extends Metrica {
    private double saturacionOxigeno; // SpO2 en porcentaje

    public MetricaOxigeno(String dispositivoOrigen, String usuarioId, double saturacionOxigeno) {
        super(dispositivoOrigen, usuarioId);
        this.saturacionOxigeno = saturacionOxigeno;
    }

    @Override
    public double calcularIndicador() {
        // Normal: >95%, crítico: <90%
        if (saturacionOxigeno >= 95) {
            return 100.0;
        } else if (saturacionOxigeno >= 90) {
            return 100 - ((95 - saturacionOxigeno) * 10);
        } else {
            return Math.max(0, 50 - ((90 - saturacionOxigeno) * 5));
        }
    }

    @Override
    public String getTipo() {
        return "Saturación de Oxígeno";
    }

    public double getSaturacionOxigeno() { return saturacionOxigeno; }
}
