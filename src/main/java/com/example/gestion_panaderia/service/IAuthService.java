package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Usuario;

public interface IAuthService {
    Usuario autenticar(String usuario, String password);
    void logout(Usuario u);
}
