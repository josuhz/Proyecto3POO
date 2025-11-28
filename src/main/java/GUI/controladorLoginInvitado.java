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
import java.time.LocalDate;

public class controladorLoginInvitado {

    @FXML
    private TextField txtNombreInvitado;

    @FXML
    private TextField txtCorreoInvitado;

    @FXML
    private Button btnAccederInvitado;

    @FXML
    private Button btnVolver;

    @FXML
    public void initialize() {
        btnAccederInvitado.setOnAction(event -> accederComoInvitado());
        btnVolver.setOnAction(event -> volverALoginPrincipal());
    }

    private void accederComoInvitado() {
        try {
            if (txtNombreInvitado.getText().isEmpty() || txtCorreoInvitado.getText().isEmpty()) {
                System.out.println("Por favor complete todos los campos");
                return;
            }

            // Guardar información del invitado
            guardarInvitado();

            // Ir al Dashboard (por ahora, mismo que usuario principal)
            irADashboard();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al acceder como invitado: " + e.getMessage());
        }
    }

    private void guardarInvitado() throws IOException {
        File archivo = new File("usuarios_invitados.txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo, true))) {
            // Formato: nombre,correo,tipo,fecha
            writer.println(
                    txtNombreInvitado.getText() + "," +
                            txtCorreoInvitado.getText() + "," +
                            "INVITADO" + "," +
                            LocalDate.now()
            );
        }

        System.out.println("Invitado registrado exitosamente");
    }

    private void irADashboard() {
        try {
            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/Dashboard.fxml");

            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            Stage stage = (Stage) btnAccederInvitado.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dashboard - Modo Invitado");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar dashboard: " + e.getMessage());
        }
    }

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
            System.out.println("Error al volver al login: " + e.getMessage());
        }
    }

    // Método para leer todos los invitados
    public static java.util.List<String[]> leerInvitados() {
        java.util.List<String[]> invitados = new java.util.ArrayList<>();
        try {
            File archivo = new File("usuarios_invitados.txt");
            if (!archivo.exists()) {
                return invitados;
            }

            java.util.Scanner scanner = new java.util.Scanner(archivo);
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine().trim();
                if (!linea.isEmpty()) {
                    String[] datos = linea.split(",");
                    invitados.add(datos);
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invitados;
    }
}