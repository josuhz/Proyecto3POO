package modelo;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;
public class SimuladorDatos {
    private static final Random random = new Random();

    // Constantes de rangos para frecuencia cardíaca
    private static final int FC_MIN = 50;
    private static final int FC_MAX = 120;
    private static final int FC_NORMAL_MIN = 60;
    private static final int FC_NORMAL_MAX = 100;
    private static final int FC_ALERTA_BAJA = 55;
    private static final int FC_ALERTA_ALTA = 105;

    // Constantes de rangos para pasos
    private static final int PASOS_SEDENTARIO = 5000;
    private static final int PASOS_POCO_ACTIVO = 7500;
    private static final int PASOS_MODERADO = 10000;
    private static final int PASOS_ACTIVO = 12500;
    private static final int PASOS_MAX = 20000;

    // Constantes de rangos para sueño
    private static final double SUENO_META = 7.5;
    private static final double SUENO_ALERTA = 6.0;
    private static final double SUENO_MAX = 10.0;
    private static final double SUENO_MIN = 4.0;

    // Constantes de rangos para oxígeno
    private static final double OXIGENO_MIN = 85.0;
    private static final double OXIGENO_MAX = 100.0;
    private static final double OXIGENO_NORMAL_MIN = 95.0;
    private static final double OXIGENO_ALERTA = 90.0;

    /**
     * Metodo main para ejecutar la generación de datos.
     * @param args
     */
    public static void main(String[] args) {
        generarDatosMes();
    }

    /**
     * Genera todos los archivos de datos de salud para los últimos 30 días.
     * Crea archivos de frecuencia cardíaca, actividad física, sueño y oxígeno.
     */
    public static void generarDatosMes() {
        generarFrecuenciaCardiaca();
        generarActividadFisica();
        generarSueno();
        generarOxigeno();
    }

    /**
     * Genera el archivo de frecuencia cardíaca con 30 días de datos.
     * Elimina el archivo existente y crea uno nuevo.
     */
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

    /**
     * Genera el archivo de actividad física con 30 días de datos.
     * Elimina el archivo existente y crea uno nuevo.
     */
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

    /**
     * Genera el archivo de sueño con 30 días de datos.
     * Elimina el archivo existente y crea uno nuevo.
     */
    public static void generarSueno() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("sueño.txt"))) {
            LocalDate fechaInicio = LocalDate.now().minusDays(29);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 0; i < 30; i++) {
                LocalDate fecha = fechaInicio.plusDays(i);
                double[] datosSueno = generarDatosSuenoRealistas();
                double totalHoras = datosSueno[0] + datosSueno[1] + datosSueno[2];
                String estadoSueno = obtenerEstadoSueno(totalHoras);

                // Formatear con un decimal y punto como separador
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

    /**
     * Genera el archivo de oxígeno con 30 días de datos.
     * Elimina el archivo existente y crea uno nuevo.
     */
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

    /**
     * Genera una frecuencia cardíaca realista.
     * @return
     */
    private static int generarFrecuenciaCardiacaRealista() {
        if (random.nextDouble() < 0.8) {
            return FC_NORMAL_MIN + random.nextInt(FC_NORMAL_MAX - FC_NORMAL_MIN + 1);
        } else {
            if (random.nextBoolean()) {
                return FC_MIN + random.nextInt(FC_ALERTA_BAJA - FC_MIN + 1);
            } else {
                return FC_ALERTA_ALTA + random.nextInt(FC_MAX - FC_ALERTA_ALTA + 1);
            }
        }
    }

    /**
     * Determina el estado de la frecuencia cardíaca.
     * @param frecuencia
     * @return
     */
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

    /**
     * Genera un número de pasos realista con distribución probabilística.
     * @return
     */
    private static int generarPasosRealistas() {
        double probabilidad = random.nextDouble();

        if (probabilidad < 0.1) {
            return random.nextInt(PASOS_SEDENTARIO);
        } else if (probabilidad < 0.3) {
            return PASOS_SEDENTARIO + random.nextInt(PASOS_POCO_ACTIVO - PASOS_SEDENTARIO);
        } else if (probabilidad < 0.6) {
            return PASOS_POCO_ACTIVO + random.nextInt(PASOS_MODERADO - PASOS_POCO_ACTIVO);
        } else if (probabilidad < 0.85) {
            return PASOS_MODERADO + random.nextInt(PASOS_ACTIVO - PASOS_MODERADO);
        } else {
            return PASOS_ACTIVO + random.nextInt(PASOS_MAX - PASOS_ACTIVO);
        }
    }

    /**
     * Calcula las calorías quemadas basándose en los pasos.
     * @param pasos
     * @return
     */
    private static int generarCalorias(int pasos) {
        return (int)(pasos * 0.04);
    }

    /**
     * Determina el nivel de actividad física según los pasos.
     * @param pasos
     * @return
     */
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

    /**
     * Genera datos de sueño realistas distribuidos en fases.
     * @return
     */
    private static double[] generarDatosSuenoRealistas() {
        double[] sueno = new double[3];
        double totalHoras = SUENO_MIN + random.nextDouble() * (SUENO_MAX - SUENO_MIN);

        sueno[0] = totalHoras * 0.5;  // 50% sueño ligero
        sueno[1] = totalHoras * 0.25; // 25% sueño profundo
        sueno[2] = totalHoras * 0.25; // 25% REM

        return sueno;
    }

    /**
     * Determina el estado de calidad del sueño según las horas dormidas.
     * @param horas
     * @return
     */
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

    /**
     * Genera un nivel de oxígeno en sangre realista.
     * @return
     */
    private static double generarOxigenoRealista() {
        if (random.nextDouble() < 0.9) {
            return OXIGENO_NORMAL_MIN + random.nextDouble() * (OXIGENO_MAX - OXIGENO_NORMAL_MIN);
        } else {
            return OXIGENO_ALERTA + random.nextDouble() * (OXIGENO_NORMAL_MIN - OXIGENO_ALERTA);
        }
    }

    /**
     * Determina el estado del nivel de oxígeno en sangre.
     * @param spo2
     * @return
     */
    private static String obtenerEstadoOxigeno(double spo2) {
        if (spo2 >= OXIGENO_NORMAL_MIN) {
            return "NORMAL";
        } else if (spo2 >= OXIGENO_ALERTA) {
            return "BAJA";
        } else {
            return "CRITICA";
        }
    }

    /**
     * Actualiza solo los archivos de datos que ya existen.
     * Verifica la existencia de cada archivo antes de procesarlo.
     */
    public static void completarSoloDatosExistentes() {
        File fcFile = new File("frecuencia_cardiaca.txt");
        if (fcFile.exists() && fcFile.isFile()) {
            completarFrecuenciaCardiacaFaltante();
            System.out.println("✓ Frecuencia cardíaca actualizada");
        }

        File actividadFile = new File("actividad_fisica.txt");
        if (actividadFile.exists() && actividadFile.isFile()) {
            completarActividadFisicaFaltante();
            System.out.println("✓ Actividad física actualizada");
        }

        File suenoFile = new File("sueño.txt");
        if (suenoFile.exists() && suenoFile.isFile()) {
            completarSuenoFaltante();
            System.out.println("✓ Sueño actualizado");
        }

        File oxigenoFile = new File("oxigeno.txt");
        if (oxigenoFile.exists() && oxigenoFile.isFile()) {
            completarOxigenoFaltante();
            System.out.println("✓ Oxígeno actualizado");
        }
    }

    /**
     * Metodo deprecado. Usar completarSoloDatosExistentes() en su lugar.
     */
    @Deprecated
    public static void completarDatosFaltantes() {
        completarSoloDatosExistentes();
    }

    /**
     * Completa datos de frecuencia cardíaca faltantes desde la última fecha registrada hasta hoy.
     * Solo procesa si el archivo ya existe.
     */
    private static void completarFrecuenciaCardiacaFaltante() {
        try {
            File archivo = new File("frecuencia_cardiaca.txt");

            if (!archivo.exists()) {
                return;
            }

            LocalDate ultimaFecha = obtenerUltimaFechaArchivo(archivo);
            LocalDate hoy = LocalDate.now();

            if (ultimaFecha.isBefore(hoy)) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(archivo, true))) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate fechaActual = ultimaFecha.plusDays(1);

                    while (!fechaActual.isAfter(hoy)) {
                        int frecuencia = generarFrecuenciaCardiacaRealista();
                        String estado = obtenerEstadoFrecuenciaCardiaca(frecuencia);
                        writer.println(fechaActual.format(formatter) + "," + frecuencia + "," + estado);
                        fechaActual = fechaActual.plusDays(1);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al completar frecuencia cardíaca: " + e.getMessage());
        }
    }

    /**
     * Completa datos de actividad física faltantes desde la última fecha registrada hasta hoy.
     * Solo procesa si el archivo ya existe.
     */
    private static void completarActividadFisicaFaltante() {
        try {
            File archivo = new File("actividad_fisica.txt");

            if (!archivo.exists()) {
                return;
            }

            LocalDate ultimaFecha = obtenerUltimaFechaArchivo(archivo);
            LocalDate hoy = LocalDate.now();

            if (ultimaFecha.isBefore(hoy)) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(archivo, true))) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate fechaActual = ultimaFecha.plusDays(1);

                    while (!fechaActual.isAfter(hoy)) {
                        int pasos = generarPasosRealistas();
                        int calorias = generarCalorias(pasos);
                        String nivelActividad = obtenerNivelActividad(pasos);
                        writer.println(fechaActual.format(formatter) + "," + pasos + "," + calorias + "," + nivelActividad);
                        fechaActual = fechaActual.plusDays(1);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al completar actividad física: " + e.getMessage());
        }
    }

    /**
     * Completa datos de sueño faltantes desde la última fecha registrada hasta hoy.
     * Solo procesa si el archivo ya existe.
     */
    private static void completarSuenoFaltante() {
        try {
            File archivo = new File("sueño.txt");

            if (!archivo.exists()) {
                return;
            }

            LocalDate ultimaFecha = obtenerUltimaFechaArchivo(archivo);
            LocalDate hoy = LocalDate.now();

            if (ultimaFecha.isBefore(hoy)) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(archivo, true))) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate fechaActual = ultimaFecha.plusDays(1);

                    while (!fechaActual.isAfter(hoy)) {
                        double[] datosSueno = generarDatosSuenoRealistas();
                        double totalHoras = datosSueno[0] + datosSueno[1] + datosSueno[2];
                        String estadoSueno = obtenerEstadoSueno(totalHoras);

                        String totalHorasStr = String.format(Locale.US, "%.1f", totalHoras);
                        String ligeroStr = String.format(Locale.US, "%.1f", datosSueno[0]);
                        String profundoStr = String.format(Locale.US, "%.1f", datosSueno[1]);
                        String remStr = String.format(Locale.US, "%.1f", datosSueno[2]);

                        writer.println(fechaActual.format(formatter) + "," +
                                totalHorasStr + "," +
                                ligeroStr + "," +
                                profundoStr + "," +
                                remStr + "," +
                                estadoSueno);
                        fechaActual = fechaActual.plusDays(1);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al completar datos de sueño: " + e.getMessage());
        }
    }

    /**
     * Completa datos de oxígeno faltantes desde la última fecha registrada hasta hoy.
     * Solo procesa si el archivo ya existe.
     */
    private static void completarOxigenoFaltante() {
        try {
            File archivo = new File("oxigeno.txt");

            if (!archivo.exists()) {
                return;
            }

            LocalDate ultimaFecha = obtenerUltimaFechaArchivo(archivo);
            LocalDate hoy = LocalDate.now();

            if (ultimaFecha.isBefore(hoy)) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(archivo, true))) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate fechaActual = ultimaFecha.plusDays(1);

                    while (!fechaActual.isAfter(hoy)) {
                        double spo2 = generarOxigenoRealista();
                        String estado = obtenerEstadoOxigeno(spo2);
                        writer.println(fechaActual.format(formatter) + "," +
                                String.format(Locale.US, "%.1f", spo2) + "," + estado);
                        fechaActual = fechaActual.plusDays(1);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al completar datos de oxígeno: " + e.getMessage());
        }
    }

    /**
     * Obtiene la última fecha registrada en un archivo de datos.
     * Lee el archivo línea por línea hasta encontrar la última entrada válida.
     * @param archivo
     * @return
     */
    private static LocalDate obtenerUltimaFechaArchivo(File archivo) {
        LocalDate ultimaFecha = LocalDate.now().minusDays(30);

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            String ultimaLinea = null;

            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    ultimaLinea = linea;
                }
            }

            if (ultimaLinea != null) {
                String[] partes = ultimaLinea.split(",");
                if (partes.length > 0) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    ultimaFecha = LocalDate.parse(partes[0].trim(), formatter);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer última fecha: " + e.getMessage());
        }

        return ultimaFecha;
    }

    /**
     * Verifica si hay dispositivos vinculados en el sistema.
     * Verifica la existencia y tamaño del archivo de dispositivos.
     * @return
     */
    public static boolean hayDispositivosVinculados() {
        File archivoDispositivos = new File("dispositivos.dat");
        return archivoDispositivos.exists() && archivoDispositivos.length() > 0;
    }
}