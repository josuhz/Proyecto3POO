package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.SimuladorDatos;

import java.io.File;
import java.io.IOException;

public class controladorLogin {

    @FXML
    private TextField correoField;

    @FXML
    private PasswordField contrasenaField;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Hyperlink enlaceInvitado;

    /**
     * Metodo que se ejecuta automáticamente al cargar menú de login en el FXML
     */
    @FXML
    public void initialize() {
        btnIniciarSesion.setOnAction(event -> irADashboard(event));

        if (enlaceInvitado != null) {
            enlaceInvitado.setOnAction(event -> irALoginInvitado(event));
        }
    }

    /**
     * Metodo que se encarga enviar al usuario al menú del dashboard.
     * Este verifica si las credenciales del usuario se llenaron y coinciden con algún usurio guardado.
     * @param event
     */
    @FXML
    private void irADashboard(ActionEvent event) {
        try {
            String correo = correoField.getText();
            String contrasena = contrasenaField.getText();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                System.out.println("Por favor, completa todos los campos");
                return;
            }

            if (!ManejadorUsuarios.verificarCredenciales(correo, contrasena)) {
                System.out.println("Credenciales incorrectas");
                return;
            }

            if (SimuladorDatos.hayDispositivosVinculados()) {
                SimuladorDatos.completarSoloDatosExistentes();
                System.out.println("Datos actualizados hasta la fecha actual");
            }

            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/Dashboard.fxml");

            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar Dashboard.fxml: " + e.getMessage());
        }
    }

    /**
     * Metodo que se encarga enviar al usuario al menú del de invitado.
     * Este verifica si el usuario elige la opción de ingresar como invitado.
     * @param event
     */
    @FXML
    private void irALoginInvitado(ActionEvent event) {
        try {
            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/menuLoginInvitado.fxml");

            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            Stage stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Acceso Invitado");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar menuLoginInvitado.fxml: " + e.getMessage());
        }
    }
}