package modelo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GenerarReporte {
    private static final DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter nombreArchivoFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static void generarYMostrarReporte() {
        // Nombre del archivo solo con fecha (sin hora)
        String nombreArchivo = "reporte_salud_" + LocalDate.now().format(nombreArchivoFormatter) + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            // Encabezado del reporte
            writer.println("==========================================");
            writer.println("          REPORTE DE SALUD");
            writer.println("==========================================");
            writer.println("Fecha de generación: " + LocalDate.now().format(displayFormatter));
            writer.println();

            // Obtener todas las fechas disponibles de todos los archivos
            Set<LocalDate> todasLasFechas = obtenerTodasLasFechas();

            if (todasLasFechas.isEmpty()) {
                writer.println("No hay datos disponibles para generar el reporte.");
                writer.println("Por favor, agregue dispositivos wearable para generar datos.");
                return;
            }

            // Convertir a lista ordenada
            List<LocalDate> fechasOrdenadas = new ArrayList<>(todasLasFechas);
            Collections.sort(fechasOrdenadas);

            // Generar reporte por cada fecha
            for (LocalDate fecha : fechasOrdenadas) {
                generarReportePorFecha(fecha, writer);
            }

            // Resumen final
            writer.println();
            writer.println("==========================================");
            writer.println("           RESUMEN GENERAL");
            writer.println("==========================================");
            generarResumenGeneral(writer);

            System.out.println("✓ Reporte generado: " + nombreArchivo);

            // Abrir el archivo en el bloc de notas
            abrirEnBlocDeNotas(nombreArchivo);

        } catch (IOException e) {
            System.out.println("✗ Error al generar reporte: " + e.getMessage());
        }
    }

    private static Set<LocalDate> obtenerTodasLasFechas() {
        Set<LocalDate> fechas = new HashSet<>();

        // Archivos posibles
        String[] archivos = {
                "frecuencia_cardiaca.txt",
                "oxigeno.txt",
                "sueño.txt",
                "actividad_fisica.txt"
        };

        for (String archivo : archivos) {
            if (Files.exists(Paths.get(archivo))) {
                fechas.addAll(obtenerFechasDeArchivo(archivo));
            }
        }

        return fechas;
    }

    private static Set<LocalDate> obtenerFechasDeArchivo(String nombreArchivo) {
        Set<LocalDate> fechas = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String[] partes = linea.split(",");
                    if (partes.length > 0) {
                        try {
                            LocalDate fecha = LocalDate.parse(partes[0].trim(), fechaFormatter);
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

    private static void generarReportePorFecha(LocalDate fecha, PrintWriter writer) {
        String fechaStr = fecha.format(fechaFormatter);
        String fechaDisplay = fecha.format(displayFormatter);

        writer.println("==========================================");
        writer.println("Fecha: " + fechaDisplay);
        writer.println("==========================================");

        // Frecuencia cardíaca
        if (Files.exists(Paths.get("frecuencia_cardiaca.txt"))) {
            Map<String, String> datosFC = leerFrecuenciaCardiaca(fechaStr);
            if (!datosFC.isEmpty()) {
                writer.println("Frecuencia Cardíaca:");
                writer.println("  - Pulsaciones: " + datosFC.getOrDefault("frecuencia", "N/A") + " BPM");
                writer.println("  - Estado: " + convertirEstadoFC(datosFC.getOrDefault("estadoFrecuencia", "N/A")));
                writer.println();
            }
        }

        // Oxígeno
        if (Files.exists(Paths.get("oxigeno.txt"))) {
            Map<String, String> datosOxigeno = leerOxigeno(fechaStr);
            if (!datosOxigeno.isEmpty()) {
                writer.println("Oxígeno en Sangre:");
                writer.println("  - SpO₂: " + datosOxigeno.getOrDefault("oxigeno", "N/A") + "%");
                writer.println("  - Estado: " + convertirEstadoOxigeno(datosOxigeno.getOrDefault("estado", "N/A")));
                writer.println();
            }
        }

        // Sueño
        if (Files.exists(Paths.get("sueño.txt"))) {
            Map<String, String> datosSueno = leerSueno(fechaStr);
            if (!datosSueno.isEmpty()) {
                writer.println("Sueño:");
                writer.println("  - Total: " + datosSueno.getOrDefault("totalSueno", "N/A") + " horas");
                writer.println("  - Ligero: " + datosSueno.getOrDefault("ligero", "N/A") + " horas");
                writer.println("  - Profundo: " + datosSueno.getOrDefault("profundo", "N/A") + " horas");
                writer.println("  - REM: " + datosSueno.getOrDefault("rem", "N/A") + " horas");
                writer.println("  - Calidad: " + convertirEstadoSueno(datosSueno.getOrDefault("estadoSueno", "N/A")));
                writer.println();
            }
        }

        // Actividad física
        if (Files.exists(Paths.get("actividad_fisica.txt"))) {
            Map<String, String> datosActividad = leerActividadFisica(fechaStr);
            if (!datosActividad.isEmpty()) {
                writer.println("Actividad Física:");
                writer.println("  - Pasos: " + datosActividad.getOrDefault("pasos", "N/A"));
                writer.println("  - Calorías: " + datosActividad.getOrDefault("calorias", "N/A"));
                writer.println("  - Nivel: " + convertirNivelActividad(datosActividad.getOrDefault("nivelActividad", "N/A")));
                writer.println();
            }
        }

        writer.println();
    }

    private static void generarResumenGeneral(PrintWriter writer) {
        // Contadores para estadísticas
        int totalDias = 0;
        int alertasFC = 0;
        int diasSuenoDeficiente = 0;
        int diasActivos = 0;
        int diasSedentarios = 0;

        // Archivos necesarios
        boolean tieneFC = Files.exists(Paths.get("frecuencia_cardiaca.txt"));
        boolean tieneSueno = Files.exists(Paths.get("sueño.txt"));
        boolean tieneActividad = Files.exists(Paths.get("actividad_fisica.txt"));

        if (tieneFC) {
            try (BufferedReader reader = new BufferedReader(new FileReader("frecuencia_cardiaca.txt"))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (!linea.trim().isEmpty()) {
                        totalDias++;
                        String[] partes = linea.split(",");
                        if (partes.length >= 3) {
                            String estado = partes[2].trim();
                            if (!estado.equals("NORMAL")) {
                                alertasFC++;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al leer frecuencia cardíaca para resumen: " + e.getMessage());
            }
        }

        if (tieneSueno) {
            try (BufferedReader reader = new BufferedReader(new FileReader("sueño.txt"))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (!linea.trim().isEmpty()) {
                        String[] partes = linea.split(",");
                        if (partes.length >= 6) {
                            String estado = partes[5].trim();
                            if (estado.equals("DEFICIENTE")) {
                                diasSuenoDeficiente++;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al leer sueño para resumen: " + e.getMessage());
            }
        }

        if (tieneActividad) {
            try (BufferedReader reader = new BufferedReader(new FileReader("actividad_fisica.txt"))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (!linea.trim().isEmpty()) {
                        String[] partes = linea.split(",");
                        if (partes.length >= 4) {
                            String nivel = partes[3].trim();
                            if (nivel.equals("SEDENTARIO")) {
                                diasSedentarios++;
                            } else if (nivel.equals("ACTIVO") || nivel.equals("MUY_ACTIVO")) {
                                diasActivos++;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al leer actividad física para resumen: " + e.getMessage());
            }
        }

        // Mostrar resumen
        if (tieneFC) {
            writer.println("Período analizado: " + totalDias + " días");
            writer.println();
        }

        if (tieneFC) {
            writer.println("Frecuencia Cardíaca:");
            writer.println("  - Días con alertas: " + alertasFC + " (" +
                    (totalDias > 0 ? String.format("%.1f", (alertasFC * 100.0 / totalDias)) : "0") + "%)");
            writer.println();
        }

        if (tieneSueno) {
            writer.println("Sueño:");
            writer.println("  - Días con sueño deficiente: " + diasSuenoDeficiente);
            writer.println();
        }

        if (tieneActividad) {
            writer.println("Actividad Física:");
            writer.println("  - Días activos/muy activos: " + diasActivos);
            writer.println("  - Días sedentarios: " + diasSedentarios);
        }
    }

    // Métodos de lectura de archivos
    private static Map<String, String> leerFrecuenciaCardiaca(String fecha) {
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
        } catch (IOException e) {
            // Archivo no encontrado o error de lectura
        }
        return datos;
    }

    private static Map<String, String> leerOxigeno(String fecha) {
        Map<String, String> datos = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("oxigeno.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 3 && partes[0].equals(fecha)) {
                    datos.put("oxigeno", partes[1]);
                    datos.put("estado", partes[2]);
                    break;
                }
            }
        } catch (IOException e) {
            // Archivo no encontrado o error de lectura
        }
        return datos;
    }

    private static Map<String, String> leerSueno(String fecha) {
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
        } catch (IOException e) {
            // Archivo no encontrado o error de lectura
        }
        return datos;
    }

    private static Map<String, String> leerActividadFisica(String fecha) {
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
        } catch (IOException e) {
            // Archivo no encontrado o error de lectura
        }
        return datos;
    }

    // Métodos de conversión
    private static String convertirEstadoFC(String estadoFC) {
        switch (estadoFC) {
            case "ALERTA_BAJA": return "Alerta Baja";
            case "ALERTA_ALTA": return "Alerta Alta";
            case "LIMITE": return "En Límite";
            case "NORMAL": return "Normal";
            default: return estadoFC;
        }
    }

    private static String convertirEstadoOxigeno(String estadoOxigeno) {
        switch (estadoOxigeno) {
            case "NORMAL": return "Normal";
            case "BAJA": return "Baja";
            case "CRITICA": return "Crítica";
            default: return estadoOxigeno;
        }
    }

    private static String convertirEstadoSueno(String estadoSueno) {
        switch (estadoSueno) {
            case "DEFICIENTE": return "Deficiente";
            case "REGULAR": return "Regular";
            case "BUENO": return "Bueno";
            case "EXCELENTE": return "Excelente";
            default: return estadoSueno;
        }
    }

    private static String convertirNivelActividad(String nivelActividad) {
        switch (nivelActividad) {
            case "SEDENTARIO": return "Sedentario";
            case "POCO_ACTIVO": return "Poco Activo";
            case "MODERADAMENTE_ACTIVO": return "Moderadamente Activo";
            case "ACTIVO": return "Activo";
            case "MUY_ACTIVO": return "Muy Activo";
            default: return nivelActividad;
        }
    }

    // Método para abrir el archivo en el bloc de notas
    private static void abrirEnBlocDeNotas(String nombreArchivo) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Process process;

            if (os.contains("win")) {
                // Windows
                process = new ProcessBuilder("notepad.exe", nombreArchivo).start();
            } else if (os.contains("mac")) {
                // macOS
                process = new ProcessBuilder("open", "-e", nombreArchivo).start();
            } else {
                // Linux/Unix
                process = new ProcessBuilder("xdg-open", nombreArchivo).start();
            }

            System.out.println("✓ Reporte abierto en el editor de texto");
        } catch (IOException e) {
            System.out.println("✗ No se pudo abrir el archivo automáticamente: " + e.getMessage());
            System.out.println("  El reporte se guardó como: " + nombreArchivo);
        }
    }
}