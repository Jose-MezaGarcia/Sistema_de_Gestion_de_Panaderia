package com.example.gestion_panaderia.model;

public class Empleado extends Usuario implements ISeller, IReportable {
    private String rol;
    
    public Empleado() { super(); }
    
    public Empleado(String id, String nombre, String usuario, String password, String rol) {
        super(id, nombre, usuario, password);
        this.rol = rol;
    }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    @Override
    public void registrarVenta(Venta v) {
        System.out.println("Venta registrada por " + getNombre() + ": " + v.getId());
    }
    
    @Override
    public void verReportes() {
        System.out.println("Empleado" + this.getNombre() + "verifica reportes");
    }


}
