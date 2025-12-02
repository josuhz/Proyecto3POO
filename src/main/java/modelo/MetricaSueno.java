package modelo;

import java.io.Serializable;

public class MetricaSueno extends Metrica implements Serializable {
    private double horasDormidas;
    private int calidadSueno;
    private int fasesREM;

    /**
     * Constructor para crear una métrica de sueño.
     * @param dispositivoOrigen
     * @param usuarioId
     * @param horasDormidas
     * @param calidadSueno
     * @param fasesREM
     */
    public MetricaSueno(String dispositivoOrigen, String usuarioId,
                        double horasDormidas, int calidadSueno, int fasesREM) {
        super(dispositivoOrigen, usuarioId);
        this.horasDormidas = horasDormidas;
        this.calidadSueno = calidadSueno;
        this.fasesREM = fasesREM;
    }

    /**
     * Calcula el indicador de calidad del sueño.
     * @return
     */
    @Override
    public double calcularIndicador() {
        double indicadorHoras;
        if (horasDormidas >= 7 && horasDormidas <= 9) {
            indicadorHoras = 100;
        } else if (horasDormidas < 7) {
            indicadorHoras = Math.max(0, 100 - (7 - horasDormidas) * 15);
        } else {
            indicadorHoras = Math.max(0, 100 - (horasDormidas - 9) * 10);
        }
        return (indicadorHoras * 0.6) + (calidadSueno * 0.4);
    }

    /**
     * Obtiene el tipo de métrica.
     * @return
     */
    @Override
    public String getTipo() {
        return "Sueño";
    }

    /**
     * Obtiene las horas de sueño registradas.
     * @return
     */
    public double getHorasSueno() {
        return horasDormidas;
    }

    /**
     * Obtiene la calidad del sueño.
     * @return
     */
    public int getCalidadSueno() {
        return calidadSueno;
    }

    /**
     * Obtiene el número de fases REM.
     * @return
     */
    public int getFasesREM() {
        return fasesREM;
    }
}