package com.example.dulce_tentacion.Servicio;

import com.example.dulce_tentacion.Modelo.Usuario;
import com.example.dulce_tentacion.Repositorio.IRepositorio;

import java.util.List;

/**
 * Implementación del servicio de autenticación.
 * Depende del repositorio de usuarios (IRepositorio<Usuario>).
 */

public class AutenServicio implements IAutenServicio {

    private final IRepositorio<Usuario> userRepo; // Depende del paquete Repositorio

    public AutenServicio(IRepositorio<Usuario> userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Usuario autenticar(String usuario, String contrasena) {
        // Implementar búsqueda real en userRepo cuando Usuario esté disponible
        return null;
    }

    @Override
    public void logout(Usuario usuario) {
        // Implementar lógica para cierre de sesión
    }
}
