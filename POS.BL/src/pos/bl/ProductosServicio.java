/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.bl;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class ProductosServicio {
    private final ArrayList<Producto> listadeProductos;

    public ProductosServicio() {
        listadeProductos = new ArrayList<>();
        
        crearDatosdePrueba();
    }

    public ArrayList<Producto> obtenerProductos() {
        return listadeProductos;
    }

    public ArrayList<Producto> obtenerProductos(String buscar) {
        
        if (buscar == null && buscar.equals("")) {
            return listadeProductos;
        }
        
        String buscarMinuscula = buscar.toLowerCase();
        ArrayList<Producto> resultado = new ArrayList<>();
        
        listadeProductos.forEach(producto -> {
            if (producto.getDescripcion().toLowerCase().contains(buscar) == true) {
                resultado.add(producto);
            }
        });
        
        return resultado;
    }
    
    public String guardar(Producto producto) { 
        String resultado = validarProducto(producto);
        
        if (resultado.equals("")) {                   

            if (producto.getId().equals(0)) {
                Integer id = obtenerSiguienteId();

                producto.setId(id);

                listadeProductos.add(producto);
            } else {
                listadeProductos.forEach(productoExistente -> {
                    if (productoExistente.getId().equals(producto.getId())) {
                        productoExistente.setDescripcion(producto.getDescripcion());
                        productoExistente.setCategoria(producto.getCategoria());
                        productoExistente.setPrecio(producto.getPrecio());
                        productoExistente.setExistencia(producto.getExistencia());
                        productoExistente.setActivo(producto.getActivo());
                    }            
                });
            }
            return "";
        } 
        
        return resultado;
    }
    
    public void eliminar(Producto producto) {
        listadeProductos.remove(producto);
    }
    
    private void crearDatosdePrueba() {
        Categoria categoria1 = new Categoria("Celulares");
        categoria1.setId(1);
        
        Producto producto1 = new Producto();
        producto1.setId(1);
        producto1.setDescripcion("iPhone X");
        producto1.setCategoria(categoria1);
        producto1.setPrecio(20000.00);
        producto1.setExistencia(10);

        Producto producto2 = new Producto();
        producto2.setId(2);
        producto2.setDescripcion("Samsung Galaxy S10");
        producto2.setCategoria(categoria1);
        producto2.setPrecio(10000.00);
        producto2.setExistencia(5);
        
        listadeProductos.add(producto1);
        listadeProductos.add(producto2);
    }   

    private Integer obtenerSiguienteId() {
        Integer maxId = 1;
        for(Producto producto: listadeProductos) {
            if (producto.getId() >= maxId) {
                maxId = producto.getId() + 1;
            }
        }
        return maxId;
    }

    private String validarProducto(Producto producto) {
        if (producto.getDescripcion() == null || 
                producto.getDescripcion().equals("")) {
            return "Ingrese la descripción";
        }
        if (producto.getCategoria() == null) {
            return "Seleccione una categoria";
        }
        if (producto.getPrecio() < 0) {
            return "Ingrese un precio mayor o igual a cero";
        }
        if (producto.getExistencia() < 0) {
            return "La existencia debe ser mayor que cero";
        }
        
        return "";
    }
}
