package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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

    /**
     * Metodo que se ejecuta automáticamente al ingresar al menu de rangos en el FXML.
     */
    @FXML
    public void initialize() {
        // Configurar eventos del menú de navegación
        configurarEventosMenu();
    }

    /**
     * Configura las opciones de los eventos posibles para cambiar de menú.
     */
    private void configurarEventosMenu() {
        menuDashboard.setOnAction(event -> irAVentana("Dashboard.fxml", "Dashboard"));
        menuDispositivos.setOnAction(event -> irAVentana("menuDispositivos.fxml", "Mis Dispositivos"));
        menuGestionInvitados.setOnAction(event -> irAVentana("menuGestionInvitados.fxml", "Gestión de Invitados"));
        menuRangos.setOnAction(event -> irAVentana("menuRangos.fxml", "Rangos"));
        menuCerrarSesion.setOnAction(event -> cerrarSesion());
    }

    private void irAVentana(String nombreArchivo, String titulo) {
        try {
            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/" + nombreArchivo);

            // Verificar si el archivo existe
            if (!fxmlFile.exists()) {
                mostrarError("Archivo no encontrado: " + fxmlFile.getAbsolutePath());
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
            mostrarError("Error al cargar " + nombreArchivo + ": " + e.getMessage());
        }
    }

    private void cerrarSesion() {
        try {
            // Mostrar confirmación antes de cerrar sesión
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cerrar Sesión");
            alert.setHeaderText("¿Está seguro que desea cerrar sesión?");
            alert.setContentText("Será redirigido a la pantalla de inicio de sesión.");

            // En una aplicación real, aquí esperarías la respuesta del usuario
            // Por simplicidad, asumimos que confirma

            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/menuLogin.fxml");

            // Verificar si el archivo existe
            if (!fxmlFile.exists()) {
                mostrarError("Archivo de login no encontrado: " + fxmlFile.getAbsolutePath());
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
            mostrarError("Error al cerrar sesión: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Métodos utilitarios para los rangos (si los necesitas)

    /**
     * Metodo para validar frecuencia cardíaca
     */
    public boolean esFrecuenciaCardiacaNormal(int bpm) {
        return bpm >= 60 && bpm <= 100;
    }

    /**
     * Metodo para evaluar nivel de actividad
     */
    public String evaluarNivelActividad(int pasos) {
        if (pasos < 5000) return "Sedentario";
        else if (pasos < 7500) return "Poco Activo";
        else if (pasos < 10000) return "Moderadamente Activo";
        else if (pasos < 12500) return "Activo";
        else return "Muy Activo";
    }

    /**
     * Metodo para evaluar calidad de sueño
     */
    public String evaluarCalidadSueno(double horas) {
        if (horas < 6) return "Insuficiente";
        else if (horas >= 7.5) return "Óptima";
        else return "Adecuada";
    }
}