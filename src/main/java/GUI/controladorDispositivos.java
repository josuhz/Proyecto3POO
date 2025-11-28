package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modelo.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class controladorDispositivos {

    @FXML private MenuButton menuNavegacion;
    @FXML private MenuItem menuDashboard;
    @FXML private MenuItem menuDispositivos;
    @FXML private MenuItem menuGestionInvitados;
    @FXML private MenuItem menuRangos;
    @FXML private MenuItem menuCerrarSesion;
    @FXML private TextArea areaDispositivos;
    @FXML private Text txtTituloDispositivos;

    @FXML private Button btnAgregarSmartWatch;
    @FXML private Button btnAgregarPulsera;
    @FXML private Button btnAgregarOximetro;
    @FXML private Button btnLimpiar;
    @FXML private Button btnMostrarDispositivos;

    private GestorDispositivos gestor = GestorDispositivos.getInstancia();
    private Random random = new Random();
    private int contadorSmartWatch = 1;
    private int contadorPulsera = 1;
    private int contadorOximetro = 1;

    @FXML
    public void initialize() {
        // Configurar navegación
        menuDashboard.setOnAction(event -> irAVentana("Dashboard.fxml", "Dashboard"));
        menuDispositivos.setOnAction(event -> irAVentana("menuDispositivos.fxml", "Mis Dispositivos"));
        menuGestionInvitados.setOnAction(event -> irAVentana("menuGestionInvitados.fxml", "Gestión de Invitados"));
        menuRangos.setOnAction(event -> irAVentana("menuRangos.fxml", "Rangos"));
        menuCerrarSesion.setOnAction(event -> cerrarSesion());

        // Configurar botones de dispositivos
        btnAgregarSmartWatch.setOnAction(event -> agregarSmartWatch());
        btnAgregarPulsera.setOnAction(event -> agregarPulsera());
        btnAgregarOximetro.setOnAction(event -> agregarOximetro());
        btnLimpiar.setOnAction(event -> limpiarDispositivos());
        btnMostrarDispositivos.setOnAction(event -> mostrarDispositivos()); // ← Nuevo

        actualizarInterfaz();
    }

    private void agregarSmartWatch() {
        String id = "SW-" + String.format("%04d", contadorSmartWatch++);
        SmartWatch smartwatch = new SmartWatch(id, "Apple Watch Series " + (7 + random.nextInt(3)), "Apple");
        gestor.agregarDispositivo(smartwatch); // ← Usar el método del gestor

        SimuladorDatos.generarFrecuenciaCardiaca();
        SimuladorDatos.generarActividadFisica();
        SimuladorDatos.generarSueno();
        actualizarInterfaz();

        areaDispositivos.setText(areaDispositivos.getText() +
                "\nSmartWatch agregado y datos generados:\n" +
                "   • Frecuencia cardíaca\n" +
                "   • Actividad física\n" +
                "   • Sueño\n");
    }

    private void agregarPulsera() {
        String id = "PL-" + String.format("%04d", contadorPulsera++);
        String[] marcas = {"Fitbit", "Xiaomi", "Samsung", "Garmin"};
        Pulsera pulsera = new Pulsera(id, "Pulsera " + marcas[random.nextInt(marcas.length)], marcas[random.nextInt(marcas.length)]);
        gestor.agregarDispositivo(pulsera); // ← Usar el método del gestor

        SimuladorDatos.generarFrecuenciaCardiaca();
        SimuladorDatos.generarActividadFisica();
        actualizarInterfaz();

        areaDispositivos.setText(areaDispositivos.getText() +
                "\nPulsera agregada y datos generados:\n" +
                "   • Actividad física\n" +
                "   • Frecuencia cardíaca\n");
    }

    private void agregarOximetro() {
        String id = "OX-" + String.format("%04d", contadorOximetro++);
        OximetroPulso oximetro = new OximetroPulso(id, "Oxímetro de Pulso", "Welue");
        gestor.agregarDispositivo(oximetro); // ← Usar el método del gestor

        SimuladorDatos.generarFrecuenciaCardiaca();
        SimuladorDatos.generarOxigeno();
        actualizarInterfaz();

        areaDispositivos.setText(areaDispositivos.getText() +
                "\nOxímetro agregado y datos generados:\n" +
                "   • Frecuencia cardíaca\n" +
                "   • Oxígeno\n");
    }

    private void mostrarDispositivos() { // ← Nuevo método
        if (gestor.getCantidadDispositivos() == 0) {
            areaDispositivos.setText("No hay dispositivos vinculados.\n\n" +
                    "Use los botones de abajo para agregar dispositivos wearable.");
            return;
        }

        StringBuilder lista = new StringBuilder("=== MIS DISPOSITIVOS ===\n\n");
        for (DispositivoWearable dispositivo : gestor.getDispositivos()) {
            lista.append("🔹 ").append(dispositivo.getTipoDispositivo()).append("\n");
            lista.append("   Nombre: ").append(dispositivo.getNombre()).append("\n");
            lista.append("   ID: ").append(dispositivo.getIdDispositivo()).append("\n");
            lista.append("   Marca: ").append(dispositivo.getMarca()).append("\n\n");
        }
        areaDispositivos.setText(lista.toString());
    }

    private String formatearValorMetrica(Metrica metrica) {
        if (metrica instanceof MetricaCardiaca) {
            MetricaCardiaca cardiaca = (MetricaCardiaca) metrica;
            return cardiaca.getFrecuenciaCardiaca() + " BPM";
        } else if (metrica instanceof MetricaActividad) {
            MetricaActividad actividad = (MetricaActividad) metrica;
            return actividad.getPasos() + " pasos, " + actividad.getCaloriasQuemadas() + " cal";
        } else if (metrica instanceof MetricaSueno) {
            MetricaSueno sueno = (MetricaSueno) metrica;
            return String.format("%.1f horas, calidad %d", sueno.getHorasSueno(), sueno.getCalidadSueno());
        } else if (metrica instanceof MetricaOxigeno) {
            MetricaOxigeno oxigeno = (MetricaOxigeno) metrica;
            return String.format("%.1f%%", oxigeno.getSpo2());
        }

        return String.valueOf(metrica.calcularIndicador());
    }

    private void limpiarDispositivos() {
        gestor.limpiarTodo(); // ← Usar método del gestor
        contadorSmartWatch = 1;
        contadorPulsera = 1;
        contadorOximetro = 1;

        new File("frecuencia_cardiaca.txt").delete();
        new File("actividad_fisica.txt").delete();
        new File("sueño.txt").delete();
        new File("oxigeno.txt").delete();

        actualizarInterfaz();
        areaDispositivos.setText("Todos los dispositivos y datos han sido eliminados.");
    }

    private void actualizarInterfaz() {
        txtTituloDispositivos.setText("Dispositivos Vinculados (" + gestor.getCantidadDispositivos() + ")"); // ← Usar método del gestor

        if (gestor.getCantidadDispositivos() == 0) {
            areaDispositivos.setText("No hay dispositivos vinculados.\n\n" +
                    "Use los botones de abajo para agregar dispositivos wearable.");
        } else {
            mostrarDispositivos(); // ← Usar el método nuevo
        }
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
        }
    }
}