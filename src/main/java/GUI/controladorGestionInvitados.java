package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class controladorGestionInvitados {

    @FXML
    private TextArea areaInvitados;

    @FXML
    private Button btnModificarPermisos;

    @FXML
    private Button btnRevocarAcceso;

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
    public void initialize() {
        menuDashboard.setOnAction(event -> irAVentana("Dashboard.fxml", "Dashboard"));
        menuDispositivos.setOnAction(event -> irAVentana("menuDispositivos.fxml", "Mis Dispositivos"));
        menuGestionInvitados.setOnAction(event -> irAVentana("menuGestionInvitados.fxml", "Gestión de Invitados"));
        menuRangos.setOnAction(event -> irAVentana("menuRangos.fxml", "Rangos"));
        menuCerrarSesion.setOnAction(event -> cerrarSesion());

        btnModificarPermisos.setOnAction(event -> modificarPermisos());
        btnRevocarAcceso.setOnAction(event -> revocarAcceso());
    }

    private void irAVentana(String nombreArchivo, String titulo) {
        try {
            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/" + nombreArchivo);

            if (!fxmlFile.exists()) {
                System.out.println("Error: No se encontró el archivo " + nombreArchivo);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            Stage stage = (Stage) menuNavegacion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar " + nombreArchivo + ": " + e.getMessage());
        }
    }

    private void cerrarSesion() {
        try {
            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/menuLogin.fxml");

            if (!fxmlFile.exists()) {
                System.out.println("Error: No se encontró menuLogin.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            Stage stage = (Stage) menuNavegacion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Inicio de Sesión");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cerrar sesión: " + e.getMessage());
        }
    }

    private void modificarPermisos() {
        System.out.println("Modificar permisos seleccionado");
    }

    private void revocarAcceso() {
        System.out.println("Revocar acceso seleccionado");
    }

    public void agregarInvitado(String nombre, String email, String relacion, String acceso, String ultimaVisualizacion) {
        String invitadoActual = areaInvitados.getText();
        String nuevoInvitado = "\n--- " + nombre + " ---\n" +
                "Email: " + email + "\n" +
                "Relación: " + relacion + "\n" +
                "Acceso: " + acceso + "\n" +
                "Última visualización: " + ultimaVisualizacion + "\n" +
                "------------------------\n";

        areaInvitados.setText(invitadoActual + nuevoInvitado);
    }
}