package modelo;

import java.util.ArrayList;
import java.util.List;
public class UsuarioPrincipal {
    private String idUsuario;
    private String nombre;
    private String email;
    private String password;
    private int edad;
    private double peso;
    private double altura;

    private GestorInvitados gestorInvitados;
    private List<Metrica> historicoMetricas;

    /**
     * Constructor para crear un usuario principal.
     * Inicializa el gestor de invitados y el histórico de métricas vacíos.
     * @param idUsuario
     * @param nombre
     * @param email
     * @param password
     */
    public UsuarioPrincipal(String idUsuario, String nombre, String email, String password) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;

        this.gestorInvitados = new GestorInvitados();
        this.historicoMetricas = new ArrayList<>();
    }

    /**
     * Sincroniza todos los dispositivos wearables vinculados.
     * Obtiene las métricas de todos los dispositivos activos.
     */
    public void sincronizarDispositivos() {
        List<Metrica> nuevasMetricas = GestorDispositivos.getInstancia().sincronizarTodos(this.idUsuario);
        historicoMetricas.addAll(nuevasMetricas);
    }

    /**
     * Obtiene las métricas más recientes del histórico.
     * @param cantidad
     * @return
     */
    public List<Metrica> obtenerMetricasRecientes(int cantidad) {
        int inicio = Math.max(0, historicoMetricas.size() - cantidad);
        return new ArrayList<>(historicoMetricas.subList(inicio, historicoMetricas.size()));
    }

    //getters y setters.
    /**
     * Obtiene el ID del usuario.
     * @return
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     * Obtiene el nombre del usuario.
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el email del usuario.
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del usuario.
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene la edad del usuario.
     * @return
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Establece la edad del usuario.
     * @param edad
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Obtiene el peso del usuario.
     * @return
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Establece el peso del usuario.
     * @param peso
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * Obtiene la altura del usuario.
     * @return
     */
    public double getAltura() {
        return altura;
    }

    /**
     * Establece la altura del usuario.
     * @param altura
     */
    public void setAltura(double altura) {
        this.altura = altura;
    }

    /**
     * Obtiene el gestor de dispositivos singleton.
     * @return
     */
    public GestorDispositivos getGestorDispositivos() {
        return GestorDispositivos.getInstancia();
    }

    /**
     * Obtiene el gestor de invitados.
     * @return
     */
    public GestorInvitados getGestorInvitados() {
        return gestorInvitados;
    }

    /**
     * Obtiene una copia del histórico de métricas.
     * @return
     */
    public List<Metrica> getHistoricoMetricas() {
        return new ArrayList<>(historicoMetricas);
    }

    /**
     * Agrega una métrica al histórico del usuario.
     * @param metrica
     */
    public void agregarMetrica(Metrica metrica) {
        if (metrica != null) {
            historicoMetricas.add(metrica);
        }
    }
}