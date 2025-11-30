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

    public static Map<String, String> leerDatosPorFecha(LocalDate fecha) {
        Map<String, String> datos = new HashMap<>();
        String fechaStr = fecha.format(formatter);

        try {
            // Leer frecuencia cardíaca
            datos.putAll(leerFrecuenciaCardiaca(fechaStr));
            // Leer actividad física
            datos.putAll(leerActividadFisica(fechaStr));
            // Leer sueño
            datos.putAll(leerSueno(fechaStr));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return datos;
    }

    public static List<LocalDate> obtenerFechasDisponibles() {
        List<LocalDate> fechas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("frecuencia_cardiaca.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 1) {
                    LocalDate fecha = LocalDate.parse(partes[0], formatter);
                    fechas.add(fecha);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fechas;
    }

    public static List<Map<String, String>> obtenerUltimos7DiasFrecuenciaCardiaca() {
        List<Map<String, String>> datos = new ArrayList<>();
        List<LocalDate> fechas = obtenerFechasDisponibles();

        fechas.sort(LocalDate::compareTo);
        int startIndex = Math.max(0, fechas.size() - 7);

        for (int i = startIndex; i < fechas.size(); i++) {
            Map<String, String> datoDia = new HashMap<>();
            try {
                datoDia.putAll(leerFrecuenciaCardiaca(fechas.get(i).format(formatter)));
                datoDia.put("fecha", fechas.get(i).format(DateTimeFormatter.ofPattern("dd/MM")));
                datos.add(datoDia);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return datos;
    }

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
                        System.out.println("Error al parsear línea: " + linea);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return resultado;
        }

        List<LocalDate> fechasOrdenadas = new ArrayList<>(oxigenoPorFecha.keySet());
        Collections.sort(fechasOrdenadas);

        int inicio = Math.max(0, fechasOrdenadas.size() - 7);
        List<LocalDate> ultimas7Fechas = fechasOrdenadas.subList(inicio, fechasOrdenadas.size());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        for (LocalDate fecha : ultimas7Fechas) {
            Map<String, String> dato = new HashMap<>();
            dato.put("fecha", fecha.format(formatter));
            dato.put("oxigeno", String.format("%.1f", oxigenoPorFecha.get(fecha)));
            resultado.add(dato);
        }

        return resultado;
    }

    public static List<Map<String, String>> obtenerUltimos7DiasActividad() {
        List<Map<String, String>> datos = new ArrayList<>();
        List<LocalDate> fechas = obtenerFechasDisponibles();

        fechas.sort(LocalDate::compareTo);
        int startIndex = Math.max(0, fechas.size() - 7);

        for (int i = startIndex; i < fechas.size(); i++) {
            Map<String, String> datoDia = new HashMap<>();
            try {
                datoDia.putAll(leerActividadFisica(fechas.get(i).format(formatter)));
                datoDia.put("fecha", fechas.get(i).format(DateTimeFormatter.ofPattern("dd/MM")));
                datos.add(datoDia);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return datos;
    }

    public static List<Map<String, String>> obtenerUltimos7DiasSueno() {
        List<Map<String, String>> datos = new ArrayList<>();
        List<LocalDate> fechas = obtenerFechasDisponibles();

        fechas.sort(LocalDate::compareTo);
        int startIndex = Math.max(0, fechas.size() - 7);

        for (int i = startIndex; i < fechas.size(); i++) {
            Map<String, String> datoDia = new HashMap<>();
            try {
                datoDia.putAll(leerSueno(fechas.get(i).format(formatter)));
                datoDia.put("fecha", fechas.get(i).format(DateTimeFormatter.ofPattern("dd/MM")));
                datos.add(datoDia);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return datos;
    }

    private static Map<String, String> leerFrecuenciaCardiaca(String fecha) throws IOException {
        Map<String, String> datos = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("frecuencia_cardiaca.txt"))) {
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

    private static Map<String, String> leerActividadFisica(String fecha) throws IOException {
        Map<String, String> datos = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("actividad_fisica.txt"))) {
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

    private static Map<String, String> leerSueno(String fecha) throws IOException {
        Map<String, String> datos = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("sueño.txt"))) {
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