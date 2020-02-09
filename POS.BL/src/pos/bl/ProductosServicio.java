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
    
    public void guardar(Producto producto) {
        if (producto.getId().equals(0)) {
            Integer id = obtenerSiguienteId();
            
            producto.setId(id);
            
            listadeProductos.add(producto);
        }
    }
    
    private void crearDatosdePrueba() {
        Producto producto1 = new Producto();
        producto1.setId(1);
        producto1.setDescripcion("iPhone X");

        Producto producto2 = new Producto();
        producto2.setId(2);
        producto2.setDescripcion("Samsung Galaxy S10");
        
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
}
