/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import pos.bl.Producto;

/**
 * FXML Controller class
 *
 * @author User
 */
public class NuevoEditarProductoController implements Initializable {
    @FXML
    Button btnCancelar;
    
    @FXML
    TextField txtId;
    
    @FXML
    TextField txtDescripcion;    
    
    
    private FormProductoController controller;
    private Producto producto;
    
    public void setController(FormProductoController controller) {
        this.controller = controller;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
        
        txtId.textProperty().bindBidirectional(producto.idProperty(), new NumberStringConverter());
        txtDescripcion.textProperty().bindBidirectional(producto.descripcionProperty());        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
    public void aceptar() {
        controller.guardar(producto);
        cerrar();
    }
    
    public void cancelar() {
        cerrar();
    }

    private void cerrar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
    
}
