/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import pos.bl.Producto;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FormProductoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Producto producto = new Producto();
        producto.setDescripcion("iPhone X");
        
        System.out.println(producto.getDescripcion());
    }    
    
}
