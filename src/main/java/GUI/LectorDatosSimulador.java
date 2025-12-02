package GUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class LectorDatosSimulador {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Lee todos los datos de salud disponibles para una fecha específica.
     * Busca información en los archivos de frecuencia cardíaca, actividad física y sueño.
     * @param fecha
     * @return
     */
    public static Map<String, String> leerDatosPorFecha(LocalDate fecha) {
        Map<String, String> datos = new HashMap<>();
        String fechaStr = fecha.format(formatter);

        try {
            // Leer frecuencia cardíaca si existe
            if (new File("frecuencia_cardiaca.txt").exists()) {
                datos.putAll(leerFrecuenciaCardiaca(fechaStr));
            }
            // Leer actividad física si existe
            if (new File("actividad_fisica.txt").exists()) {
                datos.putAll(leerActividadFisica(fechaStr));
            }
            // Leer sueño si existe
            if (new File("sueño.txt").exists()) {
                datos.putAll(leerSueno(fechaStr));
            }
        } catch (IOException e) {
            System.out.println("Error al leer datos para fecha " + fechaStr + ": " + e.getMessage());
        }

        return datos;
    }

    /**
     * Obtiene todas las fechas que tienen datos registrados.
     * @return
     */
    public static List<LocalDate> obtenerFechasDisponibles() {
        Set<LocalDate> fechas = new HashSet<>();

        // Buscar fechas en todos los archivos que existan
        if (new File("frecuencia_cardiaca.txt").exists()) {
            fechas.addAll(obtenerFechasDeArchivo("frecuencia_cardiaca.txt"));
        }
        if (new File("actividad_fisica.txt").exists()) {
            fechas.addAll(obtenerFechasDeArchivo("actividad_fisica.txt"));
        }
        if (new File("sueño.txt").exists()) {
            fechas.addAll(obtenerFechasDeArchivo("sueño.txt"));
        }
        if (new File("oxigeno.txt").exists()) {
            fechas.addAll(obtenerFechasDeArchivo("oxigeno.txt"));
        }

        List<LocalDate> listaFechas = new ArrayList<>(fechas);
        Collections.sort(listaFechas);
        return listaFechas;
    }

    /**
     * Extrae todas las fechas presentes en un archivo específico.
     * @param nombreArchivo
     * @return
     */
    private static List<LocalDate> obtenerFechasDeArchivo(String nombreArchivo) {
        List<LocalDate> fechas = new ArrayList<>();
        File archivo = new File(nombreArchivo);

        if (!archivo.exists()) {
            return fechas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String[] partes = linea.split(",");
                    if (partes.length >= 1) {
                        try {
                            LocalDate fecha = LocalDate.parse(partes[0].trim(), formatter);
                            fechas.add(fecha);
                        } catch (Exception e) {
                            System.out.println("Error al parsear fecha en " + nombreArchivo + ": " + linea);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer " + nombreArchivo + ": " + e.getMessage());
        }
        return fechas;
    }

    /**
     * Obtiene los datos de frecuencia cardíaca de los últimos 7 días.
     * Incluye la frecuencia en BPM y el estado.
     * @return
     */
    public static List<Map<String, String>> obtenerUltimos7DiasFrecuenciaCardiaca() {
        List<Map<String, String>> datos = new ArrayList<>();

        File archivoFc = new File("frecuencia_cardiaca.txt");
        if (!archivoFc.exists()) {
            return datos;
        }

        List<LocalDate> fechas = obtenerFechasDeArchivo("frecuencia_cardiaca.txt");
        fechas.sort(LocalDate::compareTo);
        int startIndex = Math.max(0, fechas.size() - 7);

        for (int i = startIndex; i < fechas.size(); i++) {
            Map<String, String> datoDia = new HashMap<>();
            try {
                datoDia.putAll(leerFrecuenciaCardiaca(fechas.get(i).format(formatter)));
                datoDia.put("fecha", fechas.get(i).format(DateTimeFormatter.ofPattern("dd/MM")));
                datos.add(datoDia);
            } catch (IOException e) {
                System.out.println("Error al leer frecuencia cardíaca para fecha " + fechas.get(i) + ": " + e.getMessage());
            }
        }

        return datos;
    }

    /**
     * Obtiene los datos de oxígeno en sangre de los últimos 7 días.
     * Lee el archivo oxigeno.txt y extrae los valores de SpO2.
     * @return
     */
    public static List<Map<String, String>> obtenerUltimos7DiasOxigeno() {
        List<Map<String, String>> resultado = new ArrayList<>();
        Map<LocalDate, Double> oxigenoPorFecha = new TreeMap<>();

        File archivo = new File("oxigeno.txt");
        if (!archivo.exists()) {
            return resultado;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 2) {
                    try {
                        LocalDate fecha = LocalDate.parse(partes[0].trim());
                        double spo2 = Double.parseDouble(partes[1].trim());
                        oxigenoPorFecha.put(fecha, spo2);
                    } catch (Exception e) {
                        System.out.println("Error al parsear línea en oxigeno.txt: " + linea);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer oxigeno.txt: " + e.getMessage());
            return resultado;
        }

        List<LocalDate> fechasOrdenadas = new ArrayList<>(oxigenoPorFecha.keySet());
        Collections.sort(fechasOrdenadas);

        int inicio = Math.max(0, fechasOrdenadas.size() - 7);
        List<LocalDate> ultimas7Fechas = fechasOrdenadas.subList(inicio, fechasOrdenadas.size());

        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM");
        for (LocalDate fecha : ultimas7Fechas) {
            Map<String, String> dato = new HashMap<>();
            dato.put("fecha", fecha.format(fechaFormatter));
            dato.put("oxigeno", String.format("%.1f", oxigenoPorFecha.get(fecha)));
            resultado.add(dato);
        }

        return resultado;
    }

    /**
     * Obtiene los datos de actividad física de los últimos 7 días.
     * Incluye pasos, calorías y nivel de actividad.
     * @return
     */
    public static List<Map<String, String>> obtenerUltimos7DiasActividad() {
        List<Map<String, String>> datos = new ArrayList<>();

        File archivoAct = new File("actividad_fisica.txt");
        if (!archivoAct.exists()) {
            return datos;
        }

        List<LocalDate> fechas = obtenerFechasDeArchivo("actividad_fisica.txt");
        fechas.sort(LocalDate::compareTo);
        int startIndex = Math.max(0, fechas.size() - 7);

        for (int i = startIndex; i < fechas.size(); i++) {
            Map<String, String> datoDia = new HashMap<>();
            try {
                datoDia.putAll(leerActividadFisica(fechas.get(i).format(formatter)));
                datoDia.put("fecha", fechas.get(i).format(DateTimeFormatter.ofPattern("dd/MM")));
                datos.add(datoDia);
            } catch (IOException e) {
                System.out.println("Error al leer actividad física para fecha " + fechas.get(i) + ": " + e.getMessage());
            }
        }

        return datos;
    }

    /**
     * Obtiene los datos de sueño de los últimos 7 días.
     * Incluye total de horas, fases de sueño (ligero, profundo, REM) y estado.
     * @return
     */
    public static List<Map<String, String>> obtenerUltimos7DiasSueno() {
        List<Map<String, String>> datos = new ArrayList<>();

        File archivoSueno = new File("sueño.txt");
        if (!archivoSueno.exists()) {
            return datos;
        }

        List<LocalDate> fechas = obtenerFechasDeArchivo("sueño.txt");
        fechas.sort(LocalDate::compareTo);
        int startIndex = Math.max(0, fechas.size() - 7);

        for (int i = startIndex; i < fechas.size(); i++) {
            Map<String, String> datoDia = new HashMap<>();
            try {
                datoDia.putAll(leerSueno(fechas.get(i).format(formatter)));
                datoDia.put("fecha", fechas.get(i).format(DateTimeFormatter.ofPattern("dd/MM")));
                datos.add(datoDia);
            } catch (IOException e) {
                System.out.println("Error al leer sueño para fecha " + fechas.get(i) + ": " + e.getMessage());
            }
        }

        return datos;
    }

    /**
     * Lee los datos de frecuencia cardíaca de una fecha específica.
     * @param fecha
     * @return
     */
    private static Map<String, String> leerFrecuenciaCardiaca(String fecha) throws IOException {
        Map<String, String> datos = new HashMap<>();
        File archivo = new File("frecuencia_cardiaca.txt");

        if (!archivo.exists()) {
            return datos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 3 && partes[0].equals(fecha)) {
                    datos.put("frecuencia", partes[1]);
                    datos.put("estadoFrecuencia", partes[2]);
                    break;
                }
            }
        }
        return datos;
    }

    /**
     * Lee los datos de actividad física de una fecha específica.
     * @param fecha
     * @return
     */
    private static Map<String, String> leerActividadFisica(String fecha) throws IOException {
        Map<String, String> datos = new HashMap<>();
        File archivo = new File("actividad_fisica.txt");

        if (!archivo.exists()) {
            return datos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 4 && partes[0].equals(fecha)) {
                    datos.put("pasos", partes[1]);
                    datos.put("calorias", partes[2]);
                    datos.put("nivelActividad", partes[3]);
                    break;
                }
            }
        }
        return datos;
    }

    /**
     * Lee los datos de sueño de una fecha específica.
     * @param fecha
     * @return
     */
    private static Map<String, String> leerSueno(String fecha) throws IOException {
        Map<String, String> datos = new HashMap<>();
        File archivo = new File("sueño.txt");

        if (!archivo.exists()) {
            return datos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 6 && partes[0].equals(fecha)) {
                    datos.put("totalSueno", partes[1]);
                    datos.put("ligero", partes[2]);
                    datos.put("profundo", partes[3]);
                    datos.put("rem", partes[4]);
                    datos.put("estadoSueno", partes[5]);
                    break;
                }
            }
        }
        return datos;
    }
}