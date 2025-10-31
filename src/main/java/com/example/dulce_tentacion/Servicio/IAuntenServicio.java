package com.example.gestion_panaderia.Servicio;

import com.example.gestion_panaderia.modelo.Usuario;

/**
 * Define operaciones de autenticación de usuario.
 */
public interface IAutenServicio {

    Usuario autenticar(String usuario, String contraseña);

    void logout(Usuario usuario);
}
