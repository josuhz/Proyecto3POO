package modelo;

import java.util.ArrayList;
import java.util.List;

public class GestorInvitados {
    private List<UsuarioInvitado> invitados;

    /**
     * Constructor que inicializa la lista de invitados vacía.
     */
    public GestorInvitados() {
        this.invitados = new ArrayList<>();
    }

    /**
     * Agrega un nuevo invitado a la lista si no existe.
     * @param invitado
     * @return
     */
    public boolean agregarInvitado(UsuarioInvitado invitado) {
        if (invitado != null && !existeInvitado(invitado.getEmail())) {
            invitados.add(invitado);
            return true;
        }
        return false;
    }

    /**
     * Elimina un invitado de la lista por su email.
     * @param email
     * @return
     */
    public boolean removerInvitado(String email) {
        return invitados.removeIf(inv -> inv.getEmail().equals(email));
    }

    /**
     * Verifica si un invitado tiene un permiso específico.
     * @param emailInvitado
     * @param permiso
     * @return
     */
    public boolean tienePermiso(String emailInvitado, String permiso) {
        UsuarioInvitado invitado = buscarInvitado(emailInvitado);
        if (invitado != null) {
            return invitado.tienePermiso(permiso);
        }
        return false;
    }

    /**
     * Busca un invitado por su email.
     * @param email
     * @return
     */
    private UsuarioInvitado buscarInvitado(String email) {
        for (UsuarioInvitado inv : invitados) {
            if (inv.getEmail().equals(email)) {
                return inv;
            }
        }
        return null;
    }

    /**
     * Verifica si un invitado con el email dado ya existe.
     * @param email
     * @return
     */
    private boolean existeInvitado(String email) {
        return buscarInvitado(email) != null;
    }

    /**
     * Obtiene una copia de la lista de invitados.
     * @return
     */
    public List<UsuarioInvitado> getInvitados() {
        return new ArrayList<>(invitados);
    }

    /**
     * Obtiene la cantidad total de invitados registrados.
     * @return
     */
    public int getCantidadInvitados() {
        return invitados.size();
    }
}