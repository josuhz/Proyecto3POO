import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;

public class SistemaMonitoreo extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {

            File archivoUsuario = new File("usuario_principal.txt");

            if (archivoUsuario.exists()) {
                cargarLogin(primaryStage);
            } else {
                cargarSignUp(primaryStage);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cargarSignUp(Stage primaryStage) throws Exception {
        String userDir = System.getProperty("user.dir");
        File fxmlFile = new File(userDir, "src/main/java/GUI/menuSignUp.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
        Parent root = loader.load();

        primaryStage.setTitle("Registro - HealthTrack");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void cargarLogin(Stage primaryStage) throws Exception {
        String userDir = System.getProperty("user.dir");
        File fxmlFile = new File(userDir, "src/main/java/GUI/menuLogin.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
        Parent root = loader.load();

        primaryStage.setTitle("Inicio de Sesión - HealthTrack");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}