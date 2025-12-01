package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import modelo.GestorDispositivos;
import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class controladorUsuarioInvitado {

    @FXML
    private MenuButton menuNavegacion;
    @FXML
    private MenuItem menuCerrarSesion;
    @FXML
    private TextArea txtInformacionUsuario;
    @FXML
    private TextArea txtComentario;
    @FXML
    private Button btnEnviarComentario;
    @FXML
    private Button btnLimpiarComentario;
    @FXML
    private Label lblMensajeEstado;

    private String emailInvitado;
    private String permisos = "0000";

    @FXML
    public void initialize() {
        menuCerrarSesion.setOnAction(event -> cerrarSesion());

        btnEnviarComentario.setOnAction(event -> enviarComentario());
        btnLimpiarComentario.setOnAction(event -> limpiarComentario());

        txtInformacionUsuario.setEditable(false);
        txtInformacionUsuario.setWrapText(true);

        cargarPermisosInvitado();
        cargarInformacionUsuario();
    }

    public void setEmailInvitado(String email) {
        this.emailInvitado = email;
        cargarPermisosInvitado();
        cargarInformacionUsuario(); // Recargar con los permisos correctos
    }

    private void cargarPermisosInvitado() {
        if (emailInvitado == null) return;

        try {
            List<String[]> invitados = ManejadorUsuarios.leerInvitados();
            for (String[] invitado : invitados) {
                if (invitado.length >= 4 && invitado[1].trim().equals(emailInvitado)) {
                    if (invitado.length > 4) {
                        permisos = invitado[4].trim();
                        if (permisos.length() < 4) {
                            permisos = "0000";
                        }
                    } else {
                        permisos = "0000";
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            permisos = "0000";
        }
    }

    private void cargarInformacionUsuario() {
        try {
            StringBuilder informacion = new StringBuilder();

            String[] usuario = ManejadorUsuarios.leerUsuarioPrincipal();
            if (usuario != null && usuario.length >= 3) {
                informacion.append("INFORMACIÓN DEL USUARIO PRINCIPAL\n");
                informacion.append("=====================================\n\n");

                informacion.append("• Nombre: ").append(usuario[0]).append("\n");
                informacion.append("• Email: ").append(usuario[1]).append("\n");

                if (usuario.length > 3 && !usuario[3].isEmpty()) {
                    informacion.append("• Edad: ").append(usuario[3]).append(" años\n");
                }
                if (usuario.length > 4 && !usuario[4].isEmpty()) {
                    informacion.append("• Peso: ").append(usuario[4]).append(" kg\n");
                }
                if (usuario.length > 5 && !usuario[5].isEmpty()) {
                    informacion.append("• Altura: ").append(usuario[5]).append(" m\n");
                }

                informacion.append("\n");

                Map<String, String> datosHoy = LectorDatosSimulador.leerDatosPorFecha(LocalDate.now());

                boolean detalleFrecuencia = permisos.charAt(0) == '1';
                boolean detalleSueno = permisos.charAt(1) == '1';
                boolean detalleActividad = permisos.charAt(2) == '1';
                boolean detalleOxigeno = permisos.charAt(3) == '1';

                informacion.append("DATOS DE SALUD\n");
                informacion.append("=======================\n\n");

                // INFORMACIÓN BÁSICA HOY
                informacion.append("--- Información Básica (Hoy) ---\n\n");

                // Frecuencia Cardíaca Básico
                if (datosHoy.containsKey("frecuencia")) {
                    informacion.append("• Frecuencia Cardíaca: ").append(datosHoy.get("frecuencia")).append(" BPM\n");
                } else {
                    informacion.append("• Frecuencia Cardíaca: No disponible\n");
                }

                // Actividad Básico
                if (datosHoy.containsKey("pasos")) {
                    informacion.append("• Pasos: ").append(datosHoy.get("pasos")).append("\n");
                } else {
                    informacion.append("• Pasos: No disponible\n");
                }

                // Sueño Básico
                if (datosHoy.containsKey("totalSueno")) {
                    informacion.append("• Sueño: ").append(datosHoy.get("totalSueno")).append(" horas\n");
                } else {
                    informacion.append("• Sueño: No disponible\n");
                }

                // Oxígeno Básico
                Map<String, String> datosOxigeno = leerUltimoOxigeno();
                if (datosOxigeno != null && datosOxigeno.containsKey("oxigeno")) {
                    informacion.append("• Oxígeno en Sangre: ").append(datosOxigeno.get("oxigeno")).append("%\n");
                } else {
                    informacion.append("• Oxígeno en Sangre: No disponible\n");
                }

                // INFORMACIÓN DETALLADA ÚLTIMOS 7 DÍAS
                if (detalleFrecuencia || detalleSueno || detalleActividad || detalleOxigeno) {
                    informacion.append("\n--- Información Detallada (Últimos 7 días) ---\n\n");

                    if (detalleFrecuencia) {
                        informacion.append("FRECUENCIA CARDÍACA DETALLADA:\n");
                        List<Map<String, String>> datosFrecuencia = LectorDatosSimulador.obtenerUltimos7DiasFrecuenciaCardiaca();
                        for (Map<String, String> datosDia : datosFrecuencia) {
                            if (datosDia.containsKey("frecuencia") && datosDia.containsKey("estadoFrecuencia") && datosDia.containsKey("fecha")) {
                                informacion.append("   • ").append(datosDia.get("fecha"))
                                        .append(": ").append(datosDia.get("frecuencia"))
                                        .append(" BPM - ").append(convertirEstado(datosDia.get("estadoFrecuencia"))).append("\n");
                            }
                        }
                        informacion.append("   • Rango normal: 60-100 BPM\n\n");
                    }

                    if (detalleActividad) {
                        informacion.append("ACTIVIDAD FÍSICA DETALLADA:\n");
                        List<Map<String, String>> datosActividad = LectorDatosSimulador.obtenerUltimos7DiasActividad();
                        for (Map<String, String> datosDia : datosActividad) {
                            if (datosDia.containsKey("pasos") && datosDia.containsKey("nivelActividad") && datosDia.containsKey("fecha")) {
                                informacion.append("   • ").append(datosDia.get("fecha"))
                                        .append(": ").append(datosDia.get("pasos"))
                                        .append(" pasos - ").append(convertirNivel(datosDia.get("nivelActividad"))).append("\n");
                            }
                        }
                        informacion.append("\n");
                    }

                    if (detalleSueno) {
                        informacion.append("SUEÑO DETALLADO:\n");
                        List<Map<String, String>> datosSueno = LectorDatosSimulador.obtenerUltimos7DiasSueno();
                        for (Map<String, String> datosDia : datosSueno) {
                            if (datosDia.containsKey("totalSueno") && datosDia.containsKey("estadoSueno") && datosDia.containsKey("fecha")) {
                                informacion.append("   • ").append(datosDia.get("fecha"))
                                        .append(": ").append(datosDia.get("totalSueno"))
                                        .append(" horas - ").append(convertirEstado(datosDia.get("estadoSueno"))).append("\n");
                            }
                        }
                        informacion.append("\n");
                    }

                    if (detalleOxigeno) {
                        informacion.append("OXÍGENO DETALLADO:\n");
                        List<Map<String, String>> datosOxigeno7Dias = LectorDatosSimulador.obtenerUltimos7DiasOxigeno();
                        for (Map<String, String> datosDia : datosOxigeno7Dias) {
                            if (datosDia.containsKey("oxigeno") && datosDia.containsKey("fecha")) {
                                informacion.append("   • ").append(datosDia.get("fecha"))
                                        .append(": ").append(datosDia.get("oxigeno"))
                                        .append("%\n");
                            }
                        }
                        informacion.append("   • Rango normal: 95-100%\n\n");
                    }
                }

            } else {
                informacion.append("No se pudo cargar la información del usuario principal.\n");
                informacion.append("Por favor, contacte al administrador del sistema.");
            }

            try {
                int cantidadDispositivos = GestorDispositivos.getInstancia().getCantidadDispositivos();
                informacion.append("\nDISPOSITIVOS VINCULADOS\n");
                informacion.append("=========================\n");
                informacion.append("• Cantidad: ").append(cantidadDispositivos).append(" dispositivo").append(cantidadDispositivos != 1 ? "s" : "");
            } catch (Exception e) {
                informacion.append("\nDISPOSITIVOS VINCULADOS\n");
                informacion.append("=========================\n");
                informacion.append("• Cantidad: No disponible");
            }

            txtInformacionUsuario.setText(informacion.toString());

        } catch (Exception e) {
            txtInformacionUsuario.setText("Error al cargar la información del usuario principal.\n\nDetalles del error:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private Map<String, String> leerUltimoOxigeno() {
        try {
            File archivo = new File("oxigeno.txt");
            if (!archivo.exists()) return null;

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String ultimaLinea = null;
            String linea;

            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    ultimaLinea = linea;
                }
            }
            br.close();

            if (ultimaLinea != null) {
                String[] partes = ultimaLinea.split(",");
                if (partes.length >= 2) {
                    Map<String, String> datos = new HashMap<>();
                    datos.put("oxigeno", partes[1].trim());
                    if (partes.length >= 3) {
                        datos.put("estado", partes[2].trim());
                    }
                    return datos;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertirEstado(String estado) {
        switch (estado.toUpperCase()) {
            case "NORMAL": return "Normal";
            case "ALERTA_BAJA": return "Alerta Baja";
            case "ALERTA_ALTA": return "Alerta Alta";
            case "LIMITE": return "En Límite";
            case "BAJA": return "Baja";
            case "CRITICA": return "Crítica";
            case "DEFICIENTE": return "Deficiente";
            case "REGULAR": return "Regular";
            case "BUENO": return "Bueno";
            case "EXCELENTE": return "Excelente";
            default: return estado;
        }
    }

    private String convertirNivel(String nivel) {
        switch (nivel.toUpperCase()) {
            case "SEDENTARIO": return "Sedentario";
            case "POCO_ACTIVO": return "Poco Activo";
            case "MODERADAMENTE_ACTIVO": return "Moderadamente Activo";
            case "ACTIVO": return "Activo";
            case "MUY_ACTIVO": return "Muy Activo";
            default: return nivel;
        }
    }

    private void enviarComentario() {
        String comentario = txtComentario.getText().trim();

        if (comentario.isEmpty()) {
            mostrarMensaje("Por favor, escribe un comentario antes de enviar.", false);
            return;
        }

        try {
            String nombreInvitado = "";
            List<String[]> invitados = ManejadorUsuarios.leerInvitados();
            for (String[] invitado : invitados) {
                if (invitado.length >= 2 && invitado[1].trim().equals(emailInvitado)) {
                    nombreInvitado = invitado[0].trim();
                    break;
                }
            }

            guardarComentario(nombreInvitado, comentario);

            mostrarMensaje("✓ Comentario enviado exitosamente", true);
            txtComentario.clear();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensaje("Error al enviar el comentario: " + e.getMessage(), false);
        }
    }

    private void guardarComentario(String nombreInvitado, String comentario) throws IOException {
        File archivo = new File("comentarios.txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo, true))) {
            writer.println(nombreInvitado + "|" + comentario + "|" + LocalDate.now());
        }

        System.out.println("Comentario guardado: " + nombreInvitado + " - " + comentario);
    }

    private void limpiarComentario() {
        txtComentario.clear();
        lblMensajeEstado.setVisible(false);
    }

    private void mostrarMensaje(String mensaje, boolean exito) {
        lblMensajeEstado.setText(mensaje);
        lblMensajeEstado.setStyle(exito ? "-fx-text-fill: #4CAF50;" : "-fx-text-fill: #F44336;");
        lblMensajeEstado.setVisible(true);
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
}