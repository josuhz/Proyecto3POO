package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorDispositivos {
    private static GestorDispositivos instancia;
    private List<DispositivoWearable> dispositivos;
    private static final String ARCHIVO_DISPOSITIVOS = "dispositivos.dat";
    private int contadorSmartWatch = 1;
    private int contadorPulsera = 1;
    private int contadorOximetro = 1;

    private GestorDispositivos() {
        this.dispositivos = new ArrayList<>();
        cargarDispositivos();
    }

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

    public void guardarDispositivos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_DISPOSITIVOS))) {
            oos.writeObject(dispositivos);
        } catch (IOException e) {
            System.err.println("Error al guardar dispositivos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarDispositivos() {
        File archivo = new File(ARCHIVO_DISPOSITIVOS);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(ARCHIVO_DISPOSITIVOS))) {
                dispositivos = (List<DispositivoWearable>) ois.readObject();

                actualizarContadores();

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar dispositivos: " + e.getMessage());
                dispositivos = new ArrayList<>();
            }
        }
    }

    private void actualizarContadores() {
        contadorSmartWatch = 0;
        contadorPulsera = 0;
        contadorOximetro = 0;

        for (DispositivoWearable d : dispositivos) {
            String id = d.getIdDispositivo();
            if (id.startsWith("SW-")) {
                int num = Integer.parseInt(id.substring(3));
                contadorSmartWatch = Math.max(contadorSmartWatch, num);
            } else if (id.startsWith("PL-")) {
                int num = Integer.parseInt(id.substring(3));
                contadorPulsera = Math.max(contadorPulsera, num);
            } else if (id.startsWith("OX-")) {
                int num = Integer.parseInt(id.substring(3));
                contadorOximetro = Math.max(contadorOximetro, num);
            }
        }

        contadorSmartWatch++;
        contadorPulsera++;
        contadorOximetro++;
    }

    public int getContadorSmartWatch() { return contadorSmartWatch; }
    public int getContadorPulsera() { return contadorPulsera; }
    public int getContadorOximetro() { return contadorOximetro; }

    public void setContadorSmartWatch(int valor) { this.contadorSmartWatch = valor; }
    public void setContadorPulsera(int valor) { this.contadorPulsera = valor; }
    public void setContadorOximetro(int valor) { this.contadorOximetro = valor; }

    public void limpiarTodo() {
        dispositivos.clear();
        guardarDispositivos();
    }
}