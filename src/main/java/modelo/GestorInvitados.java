package modelo;

import java.util.ArrayList;
import java.util.List;

public class GestorInvitados {
    private List<UsuarioInvitado> invitados;

    public GestorInvitados() {
        this.invitados = new ArrayList<>();
    }

    public boolean agregarInvitado(UsuarioInvitado invitado) {
        if (invitado != null && !existeInvitado(invitado.getEmail())) {
            invitados.add(invitado);
            return true;
        }
        return false;
    }

    public boolean removerInvitado(String email) {
        return invitados.removeIf(inv -> inv.getEmail().equals(email));
    }

    public boolean tienePermiso(String emailInvitado, String permiso) {
        UsuarioInvitado invitado = buscarInvitado(emailInvitado);
        if (invitado != null) {
            return invitado.tienePermiso(permiso);
        }
        return false;
    }

    private UsuarioInvitado buscarInvitado(String email) {
        for (UsuarioInvitado inv : invitados) {
            if (inv.getEmail().equals(email)) {
                return inv;
            }
        }
        return null;
    }

    private boolean existeInvitado(String email) {
        return buscarInvitado(email) != null;
    }

    public List<UsuarioInvitado> getInvitados() {
        return new ArrayList<>(invitados);
    }

    public int getCantidadInvitados() {
        return invitados.size();
    }
}
