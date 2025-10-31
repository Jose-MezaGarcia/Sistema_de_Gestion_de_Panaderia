package com.example.gestion_panaderia.modelo;

public class Administrador extends Empleado {

    public Administrador(String id, String nombre, String usuario, String contraseña){
        super(id, nombre, usuario, contraseña, "Administrador");
    }

    public void verReportes(){
        System.out.println("Administrador " + this.getNombre() + " Accede a reportes");
    }

}
