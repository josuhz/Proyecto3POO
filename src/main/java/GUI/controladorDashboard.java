package GUI;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
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
    private Label lblActividad;

    @FXML
    private Label lblHorasSueno;

    @FXML
    private BarChart<String, Number> bcharFrecCardiaca;

    @FXML
    private TextArea txtAlertas;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    public void initialize() {
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

    public Label getLblUsuario() {
        return lblUsuario;
    }

    public Label getLblNivelCardiaco() {
        return lblNivelCardiaco;
    }

    public Label getLblActividad() {
        return lblActividad;
    }

    public Label getLblHorasSueno() {
        return lblHorasSueno;
    }

    public BarChart<String, Number> getBcharFrecCardiaca() {
        return bcharFrecCardiaca;
    }

    public TextArea getTxtAlertas() {
        return txtAlertas;
    }

    public TextArea getTxtObservaciones() {
        return txtObservaciones;
    }
}