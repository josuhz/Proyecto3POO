package GUI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Ordenar fechas y tomar las últimas 7
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