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
public class CategoriasServicio {
    private final ArrayList<Categoria> listadeCategorias;

    public CategoriasServicio() {
        listadeCategorias = new ArrayList<>();
        
        crearDatosdePrueba();
    }
    
    public ArrayList<Categoria> obtenerCategorias() {
        return listadeCategorias;
    }

    private void crearDatosdePrueba() {
        Categoria categoria1 = new Categoria("Celulares");
        categoria1.setId(1);
        
        Categoria categoria2 = new Categoria("Accesorios");
        categoria2.setId(2);
        
        listadeCategorias.add(categoria1);
        listadeCategorias.add(categoria2);
    }
    
    
}
