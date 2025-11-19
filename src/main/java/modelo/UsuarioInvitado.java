package modelo;

import java.util.ArrayList;
import java.util.List;

public class UsuarioInvitado {
    private String idInvitado;
    private String nombre;
    private String email;
    private String usuarioPrincipalId;
    private List<String> permisos;

    public UsuarioInvitado(String idInvitado, String nombre, String email, String usuarioPrincipalId) {
        this.idInvitado = idInvitado;
        this.nombre = nombre;
        this.email = email;
        this.usuarioPrincipalId = usuarioPrincipalId;
        this.permisos = new ArrayList<>();
    }

    public void agregarPermiso(String permiso) {
        if (!permisos.contains(permiso)) {
            permisos.add(permiso);
        }
    }

    public void removerPermiso(String permiso) {
        permisos.remove(permiso);
    }

    public boolean tienePermiso(String permiso) {
        return permisos.contains(permiso);
    }

    public String getIdInvitado() { return idInvitado; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public String getUsuarioPrincipalId() { return usuarioPrincipalId; }
    public List<String> getPermisos() { return new ArrayList<>(permisos); }
}
