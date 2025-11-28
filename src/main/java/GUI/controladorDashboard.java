package GUI;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    private TextArea txtAlertas;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private ChoiceBox<String> chbFechas;
    @FXML
    private Label lblFecha;

    private DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        // Configurar navegación
        menuDashboard.setOnAction(event -> irAVentana("Dashboard.fxml", "Dashboard"));
        menuDispositivos.setOnAction(event -> irAVentana("menuDispositivos.fxml", "Mis Dispositivos"));
        menuGestionInvitados.setOnAction(event -> irAVentana("menuGestionInvitados.fxml", "Gestión de Invitados"));
        menuRangos.setOnAction(event -> irAVentana("menuRangos.fxml", "Rangos"));
        menuCerrarSesion.setOnAction(event -> cerrarSesion());

        // Cargar nombre del usuario
        cargarNombreUsuario();

        configurarChoiceBoxFechas();
        cargarDatosIniciales();
        configurarGraficoFrecuenciaCardiaca();
    }

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

    private void configurarChoiceBoxFechas() {
        // Validar que existan archivos antes de intentar leer
        if (!validarArchivosExisten()) {
            chbFechas.setItems(FXCollections.observableArrayList());
            lblFecha.setText("No hay datos disponibles");
            return;
        }

        // Obtener fechas disponibles del simulador
        List<LocalDate> fechas = LectorDatosSimulador.obtenerFechasDisponibles();

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
            if (fechaSeleccionada != null) {
                cargarDatosPorFecha(fechaSeleccionada);
            }
        });
    }

    private boolean validarArchivosExisten() {
        File fcFile = new File("frecuencia_cardiaca.txt");
        File actividadFile = new File("actividad_fisica.txt");
        File suenoFile = new File("sueño.txt");

        return fcFile.exists() || actividadFile.exists() || suenoFile.exists();
    }

    private void cargarDatosIniciales() {
        if (!validarArchivosExisten()) {
            mostrarMensajeSinDatos();
            return;
        }

        if (chbFechas.getValue() != null) {
            cargarDatosPorFecha(chbFechas.getValue());
        }
    }

    private void cargarDatosPorFecha(String fechaDisplay) {
        try {
            if (!validarArchivosExisten()) {
                mostrarMensajeSinDatos();
                return;
            }

            LocalDate fecha = LocalDate.parse(fechaDisplay, displayFormatter);

            lblFecha.setText("Resumen del día: " + fechaDisplay);

            // Obtener datos del simulador
            Map<String, String> datos = LectorDatosSimulador.leerDatosPorFecha(fecha);

            // Validar que se hayan obtenido datos
            if (datos == null || datos.isEmpty()) {
                mostrarMensajeSinDatos();
                return;
            }

            // Actualizar labels con los datos
            actualizarLabels(datos);

            // Actualizar áreas de texto
            actualizarAreasTexto(datos);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError();
        }
    }

    private void actualizarLabels(Map<String, String> datos) {
        // Frecuencia cardíaca
        if (datos.containsKey("frecuencia")) {
            lblNivelCardiaco.setText(datos.get("frecuencia"));
        }

        // Estado de frecuencia cardíaca
        if (datos.containsKey("estadoFrecuencia")) {
            String estadoFC = datos.get("estadoFrecuencia");
            lblEstado.setText(convertirEstadoFC(estadoFC));

            // Cambiar color según el estado
            switch (estadoFC) {
                case "ALERTA_BAJA":
                    lblEstado.setStyle("-fx-text-fill: #0033ff;"); // Azul para alerta baja
                    break;
                case "ALERTA_ALTA":
                    lblEstado.setStyle("-fx-text-fill: #ff0000;"); // Rojo para alerta alta
                    break;
                case "LIMITE":
                    lblEstado.setStyle("-fx-text-fill: #ff9800;"); // Naranja para límite
                    break;
                default:
                    lblEstado.setStyle("-fx-text-fill: #666666;"); // Gris para normal
                    break;
            }
        }

        // Actividad (pasos)
        if (datos.containsKey("pasos")) {
            String pasos = datos.get("pasos");
            try {
                int pasosInt = Integer.parseInt(pasos);
                lblActividad.setText("🏃 " + String.format("%,d", pasosInt));
            } catch (NumberFormatException e) {
                lblActividad.setText("🏃 " + pasos);
            }
        }
        // Nivel de actividad
        if (datos.containsKey("nivelActividad")) {
            String nivelActividad = datos.get("nivelActividad");
            lblActividadRealizada.setText(convertirNivelActividad(nivelActividad));

            // Cambiar color según el nivel de actividad
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
        }

        // Sueño
        if (datos.containsKey("totalSueno")) {
            String horasSueno = datos.get("totalSueno");
            lblHorasSueno.setText("😴 " + horasSueno + " H");
        }

        // Calidad de sueño
        if (datos.containsKey("estadoSueno")) {
            String estadoSueno = datos.get("estadoSueno");
            lblCalidadSueno.setText(convertirEstadoSueno(estadoSueno));

            // Cambiar color según la calidad del sueño
            switch (estadoSueno) {
                case "DEFICIENTE":
                    lblCalidadSueno.setStyle("-fx-text-fill: #ff0000;"); // Rojo
                    break;
                case "REGULAR":
                    lblCalidadSueno.setStyle("-fx-text-fill: #ff9800;"); // Naranja
                    break;
                case "BUENO":
                    lblCalidadSueno.setStyle("-fx-text-fill: #4caf50;"); // Verde
                    break;
                case "EXCELENTE":
                    lblCalidadSueno.setStyle("-fx-text-fill: #2196f3;"); // Azul
                    break;
                default:
                    lblCalidadSueno.setStyle("-fx-text-fill: #666666;"); // Gris
                    break;
            }
        }
    }

    private String convertirEstadoFC(String estadoFC) {
        switch (estadoFC) {
            case "ALERTA_BAJA": return "Alerta Baja";
            case "ALERTA_ALTA": return "Alerta Alta";
            case "LIMITE": return "En Límite";
            case "NORMAL": return "Normal";
            default: return estadoFC;
        }
    }

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

    private void actualizarAreasTexto(Map<String, String> datos) {
        StringBuilder alertas = new StringBuilder();
        StringBuilder observaciones = new StringBuilder();

        int contadorAlertas = 0;

        // Construir alertas basadas en los estados
        if ("ALERTA_BAJA".equals(datos.get("estadoFrecuencia"))) {
            alertas.append("Frecuencia cardíaca BAJA: ").append(datos.get("frecuencia")).append(" BPM\n");
            contadorAlertas++;
        } else if ("ALERTA_ALTA".equals(datos.get("estadoFrecuencia"))) {
            alertas.append("Frecuencia cardíaca ALTA: ").append(datos.get("frecuencia")).append(" BPM\n");
            contadorAlertas++;
        } else if ("LIMITE".equals(datos.get("estadoFrecuencia"))) {
            alertas.append("Frecuencia cardíaca en LÍMITE: ").append(datos.get("frecuencia")).append(" BPM\n");
            contadorAlertas++;
        }

        if ("DEFICIENTE".equals(datos.get("estadoSueno"))) {
            alertas.append("Sueño DEFICIENTE: ").append(datos.get("totalSueno")).append(" horas\n");
            contadorAlertas++;
        } else if ("REGULAR".equals(datos.get("estadoSueno"))) {
            observaciones.append("• Sueño REGULAR: ").append(datos.get("totalSueno")).append(" horas\n");
        }

        // Información de actividad
        if (datos.containsKey("nivelActividad")) {
            String nivelActividad = datos.get("nivelActividad");
            observaciones.append("• Nivel de actividad: ").append(convertirNivelActividad(nivelActividad)).append("\n");
        }

        // Información adicional
        if (datos.containsKey("calorias")) {
            observaciones.append("• Calorías quemadas: ").append(datos.get("calorias")).append("\n");
        }

        if (alertas.length() == 0) {
            alertas.append("No hay alertas críticas para este día");
        }

        // Actualizar el título de alertas con el contador
        actualizarTituloAlertas(contadorAlertas);

        txtAlertas.setText(alertas.toString());
        txtObservaciones.setText(observaciones.toString());
    }

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

    private void actualizarTituloAlertas(int cantidad) {
        // Buscar el label de alertas y actualizar su texto
        // Puedes agregar un fx:id al label de alertas si quieres manipularlo directamente
    }

    private void configurarGraficoFrecuenciaCardiaca() {
        bcharFrecCardiaca.getData().clear();

        if (!validarArchivosExisten()) {
            return;
        }

        try {
            // Obtener datos de los últimos 7 días
            List<Map<String, String>> datosUltimos7Dias = LectorDatosSimulador.obtenerUltimos7DiasFrecuenciaCardiaca();

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

            bcharFrecCardiaca.getData().add(series);

            bcharFrecCardiaca.setLegendVisible(false);
            bcharFrecCardiaca.setTitle("Frecuencia Cardíaca (Últimos 7 días)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private void mostrarMensajeError() {
        txtAlertas.setText("Error al cargar los datos.\n" +
                "Por favor, verifica que los archivos de datos estén correctamente generados.");
        txtObservaciones.setText("Error al cargar observaciones.");
    }
}