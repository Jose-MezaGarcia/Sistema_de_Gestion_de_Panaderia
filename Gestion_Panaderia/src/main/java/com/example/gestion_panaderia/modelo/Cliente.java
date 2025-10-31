package com.example.gestion_panaderia.modelo;

public class Cliente extends Usuario{
    private String telefono;
    private String correo;

    public Cliente(String id, String nombre, String telefono, String correo) {
        super(id,nombre, null, null);
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }
    public String getCorreo(){
        return correo;
    }
}
