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

public class controladorRangos {

    // Navegación
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

    // TextAreas
    @FXML
    private TextArea areaFrecuenciaCardiaca;

    @FXML
    private TextArea areaGlucosa;

    @FXML
    private TextArea areaCalidadSueno;

    // Botón
    @FXML
    private Button btnGuardarConfiguracion;

    @FXML
    public void initialize() {
        // Configurar eventos del menú de navegación
        menuDashboard.setOnAction(event -> irAVentana("Dashboard.fxml", "Dashboard"));
        menuDispositivos.setOnAction(event -> irAVentana("menuDispositivos.fxml", "Mis Dispositivos"));
        menuGestionInvitados.setOnAction(event -> irAVentana("menuGestionInvitados.fxml", "Gestión de Invitados"));
        menuRangos.setOnAction(event -> irAVentana("menuRangos.fxml", "Rangos"));
        menuCerrarSesion.setOnAction(event -> cerrarSesion());

        // Configurar evento del botón guardar
        btnGuardarConfiguracion.setOnAction(event -> guardarConfiguracion());
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
            System.out.println("Error al cargar " + nombreArchivo + ": " + e.getMessage());
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
            System.out.println("Error al cerrar sesión: " + e.getMessage());
        }
    }

    private void guardarConfiguracion() {
        System.out.println("Guardar configuración");
    }
}