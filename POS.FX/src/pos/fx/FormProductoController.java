/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.fx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pos.bl.Producto;
import pos.bl.ProductosServicio;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FormProductoController implements Initializable {
    @FXML
    private TableView tableView;
    
    @FXML
    private TableColumn<Producto, Integer> colId;

    @FXML
    private TableColumn<Producto, String> colDescripcion;
    
    ObservableList<Producto> data;
    
    ProductosServicio servicio;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       servicio = new ProductosServicio();
       
       colId.setCellValueFactory(new PropertyValueFactory<>("id"));
       colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
       
       cargarDatos();
    }    
    
    public void nuevoProducto() throws IOException {
        Producto nuevoProducto = new Producto();
        abrirVentanaModal(nuevoProducto, "Nuevo Producto");
    }
    
    public void guardar(Producto producto) {
        servicio.guardar(producto);
        cargarDatos();
    }

    private void abrirVentanaModal(Producto producto, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevoEditarProducto.fxml"));
        Parent root = (Parent) loader.load();
        
        NuevoEditarProductoController controller = loader.getController();
        controller.setController(this);
        controller.setProducto(producto);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(titulo);
        stage.show();
    }

    private void cargarDatos() {
       data = FXCollections.observableArrayList(servicio.obtenerProductos());
       
       tableView.setItems(data);
       tableView.refresh();
    }
    
}
