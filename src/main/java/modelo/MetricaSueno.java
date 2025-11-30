package modelo;

import java.io.Serializable;

public class MetricaSueno extends Metrica implements Serializable {
    private double horasDormidas;
    private int calidadSueno;
    private int fasesREM;

    public MetricaSueno(String dispositivoOrigen, String usuarioId,
                        double horasDormidas, int calidadSueno, int fasesREM) {
        super(dispositivoOrigen, usuarioId);
        this.horasDormidas = horasDormidas;
        this.calidadSueno = calidadSueno;
        this.fasesREM = fasesREM;
    }

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

    @Override
    public String getTipo() {
        return "Sueño";
    }

    public double getHorasSueno() { return horasDormidas; }
    public int getCalidadSueno() { return calidadSueno; }
    public int getFasesREM() { return fasesREM; }
}
