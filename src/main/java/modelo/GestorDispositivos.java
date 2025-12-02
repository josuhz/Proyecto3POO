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

    /**
     * Constructor privado para implementar el patrón Singleton.
     * Inicializa la lista de dispositivos y carga los datos guardados.
     */
    private GestorDispositivos() {
        this.dispositivos = new ArrayList<>();
        cargarDispositivos();
    }

    /**
     * Obtiene la instancia única del gestor de dispositivos.
     * @return
     */
    public static GestorDispositivos getInstancia() {
        if (instancia == null) {
            instancia = new GestorDispositivos();
        }
        return instancia;
    }

    /**
     * Agrega un nuevo dispositivo a la lista si no existe.
     * @param dispositivo
     * @return
     */
    public boolean agregarDispositivo(DispositivoWearable dispositivo) {
        if (dispositivo != null && !existeDispositivo(dispositivo.getIdDispositivo())) {
            dispositivos.add(dispositivo);
            return true;
        }
        return false;
    }

    /**
     * Elimina un dispositivo de la lista por su ID.
     * @param idDispositivo
     * @return
     */
    public boolean eliminarDispositivo(String idDispositivo) {
        return dispositivos.removeIf(d -> d.getIdDispositivo().equals(idDispositivo));
    }

    /**
     * Sincroniza los datos de todos los dispositivos activos.
     * Solo sincroniza dispositivos que estén marcados como activos.
     * @param usuarioId
     * @return
     */
    public List<Metrica> sincronizarTodos(String usuarioId) {
        List<Metrica> todasMetricas = new ArrayList<>();
        for (DispositivoWearable dispositivo : dispositivos) {
            if (dispositivo.isActivo()) {
                todasMetricas.addAll(dispositivo.sincronizarDatos(usuarioId));
            }
        }
        return todasMetricas;
    }

    /**
     * Verifica si un dispositivo con el ID dado ya existe.
     * @param idDispositivo
     * @return
     */
    private boolean existeDispositivo(String idDispositivo) {
        for (DispositivoWearable d : dispositivos) {
            if (d.getIdDispositivo().equals(idDispositivo)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene una copia de la lista de dispositivos.
     * @return
     */
    public List<DispositivoWearable> getDispositivos() {
        return new ArrayList<>(dispositivos);
    }

    /**
     * Obtiene la cantidad total de dispositivos registrados.
     * @return
     */
    public int getCantidadDispositivos() {
        return dispositivos.size();
    }

    /**
     * Busca un dispositivo específico por su ID.
     * @param idDispositivo
     * @return
     */
    public DispositivoWearable buscarDispositivo(String idDispositivo) {
        for (DispositivoWearable d : dispositivos) {
            if (d.getIdDispositivo().equals(idDispositivo)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Guarda todos los dispositivos en un archivo binario.
     * Serializa la lista completa de dispositivos en dispositivos.dat
     */
    public void guardarDispositivos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_DISPOSITIVOS))) {
            oos.writeObject(dispositivos);
        } catch (IOException e) {
            System.err.println("Error al guardar dispositivos: " + e.getMessage());
        }
    }

    /**
     * Carga los dispositivos desde el archivo.
     */
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

    /**
     * Actualiza los contadores de IDs basándose en los dispositivos existentes.
     */
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

    /**
     * Obtiene el contador actual de SmartWatch.
     * @return
     */
    public int getContadorSmartWatch() {
        return contadorSmartWatch;
    }

    /**
     * Obtiene el contador actual de Pulsera.
     * @return
     */
    public int getContadorPulsera() {
        return contadorPulsera;
    }

    /**
     * Obtiene el contador actual de Oxímetro.
     * @return
     */
    public int getContadorOximetro() {
        return contadorOximetro;
    }

    /**
     * Establece manualmente el contador de SmartWatch.
     * @param valor
     */
    public void setContadorSmartWatch(int valor) {
        this.contadorSmartWatch = valor;
    }

    /**
     * Establece manualmente el contador de Pulsera.
     * @param valor
     */
    public void setContadorPulsera(int valor) {
        this.contadorPulsera = valor;
    }

    /**
     * Establece manualmente el contador de Oxímetro.
     * @param valor
     */
    public void setContadorOximetro(int valor) {
        this.contadorOximetro = valor;
    }

    /**
     * Elimina todos los dispositivos y guarda el estado vacío.
     * Limpia completamente la lista de dispositivos.
     */
    public void limpiarTodo() {
        dispositivos.clear();
        guardarDispositivos();
    }
}