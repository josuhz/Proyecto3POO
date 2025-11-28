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
import java.util.List;
import java.util.Map;

public class controladorUsuarioInvitado {

    @FXML private MenuButton menuNavegacion;
    @FXML private MenuItem menuDashboardInvitado;
    @FXML private MenuItem menuCerrarSesion;

    @FXML private TextArea txtInformacionUsuario;
    @FXML private Label lblCantidadDispositivos;

    @FXML private TextArea txtComentario;
    @FXML private Button btnEnviarComentario;
    @FXML private Button btnLimpiarComentario;
    @FXML private Label lblMensajeEstado;

    private String emailInvitado;
    private String permisos = "111"; // Por defecto todos los permisos

    @FXML
    public void initialize() {
        // Configurar navegación
        menuDashboardInvitado.setOnAction(event -> irAVentana("dashboardInvitado.fxml", "Dashboard Invitado"));
        menuCerrarSesion.setOnAction(event -> cerrarSesion());

        // Configurar botones
        btnEnviarComentario.setOnAction(event -> enviarComentario());
        btnLimpiarComentario.setOnAction(event -> limpiarComentario());

        // Configurar TextArea de información como no editable
        txtInformacionUsuario.setEditable(false);
        txtInformacionUsuario.setWrapText(true);

        // Cargar permisos del invitado actual
        cargarPermisosInvitado();

        // Cargar información del usuario principal según permisos
        cargarInformacionUsuario();
    }

    // Este método debe llamarse desde el controlador de login después de verificar credenciales
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
                    // Si hay permisos definidos (columna 4), usarlos
                    if (invitado.length > 4) {
                        permisos = invitado[4].trim();
                    } else {
                        permisos = "111"; // Por defecto todos los permisos
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            permisos = "111"; // Por defecto en caso de error
        }
    }

    private void cargarInformacionUsuario() {
        try {
            StringBuilder informacion = new StringBuilder();

            // Leer información del usuario principal
            String[] usuario = ManejadorUsuarios.leerUsuarioPrincipal();
            if (usuario != null && usuario.length >= 3) {
                informacion.append("👤 INFORMACIÓN DEL USUARIO PRINCIPAL\n");
                informacion.append("=====================================\n\n");

                informacion.append("• Nombre: ").append(usuario[0]).append("\n");
                informacion.append("• Email: ").append(usuario[1]).append("\n");

                // Agregar información adicional si está disponible
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

                // Cargar datos de salud según permisos
                boolean verFrecuencia = permisos.charAt(0) == '1';
                boolean verSueno = permisos.charAt(1) == '1';
                boolean verActividad = permisos.charAt(2) == '1';

                if (verFrecuencia || verSueno || verActividad) {
                    informacion.append("📊 DATOS DE SALUD (Hoy)\n");
                    informacion.append("=======================\n\n");

                    // Obtener datos actuales
                    Map<String, String> datosHoy = LectorDatosSimulador.leerDatosPorFecha(LocalDate.now());

                    if (verFrecuencia) {
                        if (datosHoy.containsKey("frecuencia")) {
                            informacion.append("• Frecuencia Cardíaca: ").append(datosHoy.get("frecuencia"))
                                    .append(" lpm - ").append(datosHoy.getOrDefault("estadoFrecuencia", "N/A")).append("\n");
                        } else {
                            informacion.append("• Frecuencia Cardíaca: No disponible\n");
                        }
                    }

                    if (verActividad) {
                        if (datosHoy.containsKey("pasos")) {
                            informacion.append("• Actividad Física: ").append(datosHoy.get("pasos"))
                                    .append(" pasos - ").append(datosHoy.getOrDefault("nivelActividad", "N/A")).append("\n");
                        } else {
                            informacion.append("• Actividad Física: No disponible\n");
                        }
                    }

                    if (verSueno) {
                        if (datosHoy.containsKey("totalSueno")) {
                            informacion.append("• Sueño: ").append(datosHoy.get("totalSueno"))
                                    .append(" horas - ").append(datosHoy.getOrDefault("estadoSueno", "N/A")).append("\n");
                        } else {
                            informacion.append("• Sueño: No disponible\n");
                        }
                    }
                } else {
                    informacion.append("🔒 No tienes permisos para ver datos de salud\n");
                }
            } else {
                informacion.append("No se pudo cargar la información del usuario principal.\n");
                informacion.append("Por favor, contacte al administrador del sistema.");
            }

            // Obtener cantidad de dispositivos
            try {
                int cantidadDispositivos = GestorDispositivos.getInstancia().getCantidadDispositivos();
                informacion.append("\n\n📱 DISPOSITIVOS VINCULADOS\n");
                informacion.append("=========================\n");
                informacion.append("• Cantidad: ").append(cantidadDispositivos).append(" dispositivo").append(cantidadDispositivos != 1 ? "s" : "");
            } catch (Exception e) {
                informacion.append("\n\n📱 DISPOSITIVOS VINCULADOS\n");
                informacion.append("=========================\n");
                informacion.append("• Cantidad: No disponible");
            }

            txtInformacionUsuario.setText(informacion.toString());

        } catch (Exception e) {
            txtInformacionUsuario.setText("Error al cargar la información del usuario principal.\n\nDetalles del error:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void enviarComentario() {
        String comentario = txtComentario.getText().trim();

        if (comentario.isEmpty()) {
            mostrarMensaje("Por favor, escribe un comentario antes de enviar.", false);
            return;
        }

        // TODO: Implementar lógica para guardar el comentario
        System.out.println("Comentario a enviar: " + comentario);

        mostrarMensaje("✓ Comentario enviado exitosamente", true);
        txtComentario.clear();
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