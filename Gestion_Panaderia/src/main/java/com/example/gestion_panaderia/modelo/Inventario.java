package com.example.gestion_panaderia.modelo;

public class Inventario {
    private List<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    public Producto buscarPorNombre(String nombre){
       for(int i = 0; i < productos.size(); i++){
           producto p = productos.get(i)
           if(p.getNombre().equalsIgnoreCase(nombre)){
               return p;
           }
       }
       return null;
    }

    public void agregarProducto(Producto p){
        this.productos.add(p);
    }
    public void eliminarProducto(String idProducto){
        for (int i = 0; i < productos.size(); i++){
            producto p = productos.get(i);
            if (p.getId().equals(idProducto)){
                productos.remove(i);
                return true;
            }
        }
        return false;

    public boolean actualizarStock(String idProducto, int cantidad){
        Producto productoEncontrado = null;

        for(int i = 0; i < productos.size(); i++){
            Producto p = productos.get(i);
            if (p.getId().equals(idProducto)){
                productoEncontrado = p;
                break;
            }
        }

        if(productoEncontrado != null){
            return true;
        }
        return false;
    }

    public void agregarProducto(Producto p){
        this.productos.add(p);
    }

    public ArrayList<Producto> getProductos(){
        retrun productos;
        }
}
