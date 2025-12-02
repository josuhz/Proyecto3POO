package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
public class controladorSignUp {

    // ===== Campos de texto obligatorios =====

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtContrasena;

    // ===== Campos de texto opcionales =====
    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtPeso;
    @FXML
    private TextField txtAltura;

    // ===== Botón =====

    /** Botón para registrar al usuario en el sistema */
    @FXML
    private Button btnRegistrar;

    /**
     * Metodo de inicialización del controlador.
     * Se ejecuta automáticamente después de cargar el archivo FXML.
     * Configura el evento del botón registrar.
     */
    @FXML
    public void initialize() {
        // Asignar el evento al botón de registro
        btnRegistrar.setOnAction(event -> registrarUsuario());
    }

    /**
     * Maneja el proceso de registro de un nuevo usuario.
     * Valida que los campos obligatorios estén completos,
     * guarda la información del usuario y redirige al login.
     */
    private void registrarUsuario() {
        try {
            // Validar que los campos obligatorios no estén vacíos
            if (txtNombre.getText().isEmpty() || txtCorreo.getText().isEmpty() ||
                    txtContrasena.getText().isEmpty()) {
                System.out.println("Por favor complete todos los campos obligatorios");
                return;
            }

            // Guardar usuario en archivo
            guardarUsuarioPrincipal();

            // Redirigir al login
            irALogin();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
    }

    /**
     * Guarda la información del usuario principal en un archivo de texto.
     * La contraseña se cifra usando BCrypt antes de guardarla.
     * @throws IOException si hay un error al escribir el archivo
     */
    private void guardarUsuarioPrincipal() throws IOException {
        // Crear el archivo donde se guardará el usuario
        File archivo = new File("usuario_principal.txt");

        // Cifrar la contraseña antes de guardarla
        // BCrypt.gensalt(12) genera un salt con factor de costo 12 (alto nivel de seguridad)
        String contrasenaOriginal = txtContrasena.getText();
        String contrasenaCifrada = BCrypt.hashpw(contrasenaOriginal, BCrypt.gensalt(12));

        // Escribir la información del usuario en el archivo
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            // Formato CSV: nombre,correo,contraseña,edad,peso,altura,tipo
            writer.println(
                    txtNombre.getText() + "," +
                            txtCorreo.getText() + "," +
                            contrasenaCifrada + "," +
                            (txtEdad.getText().isEmpty() ? "0" : txtEdad.getText()) + "," +
                            (txtPeso.getText().isEmpty() ? "0" : txtPeso.getText()) + "," +
                            (txtAltura.getText().isEmpty() ? "0" : txtAltura.getText()) + "," +
                            "PRINCIPAL"  // Tipo de usuario
            );
        }

        System.out.println("Usuario principal registrado exitosamente");
    }

    /**
     * Navega a la ventana de inicio de sesión (menuLogin.fxml).
     * Cierra la ventana actual de registro y abre el login.
     */
    private void irALogin() {
        try {
            // Obtener la ruta del directorio del proyecto
            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/menuLogin.fxml");

            // Cargar el archivo FXML del login
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            // Obtener el Stage actual y cambiar la escena
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
}