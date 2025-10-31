package com.example.dulce_tentacion.Servicio;

import com.example.dulce_tentacion.Modelo.Usuario;

public interface IAutenServicio {

    /**
     * Valida las credenciales del usuario.
     *
     * @param usuario nombre de usuario.
     * @param contrasena contraseña ingresada.
     * @return el objeto Usuario autenticado si las credenciales son correctas, de lo contrario null.
     */
    Usuario autenticar(String usuario, String contrasena);

    /**
     * Cierra la sesión del usuario actual.
     * @param usuario el usuario que desea cerrar sesión.
     */
    void logout(Usuario usuario);
}
