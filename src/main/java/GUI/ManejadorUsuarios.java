package GUI;

import org.mindrot.jbcrypt.BCrypt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManejadorUsuarios {

    /**
     * Verifica las credenciales del usuario comparando el hash BCrypt
     * @param correo Correo del usuario
     * @param contrasena Contraseña en texto ingresada por el usuario
     * @return true si las credenciales son correctas, false en caso contrario
     */
    public static boolean verificarCredenciales(String correo, String contrasena) {
        String[] usuario = leerUsuarioPrincipal();

        if (usuario != null && usuario.length >= 3) {
            String correoGuardado = usuario[1];
            String hashGuardado = usuario[2];

            // Verificar correo y contraseña usando BCrypt
            if (correoGuardado.equals(correo)) {
                try {
                    // BCrypt.checkpw compara la contraseña en texto con el hash
                    return BCrypt.checkpw(contrasena, hashGuardado);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error al verificar contraseña: " + e.getMessage());

                    return hashGuardado.equals(contrasena);
                }
            }
        }

        return false;
    }

    /**
     * Lee la información del usuario principal desde el archivo
     * @return Array con los datos del usuario [nombre, correo, hash_contraseña, edad, peso, altura, tipo]
     */
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

    /**
     * Obtiene todos los invitados del archivo
     * @return Lista de arrays con los datos de cada invitado
     */
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
}