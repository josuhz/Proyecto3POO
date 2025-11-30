package modelo;

import java.io.Serializable;

public class MetricaActividad extends Metrica implements Serializable {
    private int pasos;
    private double distancia;
    private double caloriasQuemadas;

    public MetricaActividad(String dispositivoOrigen, String usuarioId,
                            int pasos, double distancia, double caloriasQuemadas) {
        super(dispositivoOrigen, usuarioId);
        this.pasos = pasos;
        this.distancia = distancia;
        this.caloriasQuemadas = caloriasQuemadas;
    }

    @Override
    public double calcularIndicador() {
        return Math.min(100, (pasos / 10000.0) * 100);
    }

    @Override
    public String getTipo() {
        return "Actividad Física";
    }

    public int getPasos() { return pasos; }
    public double getDistancia() { return distancia; }
    public double getCaloriasQuemadas() { return caloriasQuemadas; }

}
