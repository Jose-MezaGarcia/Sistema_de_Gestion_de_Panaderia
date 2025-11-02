package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Usuario;
import com.example.gestion_panaderia.repository.IRepository;

import java.util.List;

public class AuthServiceImpl implements IAuthService {
    
    private IRepository<Usuario> userRepo;
    
    public AuthServiceImpl(IRepository<Usuario> userRepo) {
        this.userRepo = userRepo;
    }
    
    @Override
    public Usuario autenticar(String usuario, String password) {
        List<Usuario> usuarios = userRepo.cargar();
        
        for (Usuario u : usuarios) {
            if (u.getUsuario().equals(usuario) && u.getPassword().equals(password)) {
                return u;
            }
        }
        
        return null;
    }
    
    @Override
    public void logout(Usuario u) {
        System.out.println("Usuario " + u.getUsuario() + " ha cerrado sesión");
    }
}
