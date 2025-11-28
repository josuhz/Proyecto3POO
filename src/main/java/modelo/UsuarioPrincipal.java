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

    public UsuarioPrincipal(String idUsuario, String nombre, String email, String password) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;

        this.gestorInvitados = new GestorInvitados();
        this.historicoMetricas = new ArrayList<>();
    }

    public void sincronizarDispositivos() {
        List<Metrica> nuevasMetricas = GestorDispositivos.getInstancia().sincronizarTodos(this.idUsuario);
        historicoMetricas.addAll(nuevasMetricas);
    }

    public List<Metrica> obtenerMetricasRecientes(int cantidad) {
        int inicio = Math.max(0, historicoMetricas.size() - cantidad);
        return new ArrayList<>(historicoMetricas.subList(inicio, historicoMetricas.size()));
    }

    public String getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    // Modificar para usar el Singleton
    public GestorDispositivos getGestorDispositivos() {
        return GestorDispositivos.getInstancia();
    }

    public GestorInvitados getGestorInvitados() { return gestorInvitados; }
    public List<Metrica> getHistoricoMetricas() { return new ArrayList<>(historicoMetricas); }

    public void agregarMetrica(Metrica metrica) {
        if (metrica != null) {
            historicoMetricas.add(metrica);
        }
    }
}