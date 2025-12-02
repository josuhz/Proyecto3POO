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
    @FXML private Button btnEliminarDispositivo;
    @FXML private Button btnMostrarDispositivos;
    @FXML private Button btnLimpiarPantalla;
    @FXML private ChoiceBox<String> chbDispositivos;

    private GestorDispositivos gestor = GestorDispositivos.getInstancia();


    /**
     * Metodo que se ejecuta automáticamente al cargar menú de dispositivos en el FXML
     */
    @FXML
    public void initialize() {
        menuDashboard.setOnAction(event -> irAVentana("Dashboard.fxml", "Dashboard"));
        menuDispositivos.setOnAction(event -> irAVentana("menuDispositivos.fxml", "Mis Dispositivos"));
        menuGestionInvitados.setOnAction(event -> irAVentana("menuGestionInvitados.fxml", "Gestión de Invitados"));
        menuRangos.setOnAction(event -> irAVentana("menuRangos.fxml", "Rangos"));
        menuCerrarSesion.setOnAction(event -> cerrarSesion());

        btnAgregarSmartWatch.setOnAction(event -> agregarSmartWatch());
        btnAgregarPulsera.setOnAction(event -> agregarPulsera());
        btnAgregarOximetro.setOnAction(event -> agregarOximetro());
        btnEliminarDispositivo.setOnAction(event -> eliminarDispositivoSeleccionado());
        btnMostrarDispositivos.setOnAction(event -> mostrarDispositivos());
        btnLimpiarPantalla.setOnAction(event -> limpiarPantalla());

        actualizarInterfaz();
    }

    /**
     * Metodo que se encarga de agregar un nuevo dispositivo SmartWatch.
     * Cada dispositivo agregado de este tipo es diferente.
     */
    private void agregarSmartWatch() {
        String id = "SW-" + String.format("%04d", gestor.getContadorSmartWatch());
        SmartWatch smartwatch = new SmartWatch(id, "Apple Watch Series " + (7 + new Random().nextInt(3)), "Apple");
        gestor.agregarDispositivo(smartwatch);
        gestor.setContadorSmartWatch(gestor.getContadorSmartWatch() + 1);
        gestor.guardarDispositivos();

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

    /**
     * Metodo que se encarga de agregar un nuevo dispositivo Pulsera.
     * Cada dispositivo agregado de este tipo es diferente.
     */
    private void agregarPulsera() {
        String id = "PL-" + String.format("%04d", gestor.getContadorPulsera());
        String[] marcas = {"Fitbit", "Xiaomi", "Samsung", "Garmin"};
        Pulsera pulsera = new Pulsera(id, "Pulsera " + marcas[new Random().nextInt(marcas.length)], marcas[new Random().nextInt(marcas.length)]);
        gestor.agregarDispositivo(pulsera);
        gestor.setContadorPulsera(gestor.getContadorPulsera() + 1);
        gestor.guardarDispositivos();

        SimuladorDatos.generarFrecuenciaCardiaca();
        SimuladorDatos.generarActividadFisica();
        actualizarInterfaz();

        areaDispositivos.setText(areaDispositivos.getText() +
                "\nPulsera agregada y datos generados:\n" +
                "   • Actividad física\n" +
                "   • Frecuencia cardíaca\n");
    }

    /**
     * Metodo que se encarga de agregar un nuevo dispositivo Oximetro.
     * Cada dispositivo agregado de este tipo es diferente.
     */
    private void agregarOximetro() {
        String id = "OX-" + String.format("%04d", gestor.getContadorOximetro());
        OximetroPulso oximetro = new OximetroPulso(id, "Oxímetro de Pulso", "Wellue");
        gestor.agregarDispositivo(oximetro);
        gestor.setContadorOximetro(gestor.getContadorOximetro() + 1);
        gestor.guardarDispositivos();

        SimuladorDatos.generarFrecuenciaCardiaca();
        SimuladorDatos.generarOxigeno();
        actualizarInterfaz();

        areaDispositivos.setText(areaDispositivos.getText() +
                "\nOxímetro agregado y datos generados:\n" +
                "   • Frecuencia cardíaca\n" +
                "   • Oxígeno\n");
    }

    /**
     * Metodo que se encarga de eliminar el dispositivo que se seleccionó.
     */
    private void eliminarDispositivoSeleccionado() {
        String seleccion = chbDispositivos.getValue();

        if (seleccion == null || seleccion.isEmpty()) {
            areaDispositivos.setText("Por favor, seleccione un dispositivo para eliminar.");
            return;
        }

        String idDispositivo = extraerIdDeSeleccion(seleccion);
        DispositivoWearable dispositivo = gestor.buscarDispositivo(idDispositivo);

        if (dispositivo == null) {
            areaDispositivos.setText("Dispositivo no encontrado.");
            return;
        }

        String tipoDispositivo = dispositivo.getTipoDispositivo();
        boolean eliminado = gestor.eliminarDispositivo(idDispositivo);

        if (eliminado) {
            gestor.guardarDispositivos();

            verificarYEliminarArchivos(tipoDispositivo);

            actualizarInterfaz();
            areaDispositivos.setText("Dispositivo eliminado: " + dispositivo.getNombre() + " (" + idDispositivo + ")");
        } else {
            areaDispositivos.setText("No se pudo eliminar el dispositivo.");
        }
    }

    /**
     * Metodo que se encarga de extraer el ID del dispositivo que ha sido selecionado.
     * @param seleccion
     * @return
     */
    private String extraerIdDeSeleccion(String seleccion) {
        int inicioId = seleccion.lastIndexOf("(");
        int finId = seleccion.lastIndexOf(")");

        if (inicioId != -1 && finId != -1) {
            return seleccion.substring(inicioId + 1, finId);
        }

        return "";
    }

    /**
     * Metedo que se encarga de verificar la cantidad de dispositivos de cada tipo,
     * en caso de no haber elimina la información que podría contener de los reportes.
     * @param tipoDispositivo
     */
    private void verificarYEliminarArchivos(String tipoDispositivo) {
        int smartwatches = 0;
        int pulseras = 0;
        int oximetros = 0;

        for (DispositivoWearable d : gestor.getDispositivos()) {
            if (d instanceof SmartWatch) smartwatches++;
            else if (d instanceof Pulsera) pulseras++;
            else if (d instanceof OximetroPulso) oximetros++;
        }

        if (smartwatches == 0) {
            if (pulseras == 0 && oximetros == 0) {
                new File("frecuencia_cardiaca.txt").delete();
            }
            if (pulseras == 0) {
                new File("actividad_fisica.txt").delete();
            }
            new File("sueño.txt").delete();
        }

        if (pulseras == 0) {
            if (smartwatches == 0 && oximetros == 0) {
                new File("frecuencia_cardiaca.txt").delete();
            }
            if (smartwatches == 0) {
                new File("actividad_fisica.txt").delete();
            }
        }

        if (oximetros == 0) {
            if (smartwatches == 0 && pulseras == 0) {
                new File("frecuencia_cardiaca.txt").delete();
            }
            new File("oxigeno.txt").delete();
        }
    }

    /**
     * Metodo que se encarga de limpiar el TextArea en donde se muestran los dispositivos
     */
    private void limpiarPantalla() {
        areaDispositivos.clear();
    }

    /**
     * Metodo que se encarga de mostrar los dispositivos que contiene el usuario.
     */
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

    /**
     * Metodo que se encarga de actualizar la cantidad de dispositivos y
     * y de actualizar la interfaz según los dispositivos nuevos.
     */
    private void actualizarInterfaz() {
        txtTituloDispositivos.setText("Dispositivos Vinculados (" + gestor.getCantidadDispositivos() + ")");

        actualizarChoiceBox();

        if (gestor.getCantidadDispositivos() == 0) {
            areaDispositivos.setText("No hay dispositivos vinculados.\n\n" +
                    "Use los botones de abajo para agregar dispositivos wearable.");
        } else {
            mostrarDispositivos();
        }
    }

    /**
     * Metodo que se encarga de actualizar el choicebox para la selección de los dispositivos.
     */
    private void actualizarChoiceBox() {
        chbDispositivos.getItems().clear();

        for (DispositivoWearable dispositivo : gestor.getDispositivos()) {
            String item = dispositivo.getTipoDispositivo() + " - " +
                    dispositivo.getNombre() + " (" +
                    dispositivo.getIdDispositivo() + ")";
            chbDispositivos.getItems().add(item);
        }

        if (!chbDispositivos.getItems().isEmpty()) {
            chbDispositivos.setValue(chbDispositivos.getItems().get(0));
        }
    }

    /**
     * Metodo que se encarga de pasar al siguiente menú dependiendo de cual se seleccione en el MenuButton.
     * @param nombreArchivo
     * @param titulo
     */
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

    /**
     * Metodo que se encarga de volver al menú de login para volver a iniciar sesión.
     */
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