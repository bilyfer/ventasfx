/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.fx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    
    @FXML
    private TableColumn<Producto, String> colCategoria;
    
    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableColumn<Producto, Integer> colExistencia;    
    
    @FXML
    private TableColumn<Producto, Boolean> colActivo;
    
    @FXML
    private TableColumn colEditar;
    
    @FXML
    private TableColumn colEliminar;
    
    @FXML
    private TextField txtBuscar;    
        
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
       colCategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()
               .getCategoria().getDescripcion()));
       colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
       colExistencia.setCellValueFactory(new PropertyValueFactory<>("existencia"));
       colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
       
       definirColumnaEditar();
       definirColumnaEliminar();
       
       cargarDatos();
    }    
    
    public void nuevoProducto() throws IOException {
        Producto nuevoProducto = new Producto();
        abrirVentanaModal(nuevoProducto, "Nuevo Producto");
    }
    
    public String guardar(Producto producto) {
        String resultado = servicio.guardar(producto);
        if (resultado.equals("")) {
            cargarDatos();          
        }
        return resultado;
    }
    
    public void buscar() {
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
        if (txtBuscar.getText() == null || txtBuscar.getText().equals("")) {
            data = FXCollections.observableArrayList(servicio.obtenerProductos());     
        } else {
            data = FXCollections
                    .observableArrayList(servicio.obtenerProductos(txtBuscar.getText()));
        }
              
       tableView.setItems(data);
       tableView.refresh();
    }

    private void definirColumnaEditar() {
        colEditar.setCellFactory(param -> new TableCell<String, String>() {
            final Button btn = new Button("Editar");
            
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    btn.setOnAction(event -> {
                        Producto producto = (Producto) getTableRow().getItem();
                        try {
                            abrirVentanaModal(producto, "Editar Producto");
                        } catch (IOException ex) {
                            Logger.getLogger(FormProductoController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    setGraphic(btn);
                    setText(null);
                }
            }            
        });
    }

    private void definirColumnaEliminar() {
        colEliminar.setCellFactory(param -> new TableCell<String, String>() {
            final Button btn = new Button("Eliminar");
            
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    btn.setOnAction(event -> {
                        Producto producto = (Producto) getTableRow().getItem();
                        eliminar(producto);
                    });
                    setGraphic(btn);
                    setText(null);
                }
            }            
        });        
    }
    
    private void eliminar(Producto producto) {
        Alert alert = new Alert(AlertType.CONFIRMATION,
                "¿Esta seguro que desea eliminar el producto " + producto.getDescripcion() + "?",
                ButtonType.YES, ButtonType.NO);
        
        alert.showAndWait();
        
        if (alert.getResult() == ButtonType.YES) {
            servicio.eliminar(producto);
            cargarDatos();   
        }       
    }
    
}
