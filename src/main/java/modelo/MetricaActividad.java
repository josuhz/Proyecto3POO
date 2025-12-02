package modelo;

import java.io.Serializable;
public class MetricaActividad extends Metrica implements Serializable {
    private int pasos;
    private double distancia;
    private double caloriasQuemadas;

    /**
     * Constructor para crear una métrica de actividad física.
     * @param dispositivoOrigen
     * @param usuarioId
     * @param pasos
     * @param distancia
     * @param caloriasQuemadas
     */
    public MetricaActividad(String dispositivoOrigen, String usuarioId,
                            int pasos, double distancia, double caloriasQuemadas) {
        super(dispositivoOrigen, usuarioId);
        this.pasos = pasos;
        this.distancia = distancia;
        this.caloriasQuemadas = caloriasQuemadas;
    }

    /**
     * Calcula el indicador de actividad basado en los pasos.
     * @return
     */
    @Override
    public double calcularIndicador() {
        return Math.min(100, (pasos / 10000.0) * 100);
    }

    /**
     * Obtiene el tipo de métrica.
     * @return
     */
    @Override
    public String getTipo() {
        return "Actividad Física";
    }

    /**
     * Obtiene el número de pasos registrados.
     * @return
     */
    public int getPasos() {
        return pasos;
    }

    /**
     * Obtiene la distancia recorrida.
     * @return
     */
    public double getDistancia() {
        return distancia;
    }

    /**
     * Obtiene las calorías quemadas.
     * @return
     */
    public double getCaloriasQuemadas() {
        return caloriasQuemadas;
    }
}