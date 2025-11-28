package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    @FXML private Button btnEliminarDispositivo; // Cambio de nombre
    @FXML private Button btnMostrarDispositivos;
    @FXML private Button btnLimpiarPantalla; // Nuevo
    @FXML private ChoiceBox<String> chbDispositivos; // Nuevo

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
        btnEliminarDispositivo.setOnAction(event -> eliminarDispositivoSeleccionado());
        btnMostrarDispositivos.setOnAction(event -> mostrarDispositivos());
        btnLimpiarPantalla.setOnAction(event -> limpiarPantalla());

        actualizarInterfaz();
    }

    private void agregarSmartWatch() {
        String id = "SW-" + String.format("%04d", contadorSmartWatch++);
        SmartWatch smartwatch = new SmartWatch(id, "Apple Watch Series " + (7 + random.nextInt(3)), "Apple");
        gestor.agregarDispositivo(smartwatch);

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
        gestor.agregarDispositivo(pulsera);

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
        OximetroPulso oximetro = new OximetroPulso(id, "Oxímetro de Pulso", "Wellue");
        gestor.agregarDispositivo(oximetro);

        SimuladorDatos.generarFrecuenciaCardiaca();
        SimuladorDatos.generarOxigeno();
        actualizarInterfaz();

        areaDispositivos.setText(areaDispositivos.getText() +
                "\nOxímetro agregado y datos generados:\n" +
                "   • Frecuencia cardíaca\n" +
                "   • Oxígeno\n");
    }

    private void eliminarDispositivoSeleccionado() {
        String seleccion = chbDispositivos.getValue();

        if (seleccion == null || seleccion.isEmpty()) {
            areaDispositivos.setText("Por favor, seleccione un dispositivo para eliminar.");
            return;
        }

        // Extraer el ID del dispositivo de la selección
        String idDispositivo = extraerIdDeSeleccion(seleccion);

        // Buscar el dispositivo antes de eliminarlo
        DispositivoWearable dispositivo = gestor.buscarDispositivo(idDispositivo);

        if (dispositivo == null) {
            areaDispositivos.setText("Dispositivo no encontrado.");
            return;
        }

        // Guardar el tipo antes de eliminar
        String tipoDispositivo = dispositivo.getTipoDispositivo();

        // Eliminar el dispositivo
        boolean eliminado = gestor.eliminarDispositivo(idDispositivo);

        if (eliminado) {
            // Verificar si quedan dispositivos del mismo tipo
            verificarYEliminarArchivos(tipoDispositivo);

            actualizarInterfaz();
            areaDispositivos.setText("Dispositivo eliminado: " + dispositivo.getNombre() + " (" + idDispositivo + ")");
        } else {
            areaDispositivos.setText("No se pudo eliminar el dispositivo.");
        }
    }

    private String extraerIdDeSeleccion(String seleccion) {
        // Formato esperado: "Tipo - Nombre (ID)"
        int inicioId = seleccion.lastIndexOf("(");
        int finId = seleccion.lastIndexOf(")");

        if (inicioId != -1 && finId != -1) {
            return seleccion.substring(inicioId + 1, finId);
        }

        return "";
    }

    private void verificarYEliminarArchivos(String tipoDispositivo) {
        // Contar cuántos dispositivos quedan de cada tipo
        int smartwatches = 0;
        int pulseras = 0;
        int oximetros = 0;

        for (DispositivoWearable d : gestor.getDispositivos()) {
            if (d instanceof SmartWatch) smartwatches++;
            else if (d instanceof Pulsera) pulseras++;
            else if (d instanceof OximetroPulso) oximetros++;
        }

        // Eliminar archivos solo si no quedan dispositivos que los generen
        if (smartwatches == 0) {
            // SmartWatch genera: FC, Actividad, Sueño
            if (pulseras == 0 && oximetros == 0) {
                new File("frecuencia_cardiaca.txt").delete();
            }
            if (pulseras == 0) {
                new File("actividad_fisica.txt").delete();
            }
            new File("sueño.txt").delete();
        }

        if (pulseras == 0) {
            // Pulsera genera: FC, Actividad
            if (smartwatches == 0 && oximetros == 0) {
                new File("frecuencia_cardiaca.txt").delete();
            }
            if (smartwatches == 0) {
                new File("actividad_fisica.txt").delete();
            }
        }

        if (oximetros == 0) {
            // Oxímetro genera: FC, Oxígeno
            if (smartwatches == 0 && pulseras == 0) {
                new File("frecuencia_cardiaca.txt").delete();
            }
            new File("oxigeno.txt").delete();
        }
    }

    private void limpiarPantalla() {
        areaDispositivos.clear();
    }

    private void mostrarDispositivos() {
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

    private void actualizarInterfaz() {
        txtTituloDispositivos.setText("Dispositivos Vinculados (" + gestor.getCantidadDispositivos() + ")");

        // Actualizar ChoiceBox
        actualizarChoiceBox();

        if (gestor.getCantidadDispositivos() == 0) {
            areaDispositivos.setText("No hay dispositivos vinculados.\n\n" +
                    "Use los botones de abajo para agregar dispositivos wearable.");
        } else {
            mostrarDispositivos();
        }
    }

    private void actualizarChoiceBox() {
        chbDispositivos.getItems().clear();

        for (DispositivoWearable dispositivo : gestor.getDispositivos()) {
            String item = dispositivo.getTipoDispositivo() + " - " +
                    dispositivo.getNombre() + " (" +
                    dispositivo.getIdDispositivo() + ")";
            chbDispositivos.getItems().add(item);
        }

        // Seleccionar el primero si hay dispositivos
        if (!chbDispositivos.getItems().isEmpty()) {
            chbDispositivos.setValue(chbDispositivos.getItems().get(0));
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