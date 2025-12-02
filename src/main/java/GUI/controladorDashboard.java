package GUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import modelo.GenerarReporte;

public class controladorDashboard {

    @FXML
    private MenuButton menuNavegacion;
    @FXML
    private MenuItem menuDashboard;
    @FXML
    private MenuItem menuDispositivos;
    @FXML
    private MenuItem menuGestionInvitados;
    @FXML
    private MenuItem menuRangos;
    @FXML
    public MenuItem menuReporte;
    @FXML
    private MenuItem menuCerrarSesion;
    @FXML
    private Label lblUsuario;
    @FXML
    private Label lblNivelCardiaco;
    @FXML
    private Label lblEstado;
    @FXML
    private Label lblActividad;
    @FXML
    private Label lblHorasSueno;
    @FXML
    private Label lblCalidadSueno;
    @FXML
    private Label lblActividadRealizada;
    @FXML
    private BarChart<String, Number> bcharFrecCardiaca;
    @FXML
    private BarChart<String, Number> bcharOxigenoSangre;
    @FXML
    private TextArea txtAlertas;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private ChoiceBox<String> chbFechas;
    @FXML
    private Label lblFecha;

    private DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Metodo que se ejecuta automáticamente al cargar el dashboard en el FXML
     */
    @FXML
    public void initialize() {
        menuDashboard.setOnAction(event -> irAVentana("Dashboard.fxml", "Dashboard"));
        menuDispositivos.setOnAction(event -> irAVentana("menuDispositivos.fxml", "Mis Dispositivos"));
        menuGestionInvitados.setOnAction(event -> irAVentana("menuGestionInvitados.fxml", "Gestión de Invitados"));
        menuRangos.setOnAction(event -> irAVentana("menuRangos.fxml", "Rangos"));
        menuReporte.setOnAction(event -> {
            System.out.println("Generando reporte...");
            GenerarReporte.generarYMostrarReporte();});
        menuCerrarSesion.setOnAction(event -> cerrarSesion());

        cargarNombreUsuario();
        cargarComentariosInvitados();
        configurarChoiceBoxFechas();
        cargarDatosIniciales();
        configurarGraficoFrecuenciaCardiaca();
        configurarGraficoOxigenoSangre();
    }

    /**
     * Metodo que se encarga de leer y cargar el nombre del usuario que se registra para mostrarlo en pantalla.
     */
    private void cargarNombreUsuario() {
        try {
            String[] usuario = ManejadorUsuarios.leerUsuarioPrincipal();
            if (usuario != null && usuario.length > 0) {
                lblUsuario.setText(usuario[0]); // El nombre está en la primera posición
            } else {
                lblUsuario.setText("Usuario");
            }
        } catch (Exception e) {
            lblUsuario.setText("Usuario");
        }
    }

    /**
     * Metedo que se encarga de mostrar en un choicebox las fechas disponibles para visualizar.
     */
    private void configurarChoiceBoxFechas() {
        // Obtener fechas disponibles del simulador (manejar null si no hay datos)
        List<LocalDate> fechas = new ArrayList<>();

        try {
            fechas = LectorDatosSimulador.obtenerFechasDisponibles();
        } catch (Exception e) {
            System.out.println("No se pudieron obtener fechas disponibles: " + e.getMessage());
        }

        // Si no hay fechas disponibles
        if (fechas == null || fechas.isEmpty()) {
            chbFechas.setItems(FXCollections.observableArrayList("No hay datos"));
            chbFechas.setValue("No hay datos");
            lblFecha.setText("No hay datos disponibles");
            return;
        }

        // Convertir a formato de visualización
        ObservableList<String> fechasDisplay = FXCollections.observableArrayList();
        for (LocalDate fecha : fechas) {
            fechasDisplay.add(fecha.format(displayFormatter));
        }

        chbFechas.setItems(fechasDisplay);

        // Seleccionar la última fecha por defecto
        if (!fechasDisplay.isEmpty()) {
            chbFechas.setValue(fechasDisplay.get(fechasDisplay.size() - 1));
        }

        // Configurar listener para cuando se seleccione una fecha
        chbFechas.setOnAction(event -> {
            String fechaSeleccionada = chbFechas.getValue();
            if (fechaSeleccionada != null && !fechaSeleccionada.equals("No hay datos")) {
                cargarDatosPorFecha(fechaSeleccionada);
            }
        });
    }

    /**
     * Metodo que se encarga de cargar la visualización de los datos de la fecha inicial.
     */
    private void cargarDatosIniciales() {
        String fechaSeleccionada = chbFechas.getValue();
        if (fechaSeleccionada != null && !fechaSeleccionada.equals("No hay datos")) {
            cargarDatosPorFecha(fechaSeleccionada);
        } else {
            mostrarMensajeSinDatos();
        }
    }

    /**
     * Metodo que se encarga de cargar la visualización de los datos por la fecha seleccionada.
     * @param fechaDisplay
     */
    private void cargarDatosPorFecha(String fechaDisplay) {
        try {
            LocalDate fecha = LocalDate.parse(fechaDisplay, displayFormatter);
            lblFecha.setText("Resumen del día: " + fechaDisplay);

            Map<String, String> datos = new HashMap<>();
            try {
                datos = LectorDatosSimulador.leerDatosPorFecha(fecha);
            } catch (Exception e) {
                System.out.println("Error al leer datos para fecha " + fechaDisplay + ": " + e.getMessage());
            }

            // Validar que se hayan obtenido datos
            if (datos == null || datos.isEmpty()) {
                mostrarMensajeSinDatosParaFecha(fechaDisplay);
                return;
            }

            // Actualizar labels con los datos
            actualizarLabels(datos);

            // Actualizar áreas de texto
            actualizarAreasTexto(datos);

        } catch (Exception e) {
            System.out.println("Error al cargar datos para fecha " + fechaDisplay + ": " + e.getMessage());
            mostrarMensajeError();
        }
    }

    /**
     * Metedo que se encarga de estar actualizando la información que se muestra en los labels del Dashboard.
     * Estos pueden cambiar de color dependiendo del estado.
     * @param datos
     */
    private void actualizarLabels(Map<String, String> datos) {
        if (datos.containsKey("frecuencia") && datos.get("frecuencia") != null) {
            lblNivelCardiaco.setText(datos.get("frecuencia"));
        } else {
            lblNivelCardiaco.setText("--");
        }

        // Estado de frecuencia cardíaca
        if (datos.containsKey("estadoFrecuencia") && datos.get("estadoFrecuencia") != null) {
            String estadoFC = datos.get("estadoFrecuencia");
            lblEstado.setText(convertirEstadoFC(estadoFC));

            // Cambiar color según el estado
            switch (estadoFC) {
                case "ALERTA_BAJA":
                    lblEstado.setStyle("-fx-text-fill: #0033ff;");
                    break;
                case "ALERTA_ALTA":
                    lblEstado.setStyle("-fx-text-fill: #ff0000;");
                    break;
                case "LIMITE":
                    lblEstado.setStyle("-fx-text-fill: #ff9800;");
                    break;
                default:
                    lblEstado.setStyle("-fx-text-fill: #666666;");
                    break;
            }
        } else {
            lblEstado.setText("Sin datos");
            lblEstado.setStyle("-fx-text-fill: #666666;");
        }

        // Actividad (pasos)
        if (datos.containsKey("pasos") && datos.get("pasos") != null) {
            String pasos = datos.get("pasos");
            try {
                int pasosInt = Integer.parseInt(pasos);
                lblActividad.setText("🏃 " + String.format("%,d", pasosInt));
            } catch (NumberFormatException e) {
                lblActividad.setText("🏃 " + pasos);
            }
        } else {
            lblActividad.setText("🏃 --");
        }

        // Nivel de actividad
        if (datos.containsKey("nivelActividad") && datos.get("nivelActividad") != null) {
            String nivelActividad = datos.get("nivelActividad");
            lblActividadRealizada.setText(convertirNivelActividad(nivelActividad));

            switch (nivelActividad) {
                case "SEDENTARIO":
                    lblActividadRealizada.setStyle("-fx-text-fill: #FF0000;");
                    break;
                case "POCO_ACTIVO":
                    lblActividadRealizada.setStyle("-fx-text-fill: #FF9800;");
                    break;
                case "MODERADAMENTE_ACTIVO":
                    lblActividadRealizada.setStyle("-fx-text-fill: #FFC107;");
                    break;
                case "ACTIVO":
                    lblActividadRealizada.setStyle("-fx-text-fill: #4CAF50;");
                    break;
                case "MUY_ACTIVO":
                    lblActividadRealizada.setStyle("-fx-text-fill: #2196F3;");
                    break;
                default:
                    lblActividadRealizada.setStyle("-fx-text-fill: #666666;");
                    break;
            }
        } else {
            lblActividadRealizada.setText("Sin datos");
            lblActividadRealizada.setStyle("-fx-text-fill: #666666;");
        }

        // Sueño
        if (datos.containsKey("totalSueno") && datos.get("totalSueno") != null) {
            String horasSueno = datos.get("totalSueno");
            lblHorasSueno.setText("😴 " + horasSueno + " H");
        } else {
            lblHorasSueno.setText("😴 -- H");
        }

        // Calidad de sueño
        if (datos.containsKey("estadoSueno") && datos.get("estadoSueno") != null) {
            String estadoSueno = datos.get("estadoSueno");
            lblCalidadSueno.setText(convertirEstadoSueno(estadoSueno));

            // Cambiar color según la calidad del sueño
            switch (estadoSueno) {
                case "DEFICIENTE":
                    lblCalidadSueno.setStyle("-fx-text-fill: #ff0000;");
                    break;
                case "REGULAR":
                    lblCalidadSueno.setStyle("-fx-text-fill: #ff9800;");
                    break;
                case "BUENO":
                    lblCalidadSueno.setStyle("-fx-text-fill: #4caf50;");
                    break;
                case "EXCELENTE":
                    lblCalidadSueno.setStyle("-fx-text-fill: #2196f3;");
                    break;
                default:
                    lblCalidadSueno.setStyle("-fx-text-fill: #666666;");
                    break;
            }
        } else {
            lblCalidadSueno.setText("Sin datos");
            lblCalidadSueno.setStyle("-fx-text-fill: #666666;");
        }
    }

    /**
     * Metodo que se encarga de convertir la alerta de frecuencia cardíaca a un string para visulizarlo en el Dashboard.
     * @param estadoFC
     * @return
     */
    private String convertirEstadoFC(String estadoFC) {
        switch (estadoFC) {
            case "ALERTA_BAJA": return "Alerta Baja";
            case "ALERTA_ALTA": return "Alerta Alta";
            case "LIMITE": return "En Límite";
            case "NORMAL": return "Normal";
            default: return estadoFC;
        }
    }

    /**
     * Metodo que se encarga de convertir la alerta de calidad de sueño a un string para visulizarlo en el Dashboard.
     * @param estadoSueno
     * @return
     */
    private String convertirEstadoSueno(String estadoSueno) {
        switch (estadoSueno) {
            case "DEFICIENTE": return "Deficiente";
            case "REGULAR": return "Regular";
            case "BUENO": return "Bueno";
            case "EXCELENTE": return "Excelente";
            case "ALERTA_INSUFICIENTE": return "Deficiente";
            case "META_ALCANZADA": return "Excelente";
            case "SUEÑO_ADECUADO": return "Bueno";
            default: return estadoSueno;
        }
    }

    /**
     * Metodo que se encarga de cargar los comentarios que han enviado los invitados para mostarlos en campo de texto.
     */
    private void cargarComentariosInvitados() {
        File archivo = new File("comentarios.txt");

        if (!archivo.exists()) {
            txtObservaciones.setText("No hay comentarios de invitados.");
            return;
        }

        try {
            List<String> comentarios = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    comentarios.add(linea);
                }
            }
            br.close();

            if (comentarios.isEmpty()) {
                txtObservaciones.setText("No hay comentarios de invitados.");
                archivo.delete();
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("COMENTARIOS DE INVITADOS\n");
            sb.append("============================\n\n");

            for (String comentario : comentarios) {
                String[] partes = comentario.split("\\|");
                if (partes.length >= 2) {
                    String nombre = partes[0];
                    String texto = partes[1];
                    String fecha = partes.length >= 3 ? partes[2] : "Fecha desconocida";

                    sb.append("👤 ").append(nombre).append(" (").append(fecha).append(")\n");
                    sb.append("   ").append(texto).append("\n\n");
                }
            }

            txtObservaciones.setText(sb.toString());

            archivo.delete();
            System.out.println("Comentarios mostrados y archivo borrado");

        } catch (IOException e) {
            e.printStackTrace();
            txtObservaciones.setText("Error al cargar comentarios.");
        }
    }

    /**
     * Metodo que se encarga de actualizar las areas de texto con los datos que se actulizan.
     * Verifica cada uno de los datos y los carga para la visualización.
     * @param datos
     */
    private void actualizarAreasTexto(Map<String, String> datos) {
        StringBuilder alertas = new StringBuilder();

        // Verificar si tenemos datos de frecuencia cardíaca antes de usarlos
        if (datos.containsKey("estadoFrecuencia") && datos.get("estadoFrecuencia") != null) {
            String estadoFC = datos.get("estadoFrecuencia");

            if ("ALERTA_BAJA".equals(estadoFC)) {
                alertas.append("Frecuencia cardíaca BAJA: ").append(datos.getOrDefault("frecuencia", "--")).append(" BPM\n");
            } else if ("ALERTA_ALTA".equals(estadoFC)) {
                alertas.append("Frecuencia cardíaca ALTA: ").append(datos.getOrDefault("frecuencia", "--")).append(" BPM\n");
            } else if ("LIMITE".equals(estadoFC)) {
                alertas.append("Frecuencia cardíaca en LÍMITE: ").append(datos.getOrDefault("frecuencia", "--")).append(" BPM\n");
            }
        }

        // Verificar si tenemos datos de sueño antes de usarlos
        if (datos.containsKey("estadoSueno") && datos.get("estadoSueno") != null) {
            if ("DEFICIENTE".equals(datos.get("estadoSueno"))) {
                alertas.append("Sueño DEFICIENTE: ").append(datos.getOrDefault("totalSueno", "--")).append(" horas\n");
            }
        }

        if (alertas.isEmpty()) {
            alertas.append("No hay alertas críticas para este día");
        }

        txtAlertas.setText(alertas.toString());
    }

    /**
     * Metodo que se encarga de convertir la alerta de actividad física en texto para mostrarlo en el Dashboard.
     * @param nivelActividad
     * @return
     */
    private String convertirNivelActividad(String nivelActividad) {
        switch (nivelActividad) {
            case "SEDENTARIO": return "Sedentario";
            case "POCO_ACTIVO": return "Poco Activo";
            case "MODERADAMENTE_ACTIVO": return "Moderadamente Activo";
            case "ACTIVO": return "Activo";
            case "MUY_ACTIVO": return "Muy Activo";
            default: return nivelActividad;
        }
    }

    /**
     * Metodo que se encarga de preparar el gráfico de la frecuencia cardíaca con los datos que han sido obtenidos en los días anteriores.
     */
    private void configurarGraficoFrecuenciaCardiaca() {
        bcharFrecCardiaca.getData().clear();

        try {
            // Obtener datos de los últimos 7 días - manejar excepción si no hay datos
            List<Map<String, String>> datosUltimos7Dias = null;
            try {
                datosUltimos7Dias = LectorDatosSimulador.obtenerUltimos7DiasFrecuenciaCardiaca();
            } catch (Exception e) {
                System.out.println("No se pudieron obtener datos para gráfico de frecuencia cardíaca: " + e.getMessage());
                return;
            }

            // Validar que se obtuvieron datos
            if (datosUltimos7Dias == null || datosUltimos7Dias.isEmpty()) {
                return;
            }

            // Crear serie de datos
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Frecuencia Cardíaca");

            for (Map<String, String> datoDia : datosUltimos7Dias) {
                if (datoDia.containsKey("frecuencia") && datoDia.containsKey("fecha")) {
                    try {
                        int frecuencia = Integer.parseInt(datoDia.get("frecuencia"));
                        series.getData().add(new XYChart.Data<>(datoDia.get("fecha"), frecuencia));
                    } catch (NumberFormatException e) {
                        // Ignorar si no se puede parsear
                    }
                }
            }

            if (!series.getData().isEmpty()) {
                bcharFrecCardiaca.getData().add(series);
                bcharFrecCardiaca.setLegendVisible(false);
                bcharFrecCardiaca.setTitle("Frecuencia Cardíaca (Últimos 7 días)");
            }
        } catch (Exception e) {
            System.out.println("Error configurando gráfico de frecuencia cardíaca: " + e.getMessage());
        }
    }

    /**
     * Metodo que se encarga de preparar el gráfico de los niveles de oxigeno en la sangre con los datos que han sido obtenidos en los días anteriores.
     */
    private void configurarGraficoOxigenoSangre() {
        bcharOxigenoSangre.getData().clear();

        File oxigenoFile = new File("oxigeno.txt");

        if (!oxigenoFile.exists()) {
            return;
        }

        try {
            Map<LocalDate, Double> oxigenoPorFecha = new TreeMap<>();
            BufferedReader br = new BufferedReader(new FileReader(oxigenoFile));
            String linea;
            int lineasLeidas = 0;

            while ((linea = br.readLine()) != null) {
                lineasLeidas++;
                String[] partes = linea.split(",");

                if (partes.length >= 2) {
                    try {
                        LocalDate fecha = LocalDate.parse(partes[0].trim());
                        double spo2 = Double.parseDouble(partes[1].trim());
                        oxigenoPorFecha.put(fecha, spo2);
                    } catch (Exception e) {
                        System.out.println("Error al parsear línea en oxigeno.txt: " + e.getMessage());
                    }
                }
            }
            br.close();

            // Obtener últimas 7 fechas
            List<LocalDate> fechasOrdenadas = new ArrayList<>(oxigenoPorFecha.keySet());
            Collections.sort(fechasOrdenadas);

            int inicio = Math.max(0, fechasOrdenadas.size() - 7);
            List<LocalDate> ultimas7 = fechasOrdenadas.subList(inicio, fechasOrdenadas.size());

            // Crear serie
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("SpO2");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
            for (LocalDate fecha : ultimas7) {
                String fechaStr = fecha.format(formatter);
                double oxigeno = oxigenoPorFecha.get(fecha);
                series.getData().add(new XYChart.Data<>(fechaStr, oxigeno));
            }

            if (!series.getData().isEmpty()) {
                bcharOxigenoSangre.getData().add(series);
                bcharOxigenoSangre.setLegendVisible(false);
                bcharOxigenoSangre.setTitle("Oxígeno en Sangre (Últimos 7 días)");
            } else {
                System.out.println("No hay datos válidos para el gráfico de oxígeno");
            }

        } catch (Exception e) {
            System.out.println("Error configurando gráfico de oxígeno: " + e.getMessage());
        }
    }

    /**
     * Metodo que se encarga de pasar al siguiente menú dependiendo de cual se seleccione de un MenuButton.
     * @param nombreArchivo
     * @param titulo
     */
    private void irAVentana(String nombreArchivo, String titulo) {
        try {
            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/" + nombreArchivo);
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();
            Stage stage = (Stage) menuNavegacion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que se encarga de volver al menú de login para volver a iniciar sesión.
     */
    private void cerrarSesion() {
        try {
            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/menuLogin.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();
            Stage stage = (Stage) menuNavegacion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Inicio de Sesión");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que se encarga de mostrar los mensajes de cada label en caso de no haber ningún dato.
     */
    private void mostrarMensajeSinDatos() {
        lblNivelCardiaco.setText("--");
        lblEstado.setText("Sin datos");
        lblActividad.setText("-- pasos");
        lblHorasSueno.setText("-- H");
        lblCalidadSueno.setText("Sin datos");

        txtAlertas.setText("No hay datos disponibles.\n\n" +
                "Para generar datos:\n" +
                "1. Ve a 'Mis Dispositivos'\n" +
                "2. Agrega un dispositivo wearable\n" +
                "3. Los datos se generarán automáticamente");

        txtObservaciones.setText("No hay observaciones disponibles.");
    }

    /**
     * Metodo que se encarga de mostrar los mensajes de cada label en caso de que en la fecha seleccionada no se encuentre ningún dato.
     * @param fecha
     */
    private void mostrarMensajeSinDatosParaFecha(String fecha) {
        lblNivelCardiaco.setText("--");
        lblEstado.setText("Sin datos");
        lblActividad.setText("-- pasos");
        lblHorasSueno.setText("-- H");
        lblCalidadSueno.setText("Sin datos");

        txtAlertas.setText("No hay datos disponibles para la fecha: " + fecha + "\n\n" +
                "Para generar datos:\n" +
                "1. Ve a 'Mis Dispositivos'\n" +
                "2. Agrega un dispositivo wearable\n" +
                "3. Los datos se generarán automáticamente");
    }

    /**
     * Metodo que se encarga de crear una alerta en caso de que ocurra un error.
     */
    private void mostrarMensajeError() {
        txtAlertas.setText("Error al cargar los datos.\n" +
                "Por favor, verifica que los archivos de datos estén correctamente generados.");
        txtObservaciones.setText("Error al cargar observaciones.");
    }
}