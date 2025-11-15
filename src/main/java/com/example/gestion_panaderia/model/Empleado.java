package com.example.gestion_panaderia.model;

/**
 * Representa un empleado dentro del sistema de gestión de panadería.
 * Extiende la clase {@link Usuario} e implementa las interfaces {@link ISeller} y {@link IReportable},
 * lo que permite a los empleados registrar ventas y verificar reportes.
 *
 * Cada empleado tiene un rol específico dentro del sistema (ejemplo: "Vendedor", "Administrador").
 *
 * @author ¿?
 * @version ¿?
 */
public class Empleado extends Usuario implements ISeller, IReportable {

    /** Rol del empleado dentro de la panadería (ejemplo: "Vendedor", "Administrador") */
    private String rol;

    /** Constructor vacío requerido por frameworks y librerías de persistencia */
    public Empleado() { super(); }

    /**
     * Constructor que inicializa un empleado con todos sus datos.
     *
     * @param id Identificador único del empleado
     * @param nombre Nombre completo del empleado
     * @param usuario Nombre de usuario para autenticación
     * @param password Contraseña del empleado
     * @param rol Rol del empleado dentro de la panadería
     */
    public Empleado(String id, String nombre, String usuario, String password, String rol) {
        super(id, nombre, usuario, password);
        this.rol = rol;
    }

    /**
     * Obtiene el rol del empleado.
     * @return rol del empleado
     */
    public String getRol() { return rol; }

    /**
     * Establece el rol del empleado.
     * @param rol nuevo rol del empleado
     */
    public void setRol(String rol) { this.rol = rol; }

    /**
     * Registra una venta realizada por el empleado.
     * Implementación del método definido en {@link ISeller}.
     *
     * @param v Venta registrada
     */
    @Override
    public void registrarVenta(Venta v) {
        System.out.println("Venta registrada por " + getNombre() + ": " + v.getId());
    }

    /**
     * Verifica los reportes disponibles en el sistema.
     * Implementación del método definido en {@link IReportable}.
     */
    @Override
    public void verReportes() {
        System.out.println("Empleado " + this.getNombre() + " verifica reportes");
    }
}
