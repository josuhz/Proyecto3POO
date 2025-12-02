package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un usuario invitado con permisos limitados.
 * Los invitados pueden acceder a ciertos datos del usuario principal
 * según los permisos que se les hayan otorgado.
 */
public class UsuarioInvitado {
    private String idInvitado;
    private String nombre;
    private String email;
    private String usuarioPrincipalId;
    private List<String> permisos;

    /**
     * Constructor para crear un usuario invitado.
     * @param idInvitado ID único del invitado
     * @param nombre Nombre del invitado
     * @param email Email del invitado
     * @param usuarioPrincipalId ID del usuario principal que lo invita
     */
    public UsuarioInvitado(String idInvitado, String nombre, String email, String usuarioPrincipalId) {
        this.idInvitado = idInvitado;
        this.nombre = nombre;
        this.email = email;
        this.usuarioPrincipalId = usuarioPrincipalId;
        this.permisos = new ArrayList<>();
    }

    /**
     * Agrega un permiso al invitado si no lo tiene.
     * @param permiso Nombre del permiso a agregar
     */
    public void agregarPermiso(String permiso) {
        if (!permisos.contains(permiso)) {
            permisos.add(permiso);
        }
    }

    /**
     * Remueve un permiso del invitado.
     * @param permiso Nombre del permiso a remover
     */
    public void removerPermiso(String permiso) {
        permisos.remove(permiso);
    }

    /**
     * Verifica si el invitado tiene un permiso específico.
     * @param permiso Nombre del permiso a verificar
     * @return true si tiene el permiso, false en caso contrario
     */
    public boolean tienePermiso(String permiso) {
        return permisos.contains(permiso);
    }

    /**
     * Obtiene el ID del invitado.
     * @return ID del invitado
     */
    public String getIdInvitado() {
        return idInvitado;
    }

    /**
     * Obtiene el nombre del invitado.
     * @return Nombre del invitado
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del invitado.
     * @param nombre Nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el email del invitado.
     * @return Email del invitado
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtiene el ID del usuario principal.
     * @return ID del usuario principal
     */
    public String getUsuarioPrincipalId() {
        return usuarioPrincipalId;
    }

    /**
     * Obtiene una copia de la lista de permisos.
     * @return Nueva lista con todos los permisos
     */
    public List<String> getPermisos() {
        return new ArrayList<>(permisos);
    }
}