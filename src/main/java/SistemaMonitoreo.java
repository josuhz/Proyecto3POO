import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;

public class SistemaMonitoreo extends Application {

    /**
     * Metodo de inicio de la aplicación JavaFX.
     * Verifica si existe un usuario principal registrado
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Verificar si existe el archivo de usuario principal
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

    /**
     * Carga la pantalla de registro (Sign Up) para crear un nuevo usuario principal.
     * @param primaryStage
     * @throws Exception
     */
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

    /**
     * Carga la pantalla de inicio de sesión (Login) para usuarios existentes.
     * @param primaryStage
     * @throws Exception
     */
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

    /**
     * Metodo principal que inicia la aplicación JavaFX.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}