package GUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManejadorUsuarios {

    // Método para verificar credenciales de login
    public static boolean verificarCredenciales(String correo, String contrasena) {
        String[] usuario = leerUsuarioPrincipal();
        if (usuario != null && usuario.length >= 3) {
            return usuario[1].equals(correo) && usuario[2].equals(contrasena);
        }
        return false;
    }

    // Método para obtener información del usuario principal
    public static String[] leerUsuarioPrincipal() {
        try {
            File archivo = new File("usuario_principal.txt");
            if (!archivo.exists()) {
                return null;
            }

            Scanner scanner = new Scanner(archivo);
            if (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                scanner.close();
                return linea.split(",");
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para obtener todos los invitados
    public static List<String[]> leerInvitados() {
        List<String[]> invitados = new ArrayList<>();
        try {
            File archivo = new File("usuarios_invitados.txt");
            if (!archivo.exists()) {
                return invitados;
            }

            Scanner scanner = new Scanner(archivo);
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

    // Método para obtener nombre del usuario actual
    public static String obtenerNombreUsuario() {
        String[] usuario = leerUsuarioPrincipal();
        if (usuario != null && usuario.length > 0) {
            return usuario[0];
        }
        return "Usuario";
    }
}