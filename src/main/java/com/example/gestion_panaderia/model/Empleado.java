package com.example.gestion_panaderia.model;

/**
 * Clase que representa a un empleado
 * Aquí se guarda su rol y se definen acciones como registrar ventas y ver reportes
 */
public class Empleado extends Usuario implements ISeller, IReportable {
    private String rol;

    /**
     * Se crea un empleado vacío
     */
    public Empleado() {
        super();
    }

    /**
     * Se crea un empleado con datos básicos y rol
     */
    public Empleado(String id, String nombre, String usuario, String password, String rol) {
        super(id, nombre, usuario, password);
        this.rol = rol;
    }

    /**
     * Se obtiene el rol del empleado
     */
    public String getRol() {
        return rol;
    }

    /**
     * Se cambia el rol del empleado
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Se registra una venta hecha por el empleado
     * Se imprime en consola el id de la venta
     */
    @Override
    public void registrarVenta(Venta v) {
        System.out.println("Venta registrada por " + getNombre() + ": " + v.getId());
    }

    /**
     * Se muestran los reportes que el empleado consulta
     * Se imprime en consola la acción
     */
    @Override
    public void verReportes() {
        System.out.println("Empleado " + this.getNombre() + " verifica reportes");
    }
}

