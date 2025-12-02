package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class controladorLoginInvitado {

    @FXML
    private TextField txtNombreInvitado;

    @FXML
    private TextField txtCorreoInvitado;

    @FXML
    private Button btnAccederInvitado;

    @FXML
    private Button btnVolver;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /**
     * Metodo que se ejecuta automáticamente al cargar menú de login de invitados en el FXML
     */
    @FXML
    public void initialize() {
        btnAccederInvitado.setOnAction(event -> accederComoInvitado());
        btnVolver.setOnAction(event -> volverALoginPrincipal());
    }

    /**
     * Metodo que se encarga de acceder al sistema de usuario.
     * Este verifica si las credenciales del invitado se llenaron no se repiten con otro invitado ya guardado.
     */
    private void accederComoInvitado() {
        try {
            String nombre = txtNombreInvitado.getText().trim();
            String correo = txtCorreoInvitado.getText().trim();

            // Validar campos vacíos
            if (nombre.isEmpty() || correo.isEmpty()) {
                mostrarAlerta("Error", "Por favor complete todos los campos", Alert.AlertType.ERROR);
                return;
            }

            // Validar formato de correo
            if (!EMAIL_PATTERN.matcher(correo).matches()) {
                mostrarAlerta("Error", "El formato del correo electrónico no es válido", Alert.AlertType.ERROR);
                return;
            }

            // Verificar si ya existe el invitado
            List<String[]> invitados = ManejadorUsuarios.leerInvitados();
            boolean existe = false;
            boolean nombreExiste = false;
            boolean correoExiste = false;

            for (String[] invitado : invitados) {
                if (invitado.length >= 2) {
                    String nombreExistente = invitado[0].trim();
                    String correoExistente = invitado[1].trim();

                    if (nombreExistente.equalsIgnoreCase(nombre) && correoExistente.equalsIgnoreCase(correo)) {
                        // Usuario ya existe con mismo nombre y correo - permitir acceso
                        existe = true;
                        break;
                    }

                    if (nombreExistente.equalsIgnoreCase(nombre)) {
                        nombreExiste = true;
                    }

                    if (correoExistente.equalsIgnoreCase(correo)) {
                        correoExiste = true;
                    }
                }
            }

            // Si el nombre existe pero con diferente correo
            if (nombreExiste && !existe) {
                mostrarAlerta("Error", "Ya existe un invitado con ese nombre pero con diferente correo.\nPor favor verifica tus datos.", Alert.AlertType.ERROR);
                return;
            }

            // Si el correo existe pero con diferente nombre
            if (correoExiste && !existe) {
                mostrarAlerta("Error", "Ya existe un invitado con ese correo pero con diferente nombre.\nPor favor verifica tus datos.", Alert.AlertType.ERROR);
                return;
            }

            // Si no existe, guardarlo
            if (!existe) {
                guardarInvitado(nombre, correo);
            }

            // Ir al menú de usuario invitado
            irAMenuUsuarioInvitado(correo);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al acceder como invitado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Metodo que se encarga de guardar el nuevo invitado en un archivo.
     * @param nombre
     * @param correo
     * @throws IOException
     */
    private void guardarInvitado(String nombre, String correo) throws IOException {
        File archivo = new File("usuarios_invitados.txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo, true))) {
            // Formato: nombre,correo,tipo,fecha,permisos (permisos por defecto: 0000 - info básica)
            writer.println(
                    nombre + "," +
                            correo + "," +
                            "INVITADO" + "," +
                            LocalDate.now() + "," +
                            "0000"  // Permisos por defecto: info básica de todo
            );
        }

        System.out.println("Invitado registrado exitosamente");
    }

    /**
     * Metodo que se encarga de preparar y mostrar una alerta depiendo de la acción realizada.
     * @param titulo
     * @param mensaje
     * @param tipo
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Metodo que se encarga de dirigir al usuario al menú de invitado y carga el email de este.
     * @param correo
     */
    private void irAMenuUsuarioInvitado(String correo) {
        try {
            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/menuUsuarioInvitado.fxml");

            if (!fxmlFile.exists()) {
                mostrarAlerta("Error", "No se encontró menuUsuarioInvitado.fxml", Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            // Obtener el controlador del menú de usuario invitado y establecer el email
            controladorUsuarioInvitado controlador = loader.getController();
            controlador.setEmailInvitado(correo);

            Stage stage = (Stage) btnAccederInvitado.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Panel de Invitado");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar menú de invitado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Metodo que se encarga de volver la menú de login principal
     */
    private void volverALoginPrincipal() {
        try {
            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/menuLogin.fxml");

            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Inicio de Sesión");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}