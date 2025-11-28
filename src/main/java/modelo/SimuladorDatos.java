package modelo;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class SimuladorDatos {
    private static final Random random = new Random();

    // Rangos basados en tu imagen
    private static final int FC_MIN = 50;
    private static final int FC_MAX = 120;
    private static final int FC_NORMAL_MIN = 60;
    private static final int FC_NORMAL_MAX = 100;
    private static final int FC_ALERTA_BAJA = 55;
    private static final int FC_ALERTA_ALTA = 105;

    private static final int PASOS_SEDENTARIO = 5000;
    private static final int PASOS_POCO_ACTIVO = 7500;
    private static final int PASOS_MODERADO = 10000;
    private static final int PASOS_ACTIVO = 12500;
    private static final int PASOS_MAX = 20000;

    private static final double SUENO_META = 7.5;
    private static final double SUENO_ALERTA = 6.0;
    private static final double SUENO_MAX = 10.0;
    private static final double SUENO_MIN = 4.0;

    // Rangos para oxígeno
    private static final double OXIGENO_MIN = 85.0;
    private static final double OXIGENO_MAX = 100.0;
    private static final double OXIGENO_NORMAL_MIN = 95.0;
    private static final double OXIGENO_ALERTA = 90.0;

    public static void main(String[] args) {
        generarDatosMes();
    }

    // Método principal que genera todos los datos
    public static void generarDatosMes() {
        generarFrecuenciaCardiaca();
        generarActividadFisica();
        generarSueno();
        generarOxigeno();
    }

    // Métodos individuales para cada tipo de dato
    public static void generarFrecuenciaCardiaca() {
        try {
            File archivo = new File("frecuencia_cardiaca.txt");
            if (archivo.exists()) {
                archivo.delete();
            }

            PrintWriter writer = new PrintWriter(new FileWriter(archivo, true));
            LocalDate fechaInicio = LocalDate.now().minusDays(29);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 0; i < 30; i++) {
                LocalDate fecha = fechaInicio.plusDays(i);
                int frecuencia = generarFrecuenciaCardiacaRealista();
                String estado = obtenerEstadoFrecuenciaCardiaca(frecuencia);

                writer.println(fecha.format(formatter) + "," + frecuencia + "," + estado);
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Error al generar frecuencia cardíaca: " + e.getMessage());
        }
    }

    public static void generarActividadFisica() {
        try {
            File archivo = new File("actividad_fisica.txt");
            if (archivo.exists()) {
                archivo.delete();
            }

            PrintWriter writer = new PrintWriter(new FileWriter(archivo, true));
            LocalDate fechaInicio = LocalDate.now().minusDays(29);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 0; i < 30; i++) {
                LocalDate fecha = fechaInicio.plusDays(i);
                int pasos = generarPasosRealistas();
                int calorias = generarCalorias(pasos);
                String nivelActividad = obtenerNivelActividad(pasos);

                writer.println(fecha.format(formatter) + "," + pasos + "," + calorias + "," + nivelActividad);
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Error al generar actividad física: " + e.getMessage());
        }
    }

    public static void generarSueno() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("sueño.txt"))) {
            LocalDate fechaInicio = LocalDate.now().minusDays(29);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 0; i < 30; i++) {
                LocalDate fecha = fechaInicio.plusDays(i);
                double[] datosSueno = generarDatosSuenoRealistas();
                double totalHoras = datosSueno[0] + datosSueno[1] + datosSueno[2];
                String estadoSueno = obtenerEstadoSueno(totalHoras);

                // Formatear correctamente con un solo decimal y punto como separador
                String totalHorasStr = String.format(Locale.US, "%.1f", totalHoras);
                String ligeroStr = String.format(Locale.US, "%.1f", datosSueno[0]);
                String profundoStr = String.format(Locale.US, "%.1f", datosSueno[1]);
                String remStr = String.format(Locale.US, "%.1f", datosSueno[2]);

                writer.println(fecha.format(formatter) + "," +
                        totalHorasStr + "," +
                        ligeroStr + "," +
                        profundoStr + "," +
                        remStr + "," +
                        estadoSueno);
            }
        } catch (IOException e) {
            System.out.println("Error al generar datos de sueño: " + e.getMessage());
        }
    }

    public static void generarOxigeno() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("oxigeno.txt"))) {
            LocalDate fechaInicio = LocalDate.now().minusDays(29);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 0; i < 30; i++) {
                LocalDate fecha = fechaInicio.plusDays(i);
                double spo2 = generarOxigenoRealista();
                String estado = obtenerEstadoOxigeno(spo2);

                writer.println(fecha.format(formatter) + "," +
                        String.format(Locale.US, "%.1f", spo2) + "," + estado);
            }
        } catch (IOException e) {
            System.out.println("Error al generar datos de oxígeno: " + e.getMessage());
        }
    }

    private static int generarFrecuenciaCardiacaRealista() {
        // 80% de probabilidad de estar en rango normal, 20% de estar fuera
        if (random.nextDouble() < 0.8) {
            return FC_NORMAL_MIN + random.nextInt(FC_NORMAL_MAX - FC_NORMAL_MIN + 1);
        } else {
            // Fuera de rango - puede ser baja o alta
            if (random.nextBoolean()) {
                return FC_MIN + random.nextInt(FC_ALERTA_BAJA - FC_MIN + 1); // Alerta baja
            } else {
                return FC_ALERTA_ALTA + random.nextInt(FC_MAX - FC_ALERTA_ALTA + 1); // Alerta alta
            }
        }
    }

    private static String obtenerEstadoFrecuenciaCardiaca(int frecuencia) {
        if (frecuencia < FC_ALERTA_BAJA) {
            return "ALERTA_BAJA";
        } else if (frecuencia > FC_ALERTA_ALTA) {
            return "ALERTA_ALTA";
        } else if (frecuencia >= FC_NORMAL_MIN && frecuencia <= FC_NORMAL_MAX) {
            return "NORMAL";
        } else {
            return "LIMITE";
        }
    }

    private static int generarPasosRealistas() {
        // Distribución realista: algunos días más activos, otros menos
        double probabilidad = random.nextDouble();

        if (probabilidad < 0.1) { // 10% sedentario
            return random.nextInt(PASOS_SEDENTARIO);
        } else if (probabilidad < 0.3) { // 20% poco activo
            return PASOS_SEDENTARIO + random.nextInt(PASOS_POCO_ACTIVO - PASOS_SEDENTARIO);
        } else if (probabilidad < 0.6) { // 30% moderado
            return PASOS_POCO_ACTIVO + random.nextInt(PASOS_MODERADO - PASOS_POCO_ACTIVO);
        } else if (probabilidad < 0.85) { // 25% activo
            return PASOS_MODERADO + random.nextInt(PASOS_ACTIVO - PASOS_MODERADO);
        } else { // 15% muy activo
            return PASOS_ACTIVO + random.nextInt(PASOS_MAX - PASOS_ACTIVO);
        }
    }

    private static int generarCalorias(int pasos) {
        // Aproximadamente 0.04 calorías por paso
        return (int)(pasos * 0.04);
    }

    private static String obtenerNivelActividad(int pasos) {
        if (pasos < PASOS_SEDENTARIO) {
            return "SEDENTARIO";
        } else if (pasos < PASOS_POCO_ACTIVO) {
            return "POCO_ACTIVO";
        } else if (pasos < PASOS_MODERADO) {
            return "MODERADAMENTE_ACTIVO";
        } else if (pasos < PASOS_ACTIVO) {
            return "ACTIVO";
        } else {
            return "MUY_ACTIVO";
        }
    }

    private static double[] generarDatosSuenoRealistas() {
        double[] sueno = new double[3]; // [ligero, profundo, REM]
        double totalHoras = SUENO_MIN + random.nextDouble() * (SUENO_MAX - SUENO_MIN);

        sueno[0] = totalHoras * 0.5; // Sueño ligero
        sueno[1] = totalHoras * 0.25; // Sueño profundo
        sueno[2] = totalHoras * 0.25; // Sueño REM

        return sueno;
    }

    private static String obtenerEstadoSueno(double horas) {
        if (horas < 5.0) {
            return "DEFICIENTE";
        } else if (horas < 6.0) {
            return "REGULAR";
        } else if (horas < 7.5) {
            return "BUENO";
        } else {
            return "EXCELENTE";
        }
    }

    private static double generarOxigenoRealista() {
        // 90% de probabilidad de estar en rango normal, 10% de estar bajo
        if (random.nextDouble() < 0.9) {
            return OXIGENO_NORMAL_MIN + random.nextDouble() * (OXIGENO_MAX - OXIGENO_NORMAL_MIN);
        } else {
            return OXIGENO_ALERTA + random.nextDouble() * (OXIGENO_NORMAL_MIN - OXIGENO_ALERTA);
        }
    }

    private static String obtenerEstadoOxigeno(double spo2) {
        if (spo2 >= OXIGENO_NORMAL_MIN) {
            return "NORMAL";
        } else if (spo2 >= OXIGENO_ALERTA) {
            return "BAJA";
        } else {
            return "CRITICA";
        }
    }
}