package com.example.gestion_panaderia.Servicio;

import com.example.gestion_panaderia.Repositorio.ILeerRepo;
import com.example.gestion_panaderia.modelo.Usuario;
import java.util.List;

/**
 * Implementa la autenticación de usuarios usando el repositorio.
 */
public class AutenServicio implements IAutenServicio {

    private final ILeerRepo<Usuario> usuarioRepo;

    public AutenServicio(ILeerRepo<Usuario> usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public Usuario autenticar(String usuario, String contraseña) {
        List<Usuario> usuarios = usuarioRepo.leer();

        if (usuarios == null || usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return null;
        }

        for (Usuario u : usuarios) {
            if (u.getUsuario().equals(usuario) && u.getContraseña().equals(contraseña)) {
                System.out.println("Inicio de sesión correcto: " + u.getNombre());
                return u;
            }
        }

        System.out.println("Credenciales incorrectas.");
        return null;
    }

    @Override
    public void logout(Usuario usuario) {
        if (usuario != null) {
            System.out.println("Usuario " + usuario.getNombre() + " ha cerrado sesión.");
        }
    }
}
