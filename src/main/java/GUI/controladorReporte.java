package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class controladorReporte {

    @FXML
    private Button btnVolver;

    @FXML
    public void initialize() {
        // Inicialización si es necesaria
        System.out.println("Reporte de Salud cargado");
    }

    @FXML
    private void volverDashboard() {
        try {
            // Cargar el Dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            // Obtener el stage actual
            Stage stage = (Stage) btnVolver.getScene().getWindow();

            // Cambiar a la escena del Dashboard
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al volver al Dashboard");
        }
    }
}