package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class controladorSignUp {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtContrasena;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtPeso;

    @FXML
    private TextField txtAltura;

    @FXML
    private Button btnRegistrar;

    @FXML
    public void initialize() {
        btnRegistrar.setOnAction(event -> registrarUsuario());
    }

    private void registrarUsuario() {
        try {
            if (txtNombre.getText().isEmpty() || txtCorreo.getText().isEmpty() ||
                    txtContrasena.getText().isEmpty()) {
                System.out.println("Por favor complete todos los campos obligatorios");
                return;
            }

            // Guardar información del usuario principal
            guardarUsuarioPrincipal();

            // Ir al login
            irALogin();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
    }

    private void guardarUsuarioPrincipal() throws IOException {
        File archivo = new File("usuario_principal.txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            // Formato: nombre,correo,contraseña,edad,peso,altura,tipo
            writer.println(
                    txtNombre.getText() + "," +
                            txtCorreo.getText() + "," +
                            txtContrasena.getText() + "," +
                            (txtEdad.getText().isEmpty() ? "0" : txtEdad.getText()) + "," +
                            (txtPeso.getText().isEmpty() ? "0" : txtPeso.getText()) + "," +
                            (txtAltura.getText().isEmpty() ? "0" : txtAltura.getText()) + "," +
                            "PRINCIPAL"
            );
        }

        System.out.println("Usuario principal registrado exitosamente");
    }

    private void irALogin() {
        try {
            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/menuLogin.fxml");

            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            Stage stage = (Stage) btnRegistrar.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Inicio de Sesión");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar login: " + e.getMessage());
        }
    }

    // Método para leer la información del usuario principal desde otro archivo
    public static String[] leerUsuarioPrincipal() {
        try {
            File archivo = new File("usuario_principal.txt");
            if (!archivo.exists()) {
                return null;
            }

            java.util.Scanner scanner = new java.util.Scanner(archivo);
            if (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                scanner.close();
                return linea.split(",");
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}