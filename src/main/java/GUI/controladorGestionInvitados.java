package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class controladorGestionInvitados {

    @FXML
    private ListView<String> listInvitados;

    @FXML
    private SplitMenuButton splInvitados;

    @FXML
    private CheckBox chkFrecCardiaca;

    @FXML
    private CheckBox chkSueno;

    @FXML
    private CheckBox chkActividad;

    @FXML
    private CheckBox chkOxigeno;

    @FXML
    private Button btnModificarPermisos;

    @FXML
    private Button btnRevocarAcceso;

    @FXML
    private MenuButton menuNavegacion;

    @FXML
    private MenuItem menuDashboard;

    @FXML
    private MenuItem menuDispositivos;

    @FXML
    private MenuItem menuGestionInvitados;

    @FXML
    private MenuItem menuRangos;

    @FXML
    private MenuItem menuCerrarSesion;

    private Map<String, String[]> datosInvitados; // email -> [nombre, fecha, permisos]
    private String invitadoSeleccionado;

    /**
     * Metodo que se ejecuta automáticamente al cargar menú de la gestión de invitados en el FXML
     */
    @FXML
    public void initialize() {
        menuDashboard.setOnAction(event -> irAVentana("Dashboard.fxml", "Dashboard"));
        menuDispositivos.setOnAction(event -> irAVentana("menuDispositivos.fxml", "Mis Dispositivos"));
        menuGestionInvitados.setOnAction(event -> irAVentana("menuGestionInvitados.fxml", "Gestión de Invitados"));
        menuRangos.setOnAction(event -> irAVentana("menuRangos.fxml", "Rangos"));
        menuCerrarSesion.setOnAction(event -> cerrarSesion());

        btnModificarPermisos.setOnAction(event -> modificarPermisos());
        btnRevocarAcceso.setOnAction(event -> revocarAcceso());

        // Inicializar datos
        datosInvitados = new HashMap<>();

        // Cargar invitados
        cargarInvitados();

        // Configurar listeners
        configurarListeners();
    }

    /**
     * Metodo que se encarga de cargar los invitados que se han registrado para su visualización.
     */
    private void cargarInvitados() {
        try {
            List<String[]> invitados = ManejadorUsuarios.leerInvitados();
            ObservableList<String> items = FXCollections.observableArrayList();
            ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();

            for (String[] invitado : invitados) {
                if (invitado.length >= 4) {
                    String nombre = invitado[0].trim();
                    String email = invitado[1].trim();
                    String fecha = invitado[3].trim();

                    // Por defecto, todos los permisos activos
                    String permisos = "111";
                    if (invitado.length > 4) {
                        permisos = invitado[4].trim();
                    }

                    // Guardar datos
                    datosInvitados.put(email, new String[]{nombre, fecha, permisos});

                    // Agregar a ListView
                    String itemLista = nombre + " - " + email + " (Desde: " + fecha + ")";
                    items.add(itemLista);

                    // Agregar a SplitMenuButton
                    MenuItem menuItem = new MenuItem(nombre + " - " + email);
                    menuItem.setOnAction(event -> {
                        splInvitados.setText(menuItem.getText());
                        invitadoSeleccionado = email;
                        cargarPermisosInvitado(email);
                    });
                    menuItems.add(menuItem);
                }
            }

            listInvitados.setItems(items);
            splInvitados.getItems().setAll(menuItems);

            // Actualizar título con cantidad
            actualizarTituloSeccion(items.size());

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar los invitados: " + e.getMessage());
        }
    }

    /**
     * Metodo que se encarga de cargar y mostrar los permisos del invitado seleccionado.
     * @param email
     */
    private void cargarPermisosInvitado(String email) {
        if (datosInvitados.containsKey(email)) {
            String[] datos = datosInvitados.get(email);
            String permisos = datos[2];

            // permisos es un string de 4 caracteres: "1010" donde:
            // posición 0: frecuencia cardíaca (1=detallado, 0=básico)
            // posición 1: sueño (1=detallado, 0=básico)
            // posición 2: actividad (1=detallado, 0=básico)
            // posición 3: oxígeno (1=detallado, 0=básico)

            if (permisos.length() >= 4) {
                chkFrecCardiaca.setSelected(permisos.charAt(0) == '1');
                chkSueno.setSelected(permisos.charAt(1) == '1');
                chkActividad.setSelected(permisos.charAt(2) == '1');
                chkOxigeno.setSelected(permisos.charAt(3) == '1');
            } else {
                // Si no hay permisos definidos, todos en basico
                chkFrecCardiaca.setSelected(true);
                chkSueno.setSelected(true);
                chkActividad.setSelected(true);
                chkOxigeno.setSelected(false);
            }
        }
    }

    /**
     *
     */
    private void configurarListeners() {
        listInvitados.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Extraer email del item seleccionado
                String email = extraerEmailDeItem(newVal);
                if (email != null) {
                    invitadoSeleccionado = email;
                    cargarPermisosInvitado(email);

                    // Actualizar SplitMenuButton también
                    String[] datos = datosInvitados.get(email);
                    splInvitados.setText(datos[0] + " - " + email);
                }
            }
        });
    }

    /**
     * Metodo que se encarga de extraer el email del invitado que ha sido seleccionado.
     * @param item
     * @return
     */
    private String extraerEmailDeItem(String item) {
        // El formato es: "Nombre - email (Desde: fecha)"
        int start = item.indexOf("-") + 2;
        int end = item.indexOf("(Desde:") - 1;
        if (start > 1 && end > start) {
            return item.substring(start, end).trim();
        }
        return null;
    }

    /**
     * Metodo que se encarga de modificar los permisos que contiene un invitado.
     */
    private void modificarPermisos() {
        if (invitadoSeleccionado == null) {
            mostrarAlerta("Advertencia", "Por favor selecciona un invitado primero");
            return;
        }

        try {
            // Construir string de permisos
            String permisos =
                    (chkFrecCardiaca.isSelected() ? "1" : "0") +
                            (chkSueno.isSelected() ? "1" : "0") +
                            (chkActividad.isSelected() ? "1" : "0") +
                            (chkOxigeno.isSelected() ? "1" : "0");

            String[] datos = datosInvitados.get(invitadoSeleccionado);
            datos[2] = permisos;

            // Guardar en archivo
            guardarInvitadosEnArchivo();

            mostrarAlerta("Éxito", "Permisos actualizados correctamente para " + datos[0]);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron actualizar los permisos: " + e.getMessage());
        }
    }

    /**
     * Metodo que se encarga de guardar los datos de los invitados en un archivo.
     * @throws IOException
     */
    private void guardarInvitadosEnArchivo() throws IOException {
        File archivo = new File("usuarios_invitados.txt");
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            for (Map.Entry<String, String[]> entry : datosInvitados.entrySet()) {
                String email = entry.getKey();
                String[] datos = entry.getValue();
                // Formato: nombre,email,INVITADO,fecha,permisos
                writer.println(datos[0] + "," + email + ",INVITADO," + datos[1] + "," + datos[2]);
            }
        }
    }

    /**
     * Metodo que se encarga de revocar el acceso de el invitado seleccionado y por lo tanto eliminarlo.
     */
    private void revocarAcceso() {
        if (invitadoSeleccionado == null) {
            mostrarAlerta("Advertencia", "Por favor selecciona un invitado primero");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar revocación");
        alert.setHeaderText("Revocar acceso");
        alert.setContentText("¿Estás seguro de que quieres revocar el acceso a " +
                datosInvitados.get(invitadoSeleccionado)[0] + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Remover de memoria
                datosInvitados.remove(invitadoSeleccionado);

                // Guardar cambios
                guardarInvitadosEnArchivo();

                // Recargar lista
                cargarInvitados();

                // Limpiar selección
                invitadoSeleccionado = null;
                splInvitados.setText("Seleccionar invitado");
                chkFrecCardiaca.setSelected(false);
                chkSueno.setSelected(false);
                chkActividad.setSelected(false);

                mostrarAlerta("Éxito", "Acceso revocado correctamente");

            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo revocar el acceso: " + e.getMessage());
            }
        }
    }

    /**
     * Metodo que se encarga de actualizar el mensaje de los invitados que fueron cargados.
     * @param cantidad
     */
    private void actualizarTituloSeccion(int cantidad) {
        // Buscar y actualizar el título de la sección
        // Podrías agregar un fx:id al Text en el FXML para esto
        System.out.println("Invitados cargados: " + cantidad);
    }

    /**
     * Metodo que se encarga de preparar y mostrar una alerta depiendo de la acción realizada.
     * @param titulo
     * @param mensaje
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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

            if (!fxmlFile.exists()) {
                System.out.println("Error: No se encontró el archivo " + nombreArchivo);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            Stage stage = (Stage) menuNavegacion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar " + nombreArchivo + ": " + e.getMessage());
        }
    }

    /**
     * Metodo que se encarga de volver al menú de login para volver a iniciar sesión.
     */
    private void cerrarSesion() {
        try {
            String userDir = System.getProperty("user.dir");
            File fxmlFile = new File(userDir, "src/main/java/GUI/menuLogin.fxml");

            if (!fxmlFile.exists()) {
                System.out.println("Error: No se encontró menuLogin.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            Stage stage = (Stage) menuNavegacion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Inicio de Sesión");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cerrar sesión: " + e.getMessage());
        }
    }
}