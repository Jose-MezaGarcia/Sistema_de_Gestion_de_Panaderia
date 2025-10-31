package com.example.gestion_panaderia.modelo;

public class Empleado extends Usuario implements IReportable, ISeller{
    private String rol;

    public Empleado(String id, String nombre, String usuario, String contraseña, String rol){
        super(id, nombre, usuario, contraseña);
        return v;
    }

    @Override
    public Venta registrarVenta(Venta v){
        System.out.println("Empleado " + this.getNombre() + "(Rol: " + this.rol + ")registra una venta.");
        return v;
    }

    @Override
    public void verReportes(){
        System.out.println("Empleado" + this.getNombre() + "verifica reportes");
    }

    public String getRol() {
        return rol;
    }
}
