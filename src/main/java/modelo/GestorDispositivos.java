package modelo;

import java.util.ArrayList;
import java.util.List;

public class GestorDispositivos {
    private static GestorDispositivos instancia;
    private List<DispositivoWearable> dispositivos;

    // Constructor privado para evitar múltiples instancias
    private GestorDispositivos() {
        this.dispositivos = new ArrayList<>();
    }

    // Método para obtener la única instancia
    public static GestorDispositivos getInstancia() {
        if (instancia == null) {
            instancia = new GestorDispositivos();
        }
        return instancia;
    }

    public boolean agregarDispositivo(DispositivoWearable dispositivo) {
        if (dispositivo != null && !existeDispositivo(dispositivo.getIdDispositivo())) {
            dispositivos.add(dispositivo);
            return true;
        }
        return false;
    }

    public boolean eliminarDispositivo(String idDispositivo) {
        return dispositivos.removeIf(d -> d.getIdDispositivo().equals(idDispositivo));
    }

    public List<Metrica> sincronizarTodos(String usuarioId) {
        List<Metrica> todasMetricas = new ArrayList<>();
        for (DispositivoWearable dispositivo : dispositivos) {
            if (dispositivo.isActivo()) {
                todasMetricas.addAll(dispositivo.sincronizarDatos(usuarioId));
            }
        }
        return todasMetricas;
    }

    private boolean existeDispositivo(String idDispositivo) {
        for (DispositivoWearable d : dispositivos) {
            if (d.getIdDispositivo().equals(idDispositivo)) {
                return true;
            }
        }
        return false;
    }

    public List<DispositivoWearable> getDispositivos() {
        return new ArrayList<>(dispositivos);
    }

    public int getCantidadDispositivos() {
        return dispositivos.size();
    }

    public DispositivoWearable buscarDispositivo(String idDispositivo) {
        for (DispositivoWearable d : dispositivos) {
            if (d.getIdDispositivo().equals(idDispositivo)) {
                return d;
            }
        }
        return null;
    }

    // Método para limpiar todos los datos (útil para logout)
    public void limpiarTodo() {
        dispositivos.clear();
    }
}